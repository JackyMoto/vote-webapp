package com.zcp.vote.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

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
	
	@RequestMapping(value="/vote/addvotepage.do")
	public String addVotePage() {
		return "vote/addvotepage";
	}
	
	@RequestMapping(value="/vote/addvote.do")
	public String addVote(String vname, int cid, String imgPic, String qrPic) {
		service.addVoteObject(vname, cid, imgPic, qrPic);
		return "redirect:/vote/pagelist.do";
	}
	
	@RequestMapping(value="/vote/updatevotepage.do")
	public String updateVotePage(Model model, int voteId) {
		VoteObject vo =service.getVoteObjectById(voteId);
		model.addAttribute("vo", vo);
		return "vote/updatevotepage";
	}
	
	@RequestMapping(value="/vote/updatevote.do")
	public String updateVote(int voteId, int cid, String vname, String imgPic, String qrPic) {
		service.updateVoteObject(voteId, vname, imgPic, qrPic);
		return "redirect:/vote/pagelist.do?cid=" + cid;
	}
	
//	@ResponseBody
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
//		if ("json".equals(type)) {
//			return JSONSerializer.toJSON(list).toString();
//		} 
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
		String voteIP = VoteUtils.getIpAddr2(request);
		if (StringUtils.isEmpty(voteIP)) {
			System.out.println("获取到IP地址为空, 无法进行投票");
			return "redirect:/vote/pagelist.do";
		}
		service.doVote(voteId, cid, voteIP);
        HttpSession session = request.getSession();   
        session.removeAttribute("result");
        session.removeAttribute("image");
		return "redirect:/vote/pagelist.do";
	}
	
	@RequestMapping(value="/vote/getvotecode.do")
	public void getVoteCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		Object resObj = session.getAttribute("result");
		Object imgObj = session.getAttribute("image");
		if (null != resObj && null != imgObj) {
			BufferedImage image = (BufferedImage) session.getAttribute("image");
			System.out.println("已经获取验证码, 重复获取");
			response.setDateHeader("Expires", 0);
			// Set standard HTTP/1.1 no-cache headers.
			response.setHeader("Cache-Control",
					"no-store, no-cache, must-revalidate");
			// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
			response.addHeader("Cache-Control", "post-check=0, pre-check=0");
			// Set standard HTTP/1.0 no-cache header.
			response.setHeader("Pragma", "no-cache");
			// return a jpeg
			response.setContentType("image/jpeg");
			OutputStream os = null;
			try {
				os = response.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 通过ImageIO对象的write静态方法将图片输出。
			ImageIO.write(image, "JPEG", os);
			os.close();
		} else {
			CodeUtils.generateCode(request, response);
		}
	}
	
	@RequestMapping(value="/vote/initvotecache.do")
	public String initRedisCache() {
		service.initRedisCache();
		return "redirect:/vote/pagelist.do";
	}
}
