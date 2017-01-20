$(document).ready(function(){
	$("#forgotPassword").click(function(){
		if($("#username").val()==""){
			$("#authFailed").html("");
			$("#authFailed").append("<h2>Please enter a username</h2>");
			$("#authFailed").show();
			setTimeout(function(){
				$("#authFailed").fadeOut("slow");
			}, 3000);
			$("#forgotPassword").removeAttr("data-target");
		}
		else{
			$("#forgotPassword").attr("data-target",".forget-modal");
		}
	});

	$('#login-form').on('submit', function (e) {
		var form = $(this)[0]; 
		var formData = new FormData(form);
		$.ajax({
			type: 'post',
			url: '/MailSendingApplication/controller?mode=loginAuthentication',
			data: formData,
			contentType: false,
			processData: false,
			success: function (result) {
				var status=JSON.parse(result);
				if(status=="true")
				{
					window.location = '/MailSendingApplication/controller?mode=dashboard';
				}
				else
				{
					var msg="username or password invalid";
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
	$('#recoveryBtn').on('click',function(e){
		var email=$("#recovery-email").val();
		var username=$("#username").val();
		$.ajax({
			type: 'post',
			url: '/MailSendingApplication/controller?mode=recoveryEmail',
			data: {email:email,
				username:username},
				success: function (result) {
					var status=JSON.parse(result);
					var initialHtml=$('#modalContent').html();
					if(status=="true")
					{
						$('#username').val("");
						$('#modalContent').html("");
						var str="<div class='row' style='margin-top:15%'><div class='col-md-12'style='padding-left:27%;'><h4>Recovery Mail Sent Successfully</h4></div></div>"
							$('#modalContent').append(str);
						$("#modalContent").show();
						$(".modal-backdrop").show();
						setTimeout(function(){
							$(".forget-modal").fadeOut("slow");
							$(".modal-backdrop").fadeOut("slow");
							$("#modalContent").html(initialHtml);
						}, 3000);

					}
					else if(status=="emailInvalid")
					{
						$('#username').val("");
						$('#modalContent').html("");
						var str="<div class='row' style='margin-top:15%'><div class='col-md-12'style='padding-left:27%;'><h4>Email is invalid</h4></div></div>"
							$('#modalContent').append(str);
						$("#modalContent").css('display','block');
						$("#modalContent").show();
						$(".modal-backdrop").show();
						setTimeout(function(){
							$('body').removeClass("modal-open");
							$(".forget-modal").fadeOut("slow");
							$(".modal-backdrop").remove();
							$("#modalContent").html(initialHtml);
						}, 3000);
					}
					else{
						$('#username').val("");
						$('#modalContent').html("");
						var str="<div class='row' style='margin-top:15%'><div class='col-md-12'style='padding-left:27%;'><h4>Mail delivery failed!!</h4></div></div>"
							$('#modalContent').append(str);
						setTimeout(function(){
							$('body').removeClass();
							$("#modalContent").fadeOut("slow");
							$(".modal-backdrop").remove();
							$("#modalContent").html(initialHtml);
						}, 3000);
					}

				},error:function(){
					console.log("err");
				}
		});
		e.preventDefault();

	});

});