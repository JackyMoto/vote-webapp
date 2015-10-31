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
	<form action="vote/addvote.do" method="post">
		<table width="50%">
			<tr>
				<td>名称:</td>
				<td><input name="vname"/></td>
			</tr>
			<tr>
				<td>类型:</td>
				<td><input name="cid"/></td>
			</tr>
			<tr>
				<td>图片:</td>
				<td><input name="imgPic"/></td>
			</tr>
			<tr>
				<td>二维码:</td>
				<td><input name="qrPic"/></td>
			</tr>
			<tr>
				<td><input type="submit"/></td>
			</tr>
		</table>
	</form>
</body>
</html>