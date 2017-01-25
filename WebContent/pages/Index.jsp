<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
<%@page import=" javax.servlet.http.HttpSession"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page session="false"%>
<script
	src="${pageContext.request.contextPath}/javascripts/jquery.min.js"
	type="text/javascript"></script>
<script
	src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.11.1/jquery.validate.min.js"></script>
<script src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" media="all"
	href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/themes/smoothness/jquery-ui.css" />
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/javascripts/Index.js"
	type="text/javascript"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/stylesheets/Index.css">
<title>Mail Sender</title>
</head>
<body>
	<%
		HttpSession session = request.getSession(false);
		if (session == null) {
	%>
	<script>
	redirectToLoginPage();
	</script>
	<%
		}else{
	%>
	<script>
	callDashBoardInController();
	</script>
	<%
		String message = (String) request.getAttribute("Message");
	%>
	<div class="container" id="container">
		<%
			if (message != null) {
		%>
		<div class="row">
			<div class="col-md-12" id="headingDiv">
				<h4 style="color: #59b7b6;"><%=message%></h4>
			</div>
		</div>
		<%
			} else {
		%>
		<div class="row">
			<div class="col-md-3"></div>
			<div class="col-md-6" id="headingDiv">
				<h1 style="color: #404040;">Email Generator</h1>
			</div>
			<form
				action="${pageContext.request.contextPath}/controller?mode=logout"
				name="logout" id="logout" method="post">
				<div class="col-md-3">
					<input type="submit" id="logOutBtn"
						class="btn btn-default logOutBtn" value="logout" />
				</div>
			</form>
		</div>
		<form enctype="multipart/form-data" name="uploadXlsFileForm"
			id="xlsUploadForm">
			<div class="row" id="uploadFileUi" style="background-color: #f7f7f7;">
				<div class="col-md-2"></div>
				<div class="col-md-10" style="margin-top: 20px;">
					<input type="text" id="selectFile" placeholder="Choose File"
						disabled="disabled" class="selectFileTBoxes" name="selectFile" />
					<div class="fileUpload btn btn-primary">
						<span>Select File</span> <input id="selectFile_replacer"
							type="file" class="upload" name="fname" />
					</div>
					<!-- <button type="Submit" id="uploadBtn" class="btn btn-default">Upload</button> -->
					<input type="submit" id="uploadBtn"
						class="btn btn-default uploadBtns" value="Upload" />
				</div>
			</div>
			<div class="row" id="uploadFileLabel"
				style="background-color: #f7f7f7;">
				<div class="col-md-2"></div>
				<div class="col-md-10">
					<h4 style="margin-top: 10px; color: #333333; margin-left: 30px;">Choose
						xls file to upload.</h4>
				</div>
			</div>
			<div class="row" id="uploadFileMsg"
				style="background-color: #f7f7f7;">
				<div class="col-md-2"></div>
				<div class="col-md-10">
					<h4 id="uploadFileMessage"
						style="margin-top: 10px; color: #49b6a9; margin-left: 30px; max-width: 400px;">
					</h4>
				</div>
			</div>
		</form>

		<form enctype="multipart/form-data" name="uploadZipForm"
			id="imageUploadForm">

			<div class="row" id="uploadZipUi" style="background-color: #f7f7f7;">
				<div class="col-md-2"></div>
				<div class="col-md-10" style="margin-top: 20px;">
					<input type="text" id="selectZip" placeholder="Choose File"
						disabled="disabled" class="selectFileTBoxes" />
					<div class="fileUpload btn btn-primary">
						<span>Select File</span> <input id="selectZip_replacer"
							type="file" class="uploadZip" name="fname" />
					</div>
					<!-- <button type="Submit" id="uploadBtn" class="btn btn-default">Upload</button> -->
					<input type="submit" id="uploadZipBtn"
						class="btn btn-default uploadBtns" value="Upload" />
				</div>
			</div>
			<div class="row" id="uploadFileLabel"
				style="background-color: #f7f7f7;">
				<div class="col-md-2"></div>
				<div class="col-md-10">
					<h4 style="margin-top: 10px; color: #333333; margin-left: 30px;">Choose
						image to upload.</h4>
				</div>
			</div>
			<div class="row" id="imageFileMsg" style="background-color: #f7f7f7;">
				<div class="col-md-2"></div>
				<div class="col-md-10">
					<h4 id="imageFileMessage"
						style="margin-top: 10px; color: #49b6a9; margin-left: 30px;">
					</h4>
				</div>
			</div>

		</form>

		<div class="row" id="changeStatusUi"
			style="background-color: #f7f7f7; margin-top: 20px; padding-top: 10px;">
			<div class="col-md-5" style="margin-top: 15px;">
				<h4 style="margin-top: 0px;">
					<input type="radio" name="status" value="enable" checked="checked">
					Enable <input type="radio" name="status" value="disable">
					Disable

				</h4>
			</div>
			<div class="col-md-2"></div>
			<form enctype="multipart/form-data" name="setTimeForm"
				id="setTimeForm">
				<div class="col-md-1" style="margin-top: 15px;">
					<select class="selectpicker" id="hours" name="hours">
						<%
							for (int i = 1; i <= 12; i++) {
						%><option><%=i%></option>
						<%
							}
						%>
					</select>
				</div>
				<div class="col-md-1" style="margin-top: 15px;">
					<select class="selectpicker" id="minutes" name="minutes">
						<%
							for (int i = 0; i < 60; i++) {
									if (i < 10) {
						%><option>0<%=i%></option>
						<%
							} else {
						%><option><%=i%></option>
						<%
							}
								}
						%>
					</select>
				</div>
				<div class="col-md-1" style="margin-top: 15px;">
					<select class="selectpicker" id="am/pm" name="am/pm">
						<option>AM</option>
						<option>PM</option>
					</select>
				</div>
				<div class="col-md-2" style="margin-top: 5px;">
					<input type="submit" id="setTime" class="btn btn-default"
						value="set"
						style="width: 80px; background-color: #49b6a9; color: #fff;" />
				</div>
			</form>
		</div>
		<div class="row" id="changeStatusLabel"
			style="background-color: #f7f7f7;">
			<div class="col-md-5">
				<h4 style="margin-top: 10px; color: #333333; margin-left: 30px;">
					Select the scheduler status</h4>
			</div>
			<div class="col-md-2"></div>
			<div class="col-md-5">
				<h4 style="margin-top: 10px; color: #333333;">Select the
					scheduler time</h4>
			</div>
		</div>
		<div class="row" style="background-color: #f7f7f7;">
			<div class="col-md-3"></div>
			<div class="col-md-7" id="status_timeMsg"></div>
		</div>

		<div class="row" id="selectTemplatesLabel"
			style="background-color: #f7f7f7; margin-top: 20px;">
			<div class="col-md-3"></div>
			<div class="col-md-6">
				<h3 style="margin-top: 10px; color: #333333; margin-left: 30px;">
					Select templates for mail</h3>
			</div>
		</div>
		<!-- <div class="row" id="zoomedImg">
		
		
		</div> -->

		<div id="myModal" class="modal">

			<!-- Modal content -->
			<div class="modal-content">
				<span class="close">&times;</span> <img id="imgPreview" src=""
					style="height: 90%; width: 100%" />
			</div>
		</div>
		<form name="setTemplatesForm" id="setTemplatesForm">
			<div class="row"
				style="background-color: #f7f7f7; padding-top: 3%; max-height: 300px; overflow-y: scroll; overflow-x: scroll;"
				id="templateChooserUi"></div>
			<div id="checkBoxValidationMsg"></div>
			<div class="row"
				style="background-color: #f7f7f7; padding-bottom: 20px; padding-top: 20px; padding-left: 150px;">
				<div class="col-md-2"></div>
				<div class="col-md-6">
					<input type="submit" class="setTemplate btn btn-default"
						value="set"
						style="color: #fff; width: 200px; height: 50px; background-color: #49b6a9;"
						id="setTemplateSubmit" />
				</div>
			</div>
			<div class="row" style="background-color: #f7f7f7;">
				<div class="col-md-2"></div>
				<div class="col-md-10" id="selectTemplateMsg"></div>
			</div>
		</form>


		<div class="row" id="mailLogsLabel"
			style="background-color: #f7f7f7; margin-top: 20px;">
			<div class="col-md-3"></div>
			<div class="col-md-6">
				<h3 style="margin-top: 10px; color: #333333; margin-left: 80px;">
					E-Mail Logs</h3>
			</div>
		</div>
		<div class="row" id="selectMailLogs"
			style="background-color: #f7f7f7; padding-left: 30px;">
			<div class="col-md-8"></div>
			<div class="col-md-2" style="padding-right: 0; padding-left: 20px;">
				<h4>Search By</h4>
			</div>
			<div class="col-md-2" style="padding-top: 7px; padding-left: 0px;">
				<select class="selectpicker" id="mailLogsSelectOption"
					name="mailLogsSelectOption">
					<option value="LastWeek">Last Week</option>
					<option value="AllLogs">All Logs</option>
				</select>
			</div>
		</div>
		<div class="row"
			style="background-color: #f7f7f7; padding-bottom: 20px; padding-top: 20px;"
			id="mailLogsRow"></div>
		<%
			}
		%>
	</div>
	<%} %>
</body>
</html>