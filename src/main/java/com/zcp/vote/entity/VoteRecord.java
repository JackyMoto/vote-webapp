package com.zcp.vote.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class VoteRecord implements Serializable {

	private static final long serialVersionUID = -8377318363099352627L;

	private int id;
	
	private int cid;
	
	private int voteId;
	
	private String voteIP;
	
	private Timestamp voteTime;
	
	public VoteRecord() {
		
	}
	
	public VoteRecord(int voteId, int cid, String voteIP) {
		this.voteId = voteId;
		this.cid = cid;
		this.voteIP = voteIP;
		this.voteTime = new Timestamp(System.currentTimeMillis());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVoteId() {
		return voteId;
	}

	public void setVoteId(int voteId) {
		this.voteId = voteId;
	}

	public String getVoteIP() {
		return voteIP;
	}

	public void setVoteIP(String voteIP) {
		this.voteIP = voteIP;
	}

	public Timestamp getVoteTime() {
		return voteTime;
	}

	public void setVoteTime(Timestamp voteTime) {
		this.voteTime = voteTime;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}
}
