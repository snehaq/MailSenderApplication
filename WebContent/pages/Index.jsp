<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
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

<script src="${pageContext.request.contextPath}/javascripts/Index.js"
	type="text/javascript"></script>


<link rel="stylesheet"
	href="${pageContext.request.contextPath}/stylesheets/Index.css">
<title>Mail Sender</title>
</head>
<body>
	<%
		ArrayList<HashMap> pastWeekMailLogs = (ArrayList<HashMap>) request.getAttribute("pastWeekMailLogs");
		ArrayList<String> templateImg = (ArrayList<String>) request.getAttribute("templateImg");
		ArrayList<String> templatesSelected = (ArrayList<String>) request.getAttribute("templatesSelected");
		String message = (String) request.getAttribute("Message");
	%>
	<div class="container" id="container">
		<%
			if (message != null) {
		%>
		<div class="row">
			<div class="col-md-12" id="headingDiv">
				<h4 style="color: #59b7b6;"><%=message%></h1>
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
			<form action="${pageContext.request.contextPath}/controller?mode=logout" name="logout" id="logout" method="post">
				<div class="col-md-3">	<input type="submit" id="logOutBtn"
						class="btn btn-default logOutBtn" value="logout" /></div>
			</form>
		</div>
		<form enctype="multipart/form-data" name="uploadXlsFileForm"
			id="xlsUploadForm">
			<div class="row" id="uploadFileUi" style="background-color: #f7f7f7;">
				<div class="col-md-2"></div>
				<div class="col-md-10" style="margin-top: 20px;">
					<input id="selectFile" placeholder="Choose File"
						disabled="disabled" class="selectFileTBoxes" />
					<div class="fileUpload btn btn-primary">
						<span>Select File</span> <input id="selectFileBtn" type="file"
							class="upload" name="fname" />
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
						style="margin-top: 10px; color: #d15353; margin-left: 30px; max-width: 400px;">

					</h4>
				</div>
			</div>
		</form>

		<form enctype="multipart/form-data" name="uploadZipForm"
			id="imageUploadForm">

			<div class="row" id="uploadZipUi" style="background-color: #f7f7f7;">
				<div class="col-md-2"></div>
				<div class="col-md-10" style="margin-top: 20px;">
					<input id="selectZip" placeholder="Choose File" disabled="disabled"
						class="selectFileTBoxes" />
					<div class="fileUpload btn btn-primary">
						<span>Select File</span> <input id="selectZipBtn" type="file"
							class="uploadZip" name="fname" />
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
						style="margin-top: 10px; color: #d15353; margin-left: 30px;">
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
							for (int i = 0; i <= 60; i++) {
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

		<%
			if (templateImg.isEmpty()) {
		%>
		<form name="setTemplatesForm" id="setTemplatesForm">
			<div class="row"
				style="background-color: #f7f7f7; padding-top: 3%; max-height: 300px; overflow-y: scroll; overflow-x: scroll;"
				id="templateChooserUi">

				<h3 style="color: #d15353; margin-left: 33%">No Templates to
					Display</h3>
			</div>
		</form>

		<%
			} else {
		%>
		<form name="setTemplatesForm" id="setTemplatesForm">
			<div class="row"
				style="background-color: #f7f7f7; max-height: 300px; overflow-y: scroll; overflow-x: scroll;"
				id="templateChooserUi">
				<%
					for (int i = 0; i < templateImg.size(); i++) {
				%>
				<div class="col-md-2  templateImages"
					style="margin: 10px; height: 120px; padding-bottom: 5%;">
					<img src=<%=templateImg.get(i)%> class="templateImgs"
						id=<%=templateImg.get(i)%> data-container="body"
						data-toggle="popover" data-placement="top"
						data-content="click to preview"
						style="height: 100%; width: 100%; outline: none; border-color: #9ecaed; box-shadow: 0 0 20px #9ecaed; border-radius: 5px;">
					<%
						String file = templateImg.get(i).split("/")[1];
									String finalComparer = file.split("\\.")[0];

									if (templatesSelected.contains(templateImg.get(i).split("/")[1].split("\\.")[0])) {
					%>
					<input type="checkbox" name="templateImgs" class="templates"
						value=<%=templateImg.get(i)%> checked
						style="margin-left: 40%; width: 20px; height: 20px; margin-bottom: 1%;" />
					<%
						} else {
					%>
					<input type="checkbox" name="templateImgs" class="templates"
						value=<%=templateImg.get(i)%>
						style="margin-left: 40%; margin-bottom: 1%; width: 20px; height: 20px;" />
					<%
						}
					%>

				</div>

				<%
					}
				%>
			</div>
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
			<div class="row" style="background-color: #f7f7f7; ">
			<div class="col-md-2"></div>
			<div class="col-md-10" id="selectTemplateMsg"></div>
			</div>
		</form>

		<%
			}
		%>


		<%
			}
		%>
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
			style="background-color: #f7f7f7; padding-bottom: 20px; padding-top: 20px;">
			<%
				if (pastWeekMailLogs.size() == 0) {
			%>
			<div class="col-md-12" id="mailLogs">
				<h2>No Mail Logs Avalable</h2>
			</div>
			<%
				} else {
			%>

			<div class="col-md-12" id="mailLogs">
				<table class="table table-hover">
					<thead>
						<tr>
							<th>id</th>
							<th>Name</th>
							<th>Email</th>
							<th>Date of Delivery</th>
						</tr>
					</thead>
					<tbody>
						<%
							for (int i = 0; i < pastWeekMailLogs.size(); i++) {
									HashMap hm = new HashMap();
									hm = (HashMap) pastWeekMailLogs.get(i);
						%>
						<tr>
							<td><%=hm.get("id")%></td>
							<td><%=hm.get("name")%></td>
							<td><%=hm.get("email")%></td>
							<td><%=hm.get("dateofdelivery")%></td>
						</tr>
						<%
							}
							}
						%>
					</tbody>
				</table>
			</div>
		</div>

	</div>
</body>
</html>