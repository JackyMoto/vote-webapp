package com.zcp.vote.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import com.zcp.vote.dao.VoteDao;
import com.zcp.vote.entity.VoteObject;
import com.zcp.vote.entity.VoteRecord;

@Repository
public class VoteRedisDaoImpl implements VoteDao {
	
	@Autowired
	@Qualifier("redisVoteTemplate")
    protected RedisTemplate<Serializable, VoteObject> redisVoteTemplate;
	
	@Autowired
	@Qualifier("redisRecordTemplate")
    protected RedisTemplate<Serializable, VoteRecord> redisRecordTemplate;

	public int addVoteObject(final VoteObject vo) {
		redisVoteTemplate.execute(new RedisCallback<VoteObject>() {
			public VoteObject doInRedis(RedisConnection connection) throws DataAccessException {
				ListOperations<Serializable, VoteObject> listOper = redisVoteTemplate.opsForList();
				listOper.rightPush("voteobject:list", vo);
				return vo;
			}
		});
		return 0;
	}

	public List<VoteObject> queryForList() {
		return redisVoteTemplate.boundListOps("voteobject:list").range(0, -1);
	}

	public int doVote(final VoteRecord record) {
		redisRecordTemplate.execute(new RedisCallback<Integer>() {
			public Integer doInRedis(RedisConnection connection)
					throws DataAccessException {
				ZSetOperations<Serializable, VoteRecord> setOper = redisRecordTemplate.opsForZSet();
				setOper.incrementScore("vote.rank", record, 1);
				return 0;
			}
		});
		return 0;
	}
}
