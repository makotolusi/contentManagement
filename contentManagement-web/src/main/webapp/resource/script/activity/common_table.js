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
