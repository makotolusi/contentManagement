$(document).ready(function() {
	$("#tabs1").tabs();
	searchWord(1);
});
function searchWord(status) {
	$("#tabs_" + status).html(
			'<img src="'+context+'/resource/image/ajax-loader.gif" />');
	$("#curTab").val(status);
	var obj = {};
	obj.curPage = "" + $("#curPage").val();
	obj.serviceName=$("#s_title").val();
	var url = '';
	if (status == '1') {
		url = context + "/web/activity/appSelect/listApp";
		$.ajax({
			type : "POST",
			url : url,
			datatype : "json",
			contentType : "application/json",
			data : $.toJSON(obj),
			success : function(data) {
				console.log(data);
				colName = [ '应用ID', '应用名称', '平台类型','AppKey_android', 'SecretKey_android','AppKey_ios', 'SecretKey_ios', '操作'];
				colWitdh = [ '5%','5%', '5%', '5%', '5%','5%','5%','5%'];
				colKey = [ 'appId','name','pushPlatForm', 'appKey','secretKey', 'appKey_ios','secretKey_ios' ];
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
								var par='';
								$.each(colKey,function(i, j) {
								
									var text='';
									if(j.indexOf('.')>=0){
										text=obj[j.split('.')[0]][j.split('.')[1]]
									}else
										text=obj[j];
									par+=j+"="+text+"&";
									
									if(text==null)
										text="";
									$("#tabs_" + status + " table tr:last").append(
											'<td class="left_txt2">' + text
													+ '</td>');
									
								});
								$("#tabs_" + status + " table tr:last").append(
										'<td class="left_txt2"><a style="color:blue;" href="push?'+par+'">进入</a></td>');
								
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

function pageselectCallback(index, jq) {
	if (pageSign) {
		$("#curPage").val(parseInt(index) + 1);
		
		searchWord($("#curTab").val());
	}
}
