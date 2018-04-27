/**
 *每一行的数据放在tr上的，可以用table_id得到
 *每一个td中都有一个div,都有相同的name,不同的id,
 *第一个都由一个checkdbox,name都是有规则的
 *
 *
 */
(function($) {
	function loadPageContent(msg,table_title,tbodyId,opts){
		console.log("=================加载1=============================");
		var begin=new Date();
		if(table_title&&table_title.length>0&&tbodyId&&tbodyId.length>0){
			if(msg&&msg.list&&msg!="null" && msg!=null){
				var tbody="";
				var tfile = $("#"+table_title).children();
				if(tfile.length<=0){ 
					return;
				}
				$("#"+tbodyId).html("");
				var numberCol =$(tfile[0]).css("display");
				for(var i=0;i<msg.list.length;i++){
					var vb = msg.list[i];
					var tr=$("<tr></tr>");
					//增加关于tr上面的属性设置
					if(typeof(vb["table_id"])=="undefined"){
						tr.attr("name",tbodyId+"_tr");
						tr.attr("id",vb.table_id);
						tr.attr("trId",vb.table_id); 
					}else{
						tr.attr("name",tbodyId+"_tr");
						tr.attr("id",vb.id);
						tr.attr("trId",vb.id);
					}
					//增加第一行序列号
					var td = $("<td></td>");
						td.attr("name",tbodyId+"_tr_td_"+(i+1));
						td.text(i+1); 
					if(numberCol =="none"){
						td.hide();
					} 
					tr.append(td);
					//增加第二行checkbox
					 if(opts.checkbox==true){
						 if($(tfile[1]).css("display") =="none"){
							 tr.append('<td width="40" style="display:none;"><input type="checkbox" name="'+tbodyId+'_checkbox" id="'+tbodyId+'_checkbox"></td>');	 
						 }else{
							
							 tr.append('<td width="40"><input type="checkbox" name="'+tbodyId+'_checkbox" id="'+tbodyId+'_checkbox"></td>');
							
						 }
					 }  
					for(var j=0;j<tfile.length;j++){
						if(j<=1){
							continue;
						}
						var th =$(tfile[j]);
						var fname = th.attr("fname");
						var width =	th.attr("width"); 
						var display=th.css("display");
						var td1 = $('<td style="display:'+display+'"></td>');
						var div =$('<div id="'+tbodyId+"_"+fname+"_tr_td_"+(i+1)+'" name="'+tbodyId+"_"+fname+"_tr_td"+'" style="width:90%;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;"></div>');
							//是否换行
							if(opts.isEnter == true){ 
								div =$('<div id="'+tbodyId+"_"+fname+"_tr_td_"+(i+1)+'" name="'+tbodyId+"_"+fname+"_tr_td"+'" style="width:100%"></div>');
							}
							td1.attr("name",tbodyId+"_tr_td_"+(i+1));
							td1.append(div);
							 
						if(typeof(vb[fname])=="undefined"){
							div.text(""); 
						}else{
							div.attr("title",vb[fname]);
							div.text(vb[fname]);
						} 
						tr.append(td1); 
					} 
					tr.data(vb.table_id,vb); 
					tr.attr("id",vb.table_id);
					
					$("#"+tbodyId).append(tr);
					if(opts.checkbox==true){
						$("tr[id="+vb.table_id+"]").click(function(){
							var obj = $(this).find("input[type=checkbox]").eq(0);
							if($(obj).prop("checked")){
								$(obj).prop("checked",false);
							}else{
								$(obj).prop("checked",true);
							}
						});
					}
				}
			}
			var end=new Date();
			console.log("表格加载时间为："+(end-begin)+"毫秒");
		}
	}

	//组件加载数据，只是单纯的加载数据
	function loadPageContent(msg,table_title,tbodyId,opts){
		console.log("=================加载3=============================");
//		console.log(opts);
		var checkboxId = opts.checkboxId;
//		console.log("data size: "+msg.list.length);
		if(table_title&&table_title.length>0&&tbodyId&&tbodyId.length>0){   
			if(msg&&msg.list&&msg!="null" && msg!=null){
					var tbody="";
					var tfile = $("#"+table_title).children();
					var skipTdNum = 0;
					if(tfile.length<=0){
						//alert("无数据列");
						return;
					}
					$("#"+tbodyId).html("");
					
					for(var i=0;i<msg.list.length;i++){ 
						var vb = msg.list[i]; 
						var tr;
						if(vb["table_id"]){
							tr= '<tr name="'+tbodyId+'_tr" id="'+vb.table_id+'" trId='+vb.table_id+'>';
						}else{
							tr= '<tr name="'+tbodyId+'_tr" id="'+vb.id+'" trId='+vb.id+'>';
						}
						if($(tfile[0]).css("display") =="none"){
							tr+='<td style="display:none;" name="'+tbodyId+"_tr_td_"+(i+1)+'">'+(i+1)+'</td>';
						}else{
							tr+='<td name="'+tbodyId+"_tr_td_"+(i+1)+'">'+(i+1)+'</td>';
						} 
						 if(opts.checkbox==true){
							
							 if($(tfile[1]).css("display") =="none"){
								 tr+='<td width="40" style="display:none;"><input value='+vb.table_id+' type="checkbox" name="'+tbodyId+'_checkbox" id="'+tbodyId+'_checkbox"></td>';	 
							 }else{
								 tr+='<td width="40"><input  type="checkbox" value='+vb.table_id+' name="'+tbodyId+'_checkbox" id="'+tbodyId+'_checkbox"></td>';
							 }
						 } else{
							 skipTdNum = 1;
						 }
						 //如果选择了checkbox==false,一定要把th上面的chebox项目删除掉，不然就要出问题
						 for(var j=0;j<tfile.length;j++){
							  	if(j<= ( 1 - skipTdNum )){
							  		continue;//因为第一行和第二行是序号和checkecbox
							  	}
							  	var tfj =$(tfile[j]);
							    var b = false;
								for(var tc in vb){ 
									if(tfj.attr('fname')==tc){
										b =true;
										var tc =tfj.attr('fname');
										var width = tfj.width();
										if(tc=="showTime"){
											tr+='<td name="'+tbodyId+"_tr_td_"+(i+1)+'">'+vb[tc].substring(0,10)+'</td>';
										}else{
											if(tfj.css("display")=="none"){
												tr+='<td style="display:none;" width="'+width+'"><div  id="'+tbodyId+"_"+tc+"_tr_td_"+(i+1)+'" name="'+tbodyId+"_"+tc+"_tr_td"+'" style="width:90%;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" title="'+vb[tc]+'">'+vb[tc]+'</div></td>';	
											}else{ 
												tr+='<td width="'+width+'"><div  id="'+tbodyId+"_"+tc+"_tr_td_"+(i+1)+'" name="'+tbodyId+"_"+tc+"_tr_td"+'" style="width:90%;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" title="'+vb[tc]+'">'+vb[tc]+'</div></td>';
											}
										} 
									}
								}
								if(b==false){//如果字段不存在，就加空字段 
									tr+='<td width="'+width+'"><div id="'+tbodyId+"_"+$(tfile[j]).attr('fname')+"_tr_td_"+(i+1)+'" name="'+tbodyId+"_"+$(tfile[j]).attr('fname')+"_tr_td"+'" style="width:90%;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" title=""></div></td>';	
								}
						 }
						tr+='</tr>';
						//tbody+=tr; 
						
						$("#"+tbodyId).append(tr);
						$("tr[trId="+vb.table_id+"]").data(vb.table_id,vb); 
						
					}
					if(opts.checkbox==true){
						$("tr[name="+tbodyId+"_tr]").click(function(){
							var mark = 0;//1:全选，2：全未选
							var obj = $(this).find("input[type=checkbox]").eq(0);
							var flag = false;
							if($(obj).prop("checked")){
								$(obj).prop("checked",false);
							}else{
								$(obj).prop("checked",true);
								flag = true;
							}
							//判断当前是否全部被选中
							var $_obj = $("#"+tbodyId).find("input[type=checkbox]");
							var checkBoxSum = $_obj.length;
//							console.log(checkBoxSum);
								$_obj.each(function(){
									if($(this).prop("checked")){
										mark = parseInt(mark) + 1
									}else{
										mark = parseInt(mark) - 1
									}
								});
//							console.log("mark: "+Math.abs(parseInt(mark)));
								
							if(parseInt(checkBoxSum) == Math.abs(parseInt(mark))){
								if($(obj).prop("checked")){
									$("#"+checkboxId).prop("checked",true);
								}else{
									$("#"+checkboxId).prop("checked",false);
								}
							}else{
								$("#"+checkboxId).prop("checked",false);
							}
							opts.trFn(this);
						});
						
						$("#"+checkboxId).click(function(){
							var $_childrenCheckBox = $("#"+tbodyId).find("input[type=checkbox]");
							$_childrenCheckBox.each(function(){
								if($("#"+checkboxId).prop("checked")){
									$(this).prop("checked",true);
								}else{
									$(this).prop("checked",false);
								}
							});
							
						});
						$("tr[name="+tbodyId+"_tr]").find("input[type=checkbox]").eq(0).click(function(){
							if($(this).prop("checked")){
								$(this).prop("checked",false);
							}else{
								$(this).prop("checked",true);
							}
						});
					}
				}
			
			}
	};
	//点击事件 li点击事件
	function LiLister(event){
		if(!$(this).is(".currentPageMsg")){
			var msg =event.data[0];
			var pagination=event.data[1];
			var data=event.data[2];
			var opts=event.data[3];
			var tvalue=$(this).attr("page-num");/*
			$("#"+pagination).find(".active").toggleClass("active");
			$(this).toggleClass("active");*/
			
			opts.data.start=$(this).text();
			
			if(tvalue == '<' || tvalue == '<<' || tvalue == '>' || tvalue == '>>'){
				loadPage($("#"+opts.tbodyId).data("data"),pagination,tvalue,data,opts);
				return;
			}

			loadPageData(opts);
			loadPage($("#"+opts.tbodyId).data("data"),pagination,tvalue,data,opts);
		}
			
	};
	function loadPageData(opts){
		 $.ajax({
		   type: "POST",
		   url: opts.url,
		   async:false,
		   data:opts.data,
		   dataType:"json",
		   success: function(msg){  
			   console.log(msg);
			     $("#"+opts.tbodyId).data("data",msg);
				 loadPageContent(msg,opts.table_title,opts.tbodyId,opts);
				 //执行成功的方法
				 opts.success();
				 //findText(opts.data.start,opts.pagination);
				 //选中已经有page
				 //loadPage(msg,opts.pagination,"<<",opts.data,opts);
		   }
		});
	}
	//给丛新加事件的li加css选中效果
	function findText(flag,pagination){
					for(var i=0;i< $("#"+pagination).children().length;i++){
						var ab = $("#"+pagination).children()[i];
						if($(ab).text()==flag){
								$("#"+pagination).find(".active").toggleClass("active");
								$(ab).toggleClass("active");
								break;
						}
					}
	};

	//生成分页
	function loadPage(msg,pagination,flag,data,opts){
		console.log(msg);
				 if(msg=="null" || msg=="null" || msg==null){
			    	 return;
			     }
				 
				var showIndex = 7;//显示的页码数
		//		var allPage =parseInt(msg.count%msg.sum)==0?parseInt(msg.count/msg.sum):parseInt(msg.count/msg.sum+1);
				var allPage = msg.count;
//				console.log("allPage: "+allPage);
				var mypage =parseInt($("#"+pagination).attr("cpage"));
			    if(!mypage){
			    	mypage=1;
			    }
				var ULContent="";
				var startPageNum = 0;
				var endPageNum = 0;
				var maxCpage = parseInt(allPage % showIndex == 0 ? 
						parseInt(allPage/showIndex) : parseInt(allPage/showIndex + 1));
				/**
				 * 计算显示页码数的开始与结束
				 * **/
				if(flag === "" || flag == "<<"){
					startPageNum = 1;
					$("#"+pagination).attr("cpage",1);
				}else if(flag == "<"){
					if(mypage == 1){
						startPageNum = 1;
						$("#"+pagination).attr("cpage",1);
					}else{
						startPageNum = (mypage - 2)  * showIndex + 1;
						$("#"+pagination).attr("cpage",parseInt(mypage) - 1);
					}
				}else if(flag == ">" || flag == "..."){
					if(allPage > (mypage * showIndex)){
						startPageNum = mypage  * showIndex + 1;
						$("#"+pagination).attr("cpage",parseInt(mypage) + 1);
					}else{
						startPageNum = (mypage - 1) * showIndex + 1;
						$("#"+pagination).attr("cpage",maxCpage);
					}
				}else if(flag == ">>"){
					if(allPage % showIndex){
						startPageNum = allPage - allPage % showIndex + 1;
					}else{
						startPageNum = allPage -  showIndex + 1; 
					}
					$("#"+pagination).attr("cpage",maxCpage);
				}else if(parseInt(flag)){
					startPageNum = (mypage - 1) * showIndex + 1;
					$("#"+pagination).attr("cpage",parseInt(mypage));
				}
				
				if(startPageNum + showIndex > allPage){
					endPageNum = allPage;
				}else{
					endPageNum = startPageNum + showIndex - 1;
				}
				
//				console.log("startPageNum: "+startPageNum);
//				console.log("endPageNum: "+endPageNum);
//				console.log("showIndex: "+showIndex);
//				console.log("allpage",allPage);
				
				ULContent ='<li page-num="<<" class="page-item" f="1"><a  class="page-link" href="javascript:;"" value="1" id="'+pagination+'pali1"><<</a></li>';
				ULContent +='<li page-num="<" class="page-item"  f="1"><a  class="page-link" href="javascript:;"" value="up" id="'+pagination+'pali2"><</a></li>';
				
				for(var i = parseInt(startPageNum);i <= parseInt(endPageNum);i++){
					 ULContent+='<li page-num="'+i+'" class="page-item" ><a  class="page-link" href="javascript:;" name="'+pagination+'pali'+'" id="'+pagination+'pali3">'+i+'</a></li>';
				}
				/***计算后置省略号**/
				if(endPageNum < allPage){
					 ULContent+='<li class="page-item" page-num="..." ><a  class="page-link" href="javascript:;" name="'+pagination+'pali'+'" id="'+pagination+'pali4">...</a></li>'
				}
				
				ULContent+='<li page-num=">" f="1"  class="page-item" ><a class="page-link" href="javascript:;"" value="down">></a></li>';
				ULContent+='<li page-num=">>" f="1" class="active page-item"><a  class="page-link" href="javascript:;" value="1">>></a></li>';
				ULContent+='<li  f="1" class="disabled page-item currentPageMsg"><a  class="page-link" href="javascript:;">当前查询共【'+msg.sum+'】条数据</a></li>';
				
				$("#"+pagination).html(ULContent);//改入页面
				$("#"+pagination).children().click([msg,pagination,data,opts],LiLister);
				findText(flag,pagination);
				
	};
	//初始化
	function init(_this,opts) {
		 $.ajax({
		   type: "POST",
		   url: opts.url,
		   data:opts.data,
		   dataType:"json",
		   success: function(msg){
//			   	  console.log(msg);
			      $("#"+opts.tbodyId).data("data",msg);
//			      console.log($("#"+opts.tbodyId).data("data"));
//			      console.log(opts.tbodyId);
			   	 if(!msg.list || msg.list.length<=0){
			   		// alert(msg.msg);
			   		 $("#"+opts.tbodyId).html("");
			   		 $("#"+opts.pagination).html("");
			   		 opts.success();
			   		 return;
			   	 }
			     if(msg=="null" || msg=="null"){
			    	 return;
			     }
				 loadPageContent(msg,opts.table_title,opts.tbodyId,opts);
				 opts.success();
				 if(opts.page == true){
					 loadPage(msg,opts.pagination,"<<",opts.data,opts);
					 findText(opts.data.start,opts.pagination);
				 } 
				 reloadImgColunm(opts);
				 console.log("---------------------------------");
				 console.log($(_this).attr("id"))
				 $("#"+$(_this).attr("id")).colResizable();
		   }
		});
	};
	function reloadImgColunm(options){
		var colunmIndex = -1;
		var maxHeight = 200;
		var maxWidth = 200;
		
		if(jQuery.isEmptyObject(options.imgColunm)){
			return;
		}
		$("#"+options.table_title).find("th").each(function(index){
			if(options.imgColunm == $(this).attr("fname")){
				colunmIndex = index;
			}
		});
		if(colunmIndex == -1){
			return;
		}
		console.log($("#"+options.tbodyId).find("tr").length);
		$("body").append("<div id='reloadImgId'></div>");
		$("#"+options.tbodyId).find("tr").each(function(index){
			$(this).find("td").each(function(i){
				if(colunmIndex == i){
					var url = $(this).find("div").eq(0).attr("title");
					var _img = document.createElement("img");
					
					_img.setAttribute("style","height:30px;width:30px;");
					_img.setAttribute("src",url);
					$(this).html(_img);

					$(_img).on("mouseover",function(){
						var _div = document.createElement("div");
						var _url = $(this).attr("src");
						var _img = document.createElement("img");
							_img.setAttribute("src",url);
						var imgHeight =_img.height;
						var imgWidth = _img.width;
		
						var newHeight = 0;
						var newWidth = 0;
						if(imgWidth/imgHeight>= maxWidth/maxHeight){      
					         if(imgWidth>maxWidth){        
					        	 newWidth=maxWidth;      
					        	 newHeight=(imgHeight*maxWidth)/imgWidth;      
					         }else{      
					        	 newWidth=imgWidth;      
					        	 newHeight=imgHeight;      
					        }      
					     }else{      
					         if(imgHeight>maxHeight){        
					        	 newHeight=maxHeight;      
					        	 newWidth=(imgWidth*maxHeight)/imgHeight;              
					         }else{      
					        	 newWidth=imgWidth;      
					        	 newHeight=imgHeight;        
					        }      
					         
					     }   
						_img.setAttribute("style","height:"+newHeight+"px;width:"+newWidth+"px");
						_div.setAttribute("style","position: absolute;;z-index:10000;left:"+($(this).offset().left - newWidth)+"px;top:"+$(this).offset().top+"px;");
						_div.appendChild(_img);
						$("#reloadImgId").append(_div);
					});
					$(_img).on("mouseout",function(){
						$("#reloadImgId").html("");
					});
				}
			});
		});
	}
	//获取行数据
	function getTrData(obj,options){
		var data =  $("#"+options.tbodyId).data("data");
//		console.log("------------");
//		console.log(data);
		if(data != undefined && data.list != undefined){
			var arr = data.list;
			var $_obj = $("#"+options.tbodyId).find("input[type=checkbox]:checked");
//			console.log($_obj.length);
			var result = new Array()
			var j = 0;
			$_obj.each(function(){
				for(var i = 0;i < arr.length;i++){
					if($(this).val() == arr[i].table_id){
						result[j] = arr[i];
						j = parseInt(j) + 1;
						break;
					}
				}
			});
			
			return result;
		}
		return undefined;
	};
	
	//组件初始化
	$.fn.wgrid = function(method, options) {
		if (typeof (method) == "string") {

			return $.fn.wgrid.methods[method](this, options);
		} else {
			var opts = $.extend({},$.fn.wgrid.defaults,method);
			init(this,opts);
		}
	};
	
	//组件默认参数
	$.fn.wgrid.defaults = {
		table_title:"",
		tbodyId:"",
		pagination:"",
		checkbox:false,
		page:true,
		isEnter:false,
		url:"",
		checkboxId: "",
		imgColunm:"",
		data:{start:1,limit:20},
		success:function(){
			//alert("d");
		},
		trFn:function(obj){
			
		}
	};
	//组件方法注册
	$.fn.wgrid.methods = {
		init : function(_this, opts) {
			init(_this,opts);
		},
		getTrData: function(_this,options){
			return getTrData(_this,options);
		}
	};
	
})(jQuery);