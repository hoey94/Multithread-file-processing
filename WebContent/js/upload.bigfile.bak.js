(function() {
	var success = 0;//单文件分割后上传文件个数
	var mark = 0;//上传文件成功个数
	var fileLength = 0;//上传文件个数
	var merageParams = new Array();//后台文件合并参数
	var fileNameArr = new Array();
	var processNum = 0;
	function AjaxQueue(funcName, params) {
		var queueTask = [];
		this.pushQueue = function(func, args) {
			var funObj = {
				"func" : func,
				"params" : args
			};
			queueTask.push(funObj);
			return queueTask.length;
		};
		this.nextAjax = function(i, index) {
			var start = 0;
			var end = 0;
			if ((i + 1) <= queueTask.length) {
				if ((i + index + 1) <= queueTask.length) {
					start = i;
					end = i + index;
				} else {
					start = i;
					end = queueTask.length;
				}
				for (var j = start; j < end; j++) {
					var fObj = queueTask[j];
					fObj.func.apply(this, fObj.params);
					queueTask.shift();
				}
			}
		};
		this.nextIndex = function(index){
			if (queueTask.length > 0) {
				for(var i = 0;i < (index > queueTask.length ? queueTask.length : index);i++){
					var fObj = queueTask[0];
					fObj.func.apply(this, fObj.params);
					queueTask.shift();
				}
			}
		};
		this.next = function() {
			if (queueTask.length > 0) {
				var fObj = queueTask[0];
				fObj.func.apply(this, fObj.params);
				queueTask.shift();
				this.next();
			}
		};
		this.size = function(){
			function a(){
				return queueTask.length;
			}
			return a;
		}
	}
	var queue = new AjaxQueue();
	var startDate = 0;
	var endDate = 0;
	var indexHtml = '<div id="uploadKeepOutPage" style="width:100%;height:100%;background-color:gray;opacity:0.1; z-index:100000000;position: absolute;left:0;top:0;"></div>';
	var merageStatus = false;
	var redoNum = 3;
	var errorQueue = new AjaxQueue();
	var uploadFile = {
		init : function(obj, params) {
			if(!window.applicationCache){
				alert("浏览器不支持html5,请升级到最新版本!");
				return;
			}
			uploadFile.initObject();
			
			startDate = Date.parse(new Date());
			var btnStyle = 'style="display: inline-block;padding: 6px 12px;margin-bottom: 0;font-size:'
							+ '14px;font-weight: normal;line-height: 1.42857143;text-align: center;white-space: nowrap;'
							+ 'vertical-align: middle;-ms-touch-action: manipulation;touch-action: manipulation;cursor: pointer;'
							+ '-webkit-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;'
							+ 'background-image: none;border: 1px solid transparent;border-radius: 4px;color: #fff;background-color: #5bc0de;border-color: #46b8da;"';
			var baseHtml = '<div id="uploadFileContent" style="width: 600px;height: 400px;border: 1px solid #ddd;'
					+ 'position: absolute;left: 20%;top: 20%;background-color:white;z-index:10000000">'
					+ '<div id="uploadFileTitle" style="font-size: 20px;height: 30px;width: 100%;'
					+ 'border-bottom: 1px solid #ddd;line-height: 30px;">&nbsp;上传'
					+'<span id="closeUploadPageId" style="margin-left:530px;cursor: pointer;" >x</span>'
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
					+ '<span style= "margin-left:10px;"></span><span id="resultMsgId"></span>'
					+ '</div>'
					+ '<div id="uploadFileListId" style="margin-left: 10px;width: 98.5%;height:308px;'
					+ 'overflow:hidden;  ">' + '</div>' + '</div>';
			
			$("#uploadFileContent").remove();
			$("body").append(baseHtml);
			console.log($("#realBtn").find("input[type=file]").length);
			$("#realBtn").find("input[type=file]").each(function(){
				$(this).on("change", function() {
					console.log("===========change================"+merageStatus);
					if(merageStatus == true){
						console.log("=--------------------");
						merageStatus = false;
						 $("#resultMsgId").html("");
						 $("#uploadFileListId").html("");
					}
					uploadFile.appendFileProgress();
				});
			});
			$("#uploadFileId").on("click", function() {
				$("body").append(indexHtml);
				/*if(!jQuery.isEmptyObject(errorQueue)){
					for(var key : errorFun){
						var fObj = errorFun[key];
					}
				}*/
				
				var fileArr = uploadFile.getUploadFileArray();
				console.log("===================1====================");
				$("#resultMsgId").html("上传个数: "+fileArr.length);
				uploadFile.upload(params,fileArr);
			});
			$("#closeUploadPageId").on("click",function(){
				$("#uploadFileContent").remove();
			});
		},
		getUploadFileArray: function(){
			var fileInput = document.getElementById('uploadFileListId').getElementsByTagName("input");
			var fileArr = new Array();
			for(var i = 0;i < fileInput.length;i++){
				var $file = fileInput[i].files;
				for(var j = 0;j < $file.length;j++){
					fileArr.push($file[j]);
				}
			}
			return fileArr;
		},
		upload : function(params,fileArr) {
			console.log(mark);
			console.log(fileArr);
			var blob = fileArr[parseInt(mark)];//document.getElementById('file').files[mark]
			fileLength = fileArr.length;
			console.log(fileLength);
			if(fileLength <= 0){
				$("#uploadKeepOutPage").remove();
				alert("请选择文件");
				return;
			}
			
			console.log(blob);
			console.log(jQuery.isEmptyObject(queue));
			var fileSize = blob.size;

			var total = Math.ceil(fileSize / params.fileSplitSize);
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
				var uploadPartParams = new Array();
				
				uploadPartParams.push(total);
				uploadPartParams.push(i + 1);
				uploadPartParams.push(partFile);
				uploadPartParams.push(fileTempName);
				uploadPartParams.push(params);
				uploadPartParams.push(blob.name);
				uploadPartParams.push(fileArr);
				//uploadFile.uploadPartFile(total, i + 1, partFile, blob.name, params);
			
				var fun = function(total, index, partFile, blobName, params,fileName,fileArr){
					return (function(){
						uploadFile.uploadPartFile(total, index, partFile, blobName, params,fileName,fileArr);
					})();
				}
				queue.pushQueue(fun,uploadPartParams);
				console.log("--------------1-------------------");
				console.log(queue.size);
				console.log("--------------1-------------------");
			}
			
			queue.nextIndex(5);
		},
		uploadPartFile : function(total, index, partFile, blobName, params,fileName,fileArr) {
			var fd = new FormData();
			console.log("=================uploadPartFile: "+index+" ===================="+blobName);
			console.log("=================uploadPartFile: "+index+" ===================="+fileName);
			console.log(partFile);
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
					/*$("#uploadKeepOutPage").remove();
					alert("上传失败，请重试!");*/
					console.log("剩余次数： "+redoNum);
					redoNum -= 1;
					
					var fun = function(total, index, partFile, blobName, params,fileName,fileArr){
						return (function(){
							uploadFile.uploadPartFile(total, index, partFile, blobName, params,fileName,fileArr);
						})();
					}
					errorQueue.pushQueue(fun);
					
					if(redoNum > 0){
						uploadFile.uploadPartFile(total, index, partFile, blobName, params,fileName,fileArr)
					}
					if(redoNum == 0){
						alert("上传失败，请重试!");	
					}
				    // 通常 textStatus 和 errorThrown 之中
				    // 只有一个会包含信息
				    this; // 调用本次AJAX请求时传递的options参数
				},
				success : function() {
					success += 1;
					uploadFile.changeProgress(index, total);
					queue.nextIndex(1);
					if (parseInt(success) == parseInt(total)) {
						uploadFile.changeDivScroll();
						var json = {};
						json.name = blobName;
						json.total = total;
						merageParams[mark] = json;

						success = 0;
						mark += 1;
						var fileNameJSON = {};
						fileNameJSON.name = fileName;
						fileNameArr.push(fileNameJSON);
						console.log(mark+"======"+fileLength);
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
				uploadFile.initObject();
				uploadFile.removeUploadFileInput();
				merageStatus = true;
				params.backFunction(JSON.stringify(jsonData));
				$("#uploadKeepOutPage").remove();
			});
		},
		changeProgress : function(index, total) {
			var prenct = (Math.floor(success * 100 / total)) + "%";
			$("#progress_" + mark).find(".progress").eq(0).find("span").eq(0)
					.html(prenct);
			$("#progress_" + mark).find(".progress-bar").eq(0).css("width",
					prenct);
		},
		removeUploadFileInput:function(){
			$("#uploadFileListId").find("input[type=file]").each(function(index){
				$(this).remove();
			});
		},
		appendFileProgress : function() {
			var fileArr = document.getElementById('changefileId').getElementsByTagName("input")[0].files;
			var _group_all_height = 0;
			for (var i = 0; i < fileArr.length; i++) {
				var _group = document.createElement("div");
				var _process = document.createElement("div");
				var _process_div = document.createElement("div");
				var _process_span = document.createElement("span");
				var _group_span = document.createElement("span");

				_group.setAttribute("id", "progress_" + (i + processNum ));
				_group.setAttribute("class", "form-group");
				_group.setAttribute("style", "height:40px;width:97%;margin-top:10px;font-size:14px;");

				_process.setAttribute("style", "padding: 0;margin: 0; height: 20px;overflow: hidden;background-color: #f5f5f5;border-radius: 4px;-webkit-box-shadow: inset 0 1px 2px rgba(0, 0, 0, .1);box-shadow: inset 0 1px 2px rgba(0, 0, 0, .1);");
				_process.setAttribute("class", "progress");

				_process_div.setAttribute("style", "width: 0%;float: left;height: 100%;font-size: 12px;line-height: 20px;color: #fff;text-align: center;background-color: #337ab7;");
				_process_div.setAttribute("class", "progress-bar");
				_process_div.setAttribute("role", "progressbar");
				_process_div.setAttribute("aria-valuenow", "60");
				_process_div.setAttribute("aria-valuemin", "0");
				_process_div.setAttribute("aria-valuemax", "100");

				_process_span.textContent = "0%";

				_group_span.setAttribute("class", "filePath");
				_group_span.textContent = fileArr[i].name;

				_process.appendChild(_process_div);
				_process.appendChild(_process_span);
				_group.appendChild(_process);
				_group.appendChild(_group_span);
				
				$("#uploadFileListId").append(_group);
				
				_group_all_height = $("#uploadFileListId").find(".form-group").length * parseInt($(_group).css("height").replace(
						'px', ''));
				var list_height = parseInt($("#uploadFileListId").css("height")
						.replace('px', ''));

				if (_group_all_height >= list_height) {
					$("#uploadFileListId").css('overflow-y', 'scroll');
				}

			}
			processNum += fileArr.length;
			var $file =  document.getElementById('changefileId').getElementsByTagName("input")[0];
			document.getElementById("uploadFileListId").appendChild($file);
			document.getElementById("realBtn").appendChild($file.cloneNode());
			$("#realBtn").find("input[type=file]").each(function(){
				$(this).on("change", function() {
					if(merageStatus == true){
						console.log("=--------------------");
						merageStatus = false;
						 $("#resultMsgId").html("");
						 $("#uploadFileListId").html("");
					}
					uploadFile.appendFileProgress();
				});
			});
		},
		changeDivScroll: function(){
			$("#uploadFileListId").scrollTop($("#uploadFileListId").scrollTop()+40);
		},
		initObject: function(){
			success = 0;//单文件分割后上传文件个数
			 mark = 0;//上传文件成功个数
			 fileLength = 0;//上传文件个数
			 merageParams = new Array();//后台文件合并参数
			 fileNameArr = new Array();
			 queue = new AjaxQueue();
			processNum = 0;
			
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
			}

	}
	
	$.fn.extend({
		uploadBigFile : function(option) {
			var options = $.extend({
				fileSplitSize : 1024 * 1024 * 10,
				uploadUrl : "",
				merageUrl : "",
				backFunction : function(data) {

				}
			}, option);	
			
			uploadFile.init(this, options);
		},
		appendResult: function(msg){
			$("#resultMsgId").append(" "+msg);
		}
	});
})();