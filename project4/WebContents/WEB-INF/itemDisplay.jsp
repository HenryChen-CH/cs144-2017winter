<!DOCTYPE html>
<html>
<head>
	<title></title>
	<% String xml = (String)request.getAttribute("xml"); %>
</head>
<body>
	<p><%= xml.length() == 0 ? "Item Not Found" : xml%></p>
</body>
</html>