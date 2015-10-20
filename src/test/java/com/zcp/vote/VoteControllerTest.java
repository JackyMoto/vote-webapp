package com.zcp.vote;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class VoteControllerTest {
	
	public static void main(String[] args) {
		try {
			String str = URLEncoder.encode("http://localhost:8080/zcp-webapp/vote/addvote.do?name=%E8%8B%B1%E9%9B%84%E8%81%94%E7%9B%9F&desc=%E6%96%97%E9%B1%BC%E6%9C%80%E7%81%AB%E7%9A%84%E6%B8%B8%E6%88%8F&imgPic=lol.png&qrPic=lolqr.png", "UTF-8");
			str = URLEncoder.encode("英雄联盟", "UTF-8");
			String str2 = URLEncoder.encode("斗鱼最火的游戏", "UTF-8");
			System.out.println(str);
			System.out.println(str2);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
