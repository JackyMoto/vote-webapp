package com.zcp.vote.entity;

import java.io.Serializable;

public class VoteObject implements Serializable {
	
	private static final long serialVersionUID = 6139805683295569327L;

	private int id;
	
	private String vname;
	
	private String vdesc;
	
	private String imgPic;
	
	private String qrPic;
	
	private int currentRank;
	
	private int currentVote;
	
	private int deleted;
	
	public VoteObject() {
		
	}

	public VoteObject(String vname, String vdesc, String imgPic, String qrPic) {
		this.vname = vname;
		this.vdesc = vdesc;
		this.imgPic = imgPic;
		this.qrPic = qrPic;
		this.currentRank = 0;
		this.currentVote = 0;
		this.deleted = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVname() {
		return vname;
	}

	public void setVname(String vname) {
		this.vname = vname;
	}

	public String getVdesc() {
		return vdesc;
	}

	public void setVdesc(String vdesc) {
		this.vdesc = vdesc;
	}

	public String getImgPic() {
		return imgPic;
	}

	public void setImgPic(String imgPic) {
		this.imgPic = imgPic;
	}

	public String getQrPic() {
		return qrPic;
	}

	public void setQrPic(String qrPic) {
		this.qrPic = qrPic;
	}

	public int getCurrentRank() {
		return currentRank;
	}

	public void setCurrentRank(int currentRank) {
		this.currentRank = currentRank;
	}

	public int getCurrentVote() {
		return currentVote;
	}

	public void setCurrentVote(int currentVote) {
		this.currentVote = currentVote;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
}
