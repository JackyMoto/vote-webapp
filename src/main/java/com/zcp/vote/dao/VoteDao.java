package com.zcp.vote.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zcp.vote.entity.VoteObject;
import com.zcp.vote.entity.VoteRecord;
@Repository
public interface VoteDao {
	
	public int addVoteObject(VoteObject vo);
	
	public List<VoteObject> queryForList();
	
	public List<VoteObject> queryForListByCid(String cid);
	
	public int doVote(VoteRecord record);
	
	public boolean initRedisCache();
	
}
