if (window.top != window.self) {
	window.top.location.href = "/" + getProjectName() + "/";
}
new Vue({
	el : '#login',
	data : {
		loginForm : {
			username : "",
			password : ""
		},
		nowYear : new Date().getFullYear()
	},
	created : function() {
	},
	methods : {
		submitForm : function(formName) {

			var _this = this
			var username = this.loginForm.username
			var password = this.loginForm.password
			var jsonData = {
				username : username,
				password : password
			}
			if(XEUtils.isEmpty(username)){
				_this.$message.closeAll();
				operationCompletion(_this, "用户名不能为空", 'error')
			}else if(XEUtils.isEmpty(password)){
				_this.$message.closeAll();
				operationCompletion(_this, "密码不能为空", 'error')
			}else{
				sendPost("/" + getProjectName() + "/login", function(response) {
					layer.closeAll('loading')
					sessionStorage.setItem("UserName", jsonData.username);// 在跳转页面前在session中存储用户的username
					location.href = "/" + getProjectName() + "/app/sites"
					//sessionStorage.setItem("storageMode", 'pvc');
					//location.href = "/" + getProjectName() + "/app/index";
				}, function(error) {
					_this.$message.closeAll();
					operationCompletion(_this, error.response.data.msg, 'error')
				}, jsonData)
			}

		},
		resetForm : function(formName) {
			this.$refs[formName].resetFields();
		}
	}
})