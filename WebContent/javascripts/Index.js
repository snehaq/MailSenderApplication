function callDashBoardInController(){
	$.ajax({
		type: 'get',
		url: '/MailSendingApplication/controller?mode=dashboard',
		success: function (result) {
			var data=JSON.parse(result);
			var templateImg=data[1];
			var pastWeekMailLogs=data[0];
			var templateSelected=data[2];
			var htmlString="";
			var htmlString1="";
			if (templateImg.length==0){
				htmlString+='<h3 style="color: #d15353; margin-left: 33%">No Templates to Display</h3></div></form>';
			}
			else{
				for(var i=0;i<templateImg.length;i++)
				{
					htmlString+='<div class="col-md-2  templateImages" style="margin: 10px; height: 120px; padding-bottom: 5%;"><img src="/MailSendingApplication/'+templateImg[i]+'" class="templateImgs" id='+templateImg[i]+' data-container="body" data-toggle="popover" data-placement="top" data-content="click to preview" style="height: 100%; width: 100%; outline: none; border-color: #9ecaed; box-shadow: 0 0 20px #9ecaed; border-radius: 5px;">';
					htmlString+='<input type="checkbox" name="templateImgs" class="templates" value="'+templateImg[i]+'"';
					for(var j = 0 ; j <=i; j++){
						if(templateImg[i].split("/")[1].split(".")[0]==templateSelected[j]){
							htmlString+=' checked ';
						}
						else{
							htmlString+=' ';
						}
					}
					htmlString+='style="margin-left: 40%; width: 20px; height: 20px; margin-bottom: 1%;" /></div>';
				}
			}
			if(pastWeekMailLogs.length==0){
				htmlString1='<div class="col-md-12" id="mailLogs"><h2>No Mail Logs Avalable</h2></div>';
			}
			else{
				htmlString1+='<div class="col-md-12" id="mailLogs"><table class="table table-hover"><thead><tr><th>id</th><th>Name</th><th>Email</th><th>Date of Delivery</th></tr></thead><tbody>';
				for(var i=0;i<pastWeekMailLogs.length;i++){
					htmlString1+='<tr><td>'+pastWeekMailLogs[i].id+'</td><td>'+pastWeekMailLogs[i].name+'</td><td>'+pastWeekMailLogs[i].email+'</td><td>'+pastWeekMailLogs[i].dateofdelivery+'</td></tr>';
				}
				htmlString1+='</tbody></table></div>';
			}
			$("#templateChooserUi").append(htmlString);
			$("#mailLogsRow").append(htmlString1);



		},error: function(data) {
			$("#container").html("");
			var errorMsg="";
			errorMsg+="<div class='row' id='msg'><div class='col-md-3'></div><div class='col-md-9'><h2 style='color:#e53939'>Something went wrong!!!</h2></div></div>";
			$("#container").append(errorMsg);
		}

	});
}
function redirectToLoginPage(){
	window.location.href="./Login.jsp";
}
$(document).ready(function(){


	$('#uploadBtn').attr('disabled','disabled');
	$('#uploadZipBtn').attr('disabled','disabled');

	$('#selectFile_replacer').change(function(){
		if($('#selectFile_replacer').val() == ""){ 
			$('#uploadBtn').attr('disabled','disabled');
		}else{ 
			$('#uploadBtn').removeAttr("disabled");
		}                             
	});
	$('#selectZip_replacer').change(function(){
		if($('#selectZip_replacer').val() == ""){ 
			$('#uploadZipBtn').attr('disabled','disabled');
		}else{ 
			$('#uploadZipBtn').removeAttr("disabled");
		}                             
	});
	$('#uploadFileMsg').hide();
	document.getElementById("selectFile_replacer").onchange = function () {
		var filePath=document.getElementById("selectFile_replacer").value;
		var fileName = filePath.substring(filePath.lastIndexOf("\\")+1,filePath.length);
		$("#selectFile").val(fileName);

	};
	document.getElementById("selectZip_replacer").onchange = function () {
		var filePath=document.getElementById("selectZip_replacer").value;
		var fileName = filePath.substring(filePath.lastIndexOf("\\")+1,filePath.length);
		$("#selectZip").val(fileName);
	};
	$('input[type="radio"]').on('change', function(e) {
		var status=$(this).val();
		$.ajax({
			type:'post',
			url: '/MailSendingApplication/controller?mode=statusChange',

			data: {
				status: status,

			},
			success: function(data){
				var result=JSON.parse(data);
				var Msg="";
				if(result!="invalid"){
					str="<h3 style='color:#49b6a9;margin:0'> Status Changed to "+result+"</h3>";
				}
				else{
					str="<h3 style='color:#49b6a9;margin:0'>Invalid Value</h3>";
				}
				$("#status_timeMsg").html("");
				$("#status_timeMsg").append(str);
				$("#status_timeMsg").show();
				setTimeout(function(){
					$("#status_timeMsg").fadeOut("slow");
				}, 3000);
			},
			error:function(){
				str="<h3 style='color:#49b6a9;margin:0'>Something Went Wrong!!!</h3>";
				$("#status_timeMsg").html("");
				$("#status_timeMsg").append(str);
				$("#status_timeMsg").show();
			}
		});

	});
	$('#xlsUploadForm').on('submit', function (e) {

		var form = $(this)[0]; 
		var formData = new FormData(form);
		$.ajax({
			type: 'post',
			url: '/MailSendingApplication/controller?mode=xlsUpload',
			data:	formData,
			contentType: false,
			processData: false,
			success: function (result) {
				var status=JSON.parse(result);
				if(status=="success"){
					$("#selectFile_replacer").val("");
					$("#selectFile").val("");
					$("#uploadBtn").attr("disabled","disabled");
					var str="File successfully uploaded!";
				}
				else{
					var str="Invalid file extension";
				}
				$('#uploadFileMessage').html("");
				$('#uploadFileMessage').append(str);
				$('#uploadFileMsg').show();
				setTimeout(function(){
					$("#uploadFileMsg").fadeOut("slow");
				}, 3000);
			},error:function(){
				var str="Something went wrong!!";
				$('#uploadFileMessage').html("");
				$('#uploadFileMessage').append(str);
				$('#uploadFileMsg').show();

			}

		});
		e.preventDefault();
	});
	$('#imageUploadForm').on('submit', function (e) {
		var form = $(this)[0]; 
		var formData = new FormData(form);

		$.ajax({
			type: 'post',
			url: '/MailSendingApplication/controller?mode=imageUpload',
			data: formData,
			contentType: false,
			processData: false,
			success: function (result) {
				var status=JSON.parse(result);
				if(status=="success"){
					$("#selectZip_replacer").val("");
					$("#selectZip").val("");
					$("#uploadZipBtn").attr("disabled","disabled");
					var str="File successfully uploaded!";
				}
				else if(status=="formatError"){
					var str="File format incorrect.";
				}
				else if(status=="extensionError"){
					var str="UPLOAD FAILED note:Only .zip , .jpg files allowed";

				}
				$('#imageFileMessage').html("");
				$('#imageFileMessage').append(str);
				$('#imageFileMessage').show();
				setTimeout(function(){
					$("#imageFileMessage").fadeOut("slow");
				}, 3000);
			},
			error:function(){
				var str="Something went wrong!";
				$('#imageFileMessage').html("");
				$('#imageFileMessage').append(str);
				$('#imageFileMessage').show();

			}
		});
		e.preventDefault();
	});

	$('#setTimeForm').on('submit', function (e) {
		var form = $(this)[0]; 
		var formData = new FormData(form);
		$.ajax({
			type: 'post',
			url: '/MailSendingApplication/controller?mode=setCronJobTime',
			data: formData,
			contentType: false,
			processData: false,
			success: function (data) {
				var result=JSON.parse(data);
				var Msg="";
				if(result=="invalid"){
					Msg="<h3 style='color:#49b6a9;margin:0'>Some Error has occured</h3>";

				}
				else{
					Msg="<h3 style='color:#49b6a9;margin:0'> Cron Time Changed to "+result+"</h3>";
				}
				$("#status_timeMsg").html("");
				$("#status_timeMsg").append(Msg);
				$("#status_timeMsg").show();
				setTimeout(function(){
					$("#status_timeMsg").fadeOut("slow");
				}, 3000);
			},
			error:function(){
				var Msg="";
				$("#status_timeMsg").html("");
				$("#status_timeMsg").append(Msg);
				$("#status_timeMsg").show();
			}
		});
		e.preventDefault();
	});

	$('input[type="checkbox"][name="templateImgs"]').on('change',function(){
		var getArrVal = $('input[type="checkbox"][name="templateImgs"]:checked').map(function(){
			return this.value;
		}).toArray();

		if(getArrVal.length){
			//execute the code
			$('#checkBoxValidationMsg').html("");

		} else {
			$(this).prop("checked",true);
			$('#checkBoxValidationMsg').html("At least one value must be checked!");
			return false;

		};
	});

	$("#setTemplatesForm").submit(function(e) {
		var url = "/MailSendingApplication/controller?mode=setTemplatesInProperties"; 
		$.ajax({
			type: "POST",
			url: url,
			data: $("#setTemplatesForm").serialize(),
			success: function(data){
				var result=JSON.parse(data);
				var str="";
				var str1="";
				for(var i=0;i<result[0].length;i++){
					str+=result[0][i]+" ";
				}
				if(result[1].length!=0){
					str1+=result[1][i]+" ";
				}
				$("#selectTemplateMsg").html("");
				$("#selectTemplateMsg").append("<h3 style='color:#49b6a9;margin:0'>Templates selected are "+str+"</h3>");
				if(str1!=""){
					$("#selectTemplateMsg").append("<h3 style='color:#49b6a9;margin:0'>Templates discarded are "+str1+"</h3>");
				}
				$("#selectTemplateMsg").show();
				setTimeout(function(){
					$("#selectTemplateMsg").fadeOut("slow");
				}, 3000);
			},error:function(){
				$("#selectTemplateMsg").html("");
				$("#selectTemplateMsg").append("<h3 style='color:red;margin:0'>Something Went Wrong </h3>");
			}
		});
		e.preventDefault(); 
	});


	$('[data-toggle="popover"]').popover({
		trigger : 'hover'
	});


	var modal = document.getElementById('myModal');

	var btn = document.getElementById("myBtn");

	var span = document.getElementsByClassName("close")[0];

	$(document).on('click','.templateImgs',function(){
		$('#imgPreview').attr('src',$(this).attr('id'));
		modal.style.display = "block";
	});

	span.onclick = function() {
		modal.style.display = "none";
	}

	window.onclick = function(event) {
		if (event.target == modal) {
			modal.style.display = "none";
		}
	}
	$('#mailLogsSelectOption').change( function(e) {
		var selectedOption=$('#mailLogsSelectOption option:selected').text();
		$.ajax({
			type: 'post',
			url: '/MailSendingApplication/controller?mode=mailLogSelectChange',
			data:{
				selectedOption: selectedOption
			},
			success: function (result) {
				var data=JSON.parse(result);
				$('#mailLogs').html('');
				var htmlString="";
				htmlString+='<table class="table table-hover">'+
				'<thead><tr><th>id</th><th>Name</th><th>Email</th><th>Date of Delivery</th></tr></thead>'+
				'<tbody>';
				for(var i=0;i<data.length;i++)
				{ 
					htmlString+='<tr><td>'+data[i].id+'</td><td>'+data[i].name+'</td><td>'+data[i].email+'</td><td>'+data[i].dateofdelivery+'</td></tr>';
				}
				htmlString+='</tbody></table>';
				$('#mailLogs').append(htmlString);

			},error:function(){
				$('#mailLogs').html('');
				var htmlString="";
				html+="<h3 style='color:red'>Something went wrong</h3>"
					$('#mailLogs').append(htmlString);
			}
		});
		e.preventDefault();
	});
});
