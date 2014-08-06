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
	if(appId=='')
		searchWord(2);
	else
		searchWord(1);
});

function popDialog(ths) {
	$("#tagMsg").text("百度只支持单选，信鸽支持多选");
	$("#contentDisplay").val("0");
	$('#tagName').val('');
	tagType = ths;
	tagName = '';
	listTags();
	tagStr = "";
	// $("#tagStr").text("");
	$("#selectable").css('visibility', 'visible');
	$("#tagList").dialog("open");
}

function popContentDialog(ths) {
	if ($('#type').val() == '999'||"#25#26#27#28#".indexOf("#"+$('#type').val()+"#")>=0)
		return;
	$("#contentDisplay").val("1");
	$('#contentName').val('');
	contentType = ths;
	contentName = '';
	tagName = '';
	listContent();
	$("#selectable").css('visibility', 'visible');
	$("#tagList").dialog("open");
}

function queryTags(ths, event) {
	event = event || window.event;
	var k = event.keyCode;
	if (k == 13) {
		tagName = $(ths).val();
		if ($("#contentDisplay").val() == '0')
			listTags();
		else
			listContent();
	}
}

function listContent() {
	$("#tn").show();
	var obj = {};
	obj.curPage = 1;
	obj.type = $('#type').val();
	if (tagName != '')
		obj.name = tagName;
	var i = $("#source").find("option:selected").attr('en');
	if (i == undefined)
		i = 'STRATEGY_VIDEO'
	var url = context + "/web/activity/tagmanager/listContent/" + i;
	$("#selectable").html(
			'<img src="' + context + '/resource/image/ajax-loader.gif" />');
	$.ajax({
		type : "POST",
		url : url,
		datatype : "json",
		contentType : "application/json",
		data : $.toJSON(obj),
		success : function(data) {
			$("#selectable").html('');
			if (data.message == 'FAILED') {
				alert('出错啦！~')
				return;
			}
			data = data.page;
			if ((data.content != null && data.content.length != 0)) {
				if (data.content != null && data.content.length != 0) {
					$.each(data.content, function(i, k) {
						selectHtml(k)
					});
				}
			}

		}
	});
}

function selectHtml(k){
	$("#selectable").append(
			'<li class="ui-widget-content" k="' + k.tagId
					+ '" val="' + k.tagName+ '" >' + k.tagName + ' (ID: '
					+ k.tagId + ')</li>');
}
function listTags() {
	$("#tn").show();
	var obj = {};
	obj.curPage = 1;
	var url;
	if (tagType == '6')
		listChannelTag();
	else if (tagType == '8')
		listVersionTag();
	else {

		if (",10,".indexOf(',' + tagType + ',') >= 0) {
			url = context + "/web/activity/tagmanager/listGameTag";
		} else if (",5,".indexOf(',' + tagType + ',') >= 0)
			obj.cname = "SearchKeyWordPv";
		else
			obj.cname = "ItemOperatePv";

		if (",0,1,3,4,".indexOf(',' + tagType + ',') >= 0)
			obj.itemType = tagType;

		if (tagType == '100') {
			$("#tn").hide();
			url = context + "/web/activity/tagmanager/listJiongTag";
		}
		if (tagType == '101')
			obj.operatorType = '8';
		if (tagType == '102')
			obj.operatorType = '3';

		if (tagType == '103') {
			url = context + "/web/activity/tagmanager/listActivityTag";
		}
		if (tagType == '104')
			obj.operatorType = '2';
		if (tagType == '105')
			obj.operatorType = '7';
		if (tagType == '106')
			url = context + "/web/activity/tagmanager/listAppTag";
		if (tagType == '203') {
			$("#tn").hide();
			url = context + "/web/activity/tagmanager/listRankTag";
		}
		if (tagType == '204') {
			$("#tn").hide();
			url = context + "/web/activity/tagmanager/listGamePlatFormTag";
		}
		// var url = context + "/web/activity/pv/listTagNameAndPV";
		if (tagType == '999')
			url = context + "/web/activity/tagmanager/findPushTagCombination";

		if (tagType == '201')
			url = context + "/web/activity/tagmanager/listLiveTag";

		if (tagType == '202')
			url = context + "/web/activity/tagmanager/listGiftTag";

		if (",1001,1002,1003,1004,".indexOf("," + tagType + ",") >= 0) {
			$("#tn").hide();
			url = context + "/web/activity/tagmanager/listGameCateTag/"
					+ tagType;
		}
		obj.pageSize = 20;
		obj.serviceName = tagName;
		$("#selectable").html(
				'<img src="' + context + '/resource/image/ajax-loader.gif" />');
		$.ajax({
			type : "POST",
			url : url,
			datatype : "json",
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				$("#selectable").html('');
				if (data.message == 'FAILED') {
					alert('出错啦！~')
					return;
				}
				data = data.page;
				if ((data.content != null && data.content.length != 0)) {
					if (data.content != null && data.content.length != 0) {
						$.each(data.content, function(i, k) {
							selectHtml(k)
						});
					}
				}

			}
		});
	}
}

function listVersionTag() {
	var obj = {};
	obj.curPage = 1;

	obj.pageSize = 20;
	obj.versionName = tagName;
	url = context + "/web/activity/tagmanager/listVersionTag";

	$("#selectable").html(
			'<img src="' + context + '/resource/image/ajax-loader.gif" />');

	$.ajax({
		type : "POST",
		url : url,
		datatype : "json",
		contentType : "application/json",
		data : $.toJSON(obj),
		success : function(data) {
			console.log(data);
			if (data != null && data.message == 'SUCCESS') {
				$("#selectable").html('');
				data = data.page;
				if (data.content != null && data.content.length != 0) {
					$.each(data.content,
							function(i, k) {
								var a = k.tagId;// .replace(new
								// RegExp(",","gm"),"-");
								selectHtml(k)

							});
				}

			}

		}
	});
}

function listChannelTag() {
	var obj = {};
	obj.curPage = 1;

	obj.pageSize = 20;
	obj.channelName = tagName;
	url = context + "/web/activity/tagmanager/listChannelTag";

	$("#selectable").html(
			'<img src="' + context + '/resource/image/ajax-loader.gif" />');

	$.ajax({
		type : "POST",
		url : url,
		datatype : "json",
		contentType : "application/json",
		data : $.toJSON(obj),
		success : function(data) {
			console.log(data);
			if (data != null && data.message == 'SUCCESS') {
				$("#selectable").html('');
				data = data.page;
				if (data.content != null && data.content.length != 0) {
					$.each(data.content,
							function(i, k) {
						selectHtml(k)

							});
				}

			}

		}
	});
}

function pageselectCallback(index, jq) {
	if (pageSign) {
		$("#curPage").val(parseInt(index) + 1);
		searchWord($("#curTab").val());
	}
}

/**
 * 增加一条附加字段
 */
function addExt() {
	var couter = $("#ios_ext_field").find("tr").size();
	var content = "<tr id=\"ios_ext_tr_"
			+ couter
			+ "\" index=\""
			+ couter
			+ "\"><td>键: <input id=\"ios_ext_key_"
			+ couter
			+ "\" type=\"text\" size=\"10\" />&nbsp;值: <input id=\"ios_ext_value_"
			+ couter
			+ "\" type=\"text\" size=\"20\" />&nbsp;<a href=\"javascript:delExt('ios_ext_tr_"
			+ couter + "');\">删除</a></td></tr>";
	$("#ios_ext_field").append(content);
}

/**
 * 
 * 展示一条附加字段 - 修改任务时显示数据库中的附加字段信息
 * 
 * @param k
 *            附加字段 - 键
 * @param v
 *            附加字段 - 值
 */
function showExt(k, v) {
	var couter = $("#ios_ext_field").find("tr").size();
	var content = "<tr id=\"ios_ext_tr_" + couter + "\" index=\"" + couter
			+ "\" ><td>键: <input id=\"ios_ext_key_" + couter
			+ "\" type=\"text\" size=\"10\" value=" + k
			+ " />&nbsp;值: <input id=\"ios_ext_value_" + couter
			+ "\" type=\"text\" size=\"20\" value=" + v
			+ " />&nbsp;<a href=\"javascript:delExt('ios_ext_tr_" + couter
			+ "');\">删除</a></td></tr>";
	$("#ios_ext_field").append(content);
}

/**
 * 删除一条附加字段
 * 
 * @param objName
 *            附加字段id
 */
function delExt(objName) {
	$("#" + objName + "").remove();
}

/**
 * 重置附加字段
 */
function resetExt() {
	$("#ios_ext_field").empty();

	var content = "<tr id=\"ios_ext_tr_0\" index=\"0\"><td>键: <input id=\"ios_ext_key_0\" type=\"text\" size=\"10\" />&nbsp;值: <input id=\"ios_ext_value_0\" type=\"text\" size=\"20\" />&nbsp;<a href=\"javascript:addExt();\">添加</a></td></tr>";
	$("#ios_ext_field").append(content);
}

/**
 * 查找
 */
function searchList() {
	var curTab = $("#curTab").val();
	searchWord(curTab);

	if (1 == curTab) {
		$("#s_jobState").val("");
	}
}

/**
 * 展示查询列表
 * 
 * @param status
 *            分类(1:即时推送列表;0:定时任务列表)
 */
function searchWord(status) {
	var obj = {};
	if (status == '1') {
		$("#jobstate").hide();
		$("#sendstate").show();
		$("#jump").show();
		$("#autoPushType").hide();
		$("#createPush").show();
		obj.pushType = 'IMMEDIATE';
	} else if (status == '0') {
		obj.pushType = 'TIMING';
		$("#sendstate").hide();
		$("#jobstate").show();
		$("#jump").show();
		$("#autoPushType").hide();
		$("#createPush").show();
	} else if (status == '2') {
		obj.pushType = 'AUTO_HISTORY';
		$("#sendstate").hide();
		$("#jobstate").show();
		$("#createPush").hide();

	} else if (status == '3') {
		obj.pushType = 'AUTO';
		$("#sendstate").hide();
		$("#jobstate").show();
		$("#jump").hide();
		$("#autoPushType").show();
		$("#createPush").hide();
	}
	$("#tabs_" + status).html(
			'<img src="' + context + '/resource/image/ajax-loader.gif" />');
	$("#curTab").val(status);
	// 附加字段
	var keyValue = "";
	var len = $("[id^=ios_ext_tr_]").length;
	$("[id^=ios_ext_tr_]").each(
			function(i) {
				var index = $(this).attr("index");
				if ($("#ios_ext_key_" + index).val() == "")
					return true;

				keyValue += $("#ios_ext_key_" + index).val() + ":"
						+ $("#ios_ext_value_" + index).val();
				keyValue += ",";
			});
	keyValue = keyValue.substring(0, keyValue.length - 1);
	keyValue = "{" + keyValue + "}";

	obj.title = $("#s_title").val();
	obj.clientType = $("#s_clientType").val();
	if (status == '0')
		obj.jobState = "" + $("#s_jobState").val();
	if (status == '1')
		obj.sendState = $("#s_sendState").val();
	obj.curPage = "" + $("#curPage").val();
	obj.iosAudioFile = "" + $("#ios_audio_file").val();
	obj.iosFootTag = "" + $("#ios_foot_tag").val();
	obj.keyValue = keyValue;
	obj.platForm = platForm;
	obj.appId = appId;

	$("#curPage").val(1);

	$
			.ajax({
				type : "POST",
				url : context + "/web/activity/push/listPush",
				datatype : "json",
				contentType : "application/json",
				data : $.toJSON(obj),
				success : function(data) {
					if (data != null) {
						if (data.page != null && data.page.content != null
								&& data.page.content.length > 0) {
							// $("#curPage").val(data.page.curPage);
							// $("#pageSize").val(data.page.pageSize);
							$("#tabs_" + status)
									.html(
											'<table width="100%" border="0" cellspacing="0" cellpadding="0" style="text-align:center;"><tr style="font-size:13px;"></tr></table>');
							$("#tabs_" + status + " table tr")
									.append(
											'<th width="5%" background="'
													+ context
													+ '/resource/image/ui/content-bg.gif"><span>ID</span></th>');
							$("#tabs_" + status + " table tr")
									.append(
											'<th width="10%" background="'
													+ context
													+ '/resource/image/ui/content-bg.gif"><span>创建时间</span></th>');
							$("#tabs_" + status + " table tr")
									.append(
											'<th width="5%" background="'
													+ context
													+ '/resource/image/ui/content-bg.gif"><span>内容类型</span></th>');
							$("#tabs_" + status + " table tr")
									.append(
											'<th width="10%" background="'
													+ context
													+ '/resource/image/ui/content-bg.gif"><span>消息标题</span></th>');
							$("#tabs_" + status + " table tr")
									.append(
											'<th width="5%" background="'
													+ context
													+ '/resource/image/ui/content-bg.gif"><span>客户端类型</span></th>');
							if (obj.pushType == 'IMMEDIATE')
								$("#tabs_" + status + " table tr")
										.append(
												'<th width="5%" background="'
														+ context
														+ '/resource/image/ui/content-bg.gif"><span>推送状态</span></th>');
							$("#tabs_" + status + " table tr")
									.append(
											'<th width="5%" background="'
													+ context
													+ '/resource/image/ui/content-bg.gif"><span>用户范围</span></th>');

							$("#tabs_" + status + " table tr")
									.append(
											'<th width="5%" background="'
													+ context
													+ '/resource/image/ui/content-bg.gif"><span>标签</span></th>');

							if (obj.pushType == 'TIMING') {
								$("#tabs_" + status + " table tr")
										.append(
												'<th width="10%" background="'
														+ context
														+ '/resource/image/ui/content-bg.gif"><span>开始时间</span></th>');
								$("#tabs_" + status + " table tr")
										.append(
												'<th width="10%" background="'
														+ context
														+ '/resource/image/ui/content-bg.gif"><span>下次执行时间</span></th>');
								$("#tabs_" + status + " table tr")
										.append(
												'<th width="10%" background="'
														+ context
														+ '/resource/image/ui/content-bg.gif"><span>上次执行时间</span></th>');
								$("#tabs_" + status + " table tr")
										.append(
												'<th width="10%" background="'
														+ context
														+ '/resource/image/ui/content-bg.gif"><span>表达式</span></th>');
							}
							$("#tabs_" + status + " table tr")
									.append(
											'<th width="10%" background="'
													+ context
													+ '/resource/image/ui/content-bg.gif"><span>操作</span></th>');

							$.each(
											data.page.content,
											function(i) {
												var tagss='';
													$.each(this.tags,
															function(i,k) {
//														console.log(k.tagName);
																tagss+=k.tagName+",";
															});
													
													
												if (i % 2 == 0) {
													$(
															"#tabs_" + status
																	+ " table")
															.append(
																	'<tr bgcolor="#f2f2f2"></tr>');
												} else {
													$(
															"#tabs_" + status
																	+ " table")
															.append('<tr></tr>');
												}

												$(
														"#tabs_"
																+ status
																+ " table tr:last")
														.append(
																'<td class="left_txt2">'
																		+ this.id
																		+ '</td>');
												$(
														"#tabs_"
																+ status
																+ " table tr:last")
														.append(
																'<td id="word_'
																		+ this.id
																		+ '" class="left_txt2">'
																		+ this.sendDate
																		+ '</td>');

												this.repl = this.repl == null ? ""
														: this.repl;
												$(
														"#tabs_"
																+ status
																+ " table tr:last")
														.append(
																'<td id="repl_'
																		+ this.id
																		+ '" class="left_txt2">'
																		+ (this.contentType == null ? 'URL'
																				: this.contentType)
																		+ '</td>');
												$(
														"#tabs_"
																+ status
																+ " table tr:last")
														.append(
																'<td id="len_'
																		+ this.id
																		+ '" class="left_txt2">'
																		+ this.title
																		+ '</td>');
												$(
														"#tabs_"
																+ status
																+ " table tr:last")
														.append(
																'<td id="project_'
																		+ this.id
																		+ '" class="left_txt2">'
																		+ this.clientType
																		+ '</td>');
												if (obj.pushType == 'IMMEDIATE')
													$(
															"#tabs_"
																	+ status
																	+ " table tr:last")
															.append(
																	'<td id="type_'
																			+ this.id
																			+ '" class="left_txt2">'
																			+ this.sendState
																			+ '</td>');
												if (obj.pushType == 'AUTO') {
												$(
														"#tabs_"
																+ status
																+ " table tr:last")
														.append(
																'<td  id="type_'
																		+ this.id
																		+ '" class="left_txt2">'
																		+ (this.userScope=='所有人'?'标签':this.userScope)
																		+ '</td>');
												}else
												{
													$(
															"#tabs_"
																	+ status
																	+ " table tr:last")
															.append(
																	'<td  id="type_'
																			+ this.id
																			+ '" class="left_txt2">'
																			+ this.userScope
																			+ '</td>');
												}
												$(
														"#tabs_"
																+ status
																+ " table tr:last")
														.append(
																'<td  id="type_'
																		+ this.id
																		+ '" class="left_txt2">'
																		+ tagss
																		+ '</td>');

												if (obj.pushType == 'TIMING') {
													$(
															"#tabs_"
																	+ status
																	+ " table tr:last")
															.append(
																	'<td id="type_'
																			+ this.id
																			+ '" class="left_txt2">'
																			+ (this.startTime == null ? '无'
																					: this.startTime)
																			+ '</td>');
													$(
															"#tabs_"
																	+ status
																	+ " table tr:last")
															.append(
																	'<td id="nextFireTime_'
																			+ this.id
																			+ '" class="left_txt2">'
																			+ (this.nextFireTime == null ? '已执行'
																					: this.nextFireTime)
																			+ '</td>');
													$(
															"#tabs_"
																	+ status
																	+ " table tr:last")
															.append(
																	'<td id="type_'
																			+ this.id
																			+ '" class="left_txt2">'
																			+ (this.previousFireTime == null ? '无'
																					: this.previousFireTime)
																			+ '</td>');
													$(
															"#tabs_"
																	+ status
																	+ " table tr:last")
															.append(
																	'<td id="type_'
																			+ this.id
																			+ '" class="left_txt2">'
																			+ (this.cronExpression == null ? '无'
																					: this.cronExpression)
																			+ '</td>');
												}

												$(
														"#tabs_"
																+ status
																+ " table tr:last")
														.append(
																'<td class="left_txt2"></td>');

												if (obj.pushType == 'TIMING') {
													if ('启用' == this.jobState) {
														$(
																"#tabs_"
																		+ status
																		+ " table tr:last td:last")
																.append(
																		'<a style="color:blue;" href="javascript:updateStatus(\''
																				+ this.id
																				+ '\', \'DISABLE\');">停用</a>&nbsp;&nbsp;&nbsp;');

													} else if ('暂停' == this.jobState) {
														$(
																"#tabs_"
																		+ status
																		+ " table tr:last td:last")
																.append(
																		'<a style="color:blue;" href="javascript:updateStatus(\''
																				+ this.id
																				+ '\', \'ENABLE\');">启用</a>&nbsp;&nbsp;&nbsp;');

													}
												}

												if (obj.pushType == 'AUTO') {
													if ('启用' == this.jobState) {
														$(
																"#tabs_"
																		+ status
																		+ " table tr:last td:last")
																.append(
																		'<a style="color:blue;" href="javascript:updateAutoPushStatus(\''
																				+ this.id
																				+ '\', \'DISABLE\');">停用</a>&nbsp;&nbsp;&nbsp;');

													} else if ('暂停' == this.jobState) {
														$(
																"#tabs_"
																		+ status
																		+ " table tr:last td:last")
																.append(
																		'<a style="color:blue;" href="javascript:updateAutoPushStatus(\''
																				+ this.id
																				+ '\', \'ENABLE\');">启用</a>&nbsp;&nbsp;&nbsp;');

													}
												}

												if (obj.pushType != 'AUTO_HISTORY') {
													if (obj.pushType != 'AUTO') {
												$(
														"#tabs_"
																+ status
																+ " table tr:last td:last")
														.append(
																'<a style="color:blue;" href="javascript:deleteWord(\''
																		+ this.id
																		+ '\','
																		+ status
																		+ ');">删除</a>&nbsp;&nbsp;&nbsp;');
													}

												if (obj.pushType == 'AUTO') {
													$(
															"#tabs_"
																	+ status
																	+ " table tr:last td:last")
															.append(
																	'<a style="color:blue;" href="javascript:editWord(\'configAutoPush\', \''
																			+ this.id
																			+ '\');">配置</a>');
												} else 	if (obj.pushType == 'TIMING') {
													$(
															"#tabs_"
																	+ status
																	+ " table tr:last td:last")
															.append(
																	'<a style="color:blue;" href="javascript:editWord(\'updateJob\', \''
																			+ this.id
																			+ '\');">修改任务</a>');
												
												}else{
													$(
															"#tabs_"
																	+ status
																	+ " table tr:last td:last")
															.append(
																	'<a style="color:blue;" href="javascript:editWord(\'sendAgain\', \''
																			+ this.id
																			+ '\');">再次发送</a>');
												}
											}
											
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
			});
}

function keyValue() {
	var map = {};

	var jump = $("input[name='jump']:checked").val();
	if (jump == 'URL') {
		var jurl = $('#jumpUrl').val();
		if (jurl == '') {
			alert('请填写跳转url！');
			return 'false';
		}
		map.URL = 'http://' + jurl;
	} else {
		var cid = $('#contentId').val();
		var type = $('#type').val();
		var source = $('#source').val();
		var title = $('#contentName').val();
		if (cid == '') {
			alert('请填写跳转内容的ID！！！');
			return 'false';
		}
		if (type == '999') {
			{
				alert('请选择跳转内容的类型！！！');
				return 'false';
			}
		}
		console.log(type);
		if (type == 1 || type == 3)
			if (source == '999') {
				{
					alert('请选择内容来源！！！');
					return 'false';
				}
			}
		if (title == '') {
			alert('请填写跳转内容的名称！！！');
			return 'false';
		}
		map.i = cid;
		map.t = title;
		map.p = type;
		map.s = source;
	}
	return map;
}

function editWord(type, id) {
	tagMap=null;
	crontab.cron("value", "0 0 1 1 *");
	var title = '推送信息';
	var buttons = {};
	buttons["取消"] = function() {
		$(this).dialog("close");
	};
	if (type == 'new') {
		title = '创建推送任务';
		resetTitle();
		resetContent();
		resetSendType();
		resetDevice();
		resetExt();
		buttons["创建"] = function() {
			var obj = getParObj();
			obj.cronExp = $("#cron").text();
			$("#wordDiv").mask("Waiting...");
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
							alert("创建成功");

						} else {
							alert("创建失败-" + data.message);
						}
						$("#wordDiv").unmask();
						$("#wordDiv").dialog("close");
						searchWord($("#curTab").val());
					}

				}
			});
		};
	} else {
		// 控制发送方式选择和cron生成器的显示和隐藏
		if (type == 'updateJob') {
			$("#deviceAndroid").attr("disabled", 'disabled');
			$("#deviceIos").attr("disabled", 'disabled');
			if ($("#nextFireTime_" + id).text() == '已执行') {
				alert('该任务已经执行完成，无法修改!');
				return;
			}
			title = '修改任务';
			resetSendTypeByUpdateJob();
			$('#sendType').show();
		} else if (type == 'sendAgain') {
			$("#deviceAndroid").attr("disabled", 'disabled');
			$("#deviceIos").attr("disabled", 'disabled');
			$("#clientSelect").attr("disabled", 'disabled');
			title = '再次发送';
			resetSendTypeBySendAgain();
			$('#sendType').show();
		} else if (type == 'configAutoPush') {
			title = '保存配置';
			resetSendTypeBySendAgain();
			resetDevice();
			$('#autoPushType').hide();
			$('#sendType').hide();
		}
		resetTitle();
		resetContent();
//		resetDevice();
		resetExt();
		$
				.ajax({
					type : "POST",
					url : context + "/web/activity/push/get?id=" + id,
					datatype : "json",
					success : function(data) {
						// $("#tagId").val(tagId);
						console.log(data);
						if (data != null) {
							if (data.message == 'SUCCESS') {
								$("#"+data.push.tagRelation).attr("checked","checked");
								$("#tagId").val(data.push.tags);
								$("#tagStr").text(data.push.tags);
								$('#cron').text(data.push.cronExpression);
								if(data.push.clientType=='ALL'){
									$('input[name="device"]')
									.attr("checked", "checked");
								}else if(data.push.clientType=='ANDROID'){
									$('input[name="device"][value="ANDROID"]')
											.attr("checked", "checked");
									$('input[name="device"][value="IOS"]')
									.attr("checked", false);
								}else{
									$('input[name="device"][value="ANDROID"]')
									.attr("checked", false);
									$('input[name="device"][value="IOS"]')
									.attr("checked", "checked");
								}

								var id_array = new Array();
								$('input[name="device"]:checked').each(
										function() {
											id_array.push($(this).val());// 向数组中添加元素
										});

								$("#ios_ext_field")
										.html(
												'<tr id="ios_ext_tr_0" index="0"><td>键: <input id="ios_ext_key_0" type="text" size="10" />&nbsp;值: <input id="ios_ext_value_0" type="text" size="20" />&nbsp;<a href="javascript:addExt();">添加</a></td></tr>');
								if (data.push.keyValue != null) {
									for ( var k in data.push.keyValue) {
										var v = data.push.keyValue[k];
										if (k == 'i') {
											$('#contentId').val(v);
											$("#cid").attr("checked", '2');
										} else if (k == 'p') {
											$("#type option[value='" + v + "']")
													.attr("selected", true);
										} else if (k == 's') {
											$("#source option[value='"+ v + "']").attr(
													"selected", true);
										} else if (k == 't') {
											$('#contentName').val(v);
										} else if (k == 'URL') {
											var n = v.split('//')[1];
											$('#jumpUrl').val(n);
											$("#jurl").attr("checked", '2');
										}
									}
								}
								var str='';
								tagMap=[];
								$.each(data.push.tags,function(i,d){
									var obj={};
									obj.tagId=d.tagId;
									obj.tagName=d.tagName;
									tagMap.push(obj);
									str+=d.tagName+",";
								});
								$("#tagStr").text(str);
								$("#type2 option[v='" + data.push.contentType+ "']")
								.attr("selected", true);
								$("#type2").attr("disabled", 'disabled');
								$("#wordDiv #title").val(data.push.title);
								$("#wordDiv #content").val(data.push.content);
								$("#timing").attr("disabled", 'disabled');
								$("#immediate").attr("disabled", 'disabled');
								$("#auto").attr("disabled", 'disabled');
								$(
										"input[name=send][value="
												+ data.push.pushType + "]")
										.attr("checked", true);
							} else {
								alert("获取失败-" + data.message);
							}
						}

					}
				});
		if (type == 'sendAgain' || type == 'configAutoPush') {
			var na = '';
			if (type == 'configAutoPush')
				na = "保存配置";
			else
				na = "立即发送";
			buttons[na] = function() {

				var obj = getParObj();
				obj.id = id;
				console.log(obj)
				$.ajax({
					type : "POST",
					url : context + "/web/activity/push/sendAgain",
					datatype : "json",
					async : false,
					contentType : "application/json",
					data : $.toJSON(obj),
					success : function(data) {
						if (data != null) {
							if (data.message == 'SUCCESS') {
								alert("成功");
								$("#wordDiv").dialog("close");
								searchWord(parseInt($("#curTab").val()));
							} else {
								alert("失败-" + data.message);
							}
						}
						$("#wordDiv").unmask();

					}
				});
			};
		}
		if (type == 'updateJob') {
			buttons["保存修改"] = function() {
				var obj = getParObj();
				obj.id = id;
				obj.cronExp = $("#cron").text();
				$.ajax({
					type : "POST",
					url : context + "/web/activity/push/update",
					datatype : "json",
					async : false,
					contentType : "application/json",
					data : $.toJSON(obj),
					success : function(data) {
						if (data != null) {
							if (data.message == 'SUCCESS') {
								alert("修改推送信息成功");
								$("#wordDiv").dialog("close");
								searchWord(parseInt($("#curTab").val()));
							} else {
								alert("修改推送信息失败-" + data.message);
							}
						}
					}
				});
			};
		}
	}

	$("#wordDiv").dialog({
		title : title,
		width : 680,
		height : 850,
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

function getParObj() {
	var pushTitle = commonUtils.trim($("#wordDiv #title").val());
	if (pushTitle.length == 0) {
		alert("请输入推送标题");
		$("#wordDiv #title").focus();
		return;
	} else if (pushTitle.length > 20) {
		alert("推送标题只允许20个字符");
		$("#wordDiv #title").focus();
		return;
	}
	var pushContent = commonUtils.trim($("#wordDiv #content").val());
	if (pushContent.length == 0) {
		alert("请输入推送消息内容");
		$("#wordDiv #content").focus();
		return;
	} else if (pushContent.length > 50) {
		alert("推送消息内容只允许50个字符");
		$("#wordDiv #content").focus();
		return;
	}
	var obj = {};
	if(platForm=='XINGE'){
	var re = /^(\d{1,2}|100)$/;
    if (!re.test($('#sh').val())||$('#sh').val()>23)
   {
       alert("时段格式有误!");
       return;
   }
    if (!re.test($('#eh').val())||$('#eh').val()>23)
    {
        alert("时段格式有误!");
        return;
    }

    if (!re.test($('#sm').val())||$('#sm').val()>59)
    {
        alert("时段格式有误!");
        return;
    }
    if (!re.test($('#em').val())||$('#em').val()>59)
    {
        alert("时段格式有误!");
        return;
    }
    var interval={'sh':$('#sh').val(),"sm":$('#sm').val(),"eh":$('#eh').val(),"em":$('#em').val()};
    obj.interval=interval;
	}
	var clientType = null;
	if ($('input[name="device"]:checked').length == 1)
		clientType = $('input[name="device"]:checked').val();
	else
		clientType = "ALL";
	var map = null;
	if ($("#curTab").val() != 3) {
		map = keyValue();
		if (map == 'false')
			return;
	}
	var jump = $("input[name='jump']:checked").val();
	obj.title = pushTitle;
	obj.content = pushContent;
	obj.clientType = clientType;
	if(tagMap!=null)
		obj.tags = tagMap;
	if ($("#curTab").val() == 3)
		obj.pushType='AUTO';
	else
		obj.pushType = $('input:radio[name="send"]:checked').val();
	obj.platForm = platForm;
	obj.appId = appId;
	if(map!=null)
		obj.keyValue = map;
	if ($("#curTab").val() == 3) {
		obj.contentTy = $("#type2").find("option:selected").attr('v');
	} else {
		if (jump != 'URL') {
			obj.contentTy = $("#type").find("option:selected").attr('v');
		}
	}
	obj.tagRelation=$("input[name='tagRelation']:checked").val();
	return obj;
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
						searchWord(0);
					} else {
						alert("修改失败：" + data.message);
					}
				}
			}
		});
	}
}
function updateAutoPushStatus(id, status) {
	if (confirm("是否确定修改任务状态？")) {
		$.ajax({
			type : "POST",
			url : context + "/web/activity/push/autoPushStatus?id="+id,
			datatype : "json",
			data : {
				status : status
			},
			async : false,
			success : function(data) {
				if (data != null) {
					if (data.message == 'SUCCESS') {
						alert("修改成功");
						searchWord(3);
					} else {
						alert("修改失败：" + data.message);
					}
				}
			}
		});
	}
}

function deleteWord(id, type) {
	if (confirm("您确定要删除这项推送任务吗？")) {
		$
				.ajax({
					type : "POST",
					url : context + "/web/activity/push/" + id + "/" + type
							+ "/delete",
					datatype : "json",
					async : false,
					success : function(data) {
						if (data != null) {
							if (data.message == 'SUCCESS') {
								alert("任务删除成功");
								searchWord(type);
							} else {
								alert("任务删除失败-" + data.message);
							}
						}
					}
				});
	}
}

/**
 * 重置发送方式和cron - 当创建任务时
 */
function resetSendType() {
	$("#timing").removeAttr("disabled");
	$("#immediate").removeAttr("disabled");
	$("#immediate").attr("checked", 'true');

	$("#cronTabs").hide();
	$("#cronDiv").hide();
	// $("#cron").text("");
}

/**
 * 重置发送方式和cron - 当修改任务时
 */
function resetSendTypeByUpdateJob() {
	$("#cron").text("");
	$("#cronTabs").show();
	$("#cronDiv").show();
	$("#timing").removeAttr("disabled");
	$("#timing").attr("checked", 'true');
	$("#immediate").attr("disabled", 'disabled');
	
}

/**
 * 重置发送方式和cron - 当再次发送时
 */
function resetSendTypeBySendAgain() {
	$("#cronTabs").hide();
	$("#cronDiv").hide();

	$("#immediate").removeAttr("disabled");
	$("#immediate").attr("checked", 'true');

	$("#timing").attr("disabled", 'disabled');

}

/**
 * 重置推送标题
 */
function resetTitle() {
	$("#wordDiv #title").val("");
	$("#tagId").val("");
	$("#tagStr").text("");
}

/**
 * 重置消息内容
 */
function resetContent() {
	$("#wordDiv #content").val("");
	$("#contentId").val("");
	$("#contentName").val("");
	$("#jumpUrl").val("");
	$("#tagStr").text("");

}

/**
 * 重置设备选项
 */
function resetDevice() {
	$("#deviceAndroid").removeAttr("disabled");
	$("#deviceIos").removeAttr("disabled");
}


function removeTag() {
	$("#tagId").val('');
	$("#tagStr").text('');
	tagMap=[];
}