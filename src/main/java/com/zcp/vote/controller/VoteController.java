package com.zcp.vote.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zcp.vote.entity.VoteObject;
import com.zcp.vote.service.VoteService;
import com.zcp.vote.utils.CodeUtils;
import com.zcp.vote.utils.VoteUtils;


@Controller
public class VoteController {
	
	@Autowired
	public VoteService service;
	
	@RequestMapping(value="/vote/ranklist.do")
	public String voteRankList(){
		return null;
	}
	
	@RequestMapping(value="/vote/addvotepage.do")
	public String addVotePage() {
		return "vote/addvotepage";
	}
	
	@RequestMapping(value="/vote/updatevotepage.do")
	public String updateVotePage() {
		return "vote/updatevotepage";
	}
	
	@RequestMapping(value="/vote/addvote.do")
	public String addVote(String vname, int cid, String imgPic, String qrPic) {
		service.addVoteObject(vname, cid, imgPic, qrPic);
		return "redirect:/vote/pagelist.do";
	}
	
	@ResponseBody
	@RequestMapping(value="/vote/pagelist.do")
	public String votePageList(Model model, String cid, String type) {
		if (StringUtils.isEmpty(cid)) {
			cid = "1";
		}
		if (StringUtils.isEmpty(type)) {
			type = "json";
		}
		List<VoteObject> list = service.getVoteList(cid);
		model.addAttribute("list", list);
		if ("json".equals(type)) {
			return JSONSerializer.toJSON(list).toString();
		} 
		return "vote/pagelist";
	}
	
	@RequestMapping(value="/vote/dovote.do")
	public String doVote(HttpServletRequest request, int voteId, int cid, int codeResult) {
		if (org.springframework.util.StringUtils.isEmpty(voteId)
				|| org.springframework.util.StringUtils.isEmpty(cid)
				|| org.springframework.util.StringUtils.isEmpty(codeResult)) {
			System.out.println("缺少参数");
			return "redirect:/vote/pagelist.do";
		}
		int result = (Integer) request.getSession().getAttribute("result");
		if (result != codeResult) {
			System.out.println("验证码不正确,无法进行投票");
			return "redirect:/vote/pagelist.do";
		}
		String voteIP = VoteUtils.getIpAddr(request);
		if (StringUtils.isEmpty(voteIP)) {
			System.out.println("获取到IP地址为空, 无法进行投票");
			return "redirect:/vote/pagelist.do";
		}
		service.doVote(voteId, cid, voteIP);
		return "redirect:/vote/pagelist.do";
	}
	
	@RequestMapping(value="/vote/getvotecode.do")
	public void getVoteCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		CodeUtils.generateCode(request, response);
	}
	
	@RequestMapping(value="/vote/initvotecache.do")
	public String initRedisCache() {
		service.initRedisCache();
		return "redirect:/vote/pagelist.do";
	}
}
