/**
 *侯语过版本的验证插件1.0
 */

(function($) {
	/*
	 *不能为空，不能为特殊字符，只能为数字
	 * notNull,isSpace,isNumber,isMobil,isLength-7(必须大于7),isMinLength-7(必须小于7)
	 * 
	 * is2Number验证小数，只保留后面两位
	 * 
	 * notsm(不能有-这种符号)
	 * 
	 * isTime0945验证时间格式为 9:45或者09:35
	 * 
	 * 
	 * kong=true 表示，如果填了，就一定要通过验证，如果不填，也可以通过验证
	 * kong=true,会先验证这个，在验证meth中的方法，如果数据可以为空，文本又为空，就跳出了循环了。。验证为true
	 */
	//初始化方法
	function init(_this,opts){ 
		try{
			 var allctr =_this.find("[require='true']");
			 var isAll=true; 
			 if(allctr.length<=0){
				 return true;
			 }else{
				 //console.log(allctr.length);
					for(var i=0;i<allctr.length;i++){
						var kong = $(allctr[i]).attr("kong");
						if(kong==null || kong.length<=0 || kong=="undefind"){
						  //表示没有这个属性，系统不管理
						}else{
						  if(kong=="true"){
							  var valuek = $(allctr[i]).val();
							  if(valuek ==""){
								  continue;
							  }
						  }
						}
						var meth = $(allctr[i]).attr("meth");
						 
						if(meth==null || meth.length<=0 || meth=="undefind"){
							alert("有控件需要验证,但没有验证方法!");
							continue;
						}
					 
						var arr = meth.split(",");
						for(var j=0;j<arr.length;j++){ 
								var m = arr[j];
								var flag=false; 
								if(m=="notNull"){
									flag =notNull($(allctr[i]).val()+"");
								}else if(m=="isSpace"){
									flag =isSpace($(allctr[i]).val()+"");
								}else if(m=="isNumber"){
									flag =isNumber($(allctr[i]).val()+"");
								}else if(m=="isMobil"){
									flag =isMobil($(allctr[i]).val()+"");
								}else if(m.indexOf("isLength")>=0){
									flag =isLength(m,$(allctr[i]).val()+"");
								}else if(m.indexOf("isMinLength")>=0){
									flag =isMinLength(m,$(allctr[i]).val()+""); 
								}else if(m=="notsm"){
									flag =notsm($(allctr[i]).val()+"");
								}else if(m=="is2Number"){
									flag=is2Number($(allctr[i]).val()+"");
								}else if(m=="isTime0945"){
									flag =isTime0945($(allctr[i]).val()+"");
								}else if(m=="isEmail"){
									flag =isEmail($(allctr[i]).val()+"");
								}else if(m=="isnull"){
									flag =isNull($(allctr[i]).val()+"");
								}; 
								if(flag==0){
									isAll=false; 
									var pro =$(allctr[i]).attr("placeholder");
									alert(pro);
									$(allctr[i]).focus();
									// $(allctr[i]).tooltip();
									// $(allctr[i]).tooltip("open");
									 //$(allctr[i]).popover("open");
									// setTimeout(function(){
									//	 $(allctr[i]).tooltip("close");
									// },3000);
									return false;
								} 
						}
					}
			 }
			 return isAll;
		}catch(e){
				console.log("验证出现在问题！");
		}
			
	};
	//不能为空
	function notNull(thisValue){ 
		if(thisValue==null ||thisValue.length<=0||thisValue==""){
			return 0;
		}else{
			return 1;
		}
	};
	//不能为空
	function isNull(){ 
		if(thisValue==null ||thisValue.length<=0||thisValue==""){
			return 1;
		}else{
			return 0;
		}
	};
	
	//不能有-
	function notsm(thisValue){
		var a =thisValue.indexOf("-");
		if(a>=0){
			return false;
		}else{
			return true;
		}
	};
	//不能输入特殊字符
	function isSpace(thisValue){
			//notNull(thisValue);
			var regu = '^[\u4e00-\u9fa5a-zA-Z0-9/\]*$';
			var re = new RegExp(regu);
			if (re.test(thisValue)) {
			   return true;
			} else{
			 return false;
		 }
	};
	//只能输入数字
	function isNumber(thisValue){
		//notNull(thisValue);
		var Letters = "1234567890";
		var i;
		var c;
		for( i = 0; i < thisValue.length; i ++ )
		{
			c = thisValue.charAt(i);
			if (Letters.indexOf(c)==-1)
			{
				return 0;
			}
		}
		return 1;
	};
	//验证两位小数
	function is2Number(value){
		   return /^-?\d+(\.\d{1,2})?$/.test(value);
	}
	//验证时间格式09:45
	function isTime0945(value){
		   if(value.indexOf(":")==2){
			   value =value.replace(":","");
			   return isNumber(value);
		   }else{
			   return false;
		   }
	}
	//验证QQ号码5-11位
	function isQQ(qq) {
	    var filter = /^\s*[.0-9]{5,11}\s*$/;
	    if (!filter.test(qq)) {
	        return false;
	    } else {
	        return true;
	    }
	}

	//验证邮箱格式
	function isEmail(str) {
	    if (str.charAt(0) == "." || str.charAt(0) == "@" || str.indexOf('@', 0) == -1 ||
	        str.indexOf('.', 0) == -1 || str.lastIndexOf("@") == str.length - 1 ||
	        str.lastIndexOf(".") == str.length - 1 ||
	        str.indexOf('@.') > -1){
	        return false;
		} else {
	        return true;
		}
	}
	//验证长度
	function isLength(m,thisValue){
		var bc = m.substring(9,m.length);
		var cc =parseInt(bc);  
		//if(thisValue.length>=cc){
		//	return true;
		//}
		 var len = 0;  
		  for (var i=0; i<thisValue.length; i++) {  
		    if (thisValue.charCodeAt(i)>127 || thisValue.charCodeAt(i)==94) {  
		       len += 2;  
		     } else {  
		       len ++;  
		     }  
		   }   
		 if(len>cc){
			 return true;
		 } 
		return false;
	};
	//验证长度
	function isMinLength(m,thisValue){
		var bc = m.substring(12,m.length);
		var cc =parseInt(bc);   
		//if(thisValue.length<=cc){  
		//	return 1;
		//}
		 var len = 0;  
		  for (var i=0; i<thisValue.length; i++) {  
		    if (thisValue.charCodeAt(i)>127 || thisValue.charCodeAt(i)==94) {  
		       len += 2;  
		     } else {  
		       len ++;  
		     }  
		   }   
		 if(len<cc){
			 return 1;
		 } 
		return 0;
	}

	//校验手机号码
	function isMobil(s) {
	    //var patrn = /^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/;
	    var patrn = /^(13[0-9]{9})|(14[0-9])|(18[0-9])|(15[0-9][0-9]{8})$/;
	    if (!patrn.exec(s)) return false
	    return true
	}
	//组件初始化
	$.fn.hzValidate = function(method, options) {
		if (typeof (method) == "string") {
			return $.fn.hzValidate.methods[method](this, options);
		} else {
			var opts = $.extend({},$.fn.hzValidate.defaults,method);
			init(this,opts);
		}
	};  
	//组件默认参数
	$.fn.hzValidate.defaults = {

	};
	//组件方法注册
	$.fn.hzValidate.methods = {
		init : function(_this, opts) { 
			return init(_this,opts);
		}
	};
})(jQuery);
;