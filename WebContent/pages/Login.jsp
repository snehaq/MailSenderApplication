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
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script
	src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.11.1/jquery.validate.min.js"></script>
<script
	src="${pageContext.request.contextPath}/javascripts/Login.js"
	type="text/javascript"></script>
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" media="all"
	href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/themes/smoothness/jquery-ui.css" />

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/stylesheets/Login.css">
<title>Login</title>
</head>
<body>
	<div id="login">
		<div class="container">
			<div class="row" id="loginUIRow">
				<div class="col-xs-12">
					<div class="form-wrap">
						<h1>Log in</h1>
						<div id="authFailed">
							<h2></h2>
						</div>
						<form role="form" method="post" id="login-form" autocomplete="off">
							<div class="form-group">
								<label for="email" class="sr-only">Email</label> <input
									type="username" name="username" id="username"
									class="form-control" placeholder="Username" />
							</div>
							<div class="form-group">
								<label for="key" class="sr-only">Password</label> <input
									type="password" name="key" id="key" class="form-control"
									placeholder="Password">
							</div>
							<div class="checkbox">
								<span class="character-checkbox" id="showPassword_checkBox"></span>
								<span class="label">Show password</span>
							</div>
							<input type="submit" id="btn-login"
								class="btn btn-custom btn-lg btn-block" value="Log in">
						</form>
						<hr>
						<a href="" class="forget" data-toggle="modal"
							data-target=".forget-modal">Forgot your password?</a>
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade forget-modal" tabindex="-1" role="dialog"
			aria-labelledby="myForgetModalLabel" aria-hidden="true"
			id="passwordRecoveryModal">
			<div class="modal-dialog modal-sm">
				<div class="modal-content" id="modalContent"
					style="min-height: 150px;">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">×</span> <span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title">Recovery password</h4>
					</div>
					<div class="modal-body">
						<p>Type your email account</p>
						<input type="email" name="recovery-email" id="recovery-email"
							class="form-control" autocomplete="off">
					</div>
					<!-- <div class="modal-footer"> -->
					<div class="row">
						<div class="col-md-2"></div>
						<div class="col-md-3">
							<button type="button" class="btn btn-default"
								data-dismiss="modal">Cancel</button>
						</div>

						<div class="col-md-3">
							<button type="button" class="btn btn-custom" id="recoveryBtn">Recovery</button>
						</div>
						<!-- </div> -->

						<!-- <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				<button type="button" class="btn btn-custom" id="recoveryBtn">Recovery</button> -->
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>
		<!-- /.modal -->
	</div>




</body>
</html>