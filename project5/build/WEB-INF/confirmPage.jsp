<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="edu.ucla.cs.cs144.*" %>
<html>
<head>
    <title>Pay Confirmation</title>
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
    <%
        Item item = (Item)request.getAttribute("item");
    %>
    <div>
        <div class="table-responsive col-lg-6 row">
            <table class="table table-bordered table-striped">
                <tr><td colspan="2" align="center">Pay Confirmation</td></tr>
                <tr>
                    <td>ItemID</td>
                    <td><%= item.id%></td>
                </tr>
                <tr>
                    <td>Item Name</td>
                    <td><%= item.name%></td>
                </tr>
                <tr>
                    <td>Buy Price</td>
                    <td><%= item.buyPrice%></td>
                </tr>
                <tr>
                    <td>Credit Card</td>
                    <td><%= request.getParameter("creditCard")%></td>
                </tr>
                <tr>
                    <td>Time</td>
                    <td><%= request.getAttribute("time")%></td>
                </tr>
            </table>
        </div>
    </div>
</body>
</html>
