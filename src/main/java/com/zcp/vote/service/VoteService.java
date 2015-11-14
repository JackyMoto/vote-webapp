package com.zcp.vote.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zcp.vote.entity.VoteObject;
@Service
public interface VoteService {
	
	public int addVoteObject(String name, int cid, String imgPic, String qrPic);
	
	public int updateVoteObject(int id, String vname, String imgPic, String qrPic, int currentVote);
	
	public Map<String, List<VoteObject>> getVoteList();
	
	public List<VoteObject> getVoteListByCid(String cid);
	
	public VoteObject getVoteObjectById(int voteId);
	
	public int doVote(int id, int cid, String voteIP);
	
	public boolean initRedisCache();
	
	public VoteObject getVoteDetails(int voteId);
	
}
