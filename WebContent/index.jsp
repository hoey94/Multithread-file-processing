<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="./js/jquery-1.10.2.min.js"></script> 
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/assets/login.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.10.2.min.js"></script> 
</head>
<body>
<table class="login" cellpadding="0" cellspacing="0" border="0">
	<tr>
    	<td>
        	<div class="main">
            	<div class="logo"><img class="vm" style="height:55px;margin-top:5px;" src="${pageContext.request.contextPath}/assets/allImage/logo.png"  alt="EDI" title="ED-In" /></div>
                <div class="form-box">
                	<div class="item"><label class="label"><img class="vm mr10" src="${pageContext.request.contextPath}/assets/allImage/login_04.png" alt="用户ico" />用户名</label><input title="请输入用户名" class="inp-txt" type="text" name="userName" id="userName" value=""/></div>
                	<div class="item"><label class="label"><img class="vm mr10" src="${pageContext.request.contextPath}/assets/allImage/login_05.png" alt="用户ico" />密<span class="vhide">—</span>码</label><input title="请输入密码" class="inp-txt" type="password" name="userPwd" id="userPwd" value=""/></div>
                	<div class="item"><label class="label">&nbsp;</label><span class="btn-box"><input class="inp-btn-1" type="button" id="login" value="登录" /></span><span class="btn-box fl-r"><input class="inp-btn-2" type="button" value="重置" /></span></div>
                </div>
            </div>
        </td>
    </tr>
</table>
 <script type="text/javascript">
(function(){
	$("#login").click(function(){
		var userName=$("#userName").val();
		var userPwd=$("#userPwd").val();
		if(null==userName||userName.length<0){
			alert("输入用户名不正确!");
			return;
		}
		if(null==userPwd||userPwd.length<0){
			alert("输入密码不正确!");
			return;
		}
		var data={};
		data.userName=userName;
		data.userPwd=userPwd;
		$.ajax({
			type: "POST",
			url: "${pageContext.request.contextPath}/loginAction/loginAction.do?method=loginSystem&r="+Math.random(),
			data:data,
			dataType:"json",
			success: function(msg){  
				console.log(msg);
				if(msg.success){  
					window.location.href="${pageContext.request.contextPath}/systemAction/systemAction.do?method=initMainPage";
				} else {
					if(msg.errMsg == "404"){
						alert('用户名或密码错误，登录失败！');
					}
					if(msg.errMsg == "405"){
						alert('此账号已经被管理员禁用！请联系管理员！');
					}
					if(msg.errMsg == "406"){
						alert('此账号未通过管理审核，请联系管理员！');
					}
				} 
			}
		});	
	});
	//搜索的回车事件
	$('#userPwd').keydown(function(e){
		if(e.keyCode==13){
		  $("#login").click();
		}
	});
})();
</script>
</body>
</html>
