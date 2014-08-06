$(function(){
	//中文字符按2个字节的长度来验证
	$.validator.addMethod("UTF8Length", function(value, element, param) {  
 				   	 var length = value.length;       
     					for(var i = 0; i < value.length; i++){       
         				if(value.charCodeAt(i) > 127){       
         					length++;   
							length++;      
         				}       
     				}  
     				return this.optional(element) || ( length >= param[0] && length <= param[1] );       
 	});
	//增加时间比较验证
	$.validator.methods.compareDate = function(value, element, param) {
		    var startDate = jQuery(param[0]).val();
            var date1 = new Date(Date.parse(startDate.replace(new RegExp("-","gm"), "/")));
            var date2 = new Date(Date.parse(value.replace(new RegExp("-","gm"), "/")));
            return date1<date2 ;
     };
	
	//时间格式验证 yyyy-mm-dd hh-mm
	$.validator.methods.dateF = function(value, element, param) {
		    var rex = /^\d\d\d\d-\d\d-\d\d$/
            return this.optional(element) ||rex.test(value);
     };
	 
	//增加FLOAT类型的检测
	$.validator.addMethod("float", function(value, element, param) {
 				   	var rex1 = new RegExp("^[-]?[\\d]{1,"+param[0]+"}$");
					var rex2=new RegExp("^[-]?[\\d]+\\.[\\d]{1,"+param[1]+"}$");
     				return this.optional(element) ||rex1.test(value)||rex2.test(value);       
 	});
	
	//增加小数或整数的检测
	$.validator.addMethod("decimal", function(value, element, param) {  
 				   	var rex1 = new RegExp("^[\\d]+$");
					var rex2=new RegExp("^[\\d]+\\.[\\d]+$");
     				return this.optional(element) ||rex1.test(value)||rex2.test(value);       
 	});
	
	$.validator.addMethod("mobile", function(value, element, param) {  
 			if(value=="")return true;
 			var phones = value.split("|");
 			var test = true;
 			var isMobile=/^(?:13\d|15\d|18\d|14\d)\d{5}(\d{3}|\*{3})$/;  
 			for(var i=0;i<phones.length;i++)
 			{
 					if(phones[i]=="")
 					{
 						test = true;
 					}
 					else{
 						test = isMobile.test(phones[i]);
	 					if(!test){
	 						return test;
	 					}
 					}
 			}
	        return  this.optional(element) || test;
 	});
	/**
	 * 是否是座机号，如果多个的话以分号隔开
	 * @param {Object} value
	 * @param {Object} element
	 * @param {Object} param
	 * @return {TypeName} 
	 */
	$.validator.addMethod("fixphone", function(value, element, param) {  
			if(value=="")return true;
 			var phones = value.split(";");
 			var test = true;
 			var mobileTest = false;
 			var isPhone=/^(800\d{7}){1}$|^(400\d{7}){1}$|^(\d){11,12}([\u4e00-\u9fa5]{1}(\d{3,5}))?$/;
 			var isMobile=/^(?:13\d|15\d|18\d)\d{5}(\d{3}|\*{3})$/;  
 			for(var i=0;i<phones.length;i++)
 			{
 					if(phones[i]=="")
 					{
 						test = true;
 					}
 					else{
 						test = isPhone.test(phones[i]);
 						mobileTest = isMobile.test(phones[i]);
	 					if(!test){
	 						return test;
	 					}
	 					if(mobileTest)
	 					{
	 						return false;
	 					}
 					}
 					
 			}
	        return  this.optional(element) || test;
 	});
	
	/**
	 * 座机号与手机号满足其一即可，如果多个的话以分号隔开
	 * @param {Object} value
	 * @param {Object} element
	 * @param {Object} param
	 * @return {TypeName} 
	 */
	$.validator.addMethod("mixphone", function(value, element, param) {
			if(value == "")
				return true;
 			var phones = value.split("|");
 			var test = true;
 			var fixPhoneReg = /^(800\d{7}){1}$|^(400\d{7}){1}$|^(\d){11,12}([\u4e00-\u9fa5]{1}(\d{3,5}))?$/;
 			var mobileReg =/^(?:13\d|15\d|18\d)\d{5}(\d{3}|\*{3})$/;
 			for(var i=0 ; i < phones.length; i++) {
				if(phones[i] == "") {
					test = true;
				}else{
					var isFixPhone = fixPhoneReg.test(phones[i]);
					var isMobile = mobileReg.test(phones[i]);
					if (isFixPhone || isMobile) {
						test = true;
					}else {
						test = false;
						break;
					}
				}
 			}
	        return this.optional(element) ||test;
 	});
	
	/**
	 * 座机号校验，格式01084481818转2707
	 * @param {Object} value
	 * @param {Object} element
	 * @param {Object} param
	 * @return {TypeName} 
	 */
	$.validator.addMethod("phone", function(value, element, param) {
			if(value == "")
				return false;
 			var phoneReg =/^(800\d{7}){1}$|^(400\d{7}){1}$|^(\d){11,12}([\u4e00-\u9fa5]{1}(\d{3,5}))?$/;
 			var mobileReg =/^(?:13\d|15\d|18\d)\d{5}(\d{3}|\*{3})$/;
 			var test = phoneReg.test(value);
 			test = test && !mobileReg.test(value);
	        return this.optional(element) ||test;
 	});
	
	/**
	 * 座机号，如果多个的话以分号隔开
	 * @param {Object} value
	 * @param {Object} element
	 * @param {Object} param
	 * @return {TypeName} 
	 */
	$.validator.addMethod("fixphoneNull", function(value, element, param) {
			if(value == "")
				return true;
 			var phones = value.split("|");
 			var test = true;
 			var fixPhoneReg = /^(800\d{7}){1}$|^(400\d{7}){1}$|^(\d){11,12}([\u4e00-\u9fa5]{1}(\d{3,5}))?$/;
 			var mobileReg =/^(?:13\d|15\d|18\d)\d{5}(\d{3}|\*{3})$/;
 			for(var i=0 ; i < phones.length; i++) {
				if(phones[i] == "") {
					test = true;
				}else{
					var isFixPhone = fixPhoneReg.test(phones[i]);
					var isMobile = mobileReg.test(value);
					if (isFixPhone && !isMobile) {
						test = true;
					}else {
						test = false;
						break;
					}
				}
 			}
	        return this.optional(element) ||test;
 	});
	
	$.validator.addMethod("mobileOptional", function(value, element, param) {  
			if(value=="")return true;	
		var isMobile=/^(?:13\d|15\d|18\d)\d{5}(\d{3}|\*{3})$/;  
         return isMobile.test(value);  
	});
	$.validator.addMethod("fixphoneOptional", function(value, element, param) {  
		if(value=="")return true;
			var isPhone=/^((0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;  
         return isPhone.test(value);  
	});
	$.validator.addMethod("mobileOrFixphoneOptional", function(value, element, param) {  
		if(value=="")return true;
			var isPhone=/^((0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
			var isMobile=/^(?:13\d|15\d|18\d)\d{5}(\d{3}|\*{3})$/;
			var isOK = false;
			if (isPhone.test(value) || isMobile.test(value)) {
				isOK = true;
			}
         return isOK;  
	});
	$.validator.addMethod("emailOptional", function(value, element, param) {  
		if(value=="")return true;
			var isEmail=/^.+\@(\[?)[a-zA-Z0-9\-\.]+\.([a-zA-Z]{2,3}|[0-9]{1,3})(\]?)$/;
			var isOK = false;
			if (isEmail.test(value)) {
				isOK = true;
			}
         return isOK;  
	});
	/**
	 * 是否是日期，可空
	 * @param {Object} value
	 * @param {Object} element
	 * @param {Object} param
	 * @return {TypeName} 
	 */
	$.validator.addMethod("dateOptional", function(value, element, param) {
		if(value == "")
			return true;
		
		var r = value.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/); 
		if(r == null)
			return false; 
		var d = new Date(r[1], r[3]-1, r[4]); 
		return (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]);
		
	});
	
	$.validator.addMethod("singleRequired", function(value, element, param) {  
		 var value2 = $(param[0]).val();
		 if(value2==""&&value=="")
			 return false
         return true;  
	});
	        
	//自定义提示信息
	jQuery.extend(jQuery.validator.messages, {
			UTF8Length: jQuery.validator.format("长度不能大于{1}"),
			"float":jQuery.validator.format("不能大于{0}位整数和{1}位小数"),
			"compareDate":jQuery.validator.format("{1}不能早于{2}"),
			"dateF":jQuery.validator.format("请输入正确的时间格式"),
			"decimal":jQuery.validator.format("请输入整数或小数"),
			"mobile":jQuery.validator.format("请输入正确的手机号,多个手机号用|号隔开"),
			"fixphone":jQuery.validator.format("请输入正确的座机号,多个座机号用|号隔开"),
			"mixphone":jQuery.validator.format("请输入正确的电话号"),
			"singleRequired":jQuery.validator.format("{1}"),
			"emailOptional":jQuery.validator.format("请输入正确的email"),
			"dateOptional":jQuery.validator.format("请输入正确的日期"),
			"phone":jQuery.validator.format("请输入正确的座机号,多个座机号用|号隔开"),
			"fixphoneNull":jQuery.validator.format("请输入正确的座机号,多个座机号用|号隔开")
	});
	
})
