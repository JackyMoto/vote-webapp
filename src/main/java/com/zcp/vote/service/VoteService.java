package com.zcp.vote.service;

import java.util.List;

import com.zcp.vote.entity.VoteObject;

public interface VoteService {
	
	public int addVoteObject(String name, String desc, String imgPic, String qrPic);
	
	public List<VoteObject> getVoteList();
	
	public int doVote(int id, String voteIP);

}
