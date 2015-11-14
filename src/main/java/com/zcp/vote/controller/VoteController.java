package com.zcp.vote.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
	@Value("${image.url.prefix}")    
	private String imageUrlPrefix;
	
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
		VoteObject vo = service.getVoteObjectById(voteId);
		model.addAttribute("vo", vo);
		return "vote/updatevotepage";
	}
	
	@RequestMapping(value="/vote/updatevote.do")
	public String updateVote(int voteId, int cid, String vname, String imgPic, 
			String qrPic, int currentVote) {
		service.updateVoteObject(voteId, vname, imgPic, qrPic, currentVote);
		return "redirect:/vote/pagelist.do?cid=" + cid;
	}
	
	@RequestMapping(value="/vote/pagelist.do")
	public String votePageList(Model model, String cid) {
		if (StringUtils.isEmpty(cid)) {
			cid = "1";
		}
		List<VoteObject> list = service.getVoteListByCid(cid);
		model.addAttribute("list", list);
		model.addAttribute("imageUrlPrefix", imageUrlPrefix);
		return "/vote/pagelist";
	}
	
	@ResponseBody
	@RequestMapping(value="/vote/pagelistByCid.do")
	public String votePageListByCid(Model model, String cid, String callback) {
		if (StringUtils.isEmpty(cid)) {
			cid = "1";
		}
		if (StringUtils.isEmpty(callback)) {
			callback = "fn";
		}
		List<VoteObject> list = service.getVoteListByCid(cid);
		model.addAttribute("list", list);
		model.addAttribute("imageUrlPrefix", imageUrlPrefix);
		
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.setExcludes(new String[]{"deleted","qrPic", "currentRank"});
		String data = JSONSerializer.toJSON(list, jsonConfig).toString();
		JSONObject result = new JSONObject();
		result.put("status", 1);
		result.put("data", data);
		// fn({status:1,data:[],msg:"jjj",errCode:1155,errMsg:"ddddfsf"})
		String str = callback + "(" + result.toString() + ")";
		return str;
	}
	
	@ResponseBody
	@RequestMapping(value="/vote/dovote.do")
	public String doVote(HttpServletRequest request, int voteId, int cid, int codeResult
			, String callback) {
		JSONObject json = new JSONObject();
		String str = null;
		if (StringUtils.isEmpty(callback)) {
			callback = "fn";
		}
		if (voteId <= 0 
				|| cid <= 0 || cid >= 6
				|| codeResult < 0
				) {
			json.put("status", -1);
			json.put("msg", "参数不正确");
			str = callback + "(" + json.toString() + ")";
			return str;
		}
		HttpSession session = request.getSession();
		if (null != session) {
			Object obj = session.getAttribute("result");
			if (null != obj && obj instanceof Integer) {
				int result = (Integer) obj;
				if (result != codeResult) {
					json.put("status", -2);
					json.put("msg", "验证失败, 请重新输入验证码结果");
					str = callback + "(" + json.toString() + ")";
					return str;
				}
			} else {
				json.put("status", -6);
				json.put("msg", "非法投票操作");
				str = callback + "(" + json.toString() + ")";
				return str;
			}
		} else {
			json.put("status", -7);
			json.put("msg", "系统出现异常");
			str = callback + "(" + json.toString() + ")";
			return str;
		}
		String voteIP = VoteUtils.getIpAddr2(request);
		if (StringUtils.isEmpty(voteIP)) {
			json.put("status", -3);
			json.put("msg", "获取到IP地址为空, 无法进行投票");
			str = callback + "(" + json.toString() + ")";
			return str;
		}
		int res = service.doVote(voteId, cid, voteIP);
		switch (res) {
		case -1:
			json.put("status", -4);
			json.put("msg", "系统内部异常,无法进行投票");
			str = callback + "(" + json.toString() + ")";
			break;
		case -2:
			json.put("status", -5);
			json.put("msg", "此类奖项您已经投票，无法进行再次投票");
			str = callback + "(" + json.toString() + ")";
			break;
		default:
			if (res >= 0) {
				json.put("status", 1);
				json.put("msg", "投票成功");
				json.put("data", res);
				str = callback + "(" + json.toString() + ")";
			} else {
				json.put("status", -8);
				json.put("msg", "数据返回异常");
				str = callback + "(" + json.toString() + ")";
			}
			break;
		}
        if (null != session) {
        	session.removeAttribute("result");
        }
		return str;
	}
	
	@RequestMapping(value="/vote/dovotenocode.do")
	public String doVoteNoCode(HttpServletRequest request, int voteId, int cid) {
		if (voteId <= 0 || cid <= 0 || cid >= 6) {
			System.out.println("参数不正确");
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
	
	@ResponseBody
	@RequestMapping(value="/vote/votedetails.do")
	public String getVoteDetails(int voteId, String callback) {
		JSONObject json = new JSONObject();
		String str = null;
		if (StringUtils.isEmpty(callback)) {
			callback = "fn";
		}
		if (voteId <= 0) {
			json.put("status", -1);
			json.put("msg", "参数不正确");
			str = callback + "(" + json.toString() + ")";
			return str;
		}
		VoteObject vo = service.getVoteDetails(voteId);
		if (null != vo && vo.getId() != 0) {
			JsonConfig jsonConfig = new JsonConfig();  
			jsonConfig.setExcludes(new String[]{"deleted","qrPic", "currentRank"});
			String data = JSONSerializer.toJSON(vo, jsonConfig).toString();
			json.put("status", 1);
			json.put("data", data);
		} else {
			json.put("status", -1);
			json.put("msg", "投票内容不存在");
		}
		str = callback + "(" + json.toString() + ")";
		return str;
	}
}
