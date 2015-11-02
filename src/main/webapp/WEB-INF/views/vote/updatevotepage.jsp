<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
String path = request.getContextPath(); 
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; 
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>"> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>更新投票内容</h1>
	<form action="vote/updatevote.do" method="post">
		<table width="50%">
			<tr>
				<td>名称:</td>
				<td><input name="vname" value="${vo.vname}"/></td>
			</tr>
			<tr>
				<td>图片:</td>
				<td><input name="imgPic" value="${vo.imgPic}"/></td>
			</tr>
			<tr>
				<td>二维码:</td>
				<td><input name="qrPic" value="${vo.qrPic}"/></td>
			</tr>
			<tr>
				<td><input type="submit"/></td>
			</tr>
		</table>
		<input type="hidden" name="voteId" value="${vo.id}"/>
		<input type="hidden" name="cid" value="${vo.cid}"/>
	</form>
</body>
</html>