<!DOCTYPE html>
<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<html>
<head>
	<title>Keyword Search Result</title>
</head>
<body>
<table border=1 align="center" width="50%">
<tr>
	<td>No</td>
	<td>ItemID</td>
	<td>Name</td>
</tr>
<%
	SearchResult[] results = (SearchResult[])request.getAttribute("results");
%>
<% for (int i = 0; i < results.length; i++) {
	%>
	<tr>
		<td><%= i+1%></td>
		<td><%= results[i].getItemId()%></td>
		<td><%= results[i].getName()%></td>
	</tr>
	<%
	}
%>
</table>
</body>
</html>
