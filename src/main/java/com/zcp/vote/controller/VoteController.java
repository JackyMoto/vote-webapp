package com.zcp.vote.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.druid.util.StringUtils;
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
	
	@RequestMapping(value="/vote/addvote.do")
	public String addVote(String vname, String vdesc, String imgPic, String qrPic) {
		service.addVoteObject(vname, vdesc, imgPic, qrPic);
		return "redirect:/vote/pagelist.do";
	}
	
	@RequestMapping(value="/vote/pagelist.do")
	public String votePageList(Model model) {
		List<VoteObject> list = service.getVoteList();
		model.addAttribute("list", list);
		return "vote/pagelist";
	}
	
	@RequestMapping(value="/vote/dovote.do")
	public String doVote(HttpServletRequest request, int voteId, int codeResult) {
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
		service.doVote(voteId, voteIP);
		return "redirect:/vote/pagelist.do";
	}
	
	@RequestMapping(value="/vote/getvotecode.do")
	public void getVoteCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		CodeUtils.generateCode(request, response);
	}
}
