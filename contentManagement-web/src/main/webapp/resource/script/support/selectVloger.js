var selectVloger = {
  pageSign : false,
  funName : '',
  contextPath : '',
  relaId : 0,
  show : function(funName, contextPath, relaList, relaId) {
    selectVloger.funName = funName;
    selectVloger.relaId = relaId;
    selectVloger.contextPath = contextPath;
  
      if(! $("#selectVlogerDiv").length > 0) {
        $("body").append('<div id="selectVlogerDiv" align="center"></div>');
        $("#selectVlogerDiv").append('<table width="100%" border="0" cellspacing="0" cellpadding="0" style="text-align:left; font-size:12px;"></table>');
        $("#selectVlogerDiv table").append('<tr><td class="left_txt2" align="center" colspan="4"></td></tr>');
        $("#selectVlogerDiv table tr:last td:last").append('名称：<input id="vlogerName" type="text" size="10" />&nbsp;');
        $("#selectVlogerDiv table tr:last td:last").append('<input type="button" value="查找" onClick="selectVloger.searchVloger(false)" />');
        $("#selectVlogerDiv table").append('<tr><td class="left_bt2" colspan="4"><hr style="color:#FFFFFF;" /><input type="hidden" id="curPage" value="1" /><input type="hidden" id="pageSize" value="4" /></td></tr>');
        $("#selectVlogerDiv table").append('<tr id="result"><td class="left_bt2" colspan="4" align="center"></td></tr>');
        $("#selectVlogerDiv table").append('<tr><td class="left_bt2" colspan="4"><hr style="color:#FFFFFF;" /></td></tr>');
        $("#selectVlogerDiv table").append('<tr><td class="left_txt2" colspan="4" align="center">拖拽下方添加，拖拽垃圾箱删除<div style="float:right;" id="pagination" class="pagination"></div></td></tr>');
        $("#selectVlogerDiv table tr:last td:last").append('<div id="recycle" style="float:left; margin-top:3px; margin-left:10px;"><img src="' + contextPath + '/resource/image/recycle.png" width="30" height="30" /></div>');
        $("#selectVlogerDiv").append('<table id="container" width="344px" height="260px" border="0" cellspacing="0" cellpadding="0" style="font-size:12px;"></table>');
        $("#selectVlogerDiv #container").append('<tr height="1px"><td class="left_bt2"><hr style="color:#FFFFFF;" /></td></tr>');
        $("#selectVlogerDiv #container").append('<tr height="100%"><td class="left_bt2" style="text-align:center;"><div id="dropp_div" style="text-align:center; height:100%; list-style-type:none; margin:0; padding:0;"><ul id="sortable1" class="ui-sortable" unselectable="on" style="list-style-type:none;margin:0px;padding:0px;"/><br /><br /><br /></div></td></tr>');
        $('#selectVlogerDiv').dialog({
          title : '相关播主',
          width : 400,
          height : 560,
          focus : true,
          modal : true,
          draggable : false,
          autoOpen : false,
          resizable : false,
          close : function() {
            $("#selectVlogerDiv").dialog('destroy');
            $("#selectVlogerDiv").remove();
          },
          open : function() {
            $("#selectVlogerDiv #dropp_div").droppable({
              addClasses : false,
              accept : '.dragg_div',
              drop : function(event, ui) {
                var vlogerId = ui.draggable.attr("id").replace("li_", "");
                if(! $("#selectVlogerDiv #dropp_div #sortable1 #vloger_" + vlogerId).length > 0) {
                  var vlogerName = ui.draggable.attr("title");
                  $("#selectVlogerDiv #dropp_div ul").append('<li title="' + vlogerName + '" id="vloger_' + vlogerId  + '" class="ui-state-default dragg_vloger dropp_vloger"  style="width: 80px;height: 50px;list-style: none;text-align:center;float:left;">' + commonUtils.subStr4view(vlogerName, 10, "..") + '</li>');
                  //$("#selectVlogerDiv #dropp_div").append('<div title="' + vlogerName + '" id="vloger_' + vlogerId + '" class="dragg_vloger dropp_vloger" style="margin:4px 4px 0 0; padding:1px; float:left; line-height:14px;"></div>');
                  //$("#selectVlogerDiv #dropp_div #vloger_" + vlogerId).append('<img src="' + ui.draggable.children("img").attr("src") + '" width="80" height="80" style="float:left;"/><br />');
                }
              }
            });
            $("#selectVlogerDiv #recycle").droppable({
              addClasses : false,
              accept : '.dropp_vloger',
              drop : function(event, ui) {
                if(confirm("您确定删除关联播主“" + ui.draggable.attr("title") + "”吗？")) {
                  ui.draggable.remove();
              }
              }
            });
            $("#selectVlogerDiv #dropp_div #sortable1 .dropp_vloger").draggable({
              addClasses : false
            });
            $("#selectVlogerDiv #dropp_div  #sortable1").sortable();
            if(relaList != null && relaList.length > 0) {
              $.each(relaList, function(i) {
            	  var vlogerName = selectVloger.getVlogerNameById(this.vlogerId);
            	$("#selectVlogerDiv #dropp_div ul").append('<li id="vloger_' + this.vlogerId + '" title="' + vlogerName + '"  class="ui-state-default dragg_vloger dropp_vloger"  style="width: 80px;height: 50px;list-style: none;text-align:center;float:left;">'+commonUtils.subStr4view(vlogerName, 10, "..")+'</li>');
              });
            }
          },
          buttons : {
            "关闭" : function() {
              $("#selectVlogerDiv").dialog("close");
            },
            "保存" : function() {
              if(selectVloger.funName != '') {
                var fun = eval(selectVloger.funName);
                var obj = new Array();
                $("#selectVlogerDiv #dropp_div .dropp_vloger").each(function(i) {
                  var id = this.id.replace("vloger_", "");
                  $.merge(obj, [id]);
                });
                fun(selectVloger.relaId, obj);
                $("#selectVlogerDiv").dialog("close");
              }
            }
          }
        });
      }
      $('#selectVlogerDiv').dialog("open");
  },

  getVlogerNameById : function(id){
	  var vlogerName ="";
	  $.ajax({
	      type : "GET",
	      url : selectVloger.contextPath + "/web/support/vloger/"+id,
	      datatype : "json",
	      contentType : "application/json",
	      async: false,
	      success : function(data) {
	        if(data != null) {
	            if(data != null) {
	            	vlogerName = data.vloger.name;
	            }
	        }
	      }
	    }); 
	  return vlogerName;
  },
  searchVloger : function(page) {
    var name = commonUtils.trim($("#selectVlogerDiv #vlogerName").val());
    if(commonUtils.len(name) == 0) {
      alert("请输入播主名称");
      $("#selectVlogerDiv #vlogerName").focus();
      return;
    }
    $("#selectVlogerDiv #result td").empty();
    $("#selectVlogerDiv #result td").append('<img src="' + selectVloger.contextPath + '/resource/image/ajax-loader.gif" />');
    var obj = {};
    obj.name = name;
    obj.status = "-1";
    if(page) {
      obj.curPage = "" + $("#selectVlogerDiv #curPage").val();
    }
    else {
      obj.curPage = "1";
    }
    obj.status = ""+1;
    obj.pageSize = "" + $("#selectVlogerDiv #pageSize").val();
    $.ajax({
      type : "POST",
      url : selectVloger.contextPath + "/web/support/vloger",
      datatype : "json",
      contentType : "application/json",
      data : $.toJSON(obj),
      success : function(data) {
        if(data != null) {
            if(data.page != null && data.page.content != null && data.page.content.length > 0) {
              $("#selectVlogerDiv #curPage").val(data.page.curPage);
              $("#selectVlogerDiv #result td").empty();
              $("#selectVlogerDiv #result td").append('<ul id="sortable1" class="ui-sortable" unselectable="on" style="list-style-type: none;margin:0px;padding:0px;"/>');
              $.each(data.page.content, function(i) {
                $("#selectVlogerDiv #result td ul").append('<li title="' + this.name + '" id="li_' + this.id + '" class="ui-state-default dragg_div"  style="width: 80px;height: 50px;list-style: none;text-align:center;float:left;">' +commonUtils.subStr4view(this.name, 10, "..") + '</li>');
              });
              for(var i = 0; i < (4 - data.page.content.length); i ++) {
                  $("#selectVlogerDiv #result td ul").append('<li class="ui-state-default"  style="width: 80px;height: 50px;list-style: none;text-align:center;float:left;"></li>');
              }
              $("#selectVlogerDiv #result .dragg_div").draggable({
                addClasses : false,
                containment : "#selectVlogerDiv",
                helper : "clone",
                revert : "invalid"
              });
              selectVloger.pageSign = false;
              $("#selectVlogerDiv #pagination").pagination(data.page.rowCount, {
                callback : selectVloger.pageselectCallback,
                items_per_page : data.page.pageSize,
                num_display_entries : 0,
                num_edge_entries : 1,
                current_page : data.page.curPage - 1,
                prev_text : "上一页",
                next_text : "下一页"
              });
              selectVloger.pageSign = true;
            }
            else {
              $("#selectVlogerDiv #result td").empty();
              $("#selectVlogerDiv #result td").append('<p>无符合条件数据记录</p>');
            }
        }
      }
    });
  },
  pageselectCallback : function(index, jq) {
    if(selectVloger.pageSign) {
      $("#selectVlogerDiv #curPage").val(parseInt(index) + 1);
      selectVloger.searchVloger(true);
    }
  },
}