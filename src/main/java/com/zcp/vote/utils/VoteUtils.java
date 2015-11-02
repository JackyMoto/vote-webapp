package com.zcp.vote.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
	
	public static String getIpAddr2(HttpServletRequest request) {   
	     String ipAddress = request.getHeader("x-forwarded-for");    
	     if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {   
	    	 ipAddress = request.getHeader("Proxy-Client-IP");   
	     }   
	     if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {   
	         ipAddress = request.getHeader("WL-Proxy-Client-IP");   
	     }   
	     if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {   
	    	 ipAddress = request.getRemoteAddr();   
	    	 if ("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)){   
	    		 //根据网卡取本机配置的IP   
	    		 InetAddress inet=null;   
	    		 try {   
	    			 inet = InetAddress.getLocalHost();   
	    		 } catch (UnknownHostException e) {   
	    			 e.printStackTrace();   
	    		 }   
	    		 ipAddress= inet.getHostAddress();   
	    	 }   
	     }   
	     //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割   
	     if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15   
	         if (ipAddress.indexOf(",") > 0) {   
	        	 ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));   
	         }   
	     }   
	     return ipAddress;    
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
