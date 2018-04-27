<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- 添加用户模态框 -->
<div class="modal fade" id="addUserModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h4 class="modal-title" id="addUserModalTitleId">添加用户</h4>
            </div>
            <div class="modal-body" id="addUserInfoId">
            		<input type="hidden" id="table_id">
            	 <div class="input-group">
            	 	<span class="input-group-addon"  style="width: 80px;">用户名</span>
            	 	<input require="true" meth="notNull,isSpace,isMinLength-9" type="text" 
            	 		class="form-control" name="userName" id="userName" style="width: 300px;"
            	 		placeholder="输入不能为空不能有特殊字符,长度小于7">
            	 </div>
            	 <div class="input-group" style="margin-top: 5px;">
            	 	<span class="input-group-addon" style="width: 80px;">密码</span>
            	 	<input type="password" require="true" meth="notNull,isSpace,isLength-3" 
            	 		class="form-control" name="userPwd" id="userPwd" style="width: 300px;"
            	 		placeholder="输入不能为空不能有特殊字符,长度大于3">
            	 </div>
            	 <div class="input-group" style="margin-top: 5px;">
            	 	<span class="input-group-addon" style="width: 80px;">确认密码</span>
            	 	<input type="password" require="true" id="userRePwd" meth="notNull,isSpace,isLength-3" 
            	 		class="form-control"  style="width: 300px;">
            	 </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" id="addUserInfoSubmitId" class="btn btn-primary" >提交更改</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<script type="text/javascript">
(function(){
	var basePath = "${pageContext.request.contextPath}"
	function checkPwd(){
		var pwd = $("#userPwd").val().trim();
		var repwd = $("#userRePwd").val().trim();
		
		if(pwd != "" && repwd != "" && pwd == repwd){
			return true;
		}
		return false;
	}
	function addUser(){
		var s = $("#addUserInfoId").hzValidate("init");
		if(s==true && checkPwd() == true){
			 var params = OAajaxSubmit("addUserInfoId");
			 var url = basePath + "/systemAction/systemAction.do?method=addSystemUser";
			$.post(url,params,function(data){
				var result = jQuery.parseJSON(data);
				if(result.status == 1){
					$("#addUserModal").modal("hide");
				}
				alert(result.msg);
			});
		}else{
			alert("输入参数不正确!");
		}
	}
	
	$(function(){
		$("#addUserInfoSubmitId").click(function(){
			addUser();
		});
	});
})();
</script>