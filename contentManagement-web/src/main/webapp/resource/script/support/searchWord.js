/**
 * 主列表分页标志符
 */
var pageSign = false;

// 页面加载
$(document).ready(function() {
	$("#tabs").tabs();
	searchApp(1);
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
    	listSearchWord(status);
	}
}

/**
 * 获取并展示搜索词汇列表
 * 
 * @param status 栏目标志符 1：搜索词汇列表
 */
function listSearchWord(status) {
	// query params
    var obj = {};
    obj.word = $("#s_word").val();
    obj.status = $("#s_status").val();
    obj.curPage = $("#curPage").val();
    
    $("#curPage").val(1);
    
    // ajax submit
    $.ajax({
    	type : "POST",
        url : context + "/web/support/searchWord",
        datatype : "json",
        contentType : "application/json",
        data : $.toJSON(obj),
        success : function(data) {
        	if(data != null) {
        		if(data.message == 'SUCCESS') {
		            if(data.page != null && data.page.content != null && data.page.content.length > 0) {
		            	// table head
		            	$("#tabs_" + status).html('<table id="shortUrlTable" width="100%" border="0" cellspacing="0" cellpadding="0" style="text-align:center;"><thead></thead><tbody></tbody></table>');
		            	$("#tabs_" + status + " table thead").append('<th width="10%" background="' + context + '/resource/image/ui/content-bg.gif"><span>编号</span></th>');
		            	$("#tabs_" + status + " table thead").append('<th width="40%" background="' + context + '/resource/image/ui/content-bg.gif"><span>词汇</span></th>');
		            	$("#tabs_" + status + " table thead").append('<th width="10%" background="' + context + '/resource/image/ui/content-bg.gif"><span>状态</span></th>');
		            	$("#tabs_" + status + " table thead").append('<th width="40%" background="' + context + '/resource/image/ui/content-bg.gif"><span>操作</span></th>');
		              
		            	// data list
		            	$.each(data.page.content, function(i) {
		            		if(i % 2 == 0) {
		            			$("#tabs_" + status + " table").append('<tr bgcolor="#f2f2f2"></tr>');
			                }else {
			                	$("#tabs_" + status + " table").append('<tr></tr>');
			                }
		            		
			                $("#tabs_" + status + " table tr:last").append('<td id="id_' + this.id + '" class="left_txt2">' + this.id + '</td>');
			                $("#tabs_" + status + " table tr:last").append('<td id="word_' + this.id + '" class="left_txt2">' + this.word + '</td>');
			                
			                if('ENABLED' == this.status) {
			                	$("#tabs_" + status + " table tr:last").append('<td id="status_' + this.id + '" class="left_txt2">' + this.statusName + '</td>');
			                } else if ('DISABLED' == this.status) {
			                	$("#tabs_" + status + " table tr:last").append('<td id="status_' + this.id + '" class="txt_red">' + this.statusName + '</td>');
			                }
			                
			                $("#tabs_" + status + " table tr:last").append('<td class="left_txt2"></td>');
			                
			                // operation
			                $("#tabs_" + status + " table tr:last td:last").append('<a style="color:blue;" href="javascript:editSearchWord(' + this.id + ');">修改</a>&nbsp;&nbsp;&nbsp;');
			                $("#tabs_" + status + " table tr:last td:last").append('<a style="color:blue;" href="javascript:detail(\'' + this.id + '\', \'' + this.word + '\');">查看同义词</a>&nbsp;&nbsp;&nbsp;');
			                
			                if('ENABLED' == this.status) {
			                	$("#tabs_" + status + " table tr:last td:last").append('<a style="color:blue;" href="javascript:updateStatus(' + this.id + ', \'DISABLED\');">停用</a>&nbsp;&nbsp;&nbsp;');
			                } else if ('DISABLED' == this.status) {
			                	$("#tabs_" + status + " table tr:last td:last").append('<a style="color:blue;" href="javascript:updateStatus(' + this.id + ', \'ENABLED\');">启用</a>&nbsp;&nbsp;&nbsp;');
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
		            }
		            else {
		            	$("#pagination").empty();
		            	$("#tabs_" + status).html("<p>无符合条件数据记录</p>");
		            }
        		}
        		else {
        			$("#tabs_" + status).html("<p>获取搜索词汇列表出错-" + data.message + "</p>");
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
		width : 400,
		height : 200,
		focus : false,
		modal : true,
		draggable : false,
		autoOpen : false,
		resizable : false,
		close : function() {
			$("#word").val("");
			$("#wordDiv").dialog('destroy');
		},
		open : function() {
		},
		buttons : buttons
	});
	$("#wordDiv").dialog("open");
}

/**
 * 新增搜索词汇
 */
function createShortUrl(){
	var title = '新增搜索词汇';
	
	var buttons = {};
	
	buttons["取消"] = function() {
		$(this).dialog("close");
	};
	
	buttons["保存"] = function() {
		// get data
		var word = commonUtils.trim($("#word").val());
		
		if(!checkForm(word))
			return false;
		
		// query params
		var obj = {};
		obj.word = word;
		
		// ajax submit
		$.ajax({
			type : "POST",
			url : context + "/web/support/searchWord/create",
			datatype : "json",
			async : false,
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				if(data.message == 'SUCCESS') {
					alert("新增搜索词汇成功");
                    $("#wordDiv").dialog("close");
                    searchApp(1);
                }else {
                	alert("新增搜索词汇失败-" + data.message);
                }
			}
		});
	};
	
	openDialog(title, buttons);
}

/**
 * 编辑搜索词汇
 * 
 * @param id 搜索词汇编号
 */
function editSearchWord(id){
	// init dialog
	$("#word").val($("#word_" + id).text());
	
	var title = '编辑搜索词汇';
	
	var buttons = {};
	
	buttons["取消"] = function() {
		$(this).dialog("close");
	};
	
	buttons["保存修改"] = function() {
		// get data
		var word = commonUtils.trim($("#word").val());
		
		if(!checkForm(word))
			return false;
		
		if(word == $("#word_" + id).text()) {
			alert("请输入不同的词汇");
	        $("#wordDiv #word").focus();
	        return false;
	    }
		
		// query params
		var obj = {};
		obj.id = id;
		obj.word = word;
		
		// ajax submit
		$.ajax({
			type : "POST",
			url : context + "/web/support/searchWord/update",
			datatype : "json",
			async : false,
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				if(data.message == 'SUCCESS') {
					alert("搜索词汇修改成功");
                    $("#wordDiv").dialog("close");
                    searchApp(1);
                }else {
                	alert("搜索词汇修改失败-" + data.message);
                }
			}
		});
	};
	
	openDialog(title, buttons);
}

/**
 * 更新搜索词汇状态
 * 
 * @param id 搜索词汇编号
 * @param status 状态（停用，启用）
 */
function updateStatus(id, status){
	var statusOpt;
	if("DISABLED" == status){
		statusOpt = "停用";
	}else if("ENABLED" == status){
		statusOpt = "启用";
	}
	
	var confirmStr = "确定 " + statusOpt + " 搜索词汇“" + $("#word_"+id).text() + "”吗？";
	if(confirm(confirmStr)){
		// ajax submit
		$.ajax({
			type : "POST",
			url : context + "/web/support/searchWord/" + id + "/status",
			datatype : "json",
			data : {status : status},
			async : false,
			success : function(data) {
				if(data.message == 'SUCCESS') {
					alert(statusOpt + "搜索词汇成功");
	                $("#wordDiv").dialog("close");
	                searchApp(1);
	            }else {
	            	alert(statusOpt + "搜索词汇失败-" + data.message);
	            }
			}
		});
	}
}

/**
 * 验证表单
 * 
 * @param word 搜索词汇
 */
function checkForm(word){
	// 验证搜索词汇 word
	if(word.length == 0) {
		alert("请输入搜索词汇");
        $("#wordDiv #word").focus();
        return false;
    }
	if(word.length > 10) {
		alert("搜索词汇不能超过10个字符");
        $("#wordDiv #word").focus();
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
 * 搜索词汇的同义词列表
 * 
 * @param id 搜索词汇编号
 */
function detail(searchWordId, word){
	$("#detail_tabs").html('<img src="'+context+'/resource/image/ajax-loader.gif" />');
	
	var title = '“' + word + '”的同义词列表';
	
	var buttons = {};
	
	buttons["关闭"] = function() {
		$(this).dialog("close");
	};
	
	buttons["保存修改"] = function() {
		var synonymStr = "";
		
		try {
			$("input[name='synonym']").each(function(i){
				var synonym = commonUtils.trim($(this).val());
				
				// 验证同义词
				if(synonym.length == 0) {
					alert("同义词不能为空");
					$(this).focus();
					throw "";
			    }
				if(synonym.length > 10) {
					alert("同义词不能超过10个字符");
					$(this).focus();
			        throw "";
			    }
				
				synonymStr += synonym + ','
			});
		} catch (e) {
            return false;
        }
		
		// query params
		var obj = {};
		obj.synonymStr = synonymStr;
		
		if(confirm("确定修改“" + word + "”的同义词吗？")){
			// ajax submit
			$.ajax({
				type : "POST",
				url : context + "/web/support/searchWord/" + searchWordId + "/update",
				datatype : "json",
				async : false,
				data : {synonymStr : synonymStr},
				success : function(data) {
					if(data.message == 'SUCCESS') {
						alert("同义词修改成功");
	                    $("#wordDiv").dialog("close");
	                }else {
	                	alert("同义词修改失败-" + data.message);
	                }
				}
			});
		}
		
	};
	
    // ajax submit
    $.ajax({
    	type : "POST",
        url : context + "/web/support/searchWord/" + searchWordId + "/listSynonym",
        datatype : "json",
        success : function(data) {
        	if(data != null) {
        		if(data.message == 'SUCCESS') {
		            if(data.page != null && data.page.length > 0) {
		            	$("#detail_tabs").html('<ul><li index="0"><a href="javascript:addSynonym();"><button>添加同义词</button></a></li></ul>');
		            	
		            	// data list
		            	$.each(data.page, function(i) {
		            		$("#detail_tabs ul").append('<li id="synonym_li_' + (i+1) + '" index="' + (i+1) + '"><a href="javascript:delSynonym(\'synonym_li_' + (i+1) + '\');"><button>删除</button></a>&nbsp;<input name="synonym" type="text" size="20" value="' + this.synonym + '" /></li>');
		            	});
		            }
		            else {
		            	$("#detail_tabs").html('<ul><li index="0"><a href="javascript:addSynonym();"><button>添加同义词</button></a></li></ul>');
		            }
        		}
        		else {
        			$("#detail_tabs").html("<p>获取同义词列表出错-" + data.message + "</p>");
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
		width : 500,
		height : 500,
		focus : false,
		modal : true,
		draggable : false,
		autoOpen : false,
		resizable : false,
		close : function() {
			$("#detailDiv").dialog('destroy');
		},
		open : function() {
		},
		buttons : buttons
	});
	$("#detailDiv").dialog("open");
}

/**
 * 添加一条新同义词
 */
function addSynonym(){
	var maxIndex = $("#detail_tabs ul li:last").attr("index");
	var index = parseInt(maxIndex) + 1;
	
	$("#detail_tabs ul").append('<li id="synonym_li_' + index + '" index="' + index + '"><a href="javascript:delSynonym(\'synonym_li_' + index + '\');"><button>删除</button></a>&nbsp;<input name="synonym" type="text" size="20" /></li>');
}

/**
 * 删除一条同义词
 * 
 * @param objId 同义词标签id
 */
function delSynonym(objId){
	$('#'+objId+'').remove();
}