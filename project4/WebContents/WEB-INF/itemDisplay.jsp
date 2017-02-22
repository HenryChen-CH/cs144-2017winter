<!DOCTYPE html>
<%@ page import="edu.ucla.cs.cs144.*" %>
<html>
<head>
    <%
        Item item = (Item)request.getAttribute("item");
        item.sortedBids();
    %>
	<title><%= "Item"+item.id%></title>

    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap.min.css">
    <script type="text/javascript">
        function initialize() {
            <% if (item.location.latitude != null && item.location.latitude.length() > 0) {
                %>
                var latlng = new google.maps.LatLng(<%= item.location.latitude%>,<%= item.location.longitude%>);
                var myOptions = {
                    zoom: 12, // default is 8
                    center: latlng,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };
                var map = new google.maps.Map(document.getElementById("map_canvas"),
                    myOptions);
                new google.maps.Marker({
                    map: map,
                    position: latlng
                });
            <%
            } else {
            %>
                var latlng = new google.maps.LatLng(0, 0);
                var myOptions = {
                    zoom: 1, // default is 8
                    center: latlng,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };
                var map = new google.maps.Map(document.getElementById("map_canvas"),
                    myOptions);
                var geocoder = new google.maps.Geocoder;
                function codeAddress() {
                    var address = "<%= item.location.location%>";
                    geocoder.geocode( { 'address': address}, function(results, status) {
                        if (status == 'OK') {
                            map.setCenter(results[0].geometry.location);
                            map.setZoom(12);
                            new google.maps.Marker({
                                map: map,
                                position: results[0].geometry.location
                            });
                        } else {
                            alert('Geocode was not successful for the following reason: ' + status);
                        }
                    });
                };
                codeAddress();
            <%
            }
            %>
        }
    </script>

    <script async defer
            src="https://maps.googleapis.com/maps/api/js?key=AIzaSyD4Uy28IziX6QmJv0ze_9_u5VfU84pp_PI&library=&callback=initialize">
    </script>

    <style type="text/css">
        html {
            height: 100%
        }

        body {
            height: 100%; margin: 0px;
            padding: 0px
        }

        #map_canvas {
            height: 100%;
            margin: 3px auto;
            height: 300px;
        }
    </style>
</head>
<body>
<div>
    <form method="GET" action="/eBay/item">
        <p>
            <label >Item ID:</label>
            <input type="text" name="id"> <br>
        </p>

        <input type="submit">
    </form>
</div>
<div>
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
</div>
<div id="map_canvas" style="width:50%;"></div>
<div>
    <% if (item.bids.size() > 0) { %>
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
</div>

</body>
</html>