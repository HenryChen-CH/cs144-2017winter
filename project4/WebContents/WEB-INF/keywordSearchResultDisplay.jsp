<!DOCTYPE html>
<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<html>
<head>
	<title>Keyword Search Result</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dropdown.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap.min.css">
    <script src="${pageContext.request.contextPath}/autocomplete.js" type='text/javascript'></script>
    <script type="text/javascript">
        window.onload = function () {
            var oTextbox = new AutoSuggestControl(document.getElementById("q"), new StateSuggestions());
        }
    </script>
</head>
<body>
<div style="width: 20%">
    <form method="GET" action="/eBay/search">
        <div class="form-group">
            <label for="q">Query</label>
            <input autocomplete="off" class="form-control" id = "q" type="text" name="q" placeholder="Enter Query">
        </div>
        <div class="form-group">
            <label for="numResultsToSkip">Num Results To Skip:</label>
            <input class="form-control" id = "numResultsToSkip" type="number" min="0" value="0" name="numResultsToSkip" placeholder="Num Results To Skip">
        </div>
        <div class="form-group">
            <label for="numResultsToReturn">Num Results To Return:</label>
            <input class="form-control" id = "numResultsToReturn" type="number" min="10" value="10" name="numResultsToReturn" placeholder="Num Results To Return">
        </div>
        <button type="submit" class="btn btn-primary">Submit</button>
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
        String next = "/eBay/search?q="+q+"&numResultsToSkip="+(results.length != numResultsToReturn? numResultsToSkip: (numResultsToSkip+numResultsToReturn))+"&numResultsToReturn="+numResultsToReturn;
    %>
    <a href="<%= prev%>"><span>prev</span></a><span>&nbsp&nbsp</span>
    <a href="<%= next%>"><span>next</span></a>
</div>
</body>
</html>
