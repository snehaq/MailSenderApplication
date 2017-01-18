$(document).ready(function(){
	
		
	
	$('#uploadFileMsg').hide();
	document.getElementById("selectFileBtn").onchange = function () {
		document.getElementById("selectFile").value = this.value;
	};
	document.getElementById("selectZipBtn").onchange = function () {
		document.getElementById("selectZip").value = this.value;
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
				if(result!="error")
					{
					str="<h3 style='color:#49b6a9;margin:0'> Status Changed to "+result+"</h3>";
					}
				else
					{
					str="<h3 style='color:#49b6a9;margin:0'>Some Error has occured</h3>";
					}
				$("#status_timeMsg").html("");
				$("#status_timeMsg").append(str);
				$("#status_timeMsg").show();
				setTimeout(function(){
					$("#status_timeMsg").fadeOut("slow");
				}, 3000);
			}
		});

	});
	var _validFileExtensions = [".xls"];    
	function Validate(fileNamePath) {
	            var sFileName = fileNamePath;
	            if (sFileName.length > 0) {
	                var blnValid = false;
	                    var sCurExtension = _validFileExtensions;
	                    if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
	                        blnValid = true;
	                    }
	                if (!blnValid) {
	                    alert("Sorry, " + sFileName + " is invalid, allowed extensions are: " + _validFileExtensions.join(", "));
	                    return false;
	                }
	            }
	        
	    return true;
	}
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
				if(status=="success")
				{
					var str="File successfully uploaded!";
				}
				else
				{
					var str="Something went wrong!! Note:check file extension";
				}
				$('#uploadFileMessage').html("");
				$('#uploadFileMessage').append(str);
				$('#uploadFileMsg').show();
				setTimeout(function(){
					$("#uploadFileMsg").fadeOut("slow");
				}, 3000);
			},error:function(){
				console.log("err");
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
					var str="File successfully uploaded!";
				}
				else if(status=="formatError"){
					var str="File format incorrect.";
				}
				else if(status=="extensionError"){
					var str="UPLOAD FAILED note:Only .zip , .jpg files allowed";
					
				}else{
					var str="Something went wrong!";
					}
				$('#imageFileMessage').html("");
				$('#imageFileMessage').append(str);
				$('#imageFileMessage').show();
				setTimeout(function(){
					$("#imageFileMessage").fadeOut("slow");
				}, 3000);
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
				if(result=="error")
					{
					str="<h3 style='color:#49b6a9;margin:0'>Some Error has occured</h3>";
					
					}
				else
					{
					str="<h3 style='color:#49b6a9;margin:0'> Cron Time Changed to "+result+"</h3>";
					}
				$("#status_timeMsg").html("");
				$("#status_timeMsg").append(str);
				$("#status_timeMsg").show();
				setTimeout(function(){
					$("#status_timeMsg").fadeOut("slow");
				}, 3000);
				
				

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
			success: function(data)
			{
				var result=JSON.parse(data);
				var str="";
				var str1="";
				for(var i=0;i<result[0].length;i++)
				{
					str+=result[0][i]+" ";
				}
				if(result[1].length!=0)
				{
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

			}
		});

		e.preventDefault(); 
	});


	$('[data-toggle="popover"]').popover({
		trigger : 'hover'
	});


	var modal = document.getElementById('myModal');

	// Get the button that opens the modal
	var btn = document.getElementById("myBtn");

	// Get the <span> element that closes the modal
	var span = document.getElementsByClassName("close")[0];

	// When the user clicks on the button, open the modal 
	$(document).on('click','.templateImgs',function(){
		$('#imgPreview').attr('src',$(this).attr('id'));
		modal.style.display = "block";
	});

	// When the user clicks on <span> (x), close the modal
	span.onclick = function() {
		modal.style.display = "none";
	}

	// When the user clicks anywhere outside of the modal, close it
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
				console.log("err");
			}

		});
		e.preventDefault();
	});

});
