package com.zcp.vote.dao;

import java.util.List;

import com.zcp.vote.entity.VoteObject;
import com.zcp.vote.entity.VoteRecord;

public interface VoteDao {
	
	public int addVoteObject(VoteObject vo);
	
	public List<VoteObject> queryForList();
	
	public int doVote(VoteRecord record);
}
