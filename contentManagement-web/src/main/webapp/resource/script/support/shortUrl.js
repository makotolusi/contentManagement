/**
 * 主列表分页标志符
 */
var pageSign = false;
/**
 * 明细表分页标志符
 */
var detail_pageSign = false;

// 页面加载
$(document).ready(function() {
	$("#tabs").tabs();
	searchApp(1);
	
	loadCreateNumTools();
	
	loadDatetimepicker();
});

/**
 * 查询列表
 */
function searchList(){
	searchApp($("#curTab").val());
}
/**
 * 切换栏目
 * 
 * @param status 栏目标志符
 */
function searchApp(status) {
	$("#tabs_" + status).html('<img src="'+context+'/resource/image/ajax-loader.gif" />');
    $("#curTab").val(status);
    
    if (status == '1') {
    	resetQueryShortUrl();
    	listShortUrl(status);
	} else if (status == '0') {
		resetQueryShortUrlVisite();
		listUserVisited(status);
	}
}

/**
 * 获取并展示短网址列表
 * 
 * @param status 栏目标志符 1：短网址列表，0：用户访问列表
 */
function listShortUrl(status) {
	// query params
    var obj = {};
    obj.name = $("#s_name").val();
    obj.shortKey = $("#s_shortKey").val();
    obj.status = $("#s_status").val();
    obj.dateFrom = $("#s_dateFrom").val();
    obj.dateTo = $("#s_dateTo").val();
    obj.curPage = $("#curPage").val();
    
    $("#curPage").val(1);
    
    // ajax submit
    $.ajax({
    	type : "POST",
        url : context + "/web/support/shortUrl",
        datatype : "json",
        contentType : "application/json",
        data : $.toJSON(obj),
        success : function(data) {
        	if(data != null) {
        		if(data.message == 'SUCCESS') {
		            if(data.page != null && data.page.content != null && data.page.content.length > 0) {
		            	// table head
		            	$("#tabs_" + status).html('<table id="shortUrlTable" width="100%" border="0" cellspacing="0" cellpadding="0" style="text-align:center;"><thead></thead><tbody></tbody></table>');
		            	$("#tabs_" + status + " table thead").append('<th width="5%" background="' + context + '/resource/image/ui/content-bg.gif"><span>编号</span></th>');
		            	$("#tabs_" + status + " table thead").append('<th width="20%" background="' + context + '/resource/image/ui/content-bg.gif"><span>短网址名称</span></th>');
		            	$("#tabs_" + status + " table thead").append('<th width="25%" background="' + context + '/resource/image/ui/content-bg.gif"><span>短网址</span></th>');
		            	$("#tabs_" + status + " table thead").append('<th width="10%" background="' + context + '/resource/image/ui/content-bg.gif"><span>PV</span></th>');
		            	$("#tabs_" + status + " table thead").append('<th width="10%" background="' + context + '/resource/image/ui/content-bg.gif"><span>UV</span></th>');
		            	$("#tabs_" + status + " table thead").append('<th width="10%" background="' + context + '/resource/image/ui/content-bg.gif"><span>状态</span></th>');
		            	$("#tabs_" + status + " table thead").append('<th width="20%" background="' + context + '/resource/image/ui/content-bg.gif"><span>操作</span></th>');
		              
		            	// data list
		            	$.each(data.page.content, function(i) {
		            		if(i % 2 == 0) {
		            			$("#tabs_" + status + " table").append('<tr bgcolor="#f2f2f2"></tr>');
			                }else {
			                	$("#tabs_" + status + " table").append('<tr></tr>');
			                }
		            		
			                $("#tabs_" + status + " table tr:last").append('<td id="id_' + this.id + '" class="left_txt2">' + this.id + '</td>');
			                $("#tabs_" + status + " table tr:last").append('<td id="name_' + this.id + '" class="left_txt2">' + this.name + '</td>');
			                $("#tabs_" + status + " table tr:last").append('<td id="shortUrl_' + this.id + '" class="left_txt2">' + this.shortUrl + '</td>');
			                $("#tabs_" + status + " table tr:last").append('<input type="hidden" id="originalUrl_' + this.id + '" value="' + this.originalUrl + '" />');
			                
			                $("#tabs_" + status + " table tr:last").append('<td class="left_txt2">' + this.pageView + '</td>');
			                $("#tabs_" + status + " table tr:last").append('<td class="left_txt2">' + this.uniqueVisitor + '</td>');
			                
			                if(this.status == 1) {
			                	$("#tabs_" + status + " table tr:last").append('<td id="status_' + this.id + '" class="left_txt2">已启用 </td>');
			                } else if (this.status == 0) {
			                	$("#tabs_" + status + " table tr:last").append('<td id="status_' + this.id + '" class="txt_red">已停用</td>');
			                }
			                
			                $("#tabs_" + status + " table tr:last").append('<td class="left_txt2"></td>');
			                
			                // operation
			                $("#tabs_" + status + " table tr:last td:last").append('<a style="color:blue;" href="javascript:editShortUrl(' + this.id + ');">修改</a>&nbsp;&nbsp;&nbsp;');
			                $("#tabs_" + status + " table tr:last td:last").append('<a style="color:blue;" href="javascript:openDetail(\'' + this.shortKey + '\');">明细</a>&nbsp;&nbsp;&nbsp;');
			                
			                if(1 == this.status) {
			                	$("#tabs_" + status + " table tr:last td:last").append('<a style="color:blue;" href="javascript:updateStatus(' + this.id + ', \'0\');">停用</a>&nbsp;&nbsp;&nbsp;');
			                } else if (0 == this.status) {
			                	$("#tabs_" + status + " table tr:last td:last").append('<a style="color:blue;" href="javascript:updateStatus(' + this.id + ', \'1\');">启用</a>&nbsp;&nbsp;&nbsp;');
			                }
			            });
		            
		            	// paging
		            	pageSign = false;
		            	$("#pagination").pagination(data.page.rowCount, {
		            		callback : pageselectCallback,
		            		items_per_page : data.page.pageSize,
		            		num_display_entries : 5,
		            		num_edge_entries : 1,
		            		current_page : data.page.curPage - 1,
		            		prev_text : "上一页",
		            		next_text : "下一页"
		            	});
		            	pageSign = true;
		            	
		            	// 启用表头排序功能
		            	$("#shortUrlTable").tablesorter();
		            }
		            else {
		            	$("#pagination").empty();
		            	$("#tabs_" + status).html("<p>无符合条件数据记录</p>");
		            }
        		}
        		else {
        			$("#tabs_" + status).html("<p>获取短网址列表出错-" + data.message + "</p>");
        		}
        	}
        }
    });
}

/**
 * 获取并展示用户访问列表
 * 
 * @param status 栏目标志符
 */
function listUserVisited(status) {
	// query params
    var obj = {};
    obj.userIp = $("#s_userIp").val();
    obj.userAgent = $("#s_userAgent").val();
    obj.dateFrom = $("#s_dateFrom").val();
    obj.dateTo = $("#s_dateTo").val();
    obj.curPage = $("#curPage").val();
    
    $("#curPage").val(1);
    
    // ajax submit
    $.ajax({
    	type : "POST",
        url : context + "/web/support/shortUrl/shortUrlVisite",
        datatype : "json",
        contentType : "application/json",
        data : $.toJSON(obj),
        success : function(data) {
        	if(data != null) {
        		if(data.message == 'SUCCESS') {
		            if(data.page != null && data.page.content != null && data.page.content.length > 0) {
		            	// table head
		            	$("#tabs_" + status).html('<table width="100%" border="0" cellspacing="0" cellpadding="0" style="text-align:center;"><tr style="font-size:13px;"></tr></table>');
		            	$("#tabs_" + status + " table tr").append('<th width="5%" background="' + context + '/resource/image/ui/content-bg.gif"><span>编号</span></th>');
		            	$("#tabs_" + status + " table tr").append('<th width="10%" background="' + context + '/resource/image/ui/content-bg.gif"><span>用户IP</span></th>');
		            	$("#tabs_" + status + " table tr").append('<th width="20%" background="' + context + '/resource/image/ui/content-bg.gif"><span>访问网址</span></th>');
		            	$("#tabs_" + status + " table tr").append('<th width="10%" background="' + context + '/resource/image/ui/content-bg.gif"><span>访问时间</span></th>');
		            	$("#tabs_" + status + " table tr").append('<th width="55%" background="' + context + '/resource/image/ui/content-bg.gif"><span>用户信息</span></th>');
		              
		            	// data list
		            	$.each(data.page.content, function(i) {
		            		if(i % 2 == 0) {
		            			$("#tabs_" + status + " table").append('<tr bgcolor="#f2f2f2"></tr>');
			                }else {
			                	$("#tabs_" + status + " table").append('<tr></tr>');
			                }
		            		
			                $("#tabs_" + status + " table tr:last").append('<td class="left_txt2">' + (i+1) + '</td>');
			                $("#tabs_" + status + " table tr:last").append('<td class="left_txt2">' + this.userIp + '</td>');
			                $("#tabs_" + status + " table tr:last").append('<td class="left_txt2">' + this.shortUrl + '</td>');
			                $("#tabs_" + status + " table tr:last").append('<td class="left_txt2">' + this.visiteTime + '</td>');
			                $("#tabs_" + status + " table tr:last").append('<td class="left_txt2">' + this.userAgent + '</td>');
			            });
		            
		            	// paging
		            	pageSign = false;
		            	$("#pagination").pagination(data.page.rowCount, {
		            		callback : pageselectCallback,
		            		items_per_page : data.page.pageSize,
		            		num_display_entries : 5,
		            		num_edge_entries : 1,
		            		current_page : data.page.curPage - 1,
		            		prev_text : "上一页",
		            		next_text : "下一页"
		            	});
		            	pageSign = true;
		            }
		            else {
		            	$("#pagination").empty();
		            	$("#tabs_" + status).html("<p>无符合条件数据记录</p>");
		            }
        		}
        		else {
        			$("#tabs_" + status).html("<p>获取短网址列表出错-" + data.message + "</p>");
        		}
        	}
        }
    });
}

/**
 * 打开新增和修改对话框
 * 
 * @param title 对话框标题
 * @param buttons 对话框按钮
 */
function openDialog(title, buttons){
	$("#wordDiv").dialog({
		title : title,
		width : 630,
		height : 300,
		focus : false,
		modal : true,
		draggable : false,
		autoOpen : false,
		resizable : false,
		close : function() {
			$("#name").val("");
			$("#originalUrl").val("");
			initCreateNum();
			$("#wordDiv").dialog('destroy');
		},
		open : function() {
		},
		buttons : buttons
	});
	$("#wordDiv").dialog("open");
}

/**
 * 新增短网址
 */
function createShortUrl(){
	var title = '新增短网址';
	
	var buttons = {};
	
	buttons["取消"] = function() {
		$(this).dialog("close");
	};
	
	buttons["保存"] = function() {
		// get data
		var name = commonUtils.trim($("#name").val());
		var originalUrl = commonUtils.trim($("#originalUrl").val());
		var createNum = commonUtils.trim($("#createNum").val());
		
		if(!checkForm(name, originalUrl))
			return false;
		
		// query params
		var obj = {};
		obj.name = name;
		obj.originalUrl = originalUrl;
		obj.createNum = createNum;
		
		// ajax submit
		$.ajax({
			type : "POST",
			url : context + "/web/support/shortUrl/create",
			datatype : "json",
			async : false,
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				if(data.message == 'SUCCESS') {
					alert("新增短网址成功");
                    $("#wordDiv").dialog("close");
                    searchApp(1);
                }else {
                	alert("新增短网址失败-" + data.message);
                }
			}
		});
	};
	
	openDialog(title, buttons);
}

/**
 * 编辑短网址
 * 
 * @param id 短网址编号
 */
function editShortUrl(id){
	// init dialog
	$("#name").val($("#name_" + id).text());
	$("#originalUrl").val($("#originalUrl_" + id).val());
	$("#createNumTd").hide();
	
	var title = '编辑短网址';
	
	var buttons = {};
	
	buttons["取消"] = function() {
		$(this).dialog("close");
	};
	
	buttons["保存修改"] = function() {
		// get data
		var name = commonUtils.trim($("#name").val());
		var originalUrl = commonUtils.trim($("#originalUrl").val());
		
		if(!checkForm(name, originalUrl))
			return false;
		
		// query params
		var obj = {};
		obj.id = id;
		obj.name = name;
		obj.originalUrl = originalUrl;
		
		// ajax submit
		$.ajax({
			type : "POST",
			url : context + "/web/support/shortUrl/update",
			datatype : "json",
			async : false,
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				if(data.message == 'SUCCESS') {
					alert("短网址修改成功");
                    $("#wordDiv").dialog("close");
                    searchApp(1);
                }else {
                	alert("短网址修改失败-" + data.message);
                }
			}
		});
	};
	
	openDialog(title, buttons);
}

/**
 * 更新短网址状态
 * 
 * @param id 短网址编号
 * @param status 状态（停用，启用）
 */
function updateStatus(id, status){
	var statusOpt;
	if("0" == status){
		statusOpt = "停用";
	}else if("1" == status){
		statusOpt = "启用";
	}
	
	var confirmStr = "确定 " + statusOpt + " 短网址“" + $("#name_"+id).text() + "”吗？";
	if(confirm(confirmStr)){
		// query params
		var obj = {};
		obj.id = id;
		obj.status = status;
		
		// ajax submit
		$.ajax({
			type : "POST",
			url : context + "/web/support/shortUrl/updateStatus",
			datatype : "json",
			async : false,
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				if(data.message == 'SUCCESS') {
					alert(statusOpt + "短网址成功");
	                $("#wordDiv").dialog("close");
	                searchApp(1);
	            }else {
	            	alert(statusOpt + "短网址失败-" + data.message);
	            }
			}
		});
	}
}

/**
 * 加载生成网址个数的工具组件
 */
function loadCreateNumTools(){
	// 滑动选择生成个数
	$( "#createNum_range" ).slider({
		range: "min",
		value: 1,
		min: 1,
		max: 10,
		slide: function( event, ui ) {
			$( "#createNum" ).val( ui.value );
		}
    });
	
	// 显示默认生成个数 1个
    $( "#createNum" ).val( $( "#createNum_range" ).slider( "value" ) );
}

/**
 * 加载日期选择框工具组件
 */
function loadDatetimepicker(){
	$("#s_dateFrom").datepicker({
        changeYear : true,
        regional : "zh-CN",
        dateFormat : "yy-mm-dd"
	});
	$("#s_dateTo").datepicker({
		changeYear : true,
		regional : "zh-CN",
		dateFormat : "yy-mm-dd"
	});
	$("#detail_s_dateFrom").datepicker({
        changeYear : true,
        regional : "zh-CN",
        dateFormat : "yy-mm-dd"
	});
	$("#detail_s_dateTo").datepicker({
		changeYear : true,
		regional : "zh-CN",
		dateFormat : "yy-mm-dd"
	});
}

/**
 * 初始化生成网址个数
 */
function initCreateNum(){
	// 显示默认生成个数 1个
    $( "#createNum" ).val(1);
    $( "#createNum_range" ).slider( "value", 1 );
    $("#createNumTd").show();
}

/**
 * 验证表单
 * 
 * @param name 短网址名称
 * @param originalUrl 原始地址
 */
function checkForm(name, originalUrl){
	// 验证短网址名称 name
	if(commonUtils.len(name) == 0) {
		alert("请输入短网址名称");
        $("#wordDiv #name").focus();
        return false;
    }
	if(commonUtils.len(name) > 27) {
		alert("短网址名称不能超过27个英文字符");
        $("#wordDiv #name").focus();
        return false;
    }
	
	// 验证原始地址 originalUrl
	if(commonUtils.len(originalUrl) == 0) {
		alert("请输入原始地址");
        $("#wordDiv #originalUrl").focus();
        return false;
    }
	if(commonUtils.len(originalUrl) > 200) {
		alert("原始地址不能超过200个英文字符");
        $("#wordDiv #originalUrl").focus();
        return false;
    }
	
	// 正则表达式验证网址 RegExp.test(originalUrl)
	var patten = new RegExp("^((https|http|ftp|rtsp|mms)?://)");
	if(!patten.test(originalUrl)){
		alert("请输入正确的网址，例如：http://www.17173.com");
		$("#wordDiv #originalUrl").focus();
        return false;
	}
	
	return true;
}

/**
 * 分页回调函数
 * 
 * @param index
 * @param jq
 * @returns
 */
function pageselectCallback(index, jq) {
	if(pageSign) {
		$("#curPage").val(parseInt(index) + 1);
		searchApp($("#curTab").val());
    }
}

/**
 * 打开短网址访问明细列表（带入主列表时间查询参数）
 * 
 * @param shortKey 短网址资源标识符
 */
function openDetail(shortKey){
	$("#detail_s_dateFrom").val($("#s_dateFrom").val());
	$("#detail_s_dateTo").val($("#s_dateTo").val());
	detail(shortKey);
}

/**
 * 短网址访问明细列表
 * 
 * @param shortKey 短网址资源标识符
 */
function detail(shortKey){
	$("#detail_tabs").html('<img src="'+context+'/resource/image/ajax-loader.gif" />');
	
	
	
	var title = '短网址访问明细';
	
	var buttons = {};
	
	buttons["关闭"] = function() {
		$(this).dialog("close");
	};
	
	// query params
    var obj = {};
    obj.shortKey = shortKey;
    obj.userIp = $("#detail_s_userIp").val();
    obj.userAgent = $("#detail_s_userAgent").val();
    obj.dateFrom = $("#detail_s_dateFrom").val();
    obj.dateTo = $("#detail_s_dateTo").val();
    obj.curPage = $("#detail_curPage").val();
    
    $("#detail_curPage").val(1);
    $("#detail_shortKey").val(shortKey);
    
    // ajax submit
    $.ajax({
    	type : "POST",
        url : context + "/web/support/shortUrl/shortUrlVisite",
        datatype : "json",
        contentType : "application/json",
        data : $.toJSON(obj),
        success : function(data) {
        	if(data != null) {
        		if(data.message == 'SUCCESS') {
		            if(data.page != null && data.page.content != null && data.page.content.length > 0) {
		            	// table head
		            	$("#detail_tabs").html('<table width="100%" border="0" cellspacing="0" cellpadding="0" style="text-align:center;"><tr style="font-size:13px;"></tr></table>');
		            	$("#detail_tabs table tr").append('<th width="5%" background="' + context + '/resource/image/ui/content-bg.gif"><span>编号</span></th>');
		            	$("#detail_tabs table tr").append('<th width="20%" background="' + context + '/resource/image/ui/content-bg.gif"><span>IP地址</span></th>');
		            	$("#detail_tabs table tr").append('<th width="20%" background="' + context + '/resource/image/ui/content-bg.gif"><span>访问时间</span></th>');
		            	$("#detail_tabs table tr").append('<th width="55%" background="' + context + '/resource/image/ui/content-bg.gif"><span>用户信息</span></th>');
		              
		            	// data list
		            	$.each(data.page.content, function(i) {
		            		if(i % 2 == 0) {
		            			$("#detail_tabs table").append('<tr bgcolor="#f2f2f2"></tr>');
			                }else {
			                	$("#detail_tabs table").append('<tr></tr>');
			                }
		            		
			                $("#detail_tabs table tr:last").append('<td class="left_txt2">' + (i+1) + '</td>');
			                $("#detail_tabs table tr:last").append('<td class="left_txt2">' + this.userIp + '</td>');
			                $("#detail_tabs table tr:last").append('<td class="left_txt2">' + this.visiteTime + '</td>');
			                $("#detail_tabs table tr:last").append('<td class="left_txt2">' + this.userAgent + '</td>');
			            });
		            
		            	// paging
		            	detail_pageSign = false;
		            	$("#detail_pagination").pagination(data.page.rowCount, {
		            		callback : detailPageselectCallback,
		            		items_per_page : data.page.pageSize,
		            		num_display_entries : 5,
		            		num_edge_entries : 1,
		            		current_page : data.page.curPage - 1,
		            		prev_text : "上一页",
		            		next_text : "下一页"
		            	});
		            	detail_pageSign = true;
		            }
		            else {
		            	$("#detail_pagination").empty();
		            	$("#detail_tabs").html("<p>无符合条件数据记录</p>");
		            }
        		}
        		else {
        			$("#detail_tabs").html("<p>获取短网址访问明细列表出错-" + data.message + "</p>");
        		}
        	}
        }
    });
    
    openDetailDialog(title, buttons);
}

/**
 * 打开短网址访问明细对话框
 * 
 * @param title 对话框标题
 * @param buttons 对话框按钮
 */
function openDetailDialog(title, buttons){
	$("#detailDiv").dialog({
		title : title,
		width : 1000,
		height : 700,
		focus : false,
		modal : true,
		draggable : false,
		autoOpen : false,
		resizable : false,
		close : function() {
			$("#detail_s_userIp").val("");
			$("#detail_s_userAgent").val("");
			$("#detail_s_dateFrom").val("");
			$("#detail_s_dateTo").val("");
			
			$("#detailDiv").dialog('destroy');
		},
		open : function() {
		},
		buttons : buttons
	});
	$("#detailDiv").dialog("open");
}

/**
 * 短网址访问明细对话框分页回调函数
 * 
 * @param index
 * @param jq
 * @returns
 */
function detailPageselectCallback(index, jq) {
	if(detail_pageSign) {
		$("#detail_curPage").val(parseInt(index) + 1);
		detail($("#detail_shortKey").val());
    }
}

/**
 * 短网址访问明细对话框查询功能
 */
function searchDetail(){
	detail($("#detail_shortKey").val());
}

/**
 * 重置短网址管理列表的查询框
 */
function resetQueryShortUrl(){
	$("#s_userIp").val("");
	$("#s_userAgent").val("");
	
	$("#s_userIp").attr("disabled","true");
	$("#s_userAgent").attr("disabled","true");
	
	$("#s_name").removeAttr("disabled");
	$("#s_shortKey").removeAttr("disabled");
	$("#s_status").removeAttr("disabled");
}

/**
 * 重置用户访问列表的查询框
 */
function resetQueryShortUrlVisite(){
	$("#s_name").val("");
	$("#s_shortKey").val("");
	$("#s_status").val("");
	
	$("#s_name").attr("disabled","true");
	$("#s_shortKey").attr("disabled","true");
	$("#s_status").attr("disabled","true");
	
	$("#s_userIp").removeAttr("disabled");
	$("#s_userAgent").removeAttr("disabled");
}