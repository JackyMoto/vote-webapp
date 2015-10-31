<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<% 
String path = request.getContextPath(); 
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; 
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>"> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Vote Page List</title>
</head>
<body>
	<table width="100%" border="1">
		<tr>
			<td>当前排名</td>
			<td>所属分类</td>
			<td>描述</td>
			<td>图片</td>
			<td>二维码</td>
			<td>当前票数</td>
			<td>操作</td>
		</tr>
		<c:forEach items="${list}" var="vo" varStatus="status">
		<tr>
			<td>${status.index + 1}</td>
			<td>${vo.vname}</td>
			<td>${vo.cid}</td>
			<td><img src="img/A0${vo.cid}/{${vo.imgPic}}"/></td>
			<td>${vo.qrPic}</td>
			<td>${vo.currentVote}</td>
				<td>
					<form action="vote/dovote.do" method="post" id="form">
					<input type="hidden" name="voteId" value="${vo.id}"/>
					<input type="hidden" name="cid" value="${vo.cid}"/>
					<input type="button" class="button" value="获取投票验证码" />
					</form>
				</td>
		</tr>
		</c:forEach>
		<tr>
			<td>
				<a href="#">首页</a>
				<a href="#">2</a>
				<a href="#">3</a>
				<a href="#">4</a>
				<a href="#">...</a>
				<a href="#">17</a>
				<a href="#">18</a>
				<a href="#">19</a>
				<a href="#">尾页</a>
			</td>
			<td><a href="vote/addvotepage.do">添加投票项</a></td>
		</tr>
	</table>
</body>
<script src="http://libs.baidu.com/jquery/1.11.1/jquery.min.js"></script>
<script type="text/javascript">
	$(".button").click(function(){
    	$(this).after("<img src='vote/getvotecode.do'/><input name='codeResult' type='text' value='test'/><input type='submit' value='投票'/>");
    	$(this).hide();
	});
</script>
</html>