package com.zcp.vote.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zcp.vote.dao.VoteDao;
import com.zcp.vote.entity.VoteObject;
import com.zcp.vote.entity.VoteRecord;
import com.zcp.vote.service.VoteService;

public class VoteServiceImpl implements VoteService {
	
	@Autowired
	public VoteDao dao;

	public int addVoteObject(String name, String desc, String imgPic,
			String qrPic) {
		VoteObject vo = new VoteObject(name, desc, imgPic, qrPic);
		return dao.addVoteObject(vo);
	}

	public List<VoteObject> getVoteList() {
		return dao.queryForList();
	}

	public int doVote(int id, String voteIP) {
		VoteRecord record = new VoteRecord(id, voteIP);
		return dao.doVote(record);
	}
}
