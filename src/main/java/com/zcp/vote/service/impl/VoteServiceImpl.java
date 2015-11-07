package com.zcp.vote.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcp.vote.dao.VoteDao;
import com.zcp.vote.dao.impl.VoteRedisDaoImpl;
import com.zcp.vote.entity.VoteObject;
import com.zcp.vote.entity.VoteRecord;
import com.zcp.vote.service.VoteService;
@Service
public class VoteServiceImpl implements VoteService {
	
	@Autowired
	public VoteDao dao;

	public int addVoteObject(String name, int cid, String imgPic,
			String qrPic) {
		VoteObject vo = new VoteObject(name, cid, imgPic, qrPic);
		return dao.addVoteObject(vo);
	}

	public Map<String, List<VoteObject>> getVoteList() {
		return dao.queryForListFromCache();
	}

	public int doVote(int id, int cid, String voteIP) {
		VoteRecord record = new VoteRecord(id, cid, voteIP);
		return dao.doVote(record);
	}

	@Override
	public boolean initRedisCache() {
		return dao.initRedisCache();
	}

	@Override
	public int updateVoteObject(int id, String vname, String imgPic,
			String qrPic) {
		VoteObject vo = dao.queryForObjectById(id);
		if (null != vo
				&& null != vo.getVname()
				&& null != vo.getImgPic()
				&& null != vo.getQrPic()) {
			vo.setVname(vname);
			vo.setImgPic(imgPic);
			vo.setQrPic(qrPic);
			return dao.updateVoteObject(vo);
		}
		return 0;
	}

	@Override
	public VoteObject getVoteObjectById(int voteId) {
		return dao.queryForObjectById(voteId);
	}

	@Override
	public List<VoteObject> getVoteListByCid(String cid) {
		return dao.queryForListByCid(cid);
	}
}
