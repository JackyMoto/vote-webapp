package com.zcp.vote.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.zcp.vote.constant.VoteConstant;
import com.zcp.vote.dao.VoteDao;
import com.zcp.vote.entity.VoteObject;
import com.zcp.vote.entity.VoteRecord;
@Repository
public class VoteRedisDaoImpl implements VoteDao {
	
	@Autowired
	private NamedParameterJdbcTemplate voteJdbc;
	
	@Autowired
    protected RedisTemplate<String, String> redisTemplate;

	public int addVoteObject(final VoteObject vo) {
		int result = 0;
		if (null != vo) {
			String sql = "insert into vote_object(vname, cid, imgPic, qrPic, currentRank, currentVote, deleted) values(:vname,:cid,:imgPic,:qrPic,:currentRank,:currentVote,:deleted)";
	        SqlParameterSource ps = new BeanPropertySqlParameterSource(vo);
	        KeyHolder keyHolder = new GeneratedKeyHolder();
	        result = voteJdbc.update(sql, ps, keyHolder);
	        System.out.println("addVoteObject result : " + result);
	        // MySQL写入成功后添加到Redis
	        if (result > 0) {
	        	final int voPKey = keyHolder.getKey().intValue();
	        	vo.setId(voPKey);
	        	final String resdisKey = VoteConstant.VOTE_DETAILS + voPKey + ".";
	        	redisTemplate.execute(new RedisCallback<Integer>() {
					@Override
					public Integer doInRedis(RedisConnection connection)
							throws DataAccessException {
						// 初始化投票内容的信息
						RedisSerializer<String> serializer = redisTemplate.getStringSerializer(); 
		                byte[] keyVname  = serializer.serialize(resdisKey + "name");  
		                byte[] valVname = serializer.serialize(vo.getVname());  
		                byte[] keyCid = serializer.serialize(resdisKey + "cid");
		                byte[] valCid = serializer.serialize(String.valueOf(vo.getCid()));
		                byte[] keyImgPic = serializer.serialize(resdisKey + "img");
		                byte[] valImgPic = serializer.serialize(vo.getImgPic());
		                byte[] keyQrPic = serializer.serialize(resdisKey + "qr");
		                byte[] valQrPic = serializer.serialize(vo.getQrPic());
		                connection.setNX(keyVname, valVname);
		                connection.setNX(keyCid, valCid);
		                connection.setNX(keyImgPic, valImgPic);
		                connection.setNX(keyQrPic, valQrPic);
		                // 加入被投票的列表
		        		ZSetOperations<String, String> setOper = redisTemplate.opsForZSet();
		        		String cRankKey = VoteConstant.VOTE_RANK_SET + vo.getCid();
		        		setOper.add(cRankKey, String.valueOf(voPKey), 0);
		                return 0;
					}
				});
	        }
		}
        return result;
	}

	public List<VoteObject> queryForListByCid(String cid) {
		if (StringUtils.isEmpty(cid)) {
			return null;
		}
		final List<VoteObject> result = new ArrayList<VoteObject>();
		String cRankKey = VoteConstant.VOTE_RANK_SET + cid;
		Set<TypedTuple<String>> set = redisTemplate.opsForZSet().reverseRangeWithScores(cRankKey, 0, -1);
		int current = 0;
		for (TypedTuple<String> s : set) {
			final String voteId = s.getValue();
			final int voteNum = s.getScore().intValue();
			final String resdisKey = VoteConstant.VOTE_DETAILS + voteId + ".";
			final int rank = ++current;
			redisTemplate.execute(new RedisCallback<Integer>() {
				@Override
				public Integer doInRedis(RedisConnection connection)
						throws DataAccessException {
					RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
					byte[] keyVname  = serializer.serialize(resdisKey + "name");  
	                byte[] keyCid= serializer.serialize(resdisKey + "cid");
	                byte[] keyImgPic = serializer.serialize(resdisKey + "img");
	                byte[] keyQrPic = serializer.serialize(resdisKey + "qr");
					
	                String valVname = serializer.deserialize(connection.get(keyVname));
	                String valCid = serializer.deserialize(connection.get(keyCid));
	                String valImgPic = serializer.deserialize(connection.get(keyImgPic));
	                String valQrPic = serializer.deserialize(connection.get(keyQrPic));
	                VoteObject vo = new VoteObject(valVname, Integer.valueOf(valCid), valImgPic, valQrPic);
	                vo.setCurrentRank(rank);
	                vo.setCurrentVote(voteNum);
	                vo.setId(Integer.valueOf(voteId));
	                result.add(vo);
					return 0;
				}
			});
		}
		return result;
	}

	public int doVote(final VoteRecord record) {
		if (null == record) {
			return -1;
		}
		// 处理IP是否已经投过票
		if (!"127.0.0.1".equals(record.getVoteIP())) {
			if (redisTemplate.opsForSet().isMember(VoteConstant.VOTE_IP_SET, record.getVoteIP())) {
				System.out.println("Redis里已经有此IP的记录，不能继续投票");
				return -2;
			}
		}
		// 添加投票数
		double d = 1.0;
		ZSetOperations<String, String> setOper = redisTemplate.opsForZSet();
		try {
			String cRankKey = VoteConstant.VOTE_RANK_SET + record.getCid();
			setOper.incrementScore(cRankKey, String.valueOf(record.getVoteId()), d);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
		try {
			// 添加投票的IP记录
			redisTemplate.opsForSet().add(VoteConstant.VOTE_IP_SET, record.getVoteIP());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 投票记录写入MySQL用异步的线程处理，
		new Thread(new Runnable() {
			public void run() {
				String sql = "insert into vote_record(voteId, cid, voteIP, voteTime) values(:voteId, :cid, :voteIP, :voteTime)";
				SqlParameterSource ps  = new BeanPropertySqlParameterSource(record);
				int result = voteJdbc.update(sql, ps);
		        System.out.println("doVote result : " + result);
		        if (result > 0) {
		        	sql = "update vote_object set currentVote = currentVote + 1 where id = :voteId";
		        	Map<String, Object> paramMap = new HashMap<String, Object>();    
		        	paramMap.put("voteId", record.getVoteId());
		        	voteJdbc.update(sql, paramMap);
		        }
			}
		}).start();
		return 0;
	}

	@Override
	public List<VoteObject> queryForList() {
		String sql = "select * from vote_object where deleted = 0 order by currentVote desc";
		return voteJdbc.query(sql, new BeanPropertyRowMapper<VoteObject>(VoteObject.class));
	}
	
	public boolean initRedisCache() {
		List<VoteObject> list = queryForList();
		if (null != list && list.size() > 0) {
			// 加入被投票的列表
    		ZSetOperations<String, String> setOper = redisTemplate.opsForZSet();
			for (final VoteObject vo : list) {
				// 初始化详细信息
				final String resdisKey = VoteConstant.VOTE_DETAILS + vo.getId() + ".";
				redisTemplate.execute(new RedisCallback<Integer>() {
					@Override
					public Integer doInRedis(RedisConnection connection)
							throws DataAccessException {
						RedisSerializer<String> serializer = redisTemplate.getStringSerializer(); 
		                byte[] keyVname  = serializer.serialize(resdisKey + "name");  
		                byte[] valVname = serializer.serialize(vo.getVname());  
		                byte[] keyCid = serializer.serialize(resdisKey + "cid");
		                byte[] valCid = serializer.serialize(String.valueOf(vo.getCid()));
		                byte[] keyImgPic = serializer.serialize(resdisKey + "img");
		                byte[] valImgPic = serializer.serialize(vo.getImgPic());
		                byte[] keyQrPic = serializer.serialize(resdisKey + "qr");
		                byte[] valQrPic = serializer.serialize(vo.getQrPic());
		                connection.setNX(keyVname, valVname);
		                connection.setNX(keyCid, valCid);
		                connection.setNX(keyImgPic, valImgPic);
		                connection.setNX(keyQrPic, valQrPic);
						return 0;
					}
				});
				
				// 初始化排行榜
        		String cRankKey = VoteConstant.VOTE_RANK_SET + vo.getCid();
        		try {
        			setOper.add(cRankKey, String.valueOf(vo.getId()), Double.valueOf(vo.getCurrentVote()));
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
			}
		}
		return true;
	}
}
