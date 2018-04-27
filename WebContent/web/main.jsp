<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<% String basePath = request.getContextPath(); %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet"  href="<%=basePath%>/assets/bootstrap/css/bootstrap.min.css">
<script type="text/javascript" src="<%=basePath %>/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/js/bootstrap-3.3.7.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/js/upload.bigfile.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/colResizable-1.6.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/grid.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/common.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/js/myValidate.js"></script>
</head>
<body>
<div style="margin: 20px 20px 0px 20px;">
<div style="float: left;height:40px;margin-right: 5px;" class="mp">
		<div class="dropdown" >
			<input type="hidden" id="inputUserId" >
			<input type="text" id="typeName" value="" class="form-control" readonly="readonly" data-toggle="dropdown" 
				role="button" aria-haspopup="true" aria-expanded="false">
		  <ul id="userNameChoiceId" class="dropdown-menu" aria-labelledby="dLabel">
		  	<li data=''><a href="#">所有</a></li><!-- 
		    <li data='1'><a href="#">自己</a></li> -->
		    <c:forEach items="${userList}" var="s">
		    	 <li data='${s.table_id}'><a href="#">${s.userName}</a></li>
		    </c:forEach>
		  </ul>
	</div>
	
	</div>
	<div style="float: left;height:40px;" class="mp">
		<input type="text" class="form-control" id="searchUploadFileParamsId" 
		class="mp" style="width: 200px;overflow: hidden;" placeholder="请输入清单名">
	</div>
	<div style="float: left;margin-left: 10px;padding-top: 3px;" class="mp">
		<button type="button" class="btn btn-info" id="searchUploadFileId" class="mp">查询</button>
	</div>
	<div style="float: left;margin-left: 10px;padding-top: 3px;" class="mp">
		<button type="button" class="btn btn-info" id="deleteUploadFileId" class="mp">删除</button>
	</div>
	<c:if test="${systemUserTable eq '1'}">
	<div style="float: left;margin-left: 10px;padding-top: 3px;" class="mp">
		<button type="button" class="btn btn-info" id="adUploadFileUserId" class="mp">添加用户</button>
	</div>
	</c:if>
	<div style="float: left;margin-left: 10px;height:40px;line-height: 40px;padding-top: 3px;" class="mp">
		<button type="button" class="btn btn-info" id="doUploadFileId" class="mp">上传</button>
	</div>
	<div style="float: left;margin-left: 10px;height:40px;line-height: 40px;padding-top: 3px;" class="mp">
		<input type="file" id="uploadFileTestBind" class="form-control">
		
	</div>
	<div style="float: left;margin-left: 10px;height:40px;line-height: 40px;padding-top: 3px;" class="mp">
		<button type="button" class="btn btn-info" id="doUploadFileTestBind" class="mp">上传</button>
	</div>
	<table id="uploadFileGridTable" class="table table-bordered">
		<thead>
			<tr id="upload_file_title_id">
				<th style="width:100px; ">编号</th>
				<th  width="50" fname="companyNameCheckBox"><input id="upload_file_add_checkbox" type="checkbox"></th>
				<th fname="fileName">文件名称</th>
				<th fname="fileType">文件类型</th>
				<th fname="fileSize">文件大小</th>
				<th fname="uploadUserName">上传人</th>
				<th fname="showTime">创建时间</th>
				<th fname="temp1">下载</th>
			</tr>
		</thead>
		<tbody id="upload_file_info_tbody_id">
			
		</tbody>
	</table>
	<nav aria-label="Page navigation example">
		<ul class="pagination" id="upload_file_pagination">
						  
	   </ul> 
	</nav> 
</div>
<div id="loadUploadFileModelAreaId">
</div>
<script type="text/javascript">
	var basePath = "${pageContext.request.contextPath}";
	function loadUploadFileGrid(data){
		if(typeof(data)=="undefined"){
			data={};
		}
		var url = basePath + "/systemAction/systemAction.do?method=getUploadFileInfo";
		data.start=1;
	
		$("#uploadFileGridTable").wgrid({
			table_title:"upload_file_title_id",
			tbodyId:"upload_file_info_tbody_id",
			pagination:"upload_file_pagination",
			url:url,
			checkbox:true,
			checkboxId: "upload_file_add_checkbox",
			data:data,
			success:function(){
				
			}
		});
	}
	$("#uploadFileTestBind").bindFileInput({
		fileSplitSize: 1024 * 1024,
		/* fileInputId: "uploadFileTestBind",  */
		/* clickId:"doUploadFileTestBind", */
		uploadUrl: "${pageContext.request.contextPath}/systemAction/systemAction.do?method=uploadPartFile&r="+Math.random(),
		merageUrl: "${pageContext.request.contextPath}/systemAction/systemAction.do?method=meragePartFile&r="+Math.random(),
		backFunction: function(data,userData){
			console.log("==============backFunction============");
			console.log(data);
			console.log(userData);
			saveFileInfo(data,userData);
		},
		processFn: function(index,total,status){
			console.log(index+"  "+total+" "+status);
		}
	});
	$("#doUploadFileId").on("click",function(){
		$("#doUploadFileId").uploadBigFile({
			fileSplitSize: 1024 * 1024,
			uploadUrl: "${pageContext.request.contextPath}/systemAction/systemAction.do?method=uploadPartFile&r="+Math.random(),
			merageUrl: "${pageContext.request.contextPath}/systemAction/systemAction.do?method=meragePartFile&r="+Math.random(),			 
			backFunction: function(data,userData){
				console.log("==============backFunction============");
				console.log(data);
				console.log(userData);
				saveFileInfo(data,userData);
			},
			errorFunction:function(erroeStatus,msg){
				alert(erroeStatus+" : "+msg);
			}
		});	
		
	});
	function saveFileInfo(data,userData){
		var url = "${pageContext.request.contextPath}/systemAction/systemAction.do?method=saveUploadFileInfo";	
		console.log(data);
		$.post(url,{"jaStr":data,"userData":userData},function(res){
			console.log(res);
			var json = jQuery.parseJSON(res);
			if(json.status == '1'){
				loadUploadFileGrid({});
			}
			$("#doUploadFileId").appendResult(json.msg);
		});
	}
	$("#searchUploadFileId").on("click",function(){
		var data = {
				"fileName": $("#searchUploadFileParamsId").val().trim(),
				"inputUserId": $("#inputUserId").val().trim()
		};
		loadUploadFileGrid(data);
	});
	$("#deleteUploadFileId").on("click",function(){
		var data = $('#uploadFileGridTable').wgrid("getTrData",{
			tbodyId: "upload_file_info_tbody_id"
		});
		console.log(data);
		if(data.length != 1){
			alert("请选择一条数据");
			return;
		}
		var s = window.confirm("是否要删除数据!");
		if(s == true){
			var url = "${pageContext.request.contextPath}/systemAction/systemAction.do?method=deleteModel";
			$.post(url,{"table_id":data[0].table_id},function(data){
				var json = jQuery.parseJSON(data);
				if(json.status == '1'){
					loadUploadFileGrid({});
				}
				alert(json.msg);
			});
		}
	});
	$("#userNameChoiceId").find("li").each(function(index){
		$(this).on("click",function(){
			$("#inputUserId").val($(this).attr("data"));
			$("#typeName").val($(this).find('a').eq(0).text());
		});
	});
	function loadUserMoal(){
		$("#loadUploadFileModelAreaId").html("");
		$("#loadUploadFileModelAreaId").load(basePath+"/web/system_user_add.jsp",{},function(){
			$("#addUserModal").on("show.bs.modal",function(event){
				/*  var button = $(event.relatedTarget) ;
				  var recipient = button.data('whatever') ;
				  var modal = $(this); */
			});
			$("#addUserModal").modal("show");
		
		});
	}
	$("#adUploadFileUserId").on("click",function(){
		loadUserMoal();
	});
	$(function(){
		loadUploadFileGrid({});
	});
	
</script>
</body>
</html>
