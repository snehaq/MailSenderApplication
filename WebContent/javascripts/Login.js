function showPassword(){
	    var key_attr = $('#key').attr('type');
	    if(key_attr != 'text') {
	        $('.checkbox').addClass('show');
	        $('#key').attr('type', 'text');
	    } else {
	        $('.checkbox').removeClass('show');
	        $('#key').attr('type', 'password');
	    }
	}
$(document).ready(function(){
	$("#showPassword_checkBox").on("click",function (){
		showPassword();
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
		console.log(email);
		$.ajax({
			type: 'post',
			url: '/MailSendingApplication/controller?mode=recoveryEmail',
			data: {email:email},

			success: function (result) {
				console.log("result  ",result);
				var status=JSON.parse(result);
				var initialHtml=$('#modalContent').html();
				if(status=="true")
				{
					$('#modalContent').html("");
					var str="<div class='row' style='margin-top:15%'><div class='col-md-12'style='padding-left:27%;'><h4>Recovery Mail Sent Successfully</h4></div></div>"
						$('#modalContent').append(str);
					setTimeout(function(){
						$("#modalContent").fadeOut("slow");
						$(".modal-backdrop").fadeOut("slow");
						$("#modalContent").html(initialHtml);
					}, 3000);
					
				}
				else
				{
					$('#modalContent').html("");
					var str="<div class='row' style='margin-top:15%'><div class='col-md-12'style='padding-left:27%;'><h4>Mail delivery failed!!</h4></div></div>"
						$('#modalContent').append(str);
					setTimeout(function(){
						$("#modalContent").fadeOut("slow");
						$(".modal-backdrop").fadeOut("slow");
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