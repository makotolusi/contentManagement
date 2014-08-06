var pageSign = false;

$(document).ready(function() {
	$("#selectable").selectable({
		selecting : function(event, ui) {
			// tags.id=$(ui.selected).attr("tagId");
			// tags.name=$(ui.selected).text();
			// tags.push({$(ui.selected).attr("tagId"),$(ui.selected).text()})

		}
	});
	$("#tabs1").tabs();
	searchWord(1);
});

function popDialog(ths) {
	$('#tagName').val('');
	tagType = $(ths).attr('id');
	tagName = '';
	listTags();
	tagStr = "";
	$("#tagStr").text("");
	$("#selectable").css('visibility', 'visible');
	$("#tagList").dialog("open");
}

function queryTags(ths) {
	if (event.keyCode == 13) {
		tagName = $(ths).val();
		listTags();
	}
}

function pageselectCallback(index, jq) {
	if (pageSign) {
		$("#curPage").val(parseInt(index) + 1);
		searchWord($("#curTab").val());
	}
}

/**
 * 查找
 */
function searchList() {
	var curTab = $("#curTab").val();
	searchWord(curTab);

	if (1 == curTab) {
		$("#s_pushState").val("");
	} else if (0 == curTab) {
		$("#s_state").val("");
	}
}

function init(){
	$("#userToken").val("");
	$("#dateFrom").val("");
	$("#dateTo").val("");
	$("#dFrom").val("");
	$("#dTo").val("");
	$("#serviceId").val("");
	$("#serviceId2").val("");
	$("#serviceName").val("");
	$("#serviceName2").val("");
	$("#itemType").val("请选择");
    $("#operatorType").val("请选择");
    $("#otherWay").val("");
    
}

/**
 * 展示查询列表
 * 
 * @param status
 *            分类(1:推送列表;0:推送历史列表)
 */
function searchWord(status,type) {
	$("#tabs_" + status).html(
			'<img src="'+context+'/resource/image/ajax-loader.gif" />');
	$("#curTab").val(status);
	var obj = {};
	obj.curPage = "" + $("#curPage").val();
	obj.userToken = "" + $("#userToken").val();
	obj.itemType = "" + $("#itemType").val();
	obj.gameCode = "" + $("#gameCode").val();
	obj.keyWord = "" + $("#keyWord").val();
	obj.otherWay = "" + $("#otherWay").val();
	obj.operatorType = "" + $("#operatorType").val();
	obj.dateFrom = "" + $("#dateFrom").val();
	obj.dateTo = "" + $("#dateTo").val();
	obj.dFrom = "" + $("#dFrom").val();
	obj.dTo = "" + $("#dTo").val();
	obj.serviceId = "" + $("#serviceId").val();
	obj.serviceId2 = "" + $("#serviceId2").val();
	obj.serviceName = "" + $("#serviceName").val();
	obj.serviceName2 = "" + $("#serviceName2").val();
	
	$("#curPage").val(1);
		obj.collectionName =status;
	var url = '';
	if (status == '1') {
		url = context + "/web/activity/pv/listClientLogCollection";
		$.ajax({
			type : "POST",
			url : url,
			datatype : "json",
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				console.log(data);
				colName = [ '用户Token', '业务ID','业务名称', 'GameCode', '游戏特征', '游戏类型', '游戏平台','业务类型', '事件类型', '操作时间', '上传时间', '附加字段', '通过其它途径', '附加'];
				colWitdh = [ '5%', '10%', '10%', '5%','5%','5%','5%', '5%', '5%','10%', '10%', '10%', '10%', '10%' ];
				colKey = [ 'uid','serviceId', 'serviceName','gameCode','gameStatus','gameType','gamePlatFormE', 'itemTypeE', 'operatorTypeE', 'operatorDate', 'uploadDate', 'keyWord', 'otherWayE', 'keyWord2' ];
				tableHtml(status,data,colName,colWitdh,colKey);
			}
		});

	}
	else if(status =='UserItemOperatePv') {
		url = context + "/web/activity/pv/listPvCollection";
		$.ajax({
					type : "POST",
					url : url,
					datatype : "json",
					contentType : "application/json",
					data : $.toJSON(obj),
					success : function(data) {
//						console.log(data);
						colName = [ '用户Token','业务ID', '业务名称', '业务类型','事件类型', 'GameCode', '游戏特征', '游戏类型', '游戏平台', '通过其它途径','附加字段','PV' ];
						colWitdh = [ '10%', '10%', '10%','10%','10%','5%','5%','5%','5%','5%','5%','10%'];
						colKey = [ 'id.uid', 'id.serviceId','value.serviceName', 'value.itemTypeE','id.operatorTypeE','value.gameCode','value.gameStatus','value.gameType','value.gamePlatFormE','id.otherWayE','value.keyWord','value.pv'];
						tableHtml(status,data,colName,colWitdh,colKey);
					}
				});
	}else if(status =='ItemPv') {
		url = context + "/web/activity/pv/listPvCollection";
		$.ajax({
					type : "POST",
					url : url,
					datatype : "json",
					contentType : "application/json",
					data : $.toJSON(obj),
					success : function(data) {
						colName = [ '业务ID','业务名称','业务类型', 'GameCode', '游戏特征', '游戏类型', '游戏平台','PV' ];
						colWitdh = ['10%',  '10%','10%','10%','10%','10%','10%','10%'];
						colKey = [ 'id.serviceId','value.serviceName','value.itemTypeE','value.gameCode','value.gameStatus','value.gameType','value.gamePlatFormE',  'value.pv'];
						tableHtml(status,data,colName,colWitdh,colKey);
					}
				});
	}else if(status =='UserGamePv') {
		console.log(url);
		url = context + "/web/activity/pv/listPvCollection";
		console.log(url);
		$.ajax({
					type : "POST",
					url : url,
					datatype : "json",
					contentType : "application/json",
					data : $.toJSON(obj),
					success : function(data) {
						colName = [ '用户Token', 'GameCode', '游戏特征', '游戏类型', '游戏平台','PV' ];
						colWitdh = [ '10%',  '10%','10%','10%','10%','10%','10%','10%'];
						colKey = [ 'id.uid','value.gameCode','value.gameStatus','value.gameType','value.gamePlatFormE','value.pv'];
						tableHtml(status,data,colName,colWitdh,colKey);
					}
				});
	}else if(status =='ItemAppPv') {
		console.log(url);
		url = context + "/web/activity/pv/listPvCollection";
		$.ajax({
					type : "POST",
					url : url,
					datatype : "json",
					contentType : "application/json",
					data : $.toJSON(obj),
					success : function(data) {
						colName = [ '游戏名称', 'GameCode', '游戏特征', '游戏类型', '游戏平台','PV' ];
						colWitdh = [ '10%',  '10%','10%','10%','10%','10%','10%','10%'];
						colKey = [ 'value.serviceName','value.gameCode','value.gameStatus','value.gameType','value.gamePlatFormE','value.pv'];
						tableHtml(status,data,colName,colWitdh,colKey);
					}
				});
	}else if(status =='ItemOperatePv') {
		url = context + "/web/activity/pv/listPvCollection";
		$.ajax({
					type : "POST",
					url : url,
					datatype : "json",
					contentType : "application/json",
					data : $.toJSON(obj),
					success : function(data) {
						colName = ['业务ID', '业务名称','业务类型', '事件类型', 'GameCode', '游戏特征', '游戏类型', '游戏平台','PV' ];
						colWitdh = [ '10%',  '10%','10%','10%','10%','10%','10%','10%'];
						colKey = [ 'id.serviceId','value.serviceName',  'value.itemTypeE','id.operatorTypeE','value.gameCode','value.gameStatus','value.gameType','value.gamePlatFormE','value.pv'];
						tableHtml(status,data,colName,colWitdh,colKey);
					}
				});
	}else if(status =='SearchKeyWordPv') {
		url = context + "/web/activity/pv/listPvCollection";
		$.ajax({
					type : "POST",
					url : url,
					datatype : "json",
					contentType : "application/json",
					data : $.toJSON(obj),
					success : function(data) {
						colName = [ '关键字', 'PV' ];
						colWitdh = [ '50%', '10%'];
						colKey = [ 'id.keyWord','value.pv'];
						tableHtml(status,data,colName,colWitdh,colKey);
					}
				});
	}else if(status =='UserSearchKeyWordPv') {
		url = context + "/web/activity/pv/listPvCollection";
		$.ajax({
					type : "POST",
					url : url,
					datatype : "json",
					contentType : "application/json",
					data : $.toJSON(obj),
					success : function(data) {
						colName = [ '用户Token','关键字','PV' ];
						colWitdh = [ '20%', '20%', '20%', ];
						colKey = [ 'id.uid', 'id.keyWord','value.pv'];
						tableHtml(status,data,colName,colWitdh,colKey);
					}
				});
	}

}

function tableHtml(status,data,colName,colWitdh,colKey) {
	$('#total').text(data.total);
	if (data != null) {
		if (data.page != null && data.page.content != null
				&& data.page.content.length > 0) {

			$("#tabs_" + status)
					.html(
							'<table width="100%" border="0" cellspacing="0" cellpadding="0" style="text-align:center;"><tr style="font-size:13px;"></tr></table>');

			$.each(colName,function(i, j) {
								$("#tabs_" + status + " table tr")
										.append(
												'<th width="'
														+ colWitdh[i]
														+ '" background="${contextPath}/resource/image/ui/content-bg.gif"><span>'
														+ colName[i]
														+ '</span></th>');

							});

		
			$.each(data.page.content,function(i,obj) {
								if (i % 2 == 0) {
									$("#tabs_" + status + " table").append(
											'<tr bgcolor="#f2f2f2"></tr>');
								} else {
									$("#tabs_" + status + " table").append(
											'<tr></tr>');
								}
								
								$.each(colKey,function(i, j) {
									console.log(j);
									var text='';
									if(j.indexOf('.')>=0){
										text=obj[j.split('.')[0]][j.split('.')[1]]
									}else
										text=obj[j];
									if(text==null)
										text="";
									$("#tabs_" + status + " table tr:last").append(
											'<td class="left_txt2">' + text
													+ '</td>');
									
								});
							
							});

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
		} else {
			$("#pagination").empty();
			$("#tabs_" + status).html("<p>无符合条件数据记录</p>");
		}
	}
}

function editWord(type, id) {

	var title = '编辑禁词';
	var buttons = {};
	buttons["取消"] = function() {
		$(this).dialog("close");
	};
	if (type == 'new') {
		title = '创建推送任务';
		$("#wordDiv #project").removeAttr("disabled");
		$("#wordDiv #type").removeAttr("disabled");
		buttons["创建"] = function() {

			var pushType = $("input[name='send']:checked").val();
		
			var word = commonUtils.trim($("#wordDiv #word").val());

			var repl = commonUtils.trim($("#wordDiv #repl").val());

			// var device=$('input:checkbox:checked[name="device"]').val();
			var id_array = new Array();
			$('input[name="device"]:checked').each(function() {
				id_array.push($(this).val());// 向数组中添加元素
			});
			var idstr = id_array.join(',');// 将数组元素连接起来以构建一个字符串
			var tags = $("#tagStr").html();
			console.log(tags.length);
		
			
			var obj = {
				title : $("#wordDiv #title").val(),
				content : $("#wordDiv #content").val(),
				clientTypes : idstr,
				pushType : pushType,
				tags : $("#tagStr").text()

			};

			$.ajax({
				type : "POST",
				url : context + "/web/activity/push/condition",
				datatype : "json",
				async : false,
				contentType : "application/json",
				data : $.toJSON(obj),
				success : function(data) {
					if (data != null) {
						if (data.message == 'SUCCESS') {
							alert("推送成功");
							$("#wordDiv").dialog("close");
							searchWord(1);
						} else {
							alert("创建禁词失败-" + data.message);
						}
					}
				}
			});
		};
	}
	$("#wordDiv").dialog({
		title : title,
		width : 630,
		height : 800,
		focus : false,
		modal : true,
		draggable : false,
		autoOpen : false,
		resizable : false,
		close : function() {
			$("#wordDiv #word").val('');
			$("#wordDiv #repl").val('');
			$("#wordDiv #wordId").val('');
			var SelectArr = $("select");
			for ( var i = 0; i < SelectArr.length; i++) {// select 重置
				SelectArr[i].options[0].selected = true;
			}
			$("#wordDiv").dialog('destroy');
		},
		open : function() {
		},
		buttons : buttons
	});
	$("#wordDiv").dialog("open");
}

function updateStatus(id, status) {
	if (confirm("是否确定修改任务状态？")) {
		$.ajax({
			type : "POST",
			url : context + "/web/activity/push/" + id + "/status",
			datatype : "json",
			data : {
				status : status
			},
			async : false,
			success : function(data) {
				if (data != null) {
					if (data.message == 'SUCCESS') {
						alert("修改成功");
						searchWord(1);
					} else {
						alert("修改失败：" + data.message);
					}
				}
			}
		});
	}
}

function deleteWord(id) {
	if (confirm("您确定要删除这项推送任务吗？")) {
		$.ajax({
			type : "POST",
			url : context + "/web/activity/push/" + id + "/delete",
			datatype : "json",
			async : false,
			success : function(data) {
				if (data != null) {
					if (data.message == 'SUCCESS') {
						alert("任务删除成功");
						searchWord(1);
					} else {
						alert("任务删除失败-" + data.message);
					}
				}
			}
		});
	}
}