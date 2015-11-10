package com.zcp.vote.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zcp.vote.entity.VoteObject;
import com.zcp.vote.entity.VoteRecord;
@Repository
public interface VoteDao {
	
	public int addVoteObject(VoteObject vo);
	
	public int updateVoteObject(VoteObject vo);
	
	public List<VoteObject> queryForListFromDB();
	
	public Map<String, List<VoteObject>> queryForListFromCache();
	
	public List<VoteObject> queryForListByCid(String cid);
	
	public VoteObject queryForObjectById(int voteId);
	
	public int doVote(VoteRecord record);
	
	public int doVoteNoCode(VoteRecord record);
	
	public boolean initRedisCache();
	
	public VoteObject getVoteDetails(int voteId);
	
}
