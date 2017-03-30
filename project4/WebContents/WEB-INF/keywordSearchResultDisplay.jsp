<!DOCTYPE html>
<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<html>
<head>

	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="">
	<meta name="author" content="">


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
							<div style="width: 100%" id = "qq" >
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
								<table class="table table-bordered table-striped">
									<thead>
										<tr>
											<th>
												No
											</th>
											<th>
												ItemId
											</th>
											<th>
												Name
											</th>
										</tr>
									</thead>
									<tbody>
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
									</tbody>
								</table>
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
							</div>

							</div>
						</div>
					</div>
					<!-- /.col-lg-12 -->
				</div>
				<!-- /.row -->

		</div>
		<!-- page wrapper-->
	</div>
	<!--Final wrapper-->



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
