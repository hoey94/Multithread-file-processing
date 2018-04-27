(function() {
	
	var success = 0;// 单文件分割后上传文件个数
	var mark = 0;// 上传文件成功个数
	var fileLength = 0;// 上传文件个数
	var merageParams = new Array();// 后台文件合并参数
	var fileNameArr = new Array();
	var processNum = 0;
	var queue = new Array();
	var startDate = 0;
	var endDate = 0;
	var indexHtml = '<div id="uploadKeepOutPage" style="width:100%;height:100%;background-color:gray;opacity:0.2; z-index:100;position: absolute;left:0;top:0;"></div>';
	var indexTopHtml = '<div id="uploadKeepTopPage" style="width:100%;height:100%;background-color:gray;opacity:0.1; z-index:1000000;position: absolute;left:0;top:0;"></div>';
	var merageStatus = false;
	var redoNum = 3;
	var fileArr = new Array();
	Array.prototype.pushQueue = function(funObj) {
		this.push(funObj);
	};
	Array.prototype.next = function(){
		if (this.length > 0) {
			var fObj = this[0];
			fObj.func.apply(this, fObj.params);
			this.shift();
		} 
	};
	Array.prototype.nextIndex = function(index){
		if (this.length > 0) {
			for(var i = 0;i < (index > this.length ? this.length : index);i++){
				var fObj = this[0];
				fObj.func.apply(this, fObj.params);
				this.shift();
			}
		}
	};
	Array.prototype.isEmpty = function(){
		if(this.length == 0){
			return false;
		}
		return true;
	};
	Array.prototype.remove=function(value,index){ 
		/***********************************************************************
		 * value: 比较的值 index:获取比较值的参数数组下标
		 */
		for(var i = 0;i < this.length;i++){
			var p = this[i].params[index];
			if(p == value){
				this.splice(i,1);
			}
		}
	};
	
	var uploadFile = {
		init : function(obj, params) {
			if(!window.applicationCache){
				params.errorFunction(0,"浏览器不支持html5,请升级到最新版本!");
				return;
			}
			uploadFile.initObject();
			var filtype = "";
			var accept = "" ;
			for(var i = 0; i < params.fileTypeFilter.length;i++){
				
				if(i == params.fileTypeFilter.length - 1){
					filtype += params.fileTypeFilter[i]
					accept += "."+params.fileTypeFilter[i]
				}else{
					filtype += params.fileTypeFilter[i] + "，"
					accept += "."+params.fileTypeFilter[i]+","
				}
			}
// <span id="closeUploadPageId" style="margin-left:530px;cursor: pointer;" >
			startDate = Date.parse(new Date());
			var scrollStyle = '<style>'
						+'#uploadFileListId::-webkit-scrollbar {width: 5px;  height: 5px;}'
				        +'#uploadFileListId::-webkit-scrollbar-thumb {border-radius: 10px;-webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);background: #535353;cursor: pointer; }'
				        +'#uploadFileListId::-webkit-scrollbar-track {-webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0);border-radius: 10px;background: white;}'
				        +'</style>'
			var btnStyle = 'style="display: inline-block;padding: 6px 12px;margin-bottom: 0;font-size:'
							+ '14px;font-weight: normal;line-height: 1.42857143;text-align: center;white-space: nowrap;'
							+ 'vertical-align: middle;-ms-touch-action: manipulation;touch-action: manipulation;cursor: pointer;'
							+ '-webkit-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;'
							+ 'background-image: none;border: 1px solid transparent;border-radius: 4px;color: #fff;background-color: #5bc0de;border-color: #46b8da;"';
			var baseHtml = '<div id="uploadFileContent" style="width: 600px;height: 400px;border: 1px solid #ddd;'
				+ 'position: absolute;left: '+params.left+';top: '+params.top+';background-color:white;z-index:100000">'
				+scrollStyle
				+ '<div id="uploadFileTitle" style="font-size: 20px;height: 30px;width: 100%;'
				+ 'border-bottom: 1px solid #ddd;line-height: 30px;">&nbsp;上传<span style="font-size:10px;">(上传文件名字相同时会被去重)</span>'
				+'<div id="closeUploadPageId" style="margin-left:570px;margin-top:-30px;cursor: pointer;width:26px;height:26px;font-size:26px;text-align:center;transform:rotate(45deg);" >+</div>'
				+'</div>'
				+ '<div id="changefileId" class="form-group" style="margin-left: 10px;margin-top: 10px;'
				+ 'width: 90%;height: 37px;">'
				+ '<label id="realBtn" '+btnStyle+'>'
				+ '<input type="file" multiple="multiple" name="file" class="mFileInput"'
				+ 'style="left:-9999px;position:absolute;">'
				+ '<span class="filePath">选择文件</span>'
				+ '</label>'
				+ '<span style= "margin-left:10px;"></span>'
				+ '<span id="uploadFileId" '+btnStyle+'>确定上传</span>'
				+ '<span style= "margin-left:10px;"></span><span id="fileTypeFilterSpan"></span><span id="resultMsgId"></span>'
				+ '</div>'
				+ '<div id="uploadFileListId" style="margin-left: 10px;width: 98.5%;height:308px;'
				+ 'overflow:hidden;  ">' + '</div>' + '</div>';
			
			$("#uploadFileContent").remove();
			$("body").append(baseHtml);
			$("body").append(indexHtml);
			$("#fileTypeFilterSpan").html(filtype);
			$("#realBtn").find("input[type=file]").each(function(){
				$(this).on("change", function() {
					if(merageStatus == true){
						merageStatus = false;
						 $("#resultMsgId").html("");
						 $("#uploadFileListId").html("");
					}
					uploadFile.appendFileProgress(params);
				});
			});
			$("#realBtn").on("click",function(){
				$("#uploadFileId").html("确定上传");
				if(queue.length > 0){
					uploadFile.initObject();
					merageStatus = true;
				}
			});
			$("#uploadFileId").on("click", function() {
				redoNum = 3;
				if(queue.length > 0){
					$("body").append(indexTopHtml);
					queue.nextIndex(5);
				}else{
					// var fileArr = uploadFile.getUploadFileArray();
					var errorArr = uploadFile.checkBigFileSize(fileArr,params);
					if(errorArr.length > 0){
						uploadFile.initObject();
						 $("#uploadFileListId").html("");
						params.errorFunction(1,errorArr);
						return;
					}
					if(fileArr.length <= 0){
						params.errorFunction(3,"请选择文件!");
						return;
					}
					var filter = uploadFile.checkFileType(fileArr,params);
					if(filter == false){
						params.errorFunction(2,"选择文件格式不正确!");
						 $("#uploadFileListId").html("");
						return;
					}
					$("#resultMsgId").html("上传个数: "+fileArr.length);
					$("body").append(indexTopHtml);
					uploadFile.upload(params,fileArr);
				}
				
			});
			$("#closeUploadPageId").on("click",function(){
				
				$("#uploadFileContent").remove();
				$("#uploadKeepOutPage").remove();
				$("#uploadKeepTopPage").remove();
				uploadFile.initObject();
			});
			if(params.isOpenDrag == true){
				// 处理文件拖拽上传
				uploadFile.dragFiles(params);
				// uploadFile.appendFileProgress(params);
			}
		},
		initBind: function(obj, params){
			params['mark'] = false;// 标记上传方式是用户自己写file绑定事件上传
			if(!jQuery.isEmptyObject(params.clickId)){
				$("#"+params.clickId).on("click",{"obj":obj,"params":params},function(event){
					console.log("================="+queue.length);
					if(queue.length > 0){
						queue.nextIndex(5);
					}else{
						uploadFile.uploadFileByBind(event.data.obj,event.data.params);
					}
				});
			}
			$(obj).on("change",{"obj":obj,"params":params},function(event){
				uploadFile.initObject();
				if(jQuery.isEmptyObject(params.clickId)){
					uploadFile.uploadFileByBind(event.data.obj,event.data.params);
				}
			});
		},
		checkUploadMark: function(params){
			if(('mark' in params) && params.mark == false){
				return false;// 不使用界面
			}
			return true;// 使用界面
		},
		checkBigFileSize: function(fileArr,params){
			var errorInfo = new Array();
			for(var i = 0;i < fileArr.length;i++){
				var fileSize = fileArr[i].file.size;
				if(params.fileSize > -1 && params.fileSize*1024 <= fileSize ){
					errorInfo.push(fileArr[i].name);
				}
			}
			return errorInfo;
		},
		uploadFileByBind:function(obj, params){
			var $file = document.getElementById($(obj).attr("id")).files;
			if($file.length <= 0){
				params.errorFunction(3,"请选择文件!");
				return;
			}
			
			var blob = $file[0];
			var fileSize = blob.size;
			
			if(params.fileSize > -1 && params.fileSize*1024 <= fileSize ){
				var errorArr = new Array();
				errorArr.push(blob.name);
				params.errorFunction(1,errorArr);
				return;
			}
			
			var filter = uploadFile.checkFileType(blob,params);
			if(filter == false){
				params.errorFunction(2,"选择文件格式不正确!("+params.fileTypeFilter+")");
				return;
			}
			var total = Math.ceil(fileSize / params.fileSplitSize);
			// 单独处理文件为空的情况
			if(total <= 0 ){
				total = 1;
			}
			var fileTempName = uploadFile.uuid();
			if(blob.name.indexOf(".") == -1){
				fileTempName = fileTempName + "." + "temp";
			}else{
				var arr = blob.name.split(".");
				fileTempName = fileTempName +"."+arr[arr.length - 1];
			}
			
			for (var i = 0; i < total; i++) {
				var start = i * params.fileSplitSize;
				var end = Math.min(fileSize, start + params.fileSplitSize);
			
				var partFile = blob.slice(start, end);
				var requestAjax = uploadFile.createAjaxRequest(total, i+1, partFile, fileTempName, params,blob.name,$file);
				
				queue.pushQueue(requestAjax);
			}
			queue.nextIndex(5);
		},
		getUploadFileArray: function(){
			var fileInput = document.getElementById('uploadFileListId').getElementsByTagName("input");
			var fileArr = new Array();
			for(var i = 0;i < fileInput.length;i++){
				var $file = fileInput[i].files;
				for(var j = 0;j < $file.length;j++){
					var json = {
						"tempName":uploadFile.uuid(),
						"file":$file[j]
					};
					fileArr.push(json);
				}
			}
			uploadFile.removeUploadFileInput();
			return fileArr;
		},
		upload : function(params,fileArr) {
			var fileJson = fileArr[parseInt(mark)];
			var fileTempName = fileJson.tempName;
			var id = fileJson.tempName;
			var blob = fileJson.file;
			
			fileLength = fileArr.length;
			
			if(fileLength <= 0){
				$("#uploadKeepOutPage").remove();
				params.errorFunction(3,"请选择文件");
				return;
			}
			
			var fileSize = blob.size;

			var total = Math.ceil(fileSize / params.fileSplitSize);
			if(total == 0){
				total = 1;
			}
			
			if(blob.name.indexOf(".") == -1){
				fileTempName = fileTempName + "." + "temp";
			}else{
				var arr = blob.name.split(".");
				fileTempName = fileTempName +"."+arr[arr.length - 1];
			}
			
			for (var i = 0; i < total; i++) {
				var start = i * params.fileSplitSize;
				var end = Math.min(fileSize, start + params.fileSplitSize);
			
				var partFile = blob.slice(start, end);
				var requestAjax = uploadFile.createAjaxRequest(total, i+1, partFile, fileTempName, params,blob.name,fileArr,id);
				
				queue.pushQueue(requestAjax);
			}
			queue.nextIndex(5);
		},
		createAjaxRequest: function(total, index, partFile, blobName, params,fileName,fileArr,id){
			var uploadPartParams = new Array();
				
				uploadPartParams.push(total);
				uploadPartParams.push(index);
				uploadPartParams.push(partFile);
				uploadPartParams.push(blobName);
				uploadPartParams.push(params);
				uploadPartParams.push(fileName);
				uploadPartParams.push(fileArr);
				uploadPartParams.push(id);
				var fun = function(total, index, partFile, blobName, params,fileName,fileArr,id){
					return (function(){
						uploadFile.uploadPartFile(total, index, partFile, blobName, params,fileName,fileArr,id);
					})();
				}
				var funObj = {
						"func" : fun,
						"params" : uploadPartParams
					};
				return funObj;
		},
		uploadPartFile : function(total, index, partFile, blobName, params,fileName,fileArr,id) {
			var fd = new FormData();
			console.log("=================uploadPartFile: "+index+" ===================="+id);
			fd.append("file", partFile);
			fd.append("name", blobName);
			fd.append("index", index);
			fd.append("total", total);
			$.ajax({
				url : params.uploadUrl,
				type : "POST",
				data : fd,
				async : true,
				processData : false,
				contentType : false,
				error: function (XMLHttpRequest, textStatus, errorThrown) {
					
					
						console.log("剩余次数： "+redoNum);
						redoNum -= 1;
						
						var requestAjax = uploadFile.createAjaxRequest(total, index, partFile, blobName, params,fileName,fileArr);
						queue.pushQueue(requestAjax);
						
						if(redoNum > 0){
							if(uploadFile.checkUploadMark(params) == true){
								$("#uploadFileId").html("继续上传");
							}else{
								$("#"+params.clickId).text("继续上传");
							}
							uploadFile.uploadPartFile(total, index, partFile, blobName, params,fileName,fileArr)
						}
						
						if(redoNum == 0){
							if(uploadFile.checkUploadMark(params) == true){
								$("#uploadKeepTopPage").remove();
							}
							params.errorFunction(4,"上传失败!");
						}
					if(uploadFile.checkUploadMark(params) == false){
						params.processFn(index,total,-1);
					}
				    this; // 调用本次AJAX请求时传递的options参数
				},
				success : function() {
					
					success += 1;
					if(uploadFile.checkUploadMark(params) == true){
						uploadFile.changeProgress(index, total,id);
					}else{
						params.processFn(index,total,1);
					}
					queue.nextIndex(1);
					if (parseInt(success) == parseInt(total)) {
						if(uploadFile.checkUploadMark(params) == true){
							uploadFile.changeDivScroll();
						}
						var json = {};
						json.name = blobName;
						json.total = total;
						merageParams[mark] = json;

						success = 0;
						mark += 1;
						var fileNameJSON = {};
						fileNameJSON.name = fileName;
						fileNameArr.push(fileNameJSON);
						if (mark < fileLength) {
							uploadFile.upload(params,fileArr);
						} else {
							
							uploadFile.merageFiles(merageParams, params,fileNameArr);
						}
					}
				}
			});
		},
		merageFiles : function(merageParams, params,fileNameArr) {
			endDate = Date.parse(new Date());
			console.log(endDate - startDate);
			$.post(params.merageUrl, {
				"jsonStr" : JSON.stringify(merageParams)
			}, function(data) {
				var jsonData = jQuery.parseJSON(data);
				
				for(var i = 0;i < jsonData.length;i++){
					jsonData[i].fileName = fileNameArr[i].name;
				}
				
				if(uploadFile.checkUploadMark(params) == true){
					uploadFile.removeUploadFileInput();
					$("#uploadFileId").html("确定上传");
					merageStatus = true;
					$("#uploadKeepOutPage").remove();
				}
				
				uploadFile.initObject();
				if(params.isSuccessClosed == true){
					$("#uploadFileContent").remove();
					$("#uploadKeepOutPage").remove();
				}
				$("#uploadKeepTopPage").remove();
				fileArr = new Array();
				console.log("================合并文件成功=================");
				console.log(jsonData);
				params.backFunction(JSON.stringify(jsonData),JSON.stringify(params.userData));
			});
		},
		changeProgress : function(index, total,id) {
			var prenct = (Math.floor(success * 100 / total)) + "%";
			$("#"+id).find(".progress").eq(0).find("span").eq(0)
					.html(prenct);
			$("#"+id).find(".progress-bar").eq(0).css("width",
					prenct);
		},
		removeUploadFileInput:function(){
			$("#uploadFileListId").find("input[type=file]").each(function(index){
				$(this).remove();
			});
		},
		appendFileProgress : function(params) {
			var tempFieArr = document.getElementById('changefileId').getElementsByTagName("input")[0].files;
			var _group_all_height = 0;
			for (var i = 0; i < tempFieArr.length; i++) {
				var mark = uploadFile.fileUniq(tempFieArr[i].name);
				if(mark == true){
					continue;
				}
				var uuidFileName = uploadFile.uuid();
				var json = {
						"tempName":uuidFileName,
						"file":tempFieArr[i]
					};
				fileArr.push(json);
					
				uploadFile.createFileProgressItem(uuidFileName,tempFieArr[i].name,_group_all_height,params);
			}
			processNum += tempFieArr.length;
			
			// 实现了多次选择不同文件夹不同多个文件
			var $file =  document.getElementById('changefileId').getElementsByTagName("input")[0];
			document.getElementById("uploadFileListId").appendChild($file);
			document.getElementById("realBtn").appendChild($file.cloneNode());
			
			$("#realBtn").find("input[type=file]").each(function(){
				$(this).on("change", function() {
					if(merageStatus == true){
						 merageStatus = false;
						 $("#resultMsgId").html("");
						 $("#uploadFileListId").html("");
					}
					uploadFile.appendFileProgress(params);
				});
			});
		},
		createFileProgressItem(uuidFileName,fileName,_group_all_height,params){
			var _group = document.createElement("div");
			var _process = document.createElement("div");
			var _process_div = document.createElement("div");
			var _process_span = document.createElement("span");
			var _group_span = document.createElement("div");
			var _group_delete = document.createElement("div");
			var _group_clear = document.createElement("div");
			
			_group.setAttribute("id", uuidFileName);
			_group.setAttribute("class", "form-group");
			_group.setAttribute("style", "min-height:25px;width:97%;margin-top:10px;font-size:14px;");

			_process.setAttribute("style", "padding: 0;margin: 0; height: 20px;overflow: hidden;background-color: #f5f5f5;border-radius: 4px;-webkit-box-shadow: inset 0 1px 2px rgba(0, 0, 0, .1);box-shadow: inset 0 1px 2px rgba(0, 0, 0, .1);");
			_process.setAttribute("class", "progress");

			_process_div.setAttribute("style", "width: 0%;float: left;height: 100%;font-size: 12px;line-height: 20px;color: #fff;text-align: center;background-color: #337ab7;");
			_process_div.setAttribute("class", "progress-bar");
			_process_div.setAttribute("role", "progressbar");
			_process_div.setAttribute("aria-valuenow", "60");
			_process_div.setAttribute("aria-valuemin", "0");
			_process_div.setAttribute("aria-valuemax", "100");

			_process_span.textContent = "0%";
			
			_group_span.setAttribute("style","width:90%;float:left;height:auto;");
			_group_span.setAttribute("title",fileName);
			_group_span.setAttribute("class", "filePath");
			_group_span.textContent = fileName;

			_group_delete.setAttribute("delete-mark",uuidFileName);
			_group_delete.setAttribute("style","float:left;cursor: pointer;width:25px;height:25px;font-size:25px;transform:rotate(45deg);text-align:right;margin-top:-10px;overflow:hidden;");
			_group_delete.textContent = "+";

			_group_clear.setAttribute("style","clear:both");
			
			
			_process.appendChild(_process_div);
			_process.appendChild(_process_span);
			_group.appendChild(_process);
			
			if(params.openDelete == true){
				_group.appendChild(_group_delete);
				$(_group_delete).on("click",function(){
					for(var i = 0;i < fileArr.length;i++){
						if(fileArr[i].tempName == $(this).attr("delete-mark")){
							fileArr.splice(i,1);
							$(this).parent().remove();
							break;
						}
					}
				});
			}
			
			_group.appendChild(_group_span);
			_group.appendChild(_group_clear);
			
			
			
			$("#uploadFileListId").append(_group);
			
			_group_all_height = $("#uploadFileListId").find(".form-group").length * parseInt($(_group).css("height").replace(
					'px', ''));
			var list_height = parseInt($("#uploadFileListId").css("height")
					.replace('px', ''));

			if (_group_all_height+25 >= list_height) {
				$("#uploadFileListId").css('overflow-y', 'scroll');
			}

		},
		changeDivScroll: function(){
			$("#uploadFileListId").scrollTop($("#uploadFileListId").scrollTop()+40);
		},
		dragFiles: function(params){
			var obj = document.getElementById("uploadFileContent");
			obj.addEventListener("dragenter",function(e){
				// 不再派发事件
		         e.stopPropagation();
		         // 取消事件的默认动作
		         e.preventDefault();
			},false);
			obj.addEventListener("dragleave",function(e){
				 e.stopPropagation();
		         e.preventDefault();
			},false);
			obj.addEventListener("dragover",function(e){
				e.stopPropagation();
		         e.preventDefault();
			},false);
			obj.addEventListener("drop",function(e){
				 e.stopPropagation();
		         e.preventDefault();
		         var files = e.target.files || e.dataTransfer.files;
		         var _group_all_height = 0;
		         console.log("====================drag=========================");
		         for(var i = 0;i < files.length;i++){
		        	 var mark = uploadFile.fileUniq(files[i].name);
						if(mark == true){
							continue;
						}
		        	 var uuidFileName = uploadFile.uuid();
						var json = {
								"tempName":uuidFileName,
								"file":files[i]
							};
						console.log(files[i].name);
						fileArr.push(json);
						
						uploadFile.createFileProgressItem(uuidFileName,files[i].name,_group_all_height,params);
		         }
			},false);
			
		},
		fileUniq: function(fileName){
			for(var i = 0;i < fileArr.length;i++){
				if(fileArr[i].file.name == fileName){
					return true;
				}
			}
			return false;
		},
		initObject: function(){
			 success = 0;// 单文件分割后上传文件个数
			 mark = 0;// 上传文件成功个数
			 fileLength = 0;// 上传文件个数
			 merageParams = new Array();// 后台文件合并参数
			 fileNameArr = new Array();
			 processNum = 0;
			 queue = new Array();
			 startDate = 0;
			 endDate = 0;
			 merageStatus = false;
			 redoNum = 3;
			 fileArr = new Array();
		},
		uuid: function() {
			  var s = [];
			  var hexDigits = "0123456789abcdef";
			  for (var i = 0; i < 36; i++) {
			    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
			  }
			  s[14] = "4"; 
			  s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); 
			  s[8] = s[13] = s[18] = s[23] = "";
			 
			  var uuid = s.join("");
			  return uuid;
			},
	   checkFileType: function(obj,params){
		   var typeArr = params.fileTypeFilter;
		   
		   if(typeArr.length == 0){
			   return true;
		   }
		   if(obj instanceof Array){
			  
			   for(var i = 0;i < obj.length;i++){
				   var mark = false;
				  if(obj[i].file.name.indexOf(".") == -1){
					  mark = false;
					  break;
				  }
				  var arr = obj[i].file.name.split(".")

				  for(var j = 0;j < typeArr.length;j++){
					  if(arr[arr.length - 1] == typeArr[j]){
						  mark = true;
					  }
				   }
				  if(mark == false){
					  break;
				  }
			   }
		   }else{
			   var mark = false;
			   if(obj.name.indexOf(".") == -1){
					  mark = false;
				  }
			  var arr = obj.name.split(".")
			  for(var j = 0;j < typeArr.length;j++){
				  if(arr[arr.length - 1] == typeArr[j]){
					  mark = true;
					  break;
				    }
				}
		   }
		   return mark;
	   }

	}
	
	$.fn.extend({
		uploadBigFile : function(option) {
			var options = $.extend({
				fileSplitSize : 1024 * 1024,
				uploadUrl : "",
				merageUrl : "",
				userData  : {},
				fileTypeFilter:[],
				fileSize:-1,
				isSuccessClosed:true,
				openDelete:true,
				left:"30%",
				top:"30%",
				isOpenDrag:true,
				backFunction : function(data) {

				},
				errorFunction:function(erroeStatus,msg){
					
				}
			}, option);	
			$("#inputUserId").val("");
			if(!jQuery.isEmptyObject(options.userData)){
				$("#inputUserId").val(options.userData.userId);
			}
			uploadFile.init(this, options);
		},
		appendResult: function(msg){
			$("#resultMsgId").append(" "+msg);
		},
		bindFileInput: function(params){
			var options = $.extend({
				fileSplitSize : 1024 * 1024,
				uploadUrl : "",
				merageUrl : "",
				userData  : {},
				clickId: "",
				fileSize:-1,
				isSuccessClosed:true,
				fileTypeFilter:[],
				backFunction : function(data) {

				},
				processFn: function(index,total,status){
					
				},
				errorFunction:function(erroeStatus,obj){
					/***********************************************************
					 * 0:浏览器不支持 1:大小超出限制， 2:文件格式错误, 3:没有选择文件 4：上传失败
					 */
				}
			},params);
			uploadFile.initBind(this,options);
		}
	});
})();