<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
<center>
<div>
<form action="uploadFile.jsp" method="post" enctype="multipart/form-data">
<table border="1px">
<thead>视频上传</thead>
<tr>
<td style="background-color: gray;">选择视频</td>
<td><input type="file" name="vedioFile" /></td>
</tr>
<tr>
<td style="background-color: gray;">视频标题</td>
<td><input type="text" name="title" /></td>
</tr>
<tr>
<td style="background-color: gray;">视频描述</td>
<td><textarea name="descript"></textarea></td>
</tr>
<tr>
<td style="background-color: gray;">&nbsp;&nbsp;&nbsp;</td>
<td><input type="submit" value="上传视频"/><input type="reset" value="重新选择"/></td>
</tr>
<tr>
<td><a href="#" onclick="go();">进入视频列表</a>
</td>
</tr>
</table>
</form>
</div>
</center>
<script type="text/javascript">
function go(){
	var url = "queryAll.jsp";
	location.href = url;
}
</script>
</body>

</html>