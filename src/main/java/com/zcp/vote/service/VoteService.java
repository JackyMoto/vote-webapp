package com.zcp.vote.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zcp.vote.entity.VoteObject;
@Service
public interface VoteService {
	
	public int addVoteObject(String name, int cid, String imgPic, String qrPic);
	
	public List<VoteObject> getVoteList(String cid);
	
	public int doVote(int id, int cid, String voteIP);
	
	public boolean initRedisCache();

}
