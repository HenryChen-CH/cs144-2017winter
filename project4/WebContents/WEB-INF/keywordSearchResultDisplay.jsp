<!DOCTYPE html>
<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<html>
<head>
	<title>Keyword Search Result</title>
</head>
<body>
<div>
    <form method="GET" action="/eBay/search">
        <p>
            <label for="">Query:</label>
            <input type="text" name="q">
        </p>
        <p>
            <label for="">Num Results To Skip:</label>
            <input type="number" name="numResultsToSkip" min="0" value="0">
        </p>
        <p>
            <label for="">Num Results To Return:</label>
            <input type="number" name="numResultsToReturn" min="0" value="10">
        </p>

        <input type="submit">
    </form>
</div>

<div>
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
            <td><a href="<%= "/eBay/item?id="+results[i].getItemId()%>"><%= results[i].getItemId()%></a></td>
            <td><%= results[i].getName()%></td>
        </tr>
        <%
            }
        %>
    </table>
</div>

<div align="center">
    <%
        String q = (String)request.getParameter("q");
        int numResultsToSkip = Integer.parseInt((String)request.getParameter("numResultsToSkip"));
        int numResultsToReturn = Integer.parseInt((String)request.getParameter("numResultsToReturn"));
        String prev = "/eBay/search?q="+q+"&numResultsToSkip="+Math.max(0, numResultsToSkip-numResultsToReturn)+"&numResultsToReturn="+numResultsToReturn;
        String next = "/eBay/search?q="+q+"&numResultsToSkip="+(numResultsToSkip+numResultsToReturn)+"&numResultsToReturn="+numResultsToReturn;
    %>
    <a href="<%= prev%>"><span>prev</span></a><span>&nbsp&nbsp</span>
    <a href="<%= next%>"><span>next</span></a>
</div>
</body>
</html>
