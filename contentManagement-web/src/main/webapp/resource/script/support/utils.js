if(typeof NM == 'undefined')
	NM = {};
(function($){
	NM = $.extend(NM, {
	    namespace: function(){
	        var a = arguments, o = null, i, j, d;
	        for (i = 0; i < a.length; i++) {
	            d = a[i].split('.');
	            o = NM;
	            for (j = (d[0] == 'NM') ? 1 : 0; j < d.length; j++) {
	                o[d[j]] = o[d[j]] || {};
	                o = o[d[j]];
	            }
	        }
	        return o;
	    }
	})
})(jQuery);

/**
 *	通过iframe加载指定的页面，并用dialog弹出加载的页面
 *  @param container 存放加载页面的对象
 *  @param url 要加载的页面地址
 *  @param dialogParam  要弹出的dialog的参数，参数参考jquery-ui 的dialog
 */
NM.namespace('tool');
NM.tool["load"]=(function(){
    var iframeName = "loadIframe";
    //定义一个静态变量，防止一个页面出现iframe重名的现象。
    var iframeIndex = 0;
    return function(container, url, dialogParam){
		var	iframe = $('<iframe frameborder="0" height="100%" width="100%" src="' + url + '" name="' + (iframeName + iframeIndex) + '">').appendTo(container).hide();
       	if($.browser.msie )
		{
			iframe[0].attachEvent("onload", loaded );  
		}
	    else{
			iframe[0].onload = loaded;
		}
		var loading = $('<div class="mymodalLoading" style="position: absolute; top: '+(dialogParam.height/2-50)+'px; left: '+dialogParam.width/2+'px; display: block;"></div>');
	    $(container).append(iframe).append(loading).dialog(dialogParam);
        iframeIndex++;
		function loaded(){
			loading.remove();
			$(iframe).show();
		}
    }
})();

/**
 * 阻止默认的事件
 * @param {Object} value
 */
NM.tool["pdEvent"] = function(e){
     //阻止默认浏览器动作(W3C)
       if ( e && e.preventDefault )
             e.preventDefault();
        //IE中阻止函数器默认动作的方式
       else
             window.event.returnValue = false;
}


NM.tool["LoadManager"] = function(options){
    var defaultOptions = {
		"id":"",
		url:"",
		callBack:function(){},
		"formId":"",
		data:""
	}
	for(var o in options){
		defaultOptions[o] = options[o];
	}
	var id = defaultOptions["id"];
	$("#"+id).load(defaultOptions["url"],defaultOptions["data"],loadedFn);
	 function loadedFn(){
	 	defaultOptions["callBack"]();
		if(defaultOptions["formId"]!=undefined)
		{
		 	var form = $("#"+defaultOptions["formId"],"#"+id)[0];
			form.submit = function(){
					var data = {};
					$(":input",$(form)).each(function(){
						data[this.name] = this.value;
					});
					$("#"+id).load(defaultOptions["url"],data,loadedFn);
			}
		}
		
	 }	
}

NM.tool["folder"]=function(e,obj){
        if ('收起' == $(obj).text()) {
            $(obj).parent().parent().next().slideUp(500);
            $(obj).text('展开');
        }
        else {
            $(obj).parent().parent().next().slideDown(500);
            $(obj).text('收起');
        }
		NM.tool["pdEvent"](e);
};
NM.tool["isNumber"] = function(str){
    return /^\d+$/.test(str);
}
NM.tool["UTF8Length"] = function(value){
    var length = value.length;
    for (var i = 0; i < value.length; i++) {
        if (value.charCodeAt(i) > 127) {
            length++;
            length++;
        }
    }
	return length;
}

NM.tool["backUtils"] = function(step){
    if (step != undefined) {
        history.go(step);
    }
    else {
        history.back();
    }
}
NM.tool["getWinHeight"]= function(){
		var a, b;
		if ($.browser.msie && $.browser.version < 7) {
			a = Math.max(
					document.documentElement.scrollHeight,
					document.body.scrollHeight);
			b = Math.max(
					document.documentElement.offsetHeight,
					document.body.offsetHeight);
			return a < b ? $(window).height() : a;
		} else
		{
			return $(document).height();
		}
}
NM.tool["getWinWidth"]=function(){
	var a, b;
	if ($.browser.msie && $.browser.version < 7) {
		a = Math.max(
				document.documentElement.scrollWidth,
				document.body.scrollWidth);
		b = Math.max(
				document.documentElement.offsetWidth,
				document.body.offsetWidth);
		return a < b ? $(window).width(): a;
	} else
	{
		return $(document).width();
	}
}

$(function(){
	NM.tool["myModal"] = (function(){
		var isShow = false;
		var selectSet ;
		var modal = $("<div style='z-index:2001'></div>")
				.addClass("ui-widget-overlay").appendTo(
				document.body).css({
						width:NM.tool["getWinWidth"](),
						height:NM.tool["getWinHeight"]()
					
				});
		
		var loading = $("<div align='center' style='z-index:2004;color:green;'><div  align='center'></div><div align='center'  class='mymodalLoading'></div></div>")
		.css({position: 'absolute', top:NM.tool["getWinHeight"]()/2 , left: NM.tool["getWinWidth"]()/2})
		.appendTo(document.body);
		return {
			show:function(html){
				$(modal).show();
				$(loading).show();
				$(loading).find("div").eq(0).html(html!=undefined?html:"");
				if($.browser.msie && $.browser.version < 7){
					selectSet = $("select:visible");
					$(selectSet).each(function(){
							$(this).hide();
						});
				}
				isShow = true;
			},
			hide:function(){
				if($.browser.msie && $.browser.version < 7){
					if(selectSet)
					{
						$(selectSet).each(function(){
							$(this).show();
						});
					}
				}
				$(modal).hide();
				$(loading).hide();
				isShow = false;
				},
			toggle:function(html){
					if(isShow)
					{
						NM.tool["myModal"].hide();
					}else{
						NM.tool["myModal"].show(html);
					}
				},
			isShow:function(){
					return isShow;
				}
			}
			;
	})();
	NM.tool["myModal"].hide();
});



//NM.tool["load"] = function(options){
//	var self = this;
//	//obj,url,data,callBack
//	var defaultOptions = {
//			//will be invoke on page loader
//			callBack : function(targetObject){},
//			//container will be load the new page
//			target : undefined,
//			//load url
//			url :null,
//			//param
//			data:null,
//			modal:false,
//			//detemine form if pass id will override submit method .
//			//如果传递过来表单,可以是id也可以是对象，那么将会改写表单的submit提交为ajax提交来实现翻页的效果
//			formId : false
//	};
//	
//	var $loadContainer;
//	if(options)
//	{
//		for(var o in options)
//		{
//			defaultOptions[o] =options[o]; 
//		}
//	} 
//	$loadContainer = getTarget(defaultOptions.target);
//	loadTemplate($loadContainer,defaultOptions.url,defaultOptions.data,null,defaultOptions.callBack,defaultOptions.formId,defaultOptions.modal);
//	return $loadContainer;
//	function copyProperty(source,target,overwritable){
//		if(overwritable==null)
//		{
//			overwritable = true;
//		}
//		for(var o in source){
//			if(overwritable)
//			{
//				target[o] = source[o];
//			}
//			else{
//				if(source[o]&&(!target[o]))
//				{
//					target[o] = source[o];
//				}
//			}	
//		}
//	}
//	function loadTemplate(container,url,data,param,callback,formId,modal){
//		if(param==null)
//		{
//			param = {};
//			copyProperty(data,param);
//		}
//		if(modal)
//		NM.tool["myModal"].toggle();
//		$(container).load(url,param,function(){
//			if(modal)
//			NM.tool["myModal"].toggle();
//			if(formId)
//			{
//				callback($(container));
//				var form =getTarget(formId)[0];
//				if(form&&form.submit)
//				{
//					form.submit = function(){
//						var formParam = getParam(form); 
//						copyProperty(data,formParam,false);
//						loadTemplate(container,url,data,formParam,callback,formId,modal);
//					};
//				}
//			}
//		});
//	}
//	function getParam(form){
//		var para = {};
//		$(form).find(":input").each(function(){
//			if($(this).val())
//				para[$(this).attr("name")]  =encodeURIComponent($(this).val());  //$(this).val();
//		});
//		return para; 
//	}
//	function getTarget(obj){
//		if(getType(obj) == "string")
//		{
//			return  $("#"+obj);
//		}
//		else if(getType(obj) == "object")
//		{
//			return $(obj);
//		}
//		else
//		{
//			return $("<div></div>");
//		}
//	} 
//	function getType(object) {
//        var _t;
//        return ((_t = typeof(object)) == "object" ? object == null && "null" || Object.prototype.toString.call(object).slice(8, -1) : _t).toLowerCase();
//    };
//}


String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
}
/**
 *以下方法都是为了listview做的
 */

/**
 * 为listpage专门先的页面跳转的插件
 * 会讲form得参数作为一个特殊的参数传递给后台保留
 * 这样就可以实现goBack函数
 * @param {Object} url
 * @param {Object} formId
 */
NM.tool["listGo"] = function(url, formId) {
	var form = document.getElementById(formId);
	document.getElementById(formId).submit = function() {
		var params = {};
		$(":input", form).each(function() {
			params[this.name] = this.value;
		})
		var queryStr = jQuery.param(params);
		if (url.indexOf("?") != -1) {
			url = url + "&pageHistoryForLocal=" + encodeURIComponent(queryStr);
		} else {
			url = url + "?pageHistoryForLocal=" + encodeURIComponent(queryStr);
		}
		location.href = url;
		return false;
	}
	form.refresh();
}
NM.tool["listBack"] = function() {
	location.href = web_context + "/list?" + pageHistoryForLocal;
}

NM.tool["LoadManager"] = function(options) {
	var defaultOptions = {
		"id" : "",
		"modal" : true,
		url : "",
		callBack : function() {
		},
		"formId" : "",
		data : ""
	}
	
	for ( var o in options) {
		defaultOptions[o] = options[o];
	}
	var id = defaultOptions["id"];
	if (defaultOptions["modal"]) {
		NM.tool["myModal"].toggle();
	}
	$("#" + id).load(defaultOptions["url"], defaultOptions["data"], loadedFn);
	
	function loadedFn() {
		if (defaultOptions["modal"]) {
			NM.tool["myModal"].toggle();
		}
		if (defaultOptions["formId"] != undefined) {
			var form = $("#" + defaultOptions["formId"], "#" + id)[0];
			form.submit = function() {
				var data = {};
				$(":input", $(form)).each(function() {
					data[this.name] = this.value;
				});
				if (defaultOptions["modal"]) {
					NM.tool["myModal"].toggle();
				}
				$("#" + id).load(defaultOptions["url"], data, loadedFn);
			}
		}
		
		if(defaultOptions["callBack"]!=undefined)
		{
			defaultOptions["callBack"]();
			//防止第2次加载的时候再次回调
			defaultOptions["callBack"] = undefined;
		}
	}
}

/*由于iframe的原因，dialog显示的位置总是存在问题，所以重写一下dialog方法*/
$(function() {
	(function() {
		var dialogCache = [];//dialog缓存
		//标识方法是否重写，防止多次加载引起的冲突
		var flag = false;
		if (!flag) {
			flag = true;
			var oldDialog = $.fn.dialog
			$.fn.dialog = function() {
				var flag = false;
				for(var i = 0 ;i<dialogCache.length;i++)
				{
						if(this ===  dialogCache[i]){
							flag = true;		
						}
				}
				if(!flag)
				{
						
				}
				else{
					
				}
				var dialog = oldDialog.apply(this, arguments);
				
				
				//获取页面可见高度
				var height = getBodyHeightInFrame();
				//获取滚动条的高度，由于被嵌入到iframe中，所以这个写法无法在非iframe中可能无法正常显示
				var scrollHeight = getScrollInFrame();
				var oheight = $(this).parent(".ui-dialog").height();
				//dialog要显示的top  90是IFRAME的高度
				var currHeight = height / 2 - oheight / 2 + scrollHeight - 90;
				if (currHeight < 0) {
					currHeight = 50;
				}
				$(this).parent(".ui-dialog").css("top", currHeight);
				return dialog;
			}
		}
	})();
})

/*由于在iframe中所以高度重新写了一下*/
function getBodyHeightInFrame() {
	var winHeight = 0;
	
	if (window.innerHeight) {
		winHeight = parent.window.innerHeight;
	}
	else if((parent.document.body)&&parent.document.documentElement){
		winHeight = Math.min(parent.document.body.clientHeight,parent.document.documentElement.clientHeight);
	}
	else if ((parent.document.body) && (parent.document.body.clientHeight)) {
		winHeight = parent.document.body.clientHeight;
	} else if (parent.document.documentElement
			&& parent.document.documentElement.clientHeight) {
		winHeight = parent.document.documentElement.clientHeight;
	}
	return winHeight;
}
function getScrollInFrame() {
	return Math.max(parent.document.documentElement.scrollTop,
			parent.document.body.scrollTop);
}
/*js 原型的修改*/
/**
 * string endWith方法
 * @param {Object} str
 * @memberOf {TypeName} 
 * @return {TypeName} 
 */
String.prototype.endWith=function(str){
   if(str==null||str==""||this.length==0||str.length>this.length)
    return false;
   if(this.substring(this.length-str.length)==str)
   return true;
   else
   return false;
   return true;
}
/**
 * string startWith方法
 * @param {Object} str
 * @memberOf {TypeName} 
 * @return {TypeName} 
 */
String.prototype.startWith=function(str){
   if(str==null||str==""||this.length==0||str.length>this.length)
    return false;
   if(this.substr(0,str.length)==str)
    return true;
   else
    return false;
   return true;
}

Array.prototype.remove = function (obj) {
	function RemoveArray(array,attachId)
	{
	    for(var i=0,n=0;i<array.length;i++)
	    {
	        if(array[i]!=attachId)
	        {
	            array[n++]=array[i]
	        }
	    }
	    array.length -= 1;
	}
    return RemoveArray(this,obj);
};



jQuery.extend( {
    /**
     * @see 将javascript数据类型转换为json字符串
     * @param 待转换对象,支持object,array,string,function,number,boolean,regexp
     * @return 返回json字符串
     */
    toJSON : function(object) {
        var type = typeof object;
        if ('object' == type) {
            if (Array == object.constructor)
                type = 'array';
            else if (RegExp == object.constructor)
                type = 'regexp';
            else
                type = 'object';
        }
        switch (type) {
        case 'undefined':
        case 'unknown':
            return;
            break;
        case 'function':
        case 'boolean':
        case 'regexp':
            return object.toString();
            break;
        case 'number':
            return isFinite(object) ? object.toString() : 'null';
            break;
        case 'string':
            return '"' + object.replace(/(\\|\")/g, "\\$1").replace(
                    /\n|\r|\t/g,
                    function() {
                        var a = arguments[0];
                        return (a == '\n') ? '\\n' : (a == '\r') ? '\\r'
                                : (a == '\t') ? '\\t' : ""
                    }) + '"';
            break;
        case 'object':
            if (object === null)
                return 'null';
            var results = [];
            for ( var property in object) {
                var value = jQuery.toJSON(object[property]);
                if (value !== undefined)
                    results.push(jQuery.toJSON(property) + ':' + value);
            }
            return '{' + results.join(',') + '}';
            break;
        case 'array':
            var results = [];
            for ( var i = 0; i < object.length; i++) {
                var value = jQuery.toJSON(object[i]);
                if (value !== undefined)
                    results.push(value);
            }
            return '[' + results.join(',') + ']';
            break;
        }
    }
});

