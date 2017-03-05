<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="edu.ucla.cs.cs144.*" %>
<html>
<head>
    <title>Pay Now</title>
    <link href="${pageContext.request.contextPath}/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="${pageContext.request.contextPath}/vendor/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/dist/css/sb-admin-2.css" rel="stylesheet">

    <!-- Morris Charts CSS -->
    <link href="${pageContext.request.contextPath}/vendor/morrisjs/morris.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="${pageContext.request.contextPath}/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

</head>
<body>
<div class="col-lg-6">
    <%
        int securePort = 8443;
        String secureUrl = "https://"+request.getServerName()+":"+securePort+request.getContextPath()+"/buy";
        Item item = (Item)request.getAttribute("item");
    %>
    <div class="table-responsive">
        <table class="table table-bordered table-striped">
            <tr><td colspan="2" align="center">Pay Now</td></tr>
            <tr>
                <td>ItemID</td>
                <td><%= item.id%></td>
            </tr>
            <tr>
                <td>ItemName</td>
                <td><%= item.name%></td>
            </tr>
            <tr>
                <td>Buy Price</td>
                <td><%= item.buyPrice%></td>
            </tr>
        </table>
    </div>
    <form method="post" action="<%= secureUrl%>">
        <div class="form-group">
            <label for="creditCard">Credit Card</label>
            <input class="form-control" id = "creditCard" type="text" name="creditCard" placeholder="Enter Credit Card Number">
        </div>
        <button type="submit" class="btn btn-primary">Submit</i></button>
    </form>
</div>
</body>
</html>
