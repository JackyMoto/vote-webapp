package com.zcp.vote.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VoteUtils {

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}

	public String getCookieIPAddress(HttpServletRequest request,
			HttpServletResponse response, String ip_address) {
		Cookie[] cookies = request.getCookies();
		String cookievalue = "";
		if (null != cookies && cookies.length != 0) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookie.getName().equals("cookie_ip_" + ip_address)) {
					String value = cookie.getValue();
					String[] value_array = value.split("\\|");
					int time = Integer.parseInt(value_array[1]) + 1;
					cookievalue = value_array[0] + "|" + time;// 取出原来cookie中的信息，在后面追加信息
					Cookie c = new Cookie("cookie_ip_" + ip_address,
							cookievalue); // 编码后添加都cookie
					c.setMaxAge(60 * 60);
					c.setPath("/");
					response.addCookie(c);
				} else {
					Cookie c = new Cookie("cookie_ip_" + ip_address, ip_address
							+ "|1");
					c.setMaxAge(24 * 60 * 60);
					c.setPath("/");
					response.addCookie(c);
				}
				System.out.println(cookievalue);
			}
		}
		return cookievalue;
	}
}
