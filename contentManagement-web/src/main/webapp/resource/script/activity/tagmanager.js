var pageSign = false;

var pageSign2 = false;
$(document).ready(function() {
	$("#selectable").selectable({
		selecting : function(event, ui) {
			// tags.id=$(ui.selected).attr("tagId");
			// tags.name=$(ui.selected).text();
			// tags.push({$(ui.selected).attr("tagId"),$(ui.selected).text()})

		}
	});
	$("#tabs1").tabs();
	searchWord('CombinationTag');
	searchWord('ItemOperatePv');
	
});

var tagType;
function addCTag() {
	if($('#ctag').val().length>50)
		alert("标题过长！")
	if ($('#ctag').val() == null || $('#ctag').val() == '') {
		alert('请输入组合标签名称');
		return;
	}

	var obj = {};
	var tagList = [];
	var ctagList = [];
	var vtagList = [];
	obj.tagName = $('#ctag').val();

	var k=0;
	$("#tags input[name='tag']").each(function(i, x) {
		
		if ($(x).attr("checked")) {
			var tags = {}
			tags.tagName = $.trim($(x).attr('n'));
			tags.tagId = $.trim($(x).attr('v'));
			tags.tagType = $.trim($(x).attr('t'));
			tagList.push(tags);
			k++;
		}
	});
	$("#ctags input[name='tag']").each(function(i, x) {
		if ($(x).attr("checked")) {
			var tags = {}
			tags.tagName = $.trim($(x).attr('n'));
			tags.tagId = $.trim($(x).attr('v'));
			tags.tagType = $.trim($(x).attr('t'));
			ctagList.push(tags);
			k++;
		}

	});
	
	$("#vtags input[name='tag']").each(function(i, x) {
		if ($(x).attr("checked")) {
			var tags = {}
			tags.tagName = $.trim($(x).attr('n'));
			tags.tagId = $.trim($(x).attr('v'));
			tags.tagType = $.trim($(x).attr('t'));
			vtagList.push(tags);
			k++;
		}
	});
	if (k==0) {
		alert('请添加标签');
		return;
	}
		
	if (k==1) {
			alert('才一个标签,请不要浪费资源，使用推送功能直接推送');
			return;
	}

	obj.tags = tagList;
	obj.ctags = ctagList;
	obj.vtags = vtagList;
	obj.tagsop = $("#tagsop").val();
	obj.cvtagsop = $("#cvtagsop").val();
	
	var url = context + "/web/activity/tagmanager/makePushTagCombination";
	$.ajax({
		type : "POST",
		url : url,
		datatype : "json",
		contentType : "application/json",
		data : $.toJSON(obj),
		success : function(data) {
			if (data.message == 'SUCCESS')
			{	alert('添加成功');
			location.reload();
			}
			else
				alert('出错啦!'+data.message);

		}
	});
}
var flag=0;
function getRow(o) {

	var tagids='tags';
	var tr = o.parentNode.parentNode;
	var tds = tr.cells;
	if ($("#curTab").val() == 'ItemOperatePv') {
		var tagid = $.trim($(tds[1]).text()) + "_" + $.trim($(tds[4]).text())
				+ "_" +$.trim($(tds[6]).text());
		var tagname = $.trim($(tds[2]).text()) + "_" + $(tds[3]).text()
				+ "_" + $.trim($(tds[5]).text());
	}else if ('mobileStatus,mobileType,pcStatus,pcType'.indexOf($("#curTab").val())>=0) {
		var tagid = $.trim($(tds[1]).text());
		var tagname = $.trim($(tds[1]).text());
	}else{
		var tagid = $.trim($(tds[1]).text());
		var tagname = $.trim($(tds[2]).text());
	}
//	if ($("#curTab").val() == 'ItemPv') {
//		var tagid = $.trim(tds[1].innerText);
//		var tagname = $.trim(tds[2].innerText);
//	}
//	
//
	if ($("#curTab").val() == 'ChannelTag') {
		tagids='ctags';
		var tagid =$.trim($(tds[1]).text());
		var tagname = $.trim($(tds[2]).text());
	}

	if ($("#curTab").val() == 'VersionTag') {
		tagids='vtags';
		var tagid = $.trim($(tds[1]).text());
		var tagname =$.trim($(tds[2]).text());
	}
	
	
//	if(flag!=0)
//		$('#'+tagids)
//		.append(
//				'<select id="tags">'+and+or+' </select>');
	

	$('#'+tagids)
			.append(
					'<input type=checkbox v=' + tagid
							+ ' name="tag" checked n="' + tagname + '" t='
							+ tagType + ' >' + tagname + '</input> ');

	flag++;
}

function popConfirm(o) {

	var tr = o.parentNode.parentNode;
	var tds = tr.cells;
	var url = context + "/web/activity/tagmanager/countPushTagCombination";
	var obj = {};
	obj.name =  $.trim($(tds[1]).text());
	$.ajax({
		type : "POST",
		url : url,
		datatype : "json",
		contentType : "application/json",
		data : $.toJSON(obj),
		success : function(data) {
			if (data.message == 'SUCCESS') {
				$("#size").text(data.size);
				$("#name").text(obj.name);	
				
				$("#dialog").dialog("open");
			}else{
				alert(data.message);
			}

		}
	});

}

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

function pageselectCallback2(index, jq) {

	if (pageSign2) {
		$("#curPage").val(parseInt(index) + 1);
		console.log(index+" --------xxx------- "+pageSign2)
		searchWord('CombinationTag');
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

/**
 * 查找
 */
function searchCombinationTagList() {
	searchWord("CombinationTag");

	if (1 == curTab) {
		$("#s_pushState").val("");
	} else if (0 == curTab) {
		$("#s_state").val("");
	}
}
function init() {
	// $("#userToken").val("");
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
}

/**
 * 展示查询列表
 * 
 * @param status
 *            分类(1:推送列表;0:推送历史列表)
 */
function searchWord(status, type) {
console.log(status+" ------------------"+type);
	$("#tabs_" + status).html(
			'<img src="' + context + '/resource/image/ajax-loader.gif" />');


	var obj = {};
	obj.curPage = "" + $("#curPage").val();

	// obj.userToken = "" + $("#userToken").val();
	obj.itemType = "" + $("#itemType").val();
	obj.operatorType = "" + $("#operatorType").val();
//	obj.dateFrom = "" + $("#dateFrom").val();
//	obj.dateTo = "" + $("#dateTo").val();

//	obj.dFrom = "" + $("#dFrom").val();
//	obj.dTo = "" + $("#dTo").val();

//	obj.serviceId = "" + $("#serviceId").val();
//	obj.serviceId2 = "" + $("#serviceId2").val();
	obj.serviceName = "" + $("#serviceName").val();
//	obj.serviceName2 = "" + $("#serviceName2").val();
	$("#curPage").val(1);
	obj.collectionName = status;
	var url = '';
	if (status == 'ItemPv') {
		$("#curTab").val(status);
		tagType = "100";
		obj.pageSize=10;
		url = context + "/web/activity/tagmanager/listGameTag";
		$.ajax({
			type : "POST",
			url : url,
			datatype : "json",
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				colName = [ 'GameCode', '名称'];
				colWitdh = [ '50%', '50%' ];
				colKey = [ 'tagId', 'tagName'];
				tableHtml(status, data, colName, colWitdh, colKey);
			}
		});
	} else if (status == 'ItemOperatePv') {
		$("#curTab").val(status);
		tagType = "101";
//		obj.tagType='1';
		url = context + "/web/activity/pv/listPvCollection";
		$.ajax({
			type : "POST",
			url : url,
			datatype : "json",
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				colName = [ '业务ID', '业务名称', '业务类型','业务ID','事件类型','事件ID', 'GameCode', '游戏特征', '游戏类型', '游戏平台', 'PV' ];
				colWitdh = [ '10%', '10%', '10%','10%','10%','10%','5%','5%','5%','5%','10%'];
				colKey = [ 'id.serviceId','value.serviceName', 'value.itemTypeE','value.itemType','id.operatorTypeE','id.operatorType','value.gameCode','value.gameStatus','value.gameType','value.gamePlatFormE','value.pv'];
				tableHtml(status, data, colName, colWitdh, colKey);
			}
		});
	} else if (status == 'mobileStatus') {
		$("#curTab").val(status);
		tagType = "1001";
		url = context + "/web/activity/tagmanager/listGameCateTag/1001";
		$.ajax({
			type : "POST",
			url : url,
			datatype : "json",
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				colName = [ 'ID','名称' ];
				colWitdh = [ '50%' ,'50%'];
				colKey = [ 'tagId','tagName' ];
				tableHtml(status, data, colName, colWitdh, colKey);
			}
		});
	} else if (status == 'mobileType') {
		
		$("#curTab").val(status);
		tagType = "1002";
		url = context + "/web/activity/tagmanager/listGameCateTag/1002";
		$.ajax({
			type : "POST",
			url : url,
			datatype : "json",
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				colName = [ 'ID','名称' ];
				colWitdh = [  '50%' ,'50%'];
				colKey = [ 'tagId','tagName' ];
				tableHtml(status, data, colName, colWitdh, colKey);
			}
		});
	}else if (status == 'live') {
		
		$("#curTab").val(status);
		tagType = "100";
		url = context + "/web/activity/tagmanager/listLiveTag";
		$.ajax({
			type : "POST",
			url : url,
			datatype : "json",
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				colName = [ 'ID','名称' ];
				colWitdh = [  '50%' ,'50%'];
				colKey = [ 'tagId','tagName' ];
				tableHtml(status, data, colName, colWitdh, colKey);
			}
		});
	}else if (status == 'gift') {
		
		$("#curTab").val(status);
		tagType = "100";
		url = context + "/web/activity/tagmanager/listGiftTag";
		$.ajax({
			type : "POST",
			url : url,
			datatype : "json",
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				colName = [ 'ID','名称' ];
				colWitdh = [  '50%' ,'50%'];
				colKey = [ 'tagId','tagName' ];
				tableHtml(status, data, colName, colWitdh, colKey);
			}
		});
	}else if (status == 'pcStatus') {
		$("#curTab").val(status);
		tagType = "1003";
		url = context + "/web/activity/tagmanager/listGameCateTag/1003";
		$.ajax({
			type : "POST",
			url : url,
			datatype : "json",
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				colName = ['ID', '名称' ];
				colWitdh = [ '50%' ,'50%'];
				colKey = [ 'tagId','tagName' ];
				tableHtml(status, data, colName, colWitdh, colKey);
			}
		});
	} else if (status == 'pcType') {
		$("#curTab").val(status);
		tagType = "1004";
		url = context + "/web/activity/tagmanager/listGameCateTag/1004";
		$.ajax({
			type : "POST",
			url : url,
			datatype : "json",
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				colName = [ 'ID','名称' ];
				colWitdh = [  '50%' ,'50%'];
				colKey = [ 'tagId','tagName' ];
				tableHtml(status, data, colName, colWitdh, colKey);
			}
		});
	}else if (status == 'jiong') {
		tagType = "2";
		$("#curTab").val(status);
		url = context + "/web/activity/tagmanager/listJiongTag";
		$.ajax({
			type : "POST",
			url : url,
			datatype : "json",
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				colName = [ 'ID','名称' ];
				colWitdh = [ '50%','50%' ];
				colKey = [ 'tagId','tagName' ];
				tableHtml(status, data, colName, colWitdh, colKey);
			}
		});
	}else if (status == 'rank') {
		tagType = "15";
		$("#curTab").val(status);
		url = context + "/web/activity/tagmanager/listRankTag";
		$.ajax({
			type : "POST",
			url : url,
			datatype : "json",
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				colName = [ 'ID','名称' ];
				colWitdh = [ '50%','50%' ];
				colKey = [ 'tagId','tagName' ];
				tableHtml(status, data, colName, colWitdh, colKey);
			}
		});
	}else if (status == 'gamePlatForm') {
		tagType = "102";
		$("#curTab").val(status);
		url = context + "/web/activity/tagmanager/listGamePlatFormTag";
		$.ajax({
			type : "POST",
			url : url,
			datatype : "json",
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				colName = [ 'ID','名称' ];
				colWitdh = [ '50%','50%' ];
				colKey = [ 'tagId','tagName' ];
				tableHtml(status, data, colName, colWitdh, colKey);
			}
		});
	} else if (status == 'CombinationTag') {
	
		obj.serviceName = "" + $("#combinationName").val();
		url = context + "/web/activity/tagmanager/findPushTagCombination";
		$.ajax({
			type : "POST",
			url : url,
			datatype : "json",
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				colName = [ '名称', '子标签','子标签关系','渠道(OR)','渠道版本关系','版本(OR)','进度','成功数','状态'  ];
				colWitdh = [ '10%', '30%','5%','20%','5%','20%','5%','5%'];
				colKey = [ 'tagName', 'tags','tagsop','ctags','cvtagsop','vtags','process','setTotal','state' ];
				
				tableHtml2(status, data, colName, colWitdh, colKey);
			}
		});
	} else if (status == 'ChannelTag') {
		tagType = "888";
		$("#curTab").val(status);
		obj.channelName = tagName;
		url = context + "/web/activity/tagmanager/listChannelTag";
		$.ajax({
			type : "POST",
			url : url,
			datatype : "json",
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				colName = [ '渠道ID', '渠道名称' ];
				colWitdh = [ '10%', '100%' ];
				colKey = [ 'tagId', 'tagName' ];
				tableHtml(status, data, colName, colWitdh, colKey);
			}
		});
	}else if (status == 'VersionTag') {
		$("#curTab").val(status);
		tagType = "887";
		url = context + "/web/activity/tagmanager/listVersionTag";
		$.ajax({
			type : "POST",
			url : url,
			datatype : "json",
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				
				colName = [ '版本ID', '版本名称' ];
				colWitdh = [ '10%', '100%' ];
				colKey = [ 'tagId', 'tagName' ];
				tableHtml(status, data, colName, colWitdh, colKey);
			}
		});
	}else if (status == 'act') {
		$("#curTab").val(status);
		tagType = "103";
		url = context + "/web/activity/tagmanager/listActivityTag";
		$.ajax({
			type : "POST",
			url : url,
			datatype : "json",
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				
				colName = [ '版本ID', '版本名称' ];
				colWitdh = [ '10%', '100%' ];
				colKey = [ 'tagId', 'tagName' ];
				tableHtml(status, data, colName, colWitdh, colKey);
			}
		});
	}



}

function tableHtml(status, data, colName, colWitdh, colKey) {
	if (data != null) {
		if (data.page != null && data.page.content != null
				&& data.page.content.length > 0) {

			$("#tabs_" + status)
					.html(
							'<table width="100%" border="0" cellspacing="0" cellpadding="0" style="text-align:center;"><tr style="font-size:13px;"></tr></table>');

			$("#tabs_" + status + " table tr")
					.append(
							'<th width="" background="${contextPath}/resource/image/ui/content-bg.gif"><span>选择</span></th>');
			$
					.each(
							colName,
							function(i, j) {
								$("#tabs_" + status + " table tr")
										.append(
												'<th width="'
														+ colWitdh[i]
														+ '" background="${contextPath}/resource/image/ui/content-bg.gif"><span>'
														+ colName[i]
														+ '</span></th>');

							});

			$
					.each(
							data.page.content,
							function(i, obj) {
							
								if (i % 2 == 0) {
									$("#tabs_" + status + " table").append(
											'<tr bgcolor="#f2f2f2"></tr>');
								} else {
									$("#tabs_" + status + " table").append(
											'<tr></tr>');
								}

								$("#tabs_" + status + " table tr:last")
										.append(
												'<td class="left_txt2"><input type=button id=ck name=ck value="添加" onclick="getRow(this)"/></td>');
								
								$.each(colKey, function(i, j) {
									var text = '';
									if (j.indexOf('.') >= 0) {
										text = obj[j.split('.')[0]][j
												.split('.')[1]]
									} else
										text = obj[j];
									
						
									
									$("#tabs_" + status + " table tr:last")
											.append(
													'<td class="left_txt2">'
															+ text + '</td>');

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

function tableHtml2(status, data, colName, colWitdh, colKey) {

	console.log("#tabs_" + status);
	if (data != null) {
		if (data.page != null && data.page.content != null
				&& data.page.content.length > 0) {

			$("#tabs_" + status)
					.html(
							'<table width="100%" border="0" cellspacing="0" cellpadding="0" style="text-align:center;"><tr style="font-size:13px;"></tr></table>');

			$("#tabs_" + status + " table tr")
					.append(
							'<th width="" background="${contextPath}/resource/image/ui/content-bg.gif"><span>选择</span></th>');
			$
					.each(
							colName,
							function(i, j) {
								$("#tabs_" + status + " table tr")
										.append(
												'<th width="'
														+ colWitdh[i]
														+ '" background="${contextPath}/resource/image/ui/content-bg.gif"><span>'
														+ colName[i]
														+ '</span></th>');

							});

			$
					.each(
							data.page.content,
							function(i, obj) {
								if (i % 2 == 0) {
									$("#tabs_" + status + " table").append(
											'<tr bgcolor="#f2f2f2"></tr>');
								} else {
									$("#tabs_" + status + " table").append(
											'<tr></tr>');
								}

								$("#tabs_" + status + " table tr:last")
										.append(
												'<td class="left_txt2"><input type=button id=ck name=ck value="发送标签" onclick="popConfirm(this)"/></td>');
								$
										.each(
												colKey,
												function(i, j) {
													var text = '';
													if (j.indexOf('.') >= 0) {
														text = obj[j.split('.')[0]][j
																.split('.')[1]]
													} else {
														var t = ''
														if (obj[j] instanceof Array) {
															var tags = obj[j];
															for ( var int = 0; int < tags.length; int++) {
																t += tags[int].tagName
																		+ "("+tags[int].tagId+") , ";

															}
															text = t;
														} else
															text = obj[j];
													}
													$(
															"#tabs_"
																	+ status
																	+ " table tr:last")
															.append(
																	'<td class="left_txt2">'
																			+ text
																			+ '</td>');

												});

							});

			pageSign2 = false;
	
			$("#pagination2").pagination(data.page.rowCount, {
				callback : pageselectCallback2,
				items_per_page : data.page.pageSize,
				num_display_entries : 5,
				num_edge_entries : 1,
				current_page : data.page.curPage - 1,
				prev_text : "上一页",
				next_text : "下一页"
			});
			pageSign2 = true;
		} else {
			$("#pagination2").empty();
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
	} else {
		$("#wordDiv #word").val($("#word_" + id).text());
		$("#wordDiv #repl").val($("#repl_" + id).text());
		$(
				"#wordDiv #project option[value='" + $("#project_" + id).text()
						+ "']").attr("selected", "selected");
		$("#wordDiv #type option[value='" + $("#type_" + id).text() + "']")
				.attr("selected", "selected");
		$("#wordDiv #wordId").val(id);
		$("#wordDiv #project").attr("disabled", "disabled");
		$("#wordDiv #type").attr("disabled", "disabled");
		buttons["保存"] = function() {
			var word = commonUtils.trim($("#wordDiv #word").val());
			if (commonUtils.len(word) == 0) {
				alert("请输入禁词");
				$("#wordDiv #taboo").focus();
				return;
			}
			var repl = commonUtils.trim($("#wordDiv #repl").val());
			/*
			 * if(commonUtils.len(repl) == 0) { alert("请输入替换词"); $("#wordDiv
			 * #repl").focus(); return; }
			 */
			var project = commonUtils.trim($("#wordDiv #project").val());
			if (commonUtils.len(project) == 0) {
				alert("请选择应用项目");
				return;
			}
			var type = commonUtils.trim($("#wordDiv #type").val());
			if (commonUtils.len(type) == 0) {
				alert("请选择应用标识");
				return;
			}
			var obj = {};
			obj.word = word;
			obj.repl = repl;
			obj.project = project;
			obj.type = type;
			obj.status = parseInt($("#curTab").val());
			$.ajax({
				type : "POST",
				url : "${contextPath}/web/activity/taboo/" + id + "/update",
				datatype : "json",
				async : false,
				contentType : "application/json",
				data : $.toJSON(obj),
				success : function(data) {
					if (data != null) {
						if (data.message == 'SUCCESS') {
							alert("修改禁词成功");
							$("#wordDiv").dialog("close");
							searchWord(parseInt($("#curTab").val()));
						} else {
							alert("修改禁词失败-" + data.message);
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