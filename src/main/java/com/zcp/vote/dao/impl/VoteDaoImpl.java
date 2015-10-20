package com.zcp.vote.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.zcp.vote.dao.VoteDao;
import com.zcp.vote.entity.VoteObject;
import com.zcp.vote.entity.VoteRecord;

@Repository
public class VoteDaoImpl implements VoteDao {
	
	@Autowired
	private NamedParameterJdbcTemplate voteJdbc;

	public int addVoteObject(VoteObject vo) {
		String sql = "insert into vote_object(vname, vdesc, imgPic, qrPic, currentRank, currentVote, deleted) values(:vname,:vdesc,:imgPic,:qrPic,:currentRank,:currentVote,:deleted)";
        SqlParameterSource ps = new BeanPropertySqlParameterSource(vo);
        int result = voteJdbc.update(sql, ps);
        System.out.println("addVoteObject result : " + result);
        return result;
	}
	
	public List<VoteObject> queryForList() {
		String sql = "select * from vote_object where deleted = 0 order by currentVote desc";
		return voteJdbc.query(sql, new BeanPropertyRowMapper<VoteObject>(VoteObject.class));
	}

	public int doVote(VoteRecord record) {
		if (!"127.0.0.1".equals(record.getVoteIP())) {
			String sql = "select count(*) from vote_record where voteIP = :voteIP";
			SqlParameterSource ps  = new BeanPropertySqlParameterSource(record);
			int m = voteJdbc.queryForInt(sql, ps);
			if (m > 0) {
				return -2;
			}
		}
		String sql = "insert into vote_record(voteId, voteIP, voteTime) values(:voteId, :voteIP, :voteTime)";
		SqlParameterSource ps  = new BeanPropertySqlParameterSource(record);
		int result = voteJdbc.update(sql, ps);
        System.out.println("doVote result : " + result);
        if (result > 0) {
        	sql = "update vote_object set currentVote = currentVote + 1 where id = :voteId";
        	Map<String, Object> paramMap = new HashMap<String, Object>();    
        	paramMap.put("voteId", record.getVoteId());
        	int ures = voteJdbc.update(sql, paramMap);
        	return ures;
        } else {
        	return -1;
        }
	}
}
