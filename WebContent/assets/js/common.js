/**
 *
 * 收集所有表单的方法
 */
 function OAajaxSubmit(id){
	var text = $("#"+id).find("[type='text']");
	var tel = $("#"+id).find("[type='tel']");
	var number = $("#"+id).find("[type='number']");
	var email = $("#"+id).find("[type='email']");
	var date = $("#"+id).find("[type='date']");
	var hidden =$("#"+id).find("[type='hidden']");
	var password =$("#"+id).find("[type='password']");
	var select= $("#"+id).find("select");
	var textarea= $("#"+id).find("textarea");
	var object = {};
	if(text.length>0){
		for(var i=0;i<text.length;i++){
			var id = $(text[i]).attr("id");
			var value=$(text[i]).val();
			object[id]=value;
		}
	}
	if(date.length>0){
		for(var i=0;i<date.length;i++){
			var id = $(date[i]).attr("id");
			var value=$(date[i]).val();
			object[id]=value;
		}
	}
	if(tel.length>0){
		for(var i=0;i<tel.length;i++){
			var id = $(tel[i]).attr("id");
			var value=$(tel[i]).val();
			object[id]=value;
		}
	}
	if(number.length>0){
		for(var i=0;i<number.length;i++){
			var id = $(number[i]).attr("id");
			var value=$(number[i]).val();
			object[id]=value;
		}
	}
	if(email.length>0){
		for(var i=0;i<email.length;i++){
			var id = $(email[i]).attr("id");
			var value=$(email[i]).val();
			object[id]=value;
		}
	}
	if(password.length>0){
		for(var i=0;i<password.length;i++){
			var id = $(password[i]).attr("id");
			var value=$(password[i]).val();
			object[id]=value;
		}
	}
	if(hidden.length>0){
		for(var i=0;i<hidden.length;i++){
			var id = $(hidden[i]).attr("id");
			var value=$(hidden[i]).val();
			object[id]=value;
		}
	}
	if(select.length>0){
		for(var i=0;i<select.length;i++){
			var $optioned = $(select[i]).find("option:selected");
			for(var j = 0;j < $optioned.length;j++){
				var value =$optioned.eq(j).val();
				var id=$(select[i]).attr("id");
				if(object[id] != undefined){
					object[id]=object[id] + "," + value;
				}else{
					object[id] =  value;
				}
			}
			
		}
	}
	if(textarea.length>0){
		for(var i=0;i<textarea.length;i++){
			var id = $(textarea[i]).attr("id");
			var value=$(textarea[i]).val();
			object[id]=value;
		}
	}
	return object;
 };

 // 将传入的json值 传入form表单中
 function setFormData(id, data){
	var text = $("#"+id).find("[type='text']");
	var tel = $("#"+id).find("[type='tel']");
	var number = $("#"+id).find("[type='number']");
	var email = $("#"+id).find("[type='email']");
	var date = $("#"+id).find("[type='date']");
	var hidden =$("#"+id).find("[type='hidden']");
	var password =$("#"+id).find("[type='password']");
	var select= $("#"+id).find("select");
	var textarea= $("#"+id).find("textarea");
	var json;
	if(typeof(data)=="string"){
		var json = eval("("+ data +")");
	}else{
		json = data;
	}
	if(text.length>0){
		for(var i=0;i<text.length;i++){
			var id = $(text[i]).attr("id");
			if(!(id in json)){
				id = $(text[i]).attr("name");
			}
			$(text[i]).val(json[id]);
			
		}
	}
	if(date.length>0){
		for(var i=0;i<date.length;i++){
			var id = $(date[i]).attr("id");
			if(!(id in json)){
				id = $(date[i]).attr("name");
			}
			$(date[i]).val(json[id]);
			
		}
	}
	if(tel.length>0){
		for(var i=0;i<tel.length;i++){
			var id = $(tel[i]).attr("id");
			if(!(id in json)){
				id = $(tel[i]).attr("name");
			}
			$(tel[i]).val(json[id]);
			
		}
	}
	if(number.length>0){
		for(var i=0;i<number.length;i++){
			var id = $(number[i]).attr("id");
			if(!(id in json)){
				id = $(number[i]).attr("name");
			}
			$(number[i]).val(json[id]);
			
		}
	}
	if(email.length>0){
		for(var i=0;i<email.length;i++){
			var id = $(email[i]).attr("id");
			if(!(id in json)){
				id = $(email[i]).attr("name");
			}
			$(email[i]).val(json[id]);
			
		}
	}
	if(password.length>0){
		for(var i=0;i<password.length;i++){
			var id = $(password[i]).attr("id");
			if(!(id in json)){
				id = $(password[i]).attr("name");
			}
			$(password[i]).val(json[id]);
			
		}
	}
	if(hidden.length>0){
		for(var i=0;i<hidden.length;i++){
			var id = $(hidden[i]).attr("id");
			if(!(id in json)){
				id = $(hidden[i]).attr("name");
			}
			$(hidden[i]).val(json[id]);
			
		}
	}
	if(select.length>0){
		for(var i=0;i<select.length;i++){
			var id=$(select[i]).attr("id");
			var optionVals = json[id].split(",");
			if(!(id in json)){
				id = $(select[i]).attr("name");
			}
			for(var j = 0;j < optionVals.length;j++){
				$(select[i]).find("option[value='"+optionVals[j]+"']").attr("selected",true);;
				
			}
		}
	}
	if(textarea.length>0){
		for(var i=0;i<textarea.length;i++){
			var id = $(textarea[i]).attr("id");
			if(!(id in json)){
				id = $(textarea[i]).attr("name");
			}
			$(textarea[i]).val(json[id]);
			
		}
	}
 }
	 
 //对象转字符串
function obj2str(o){
    var r = [];
    if(typeof o =="string") return "\""+o.replace(/([\'\"\\])/g,"\\$1").replace(/(\n)/g,"\\n").replace(/(\r)/g,"\\r").replace(/(\t)/g,"\\t")+"\"";
    if(typeof o == "object"){
        if(!o.sort){
            for(var i in o)
                r.push(i+":"+obj2str(o[i]));
            if(!!document.all && !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/.test(o.toString)){
                r.push("toString:"+o.toString.toString());
            }
            r="{"+r.join()+"}";
        }else{
            for(var i =0;i<o.length;i++)
                r.push(obj2str(o[i]));
            r="["+r.join()+"]";
        }
        return r;
    }
    return o.toString();
}
 
//扩展数组的删除
Array.prototype.del = function(n)
{
	if (n<0) return this;
	return this.slice(0,n).concat(this.slice(n+1,this.length));
}

$(window).error(function(){
	  //return true;
});

// 对Date的扩展，将 Date 转化为指定格式的String 
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
// 例子： 
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.Format = function(fmt) 
{ //author: meizz 
  var o = { 
    "M+" : this.getMonth()+1,                 //月份 
    "d+" : this.getDate(),                    //日 
    "h+" : this.getHours(),                   //小时 
    "m+" : this.getMinutes(),                 //分 
    "s+" : this.getSeconds(),                 //秒 
    "q+" : Math.floor((this.getMonth()+3)/3), //季度 
    "S"  : this.getMilliseconds()             //毫秒 
  }; 
  if(/(y+)/.test(fmt)) 
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
  for(var k in o) 
    if(new RegExp("("+ k +")").test(fmt)) 
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length))); 
  return fmt; 
} 
/**
 *转换日期对象为日期字符串
 * @param date 日期对象
 * @param isFull 是否为完整的日期数据,
 * 为true时, 格式如"2000-03-05 01:05:04"
 * 为false时, 格式如 "2000-03-05"
 * @return 符合要求的日期字符串
 */
function getSmpFormatDate(date, isFull) {
    var pattern = "";
    if (isFull == true || isFull == undefined) {
        pattern = "yyyy-MM-dd hh:mm:ss";
    } else {
        pattern = "yyyy-MM-dd";
    }
    return getFormatDate(date, pattern);
}
/**
 *转换当前日期对象为日期字符串
 * @param date 日期对象
 * @param isFull 是否为完整的日期数据,
 * 为true时, 格式如"2000-03-05 01:05:04"
 * 为false时, 格式如 "2000-03-05"
 * @return 符合要求的日期字符串
 */
function getSmpFormatNowDate(isFull) {
    return getSmpFormatDate(new Date(), isFull);
}
/**
 *转换long值为日期字符串
 * @param l long值
 * @param isFull 是否为完整的日期数据,
 * 为true时, 格式如"2000-03-05 01:05:04"
 * 为false时, 格式如 "2000-03-05"
 * @return 符合要求的日期字符串
 */
function getSmpFormatDateByLong(l, isFull) {
    return getSmpFormatDate(new Date(l), isFull);
}
/**
 *转换long值为日期字符串
 * @param l long值
 * @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss
 * @return 符合要求的日期字符串
 */
function getFormatDateByLong(l, pattern) {
    return getFormatDate(new Date(l), pattern);
}
/**
 *转换日期对象为日期字符串
 * @param l long值
 * @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss
 * @return 符合要求的日期字符串
 */
function getFormatDate(date, pattern) {
    if (date == undefined) {
        date = new Date();
    }
    if (pattern == undefined) {
        pattern = "yyyy-MM-dd hh:mm:ss";
    }
    return date.Format(pattern);
}
function getBrowserInfo(){
    var Sys = {};
    var ua = navigator.userAgent.toLowerCase();
    var re =/(msie|firefox|chrome|opera|edge|version).*?([\d.]+)/;
    var m = ua.match(re);
    Sys.browser = m[1].replace(/version/, "'safari");
    Sys.ver = m[2];
    return Sys;
}
function initLoading(){
    $("body").append("<!-- loading -->" +
            "<div class='modal fade' id='loading' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' data-backdrop='static'>" +
            "<div class='modal-dialog' role='document'>" +
            "<div class='modal-content'>" +
            "<div class='modal-header'>" +
            "<h4 class='modal-title' id='myModalLabel'>提示</h4>" +
            "</div>" +
            "<div id='loadingText' class='modal-body'>" +
            "<span class='glyphicon glyphicon-refresh' aria-hidden='true'></span>" +
            "处理中，请稍候。。。" +
            "</div>" +
            "</div>" +
            "</div>" +
            "</div>"
    );
}
function showLoading(text){
	if($("#loading").length<=0){
		initLoading();
	} 
    $("#loading").modal("show");
}
function hideLoading(){
    $("#loading").modal("hide");
}
$.ajaxSetup({ 
	beforeSend:function(xhr){
		//showLoading();
	},
	complete:function(XMLHttpRequest, textStatus){ 
		//hideLoading();
	}
});

 


