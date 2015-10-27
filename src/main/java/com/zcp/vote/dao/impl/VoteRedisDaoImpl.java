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
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

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
			String sql = "insert into vote_object(vname, vdesc, imgPic, qrPic, currentRank, currentVote, deleted) values(:vname,:vdesc,:imgPic,:qrPic,:currentRank,:currentVote,:deleted)";
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
		                byte[] keyVdesc = serializer.serialize(resdisKey + "desc");
		                byte[] valVdesc = serializer.serialize(vo.getVdesc());
		                byte[] keyImgPic = serializer.serialize(resdisKey + "img");
		                byte[] valImgPic = serializer.serialize(vo.getImgPic());
		                byte[] keyQrPic = serializer.serialize(resdisKey + "qr");
		                byte[] valQrPic = serializer.serialize(vo.getQrPic());
		                connection.setNX(keyVname, valVname);
		                connection.setNX(keyVdesc, valVdesc);
		                connection.setNX(keyImgPic, valImgPic);
		                connection.setNX(keyQrPic, valQrPic);
		                // 加入被投票的列表
		        		ZSetOperations<String, String> setOper = redisTemplate.opsForZSet();
		        		setOper.add(VoteConstant.VOTE_RANK_SET, String.valueOf(voPKey), 0);
		                return 0;
					}
				});
	        }
		}
        return result;
	}

	public List<VoteObject> queryForList() {
		final List<VoteObject> result = new ArrayList<VoteObject>();
		Set<TypedTuple<String>> set = redisTemplate.opsForZSet().reverseRangeWithScores(VoteConstant.VOTE_RANK_SET, 0, -1);
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
	                byte[] keyVdesc = serializer.serialize(resdisKey + "desc");
	                byte[] keyImgPic = serializer.serialize(resdisKey + "img");
	                byte[] keyQrPic = serializer.serialize(resdisKey + "qr");
					
	                String valVname = serializer.deserialize(connection.get(keyVname));
	                String valVdesc = serializer.deserialize(connection.get(keyVdesc));
	                String valImgPic = serializer.deserialize(connection.get(keyImgPic));
	                String valQrPic = serializer.deserialize(connection.get(keyQrPic));
	                VoteObject vo = new VoteObject(valVname, valVdesc, valImgPic, valQrPic);
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
			setOper.incrementScore(VoteConstant.VOTE_RANK_SET, String.valueOf(record.getVoteId()), d);
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
				String sql = "insert into vote_record(voteId, voteIP, voteTime) values(:voteId, :voteIP, :voteTime)";
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
}
