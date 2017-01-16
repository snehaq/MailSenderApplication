<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page session="false" %>
<script
	src="${pageContext.request.contextPath}/javascripts/jquery.min.js"
	type="text/javascript"></script>
<script src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" media="all"
	href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/themes/smoothness/jquery-ui.css" />
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script
	src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.11.1/jquery.validate.min.js"></script>
<script
	src="${pageContext.request.contextPath}/javascripts/recoverPassword.js"
	type="text/javascript"></script>


<link rel="stylesheet"
	href="${pageContext.request.contextPath}/stylesheets/Login.css">
<title>Insert title here</title>
</head>
<body>
	<div id="login">
		<%
		String message = (String) request.getAttribute("msg");
	%>
		<div class="container">
			<%if(message!=null||message==""){ %>
			<div class="row">
				<div class="col-xs-12">
					<h1><%=message %></h1>
				</div>
			</div>
			<%}else{ %>
			<div class="row">
				<div class="col-xs-12">
					<div class="form-wrap">
						<h1>Recover Password</h1>
						<div id="authFailed">
							<h2></h2>
						</div>
						<form role="form" method="post" id="recover-form"
							autocomplete="off">
							<div class="form-group">
								<label for="new_pass" class="sr-only">New Password</label> <input
									type="password" name="new_pass" id="new_pass"
									class="form-control" placeholder="New Password" />
							</div>
							<div class="form-group">
								<label for="conf_pass" class="sr-only">Confirm Password</label>
								<input type="password" name="conf_pass" id="conf_pass"
									class="form-control" placeholder="Confirm Password">
							</div>

							<input type="submit" id="btn-setPassword"
								class="btn btn-custom btn-lg btn-block" value="Set Password">
						</form>
						<hr>

					</div>
				</div>
			</div>
			<%} %>
		</div>
	</div>
</body>
</html>