// JavaScript Document
//支持Enter键登录
document.onkeydown = function(e) {
	if ($(".bac").length == 0) {
		if (!e)
			e = window.event;
		if ((e.keyCode || e.which) == 13) {
			var obtnLogin = document.getElementById("submit_btn")
			obtnLogin.focus();
		}
	}
}
$(function() {
	// 提交表单
	$('#submit_btn').click(function() {
		show_loading();
		// var myReg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/; // 邮件正则
		// } else if (!myReg.test($('#email').val())) {
		// show_err_msg('您的邮箱格式错咯！');
		$('#username').focus();
		if ($('#username').val() == '') {
			show_err_msg('帐号还没填呢！');
			$('#username').focus();
		} else if ($('#password').val() == '') {
			show_err_msg('密码还没填呢！');
			$('#password').focus();
		} else if ($('#j_captcha').val() == '') {
			show_err_msg('验证码还没填呢！');
			$('#j_captcha').focus();
		} else {
			// ajax提交表单，#login_form为表单的ID。如：$('#login_form').ajaxSubmit(function(data)
			$("#login_form").ajaxSubmit({
				success : function(data) {
					var obj = eval(data);
					if (!obj.success) {
						show_err_msg(obj.message);
                        setTimeout(function(){
                            location.reload();
                        },3000);
					} else {
						show_msg(obj.message, $("#system").val());
					}
				},
				error : function(XmlHttpRequest, textStatus, errorThrown) {
					show_err_msg('登录失败！');
					$('#username').focus();
                    setTimeout(function(){
                        location.reload();
					},3000);
				}
			});

		}
	});
});