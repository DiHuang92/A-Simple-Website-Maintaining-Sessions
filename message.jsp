<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Session Management</title>
</head>
<body>
<form action="SessionManagementServlet" method="post">
<p>NetID:dh626 &nbsp; Session: ${SessionID} &nbsp; Version: ${Version} &nbsp; Date: <%= new Date()%> </p>
<br>
<h3> ${Message}</h3> 
<br>
<input type="submit" name="Replace" value="Replace"> <input type="text" name="newMessage">
<br>
<input type="submit" name="Refresh" value="Refresh">
<br>
<input type="submit" name="Logout" value="Logout">
<br>
<p>Cookie: ${Cookie} &nbsp; Expires: ${Expires}</p>
</form>
</body>
</html>