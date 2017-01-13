$(document).ready(function(){
	console.log("in password js");
	$("#recover-form").validate({
		rules: {
			new_pass: { 
				required: true,
				minlength: 6,
				maxlength: 15,

			} , 

			conf_pass: { 
				equalTo: "#new_pass",
				minlength: 6,
				maxlength: 15
			}


		},
		messages:{
			new_pass: { 
				required:"the password is required"

			}
		}

	});
	$('#recover-form').on('submit', function (e) {
		var form = $(this)[0]; 
		var formData = new FormData(form);

		$.ajax({
			type: 'post',
			url: '/MailSendingApplication/controller?mode=changePassword',
			data: formData,
			contentType: false,
			processData: false,
			success: function (result) {
				var status=JSON.parse(result);
				console.log("status is ",status);

				if(status==1)
				{
					var str="";
					str+="<div class='row' id='msg'><div class='col-md-4'></div><div class='col-md-4'><h1>Your Password was changed successfully!</h1></div></div>";
					str+="<div class='row' id='msg'><div class='col-md-5'></div><div class='col-md-4'><a href='pages/Login.jsp'>Click here to login</a></div></div>"
					$('.container').html('');
					$('.container').append(str);
				}
				else
				{
					var msg="something went wrong";
					$('#authFailed h2').html(msg);
					$('#username').val("");
					$('#key').val("");
				}

			},error:function(){
				console.log("err");
			}
		});
		e.preventDefault();
	});
});