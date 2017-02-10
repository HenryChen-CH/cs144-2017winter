<!DOCTYPE html>
<%@ page import="edu.ucla.cs.cs144.*" %>
<html>
<head>
    <%
        Item item = (Item)request.getAttribute("item");
        item.sortedBids();
    %>
	<title><%= "Item"+item.id%></title>
</head>
<body>
	<% if (item == null) { %>
        <p>Item Not foune</p>
    <%
        } else {
    %>
    <table border=1 align="center" width="50%">
        <tr>
            <td colspan="2" align="center">Item Information</td>
        </tr>
        <tr>
            <td>ItemID:</td>
            <td><%= item.id%></td>
        </tr>
        <tr>
            <td>Name:</td>
            <td><%= item.name%></td>
        </tr>
        <tr>
            <td>Category:</td>
            <td>
                <ul>
                    <% for (String cat: item.categories) { %>
                    <li><%= cat%></li>
                    <%
                        }
                    %>
                </ul>
            </td>
        </tr>
        <tr>
            <td>Currently:</td>
            <td><%= item.currently%></td>
        </tr>
        <% if (item.firstBId != null && item.firstBId.length() != 0) { %>
            <tr>
                <td>First Bid:</td>
                <td><%= item.firstBId%></td>
            </tr>
        <%
        }
        %>
        <tr>
            <td>Num of bids:</td>
            <td><%= item.numOfBids%></td>
        </tr>
        <tr>
            <td>Location:</td>
            <td>
                <ul>
                    <li><%= item.location.location%></li>
                    <% if (item.location.latitude!= null && item.location.latitude.length() != 0) { %>
                        <li><%= "Latitude: "+item.location.latitude%></li>
                        <li><%= "Longitude: "+item.location.longitude%></li>
                    <%
                    }
                    %>
                </ul>
            </td>
        </tr>
        <tr>
            <td>Country:</td>
            <td><%= item.country%></td>
        </tr>
        <tr>
            <td>Started:</td>
            <td><%= item.started%></td>
        </tr>
        <tr>
            <td>Ends:</td>
            <td><%= item.ends%></td>
        </tr>
        <tr>
            <td>Seller:</td>
            <td>
                <ul>
                    <li><%= "UserID: "+item.seller.userID%></li>
                    <li><%= "Rating: "+item.seller.rating%></li>
                </ul>
            </td>
        </tr>
        <tr>
            <td>Description:</td>
            <td><%= item.description%></td>
        </tr>
    </table>
    <%
        }
    %>

    <% if (item.bids.size() > 0) { %>
    <br>
    <br>
    <table border="1" align="center" width="50%">
        <tr>
            <td colspan="4" align="center">Bids Information</td>
        </tr>
        <tr>
            <td>No</td>
            <td>Bidder</td>
            <td>Time</td>
            <td>Amount</td>
        </tr>
        <% int i = 0;%>
        <% for (Bid bid: item.bids) { %>
            <tr>
                <td><%= ++i %></td>
                <td>
                    <ul>
                        <li><%= "UserID: "+bid.bidder.userID%></li>
                        <li><%= "Rating: "+bid.bidder.rating%></li>
                    </ul>
                </td>
                <td><%= bid.time%></td>
                <td><%= bid.amount%></td>
            </tr>
        <%
        }
        %>
    </table>
    <%
        }
    %>
</body>
</html>