package com.zcp.vote.entity;

import java.io.Serializable;
import java.util.List;

public class VoteRedisRecordObject implements Serializable {

	private static final long serialVersionUID = 3456785064131467162L;

	private VoteObject vo;
	
	private List<VoteRecord> list;

	public VoteObject getVo() {
		return vo;
	}

	public void setVo(VoteObject vo) {
		this.vo = vo;
	}

	public List<VoteRecord> getList() {
		return list;
	}

	public void setList(List<VoteRecord> list) {
		this.list = list;
	}
}
