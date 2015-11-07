<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% 
String path = request.getContextPath(); 
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; 
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>" /> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>硬核联盟</title>
<link href="${imageUrlPrefix}css/main.css" type="text/css" rel="stylesheet" />
<script src="${imageUrlPrefix}js/jquery-1.8.3.min.js" type="text/javascript"></script>
</head>
<body>
	<div id="nav">
		<div class="nav-main">
			<div class="nav-left">
				<img src="${imageUrlPrefix}images/logo.jpg" alt="硬核联盟" />
			</div>
			<div class="nav-right">
				<ul>
					<li><a href="#">首页</a></li>
					<li><a href="#">关于大会</a></li>
					<li><a href="#">大会日程</a></li>
					<li><a href="#">参加大会</a></li>
					<li><a href="#">演讲嘉宾</a></li>
					<li><a href="#">媒体合作</a></li>
				</ul>
			</div>
		</div>
	</div>
	<div class="banner">
		<img src="${imageUrlPrefix}images/banner.jpg" alt="硬核联盟" />
	</div>
	<div class="main">
		<div id="tp-nav">
			<ul>
				<li class="active"><a href="javascript:void(0);">十佳人气游戏奖</a></li>
				<li><a href="javascript:void(0);">十佳最受期待游戏奖</a></li>
				<li><a href="javascript:void(0);">十佳单机游戏奖</a></li>
				<li><a href="javascript:void(0);">十佳网络游戏奖</a></li>
				<li><a href="javascript:void(0);">十佳IP奖</a></li>
			</ul>
		</div>
		<div id="tp-box">
			<div class="tp-box-list renqi">
				<c:forEach items="${listA01}" var="vo" varStatus="status">
				<c:if test="${status.index % 5 == 0}">
				<a name="row${status.index % 5}"></a>
				<ul>
				</c:if>
					<li>
						<img src="${imageUrlPrefix}/images/A0${vo.cid}/${vo.imgPic}" alt="${vo.vname}" />
						<p class="tp-box-title">${vo.vname}</p>
						<p class="tp-box-ticketNum">
							票数：<span class="ticketNum">${vo.currentVote}</span>
						</p> <a href="vote/dovote.do?cid=${vo.cid}&voteId=${vo.id}#row${status.index % 5}"></a>
					</li>
				<c:if test="${status.index % 5 == 4}">
				</ul>
				</c:if>
				</c:forEach>
			</div>
			<!--qidai-->
			<div class="tp-box-list qidai">
				<c:forEach items="${listA02}" var="vo" varStatus="status">
				<c:if test="${status.index % 5 == 0}">
				<ul>
				</c:if>
					<li><img src="${imageUrlPrefix}/images/A0${vo.cid}/${vo.imgPic}" alt="${vo.vname}" />
						<p class="tp-box-title">${vo.vname}</p>
						<p class="tp-box-ticketNum">
							票数：<span class="ticketNum">${vo.currentVote}</span>
						</p> <a href="vote/dovote.do?cid=${vo.cid}&voteId=${vo.id}#row${status.index % 5}"></a>
					</li>
				<c:if test="${status.index % 5 == 4}">
				</ul>
				</c:if>
				</c:forEach>
			</div>
			<!--danji-->
			<div class="tp-box-list danji">
				<c:forEach items="${listA03}" var="vo" varStatus="status">
				<c:if test="${status.index % 5 == 0}">
				<ul>
				</c:if>
					<li><img src="${imageUrlPrefix}/images/A0${vo.cid}/${vo.imgPic}" alt="${vo.vname}" />
						<p class="tp-box-title">${vo.vname}</p>
						<p class="tp-box-ticketNum">
							票数：<span class="ticketNum">${vo.currentVote}</span>
						</p> <a href="vote/dovote.do?cid=${vo.cid}&voteId=${vo.id}#row${status.index % 5}"></a>
					</li>
				<c:if test="${status.index % 5 == 4}">
				</ul>
				</c:if>
				</c:forEach>
				<!--ul-->
			</div>
			<!--wangluo-->
			<div class="tp-box-list wangluo">
				<c:forEach items="${listA04}" var="vo" varStatus="status">
				<c:if test="${status.index % 5 == 0}">
				<ul>
				</c:if>
					<li><img src="${imageUrlPrefix}/images/A0${vo.cid}/${vo.imgPic}" alt="${vo.vname}" />
						<p class="tp-box-title">${vo.vname}</p>
						<p class="tp-box-ticketNum">
							票数：<span class="ticketNum">${vo.currentVote}</span>
						</p> <a href="vote/dovote.do?cid=${vo.cid}&voteId=${vo.id}#row${status.index % 5}"></a>
					</li>
				<c:if test="${status.index % 5 == 4}">
				</ul>
				</c:if>
				</c:forEach>
				<!--ul-->
			</div>
			<!--sjip-->
			<div class="tp-box-list sjip">
				<c:forEach items="${listA05}" var="vo" varStatus="status">
				<c:if test="${status.index % 5 == 0}">
				<ul>
				</c:if>
					<li><img src="${imageUrlPrefix}/images/A0${vo.cid}/${vo.imgPic}" alt="${vo.vname}" />
						<p class="tp-box-title">${vo.vname}</p>
						<p class="tp-box-ticketNum">
							票数：<span class="ticketNum">${vo.currentVote}</span>
						</p> <a href="vote/dovote.do?cid=${vo.cid}&voteId=${vo.id}#row${status.index % 5}"></a>
					</li>
				<c:if test="${status.index % 5 == 4}">
				</ul>
				</c:if>
				</c:forEach>
			</div>
			<!--sjip-->
		</div>
	</div>
	<div class="footer">
		<div class="footer-main">
			<div class="footer-l">
				<div class="footer-title">关于M.H.A大会</div>
				<ul class="footer-list">
					<li><a href="#">关于大会</a></li>
					<li><a href="#">大会日程</a></li>
					<li><a href="#">参加大会</a></li>
					<li><a href="#">合作伙伴</a></li>
				</ul>
			</div>
			<div class="footer-m">
				<div class="footer-title">关于硬核联盟</div>
				<p>硬核联盟于2014年8月1日正式成立，英文名称为Mobile Hardcore
					Alliance(简称M.H.A)是由玩咖欢聚文化传媒（北京）有限公司联手国内一线智能手机制造商：OPPO、vivo、酷派、金立、联想、华为发起成立的，联盟的统一活动由玩咖欢聚文化传媒（北京）有限公司负责，硬核联盟指定玩咖为联盟官方媒体</p>
			</div>
			<div class="footer-r">
				<div class="footer-title">联系我们</div>
				<ul class="footer-list">
					<li><a>地址:&nbsp;北京市朝阳区安立路60号<br />
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;润枫德尚苑B座2401
					</a></li>
					<li><a>电话:&nbsp;1873023087</a></li>
				</ul>
			</div>
		</div>
	</div>
	<!--footer-->
	<div class="copyright">Copyright © 2015Banquanxinxi Banquanxinxi.
		ICP备案号: 粤ICP备100001号-1</div>
</body>
<script>
$(function(){
	$(".tp-box-list").eq(0).show();	
	$("#tp-nav ul li").click(function(){
		index=$(this).index();
		$(this).addClass("active").siblings().removeClass("active");
		$(".tp-box-list").eq(index).show().siblings().hide();	
	});
})
</script>
</html>

