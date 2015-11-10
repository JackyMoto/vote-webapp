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
import com.zcp.vote.utils.ThreadPoolUtils;
import com.zcp.vote.utils.TimeUtils;
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

	public Map<String, List<VoteObject>> queryForListFromCache() {
		final Map<String, List<VoteObject>> result = new HashMap<String, List<VoteObject>>();
		for (int cid = 1; cid < 6; cid++) {
			final List<VoteObject> list = new ArrayList<VoteObject>();
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
		                list.add(vo);
						return 0;
					}
				});
			}
			result.put("listA0" + cid, list);
		}
		return result;
	}

	public int doVote(final VoteRecord record) {
		Double result = 0D;
		if (null == record) {
			return -1;
		}
		String redisKey = VoteConstant.VOTE_IP_SET + TimeUtils.getCurrentDate() + "." + record.getCid();
		// 处理IP是否已经投过票
		if (!"127.0.0.1".equals(record.getVoteIP())) {
			if (redisTemplate.opsForSet().isMember(redisKey, record.getVoteIP())) {
				System.out.println("Redis里已经有此IP的记录，不能继续投票");
				return -2;
			}
		}
		// 添加投票数
		double d = 1.0;
		ZSetOperations<String, String> setOper = redisTemplate.opsForZSet();
		try {
			String cRankKey = VoteConstant.VOTE_RANK_SET + record.getCid();
			result = setOper.incrementScore(cRankKey, String.valueOf(record.getVoteId()), d);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
		try {
			// 添加投票的IP记录
			redisTemplate.opsForSet().add(redisKey, record.getVoteIP());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 投票记录写入MySQL用异步的线程处理，
		ThreadPoolUtils.getPool().execute(new Runnable() {
			@Override
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
		});
		return result.intValue();
	}
	
	public int doVoteNoCode(final VoteRecord record) {
		if (null == record) {
			return -1;
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
			String redisKey = VoteConstant.VOTE_IP_SET + TimeUtils.getCurrentDate() + "." + record.getCid();
			redisTemplate.opsForSet().add(redisKey, record.getVoteIP());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 投票记录写入MySQL用异步的线程处理，
		ThreadPoolUtils.getPool().execute(new Runnable() {
			@Override
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
		});
		return 0;
	}

	@Override
	public List<VoteObject> queryForListFromDB() {
		String sql = "select * from vote_object where deleted = 0 order by currentVote desc";
		return voteJdbc.query(sql, new BeanPropertyRowMapper<VoteObject>(VoteObject.class));
	}
	
	public boolean initRedisCache() {
		List<VoteObject> list = queryForListFromDB();
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
	
	@Override
	public VoteObject queryForObjectById(final int voteId) {
		final VoteObject vo = new VoteObject();
		final String resdisKey = VoteConstant.VOTE_DETAILS + voteId + ".";
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
                vo.setId(voteId);
                vo.setVname(valVname);
                vo.setCid(Integer.valueOf(valCid));
                vo.setImgPic(valImgPic);
                vo.setQrPic(valQrPic);
                return 0;
			}
		});
    	return vo;
	}

	@Override
	public int updateVoteObject(final VoteObject vo) {
		int result = 0;
		if (null != vo) {
			String sql = "update vote_object set vname = :vname, imgPic = :imgPic, qrPic = :qrPic where id = :voteId";
			Map<String, Object> paramMap = new HashMap<String, Object>();    
			paramMap.put("vname", vo.getVname());
			paramMap.put("imgPic", vo.getImgPic());
			paramMap.put("qrPic", vo.getQrPic());
			paramMap.put("voteId", vo.getId());
			result = voteJdbc.update(sql, paramMap);
	        System.out.println("updateVoteObject result : " + result);
	        // MySQL写入成功后更新到Redis
	        if (result > 0) {
	        	final String resdisKey = VoteConstant.VOTE_DETAILS + vo.getId() + ".";
	    		redisTemplate.execute(new RedisCallback<Integer>() {
	    			@Override
	    			public Integer doInRedis(RedisConnection connection)
	    					throws DataAccessException {
	    				RedisSerializer<String> serializer = redisTemplate.getStringSerializer(); 
	                    byte[] keyVname  = serializer.serialize(resdisKey + "name");  
	                    byte[] valVname = serializer.serialize(vo.getVname());  
	                    byte[] keyImgPic = serializer.serialize(resdisKey + "img");
	                    byte[] valImgPic = serializer.serialize(vo.getImgPic());
	                    byte[] keyQrPic = serializer.serialize(resdisKey + "qr");
	                    byte[] valQrPic = serializer.serialize(vo.getQrPic());
	                    connection.set(keyVname, valVname);
	                    connection.set(keyImgPic, valImgPic);
	                    connection.set(keyQrPic, valQrPic);
	    				return 0;
	    			}
	    		});
	        }
		}
		return 0;
	}

	@Override
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

	@Override
	public VoteObject getVoteDetails(final int voteId) {
		VoteObject vo = null;
		final String resdisKey = VoteConstant.VOTE_DETAILS + voteId + ".";
    	redisTemplate.execute(new RedisCallback<Integer>() {
			@Override
			public Integer doInRedis(RedisConnection connection)
					throws DataAccessException {
				// 初始化投票内容的信息
				RedisSerializer<String> serializer = redisTemplate.getStringSerializer(); 
                byte[] keyVname  = serializer.serialize(resdisKey + "name");  
                byte[] keyCid = serializer.serialize(resdisKey + "cid");
                byte[] keyImgPic = serializer.serialize(resdisKey + "img");
                byte[] keyQrPic = serializer.serialize(resdisKey + "qr");
                
                String valVname = serializer.deserialize(connection.get(keyVname));
                String valCid = serializer.deserialize(connection.get(keyCid));
                String valImgPic = serializer.deserialize(connection.get(keyImgPic));
                String valQrPic = serializer.deserialize(connection.get(keyQrPic));
                VoteObject vo = new VoteObject(valVname, Integer.valueOf(valCid), valImgPic, valQrPic);
                vo.setId(voteId);
                return 0;
			}
		});
    	return vo;
	}
}
