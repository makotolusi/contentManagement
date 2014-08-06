var selectPic = {
  pageSign : false,
  funName : '',
  contextPath : '',
  show : function(funName, contextPath) {
    selectPic.funName = funName;
    selectPic.contextPath = contextPath;
    if(! $("#selectPicDiv").length > 0) {
      $("body").append('<div id="selectPicDiv"></div>');
      $("#selectPicDiv").append('<table width="100%" border="0" cellspacing="0" cellpadding="0" style="text-align:left; font-size:12px;"></table>');
      $("#selectPicDiv table").append('<tr><td class="left_txt2" align="center" colspan="2"></td></tr>');
      $("#selectPicDiv table tr:last td:last").append('名称：<input id="picName" type="text" size="10" />&nbsp;<input type="button" value="查找" onClick="selectPic.searchPic(false)" />');
      $("#selectPicDiv table").append('<tr><td class="left_bt2" colspan="2"><hr style="color:#FFFFFF;" /><input type="hidden" id="curPage" value="1" /><input type="hidden" id="pageSize" value="4" /></td></tr>');
      $("#selectPicDiv table").append('<tr></tr>');
      $("#selectPicDiv table").append('<tr></tr>');
      $("#selectPicDiv table").append('<tr><td class="left_bt2" colspan="2"><hr style="color:#FFFFFF;" /></td></tr>');
      $("#selectPicDiv table").append('<tr><td class="left_txt2" align="right" colspan="2"><div id="pagination" class="pagination"></div></td></tr>');
      $('#selectPicDiv').dialog({
        title : '图片选择',
        width : 240,
        height : 330,
        focus : true,
        modal : true,
        draggable : false,
        autoOpen : false,
        resizable : false,
        close : function() {
          $("#selectPicDiv").dialog('destroy');
          $("#selectPicDiv").remove();
        },
        open : function() {
        },
        buttons : {
          "关闭" : function() {
            $("#selectPicDiv").dialog("close");
          }
        }
      });
    }
    $('#selectPicDiv').dialog("open");
  },
  searchPic : function(page) {
    var name = commonUtils.trim($("#selectPicDiv #picName").val());
    if(commonUtils.len(name) == 0) {
      alert("请输入图片名称");
      $("#selectPicDiv #picName").focus();
      return;
    }
    $("#selectPicDiv table tr:last").prev("tr").prev("tr").prev("tr").empty();
    $("#selectPicDiv table tr:last").prev("tr").prev("tr").empty().append('<td class="left_txt2" align="center" colspan="2"><img src="' + selectPic.contextPath + '/resource/image/ajax-loader.gif" /></td>');
    var obj = {};
    obj.name = name;
    if(page) {
      obj.curPage = "" + $("#selectPicDiv #curPage").val();
    }
    else {
      obj.curPage = "1";
    }
    obj.pageSize = "" + $("#selectPicDiv #pageSize").val();
    $.ajax({
      type : "POST",
      url : selectPic.contextPath + "/web/support/picture",
      datatype : "json",
      contentType : "application/json",
      data : $.toJSON(obj),
      success : function(data) {
        if(data != null) {
          if(data.message == 'SUCCESS') {
            if(data.page != null && data.page.content != null && data.page.content.length > 0) {
              $("#selectPicDiv #curPage").val(data.page.curPage);
              $("#selectPicDiv table tr:last").prev("tr").prev("tr").empty();
              $("#selectPicDiv table tr:last").prev("tr").prev("tr").append('<td class="left_txt2" align="center"></td><td class="left_txt2" align="center"></td>');
              $("#selectPicDiv table tr:last").prev("tr").prev("tr").prev("tr").append('<td class="left_txt2" align="center"></td><td class="left_txt2" align="center"></td>');
              $.each(data.page.content, function(i) {
                if(i == 0) {
                  $("#selectPicDiv table tr:last").prev("tr").prev("tr").prev("tr").children("td:first").append('<img ondblclick="selectPic.picSelected(' + this.id + ', this)" src="' + selectPic.picUrl(this) + '" width="80" height="80" />');
                }
                else if(i == 1) {
                  $("#selectPicDiv table tr:last").prev("tr").prev("tr").prev("tr").children("td:last").append('<img ondblclick="selectPic.picSelected(' + this.id + ', this)" src="' + selectPic.picUrl(this) + '" width="80" height="80" />');
                }
                else if(i == 2) {
                  $("#selectPicDiv table tr:last").prev("tr").prev("tr").children("td:first").append('<img ondblclick="selectPic.picSelected(' + this.id + ', this)" src="' + selectPic.picUrl(this) + '" width="80" height="80" />');
                }
                else {
                  $("#selectPicDiv table tr:last").prev("tr").prev("tr").children("td:last").append('<img ondblclick="selectPic.picSelected(' + this.id + ', this)" src="' + selectPic.picUrl(this) + '" width="80" height="80" />');
                }
              });
              for(var i = 0; i < (4 - data.page.content.length); i ++) {
                if(i == 0) {
                  $("#selectPicDiv table tr:last").prev("tr").prev("tr").children("td:last").append('<img src="" width="80" height="80" />');
                }
                else if(i == 1) {
                  $("#selectPicDiv table tr:last").prev("tr").prev("tr").children("td:first").append('<img src="" width="80" height="80" />');
                }
                else if(i == 2) {
                  $("#selectPicDiv table tr:last").prev("tr").prev("tr").prev("tr").children("td:last").append('<img src="" width="80" height="80" />');
                }
              }
              selectPic.pageSign = false;
              $("#selectPicDiv #pagination").pagination(data.page.rowCount, {
                callback : selectPic.pageselectCallback,
                items_per_page : data.page.pageSize,
                num_display_entries : 0,
                num_edge_entries : 1,
                current_page : data.page.curPage - 1,
                prev_text : "上一页",
                next_text : "下一页"
              });
              selectPic.pageSign = true;
            }
            else {
              $("#selectPicDiv table tr:last").prev("tr").prev("tr").children("td").empty();
              $("#selectPicDiv table tr:last").prev("tr").prev("tr").children("td").append('<p>无符合条件数据记录</p>');
            }
          }
          else {
            $("#selectPicDiv table tr:last").prev("tr").prev("tr").children("td").empty();
            $("#selectPicDiv table tr:last").prev("tr").prev("tr").children("td").append('<p>获取图片列表出错-' + data.message + '</p>');
          }
        }
      }
    });
  },
  pageselectCallback : function(index, jq) {
    if(selectPic.pageSign) {
      $("#selectPicDiv #curPage").val(parseInt(index) + 1);
      selectPic.searchPic(true);
    }
  },
  picUrl: function(pic) {
    /*var url = "http://i1.cdn.test.17173.com/sdnsoe";
   // url += pic.createDate.split("-")[0];
    url += "/pic/cms/";
    url += pic.createDate.split(" ")[0].replaceAll("-", "/");
    url += "/" + pic.id;
    url += "." + pic.suffix;*/
    var url = pic.picUrl;
    return url;
  },
  picSelected : function(picId, imgObj) {
    if(selectPic.funName != '') {
      var fun = eval(selectPic.funName);
      fun(picId, imgObj.src);
      $("#selectPicDiv").dialog("close");
    }
  }
}