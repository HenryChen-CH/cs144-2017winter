<!DOCTYPE html>
<%@ page import="edu.ucla.cs.cs144.*" %>
<html>
<head>
  <meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="">
	<meta name="author" content="">

    <script src="autocomplete.js" type='text/javascript'></script>
    <script type="text/javascript">
        window.onload = function () {
            var oTextbox = new AutoSuggestControl(document.getElementById("q"), new StateSuggestions());
        }
    </script>

    <style type="text/css">
        form {
            display: table;
        }

        p {
            display: table-row;
        }

        label {
            display: table-cell;
        }

        input {
            display: table-cell;
        }
    </style>

	<!-- Bootstrap Core CSS -->
	<link href="${pageContext.request.contextPath}/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

	<!-- MetisMenu CSS -->
	<link href="${pageContext.request.contextPath}/vendor/metisMenu/metisMenu.min.css" rel="stylesheet">

	<!-- Custom CSS -->
	<link href="${pageContext.request.contextPath}/dist/css/sb-admin-2.css" rel="stylesheet">

	<!-- Morris Charts CSS -->
	<link href="${pageContext.request.contextPath}/vendor/morrisjs/morris.css" rel="stylesheet">

	<!-- Custom Fonts -->
	<link href="${pageContext.request.contextPath}/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
	<!--[if lt IE 9]>
	<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
	<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
	<![endif]-->

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
                            console.log('Geocode was not successful for the following reason: ' + status);
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



  <div id="wrapper">
		<nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="index.html">Welcome to Item Search System</a>
			</div>
			<!-- /.navbar-header -->


			<div class="navbar-default sidebar" role="navigation">
				<div class="sidebar-nav navbar-collapse">
					<ul class="nav" id="side-menu">
						<li class="sidebar-search">
							<div style="width: 100%" id = "qq">
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
										<input class="form-control" id = "numResultsToReturn" type="number" min="0" value="10" name="numResultsToReturn" placeholder="Num Results To Return">
									</div>
									<button type="submit" class="btn btn-primary"><i class="fa fa-search"></i></button>
								</form>
							</div>
							<!-- /input-group -->
						</li>
						<li>
							<a href="/eBay/search"><i class="fa fa-dashboard fa-fw"></i> Dashboard</a>
						</li>
					</ul>
				</div>
				<!-- /.sidebar-collapse -->
			</div>
			<!-- /.navbar-static-side -->
		</nav>

		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">Dashboard</h1>
				</div>
				<!-- /.col-lg-12 -->
			</div>
			<!-- /.row -->

      <div class="row">
				<div class="col-lg-12">
					<div class="panel panel-default">
						<div class="panel-body">
							<div class="table-responsive">
                <% if (item == null) { %>
                <p>Item Not found</p>
                <%
                } else {
                %>
								<table class="table table-bordered table-striped">
									<thead>
                    <tr>
                      <td colspan="2" align="center">Item Information</td>
                    </tr>
									</thead>

									<tbody>
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

									</tbody>
								</table>
                <%
                    }
                %>
							</div>

							</div>
						</div>
					</div>
					<!-- /.col-lg-12 -->
				</div>
				<!-- /.row -->

      <div class="row">
       <div class="col-lg-6">
         <div id="map_canvas" style="width:100%;"></div>
       </div>
       <div class="col-lg-6">
           <% if (item.bids.size() > 0) { %>
           <table class="table table-bordered table-striped">
               <tr>
                 <td colspan="4" align="center">Bid Information</td>
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

     </div>


       <!-- /.col-lg-12 -->

		</div>
		<!-- page wrapper-->
	</div>
	<!--Final wrapper-->

<!--
<div>
    <form method="GET" action="/eBay/item">
        <p>
            <label >Item ID:</label>
            <input type="text" name="id"> <br>
        </p>

        <input type="submit">
    </form>
</div> -->





<script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.min.js"></script>

<!-- Metis Menu Plugin JavaScript -->
<script src="${pageContext.request.contextPath}/vendor/metisMenu/metisMenu.min.js"></script>

<!-- Morris Charts JavaScript -->
<script src="${pageContext.request.contextPath}/vendor/raphael/raphael.min.js"></script>
<script src="${pageContext.request.contextPath}/vendor/morrisjs/morris.min.js"></script>
<script src="${pageContext.request.contextPath}/data/morris-data.js"></script>

<!-- Custom Theme JavaScript -->
<script src="${pageContext.request.contextPath}/dist/js/sb-admin-2.js"></script>

</body>
</html>
