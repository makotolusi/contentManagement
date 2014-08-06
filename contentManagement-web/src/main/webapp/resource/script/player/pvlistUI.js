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
	searchWord('PlayerPvList');
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
	$("#dFrom").val("");
	$("#dTo").val("");
	$("#appId").val("");
	$("#appId2").val("");    
}

/**
 * 展示查询列表
 * 
 * @param status
 *            
 */
function searchWord(status,type) {
	$("#tabs_" + status).html(
			'<img src="'+context+'/resource/image/ajax-loader.gif" />');
	$("#curTab").val(status);
	var obj = {};
	obj.curPage = "" + $("#curPage").val();
	obj.dateFrom = "" + $("#dFrom").val();
	obj.dateTo = "" + $("#dTo").val();
	obj.appid = "" + $("#appId").val();
	obj.appid2 = "" + $("#appId2").val();
	console.log(obj);
	$("#curPage").val(1);
		obj.collectionName =status;
	var url = '';
	if (status == '1') {
		url = context + "/web/player/pv/listPlayerLog";
		$.ajax({
			type : "POST",
			url : url,
			datatype : "json",
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				console.log(data);
				colName = [ 'appid', 'app名称','类型', '用户id','版本号&机型', '播放时长', '视频总时长', '视频url','码率', '分辨率', '总格式', '音频编码格式', '视频编码格式', '视频颜色空间', '参考帧数（仅AVC）', 'profile等级（仅AVC）','上传时间'];
				colWitdh = [ '5%', '10%', '5%', '5%','5%','5%','5%','10%', '5%', '5%','5%', '5%', '5%', '5%', '5%','5%','10%' ];
				colKey = [ 'appid','appname', 'type','uid','ua','playtime','duration','src', 'br', 'res', 'fmt', 'afmt', 'vfmt', 'vpixfmt', 'vrefs','vprofile','lastUpdate' ];
				tableHtml(status,data,colName,colWitdh,colKey);
			}
		});

	}
	else if(status =='PlayerPvList') {
		url = context + "/web/player/pv/listPv";
		$.ajax({
					type : "POST",
					url : url,
					datatype : "json",
					contentType : "application/json",
					data : $.toJSON(obj),
					success : function(data) {
						console.log(data);
						colName = [ 'appid','视频播放次数', '视频播放用户数', '视频播放时长(秒)','日期'];
						colWitdh = [ '20%', '20%', '20%','20%','20%'];
						colKey = [ 'appid', 'pv','uv', 'time','statdate'];
						tableHtml(status,data,colName,colWitdh,colKey);
					}
				});
	}
	else if(status =='PlayerAvList') {
		url = context + "/web/player/pv/listAv";
		$.ajax({
					type : "POST",
					url : url,
					datatype : "json",
					contentType : "application/json",
					data : $.toJSON(obj),
					success : function(data) {
						console.log(data);
						colName = [ 'appid','视频播放次数', '视频播放用户数', '视频播放时长(秒)','日期'];
						colWitdh = [ '20%', '20%', '20%','20%','20%'];
						colKey = [ 'appid', 'pv','uv', 'time','statdate'];
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
