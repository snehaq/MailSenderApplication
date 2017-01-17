$(document).ready(function(){



	$('#uploadFileMsg').hide();
	document.getElementById("selectFileBtn").onchange = function () {
		document.getElementById("selectFile").value = this.value;
	};
	document.getElementById("selectZipBtn").onchange = function () {
		document.getElementById("selectZip").value = this.value;
	};


	$('input[type="radio"]').on('change', function(e) {
		console.log($(this).val());
		var status=$(this).val();


		$.ajax({
			type:'post',
			url: '/MailSendingApplication/controller?mode=statusChange',

			data: {
				status: status,

			},
			success: function(result){


			}
		});

	});
	$('#xlsUploadForm').on('submit', function (e) {
		var form = $(this)[0]; 
		var formData = new FormData(form);
		console.log($(this).attr('id'));

		$.ajax({
			type: 'post',
			url: '/MailSendingApplication/controller?mode=xlsUpload',
			data: formData,
			contentType: false,
			processData: false,
			success: function (result) {
				console.log(result);
				var status=JSON.parse(result);

				if(status=="success")
				{
					var str="File successfully uploaded!";
				}
				else
				{
					var str="Something went wrong!";
				}
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
		console.log($(this).attr('id'));
		$.ajax({
			type: 'post',
			url: '/MailSendingApplication/controller?mode=imageUpload',
			data: formData,
			contentType: false,
			processData: false,
			success: function () {
				var str="File successfully uploaded!";
				$('#imageFileMsg').show();
				$('#imageFileMessage').append(str);
				setTimeout(function(){
					$("#imageFileMsg").fadeOut("slow");
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
			success: function () {
				console.log("success");

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
		console.log($(this).attr('id'));
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

		console.log($('#mailLogsSelectOption option:selected').text());
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
