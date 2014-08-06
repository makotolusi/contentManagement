var selectApp = {
  pageSign : false,
  funName : '',
  contextPath : '',
  relaId : 0,
  platList : new Array(),
  show : function(funName, contextPath, relaList, relaId) {
    selectApp.funName = funName;
    selectApp.relaId = relaId;
    selectApp.contextPath = contextPath;
    if(selectApp.platList.length == 0) {
      selectApp.getPlat(3, contextPath);
    }
    if(selectApp.platList.length > 0) {
      if(! $("#selectAppDiv").length > 0) {
        $("body").append('<div id="selectAppDiv" align="center"></div>');
        $("#selectAppDiv").append('<table width="100%" border="0" cellspacing="0" cellpadding="0" style="text-align:left; font-size:12px;"></table>');
        $("#selectAppDiv table").append('<tr><td class="left_txt2" align="center" colspan="4"></td></tr>');
        $("#selectAppDiv table tr:last td:last").append('名称：<input id="appName" type="text" size="10" />&nbsp;');
        $("#selectAppDiv table tr:last td:last").append('平台：<select id="plat"></select>&nbsp;');
        $("#selectAppDiv #plat").empty();
        $.each(selectApp.platList, function(i) {
          $("#selectAppDiv #plat").append('<option value="' + this.id + '">' + this.value + '</option>');
        });
        $("#selectAppDiv table tr:last td:last").append('<input type="button" value="查找" onClick="selectApp.searchApp(false)" />');
        $("#selectAppDiv table").append('<tr><td class="left_bt2" colspan="4"><hr style="color:#FFFFFF;" /><input type="hidden" id="curPage" value="1" /><input type="hidden" id="pageSize" value="4" /></td></tr>');
        $("#selectAppDiv table").append('<tr id="result"><td class="left_bt2" colspan="4" align="center"></td></tr>');
        for(var i = 0; i < 4; i ++) {
           $("#selectAppDiv #result td").append('<div style="display:inline-block; margin-left:5px;"><img src="" width="80" height="80" /><br />&nbsp;</div>');
        }
        $("#selectAppDiv table").append('<tr><td class="left_bt2" colspan="4"><hr style="color:#FFFFFF;" /></td></tr>');
        $("#selectAppDiv table").append('<tr><td class="left_txt2" colspan="4" align="center">拖拽下方添加，拖拽垃圾箱删除<div style="float:right;" id="pagination" class="pagination"></div></td></tr>');
        $("#selectAppDiv table tr:last td:last").append('<div id="recycle" style="float:left; margin-top:3px; margin-left:10px;"><img src="' + contextPath + '/resource/image/recycle.png" width="30" height="30" /></div>');
        $("#selectAppDiv").append('<table id="container" width="344px" height="290px" border="0" cellspacing="0" cellpadding="0" style="font-size:12px;"></table>');
        $("#selectAppDiv #container").append('<tr height="1px"><td class="left_bt2"><hr style="color:#FFFFFF;" /></td></tr>');
        $("#selectAppDiv #container").append('<tr height="100%"><td class="left_bt2" style="text-align:center;"><div id="dropp_div" style="text-align:center; height:100%; list-style-type:none; margin:0; padding:0;"></div></td></tr>');
        $('#selectAppDiv').dialog({
          title : '相关APP',
          width : 400,
          height : 600,
          focus : true,
          modal : true,
          draggable : false,
          autoOpen : false,
          resizable : false,
          close : function() {
            $("#selectAppDiv").dialog('destroy');
            $("#selectAppDiv").remove();
          },
          open : function() {
            $("#selectAppDiv #dropp_div").droppable({
              addClasses : false,
              accept : '.dragg_div',
              drop : function(event, ui) {
                var appId = ui.draggable.attr("id").replace("div_", "");
                if(! $("#selectAppDiv #dropp_div #app_" + appId).length > 0) {
                  var appName = ui.draggable.attr("title");
                  $("#selectAppDiv #dropp_div").append('<div title="' + appName + '" id="app_' + appId + '" class="dragg_app dropp_app" style="margin:4px 4px 0 0; padding:1px; float:left; line-height:14px;"></div>');
                  $("#selectAppDiv #dropp_div #app_" + appId).append('<img src="' + ui.draggable.children("img").attr("src") + '" width="80" height="80" style="float:left;"/><br />');
                  var resultText = ui.draggable.text();
                  var name = resultText.split(" ");
                  $("#selectAppDiv #dropp_div #app_" + appId).append($("#selectAppDiv #plat option:selected").text() + '<br />' + name[0] +'<br />'+ (name[1] == undefined ? ' ':name[1]));
                }
              }
            });
            $("#selectAppDiv #recycle").droppable({
              addClasses : false,
              accept : '.dropp_app',
              drop : function(event, ui) {
                if(confirm("您确定删除关联应用“" + ui.draggable.attr("title") + "”吗？")) {
                  ui.draggable.remove();
              }
              }
            });
            $("#selectAppDiv #dropp_div .dropp_app").draggable({
              addClasses : false
            });
            $("#selectAppDiv #dropp_div").sortable();
            if(relaList != null && relaList.length > 0) {
              $.each(relaList, function(i) {
                $("#selectAppDiv #dropp_div").append('<div title="" id="app_' + this.appSoftwareId + '" class="dragg_app dropp_app" style="margin:4px 4px 0 0; padding:1px; float:left; line-height:14px;"></div>');
                $("#selectAppDiv #dropp_div #app_" + this.appSoftwareId).append('<img src="' + selectApp.contextPath + '/resource/image/ajax-loader.gif" style="float:left;" /><br />');
                $.ajax({
                  type : "GET",
                  url : selectApp.contextPath + "/web/support/appSoftware/" + this.appSoftwareId,
                  datatype : "json",
                  success : function(data) {
                    if(data != null) {
                      var channel = data.app.channelName == null ? "" : data.app.channelName;
                      $("#selectAppDiv #dropp_div #app_" + data.app.id).attr("title", data.app.name+"("+channel+")");
                      $("#selectAppDiv #dropp_div #app_" + data.app.id).append($("#selectAppDiv #plat option[value='" + data.app.plat + "']").text() + '<br />' + commonUtils.subStr4view(data.app.name, 8, "")+'<br /> &nbsp;'+commonUtils.subStr4view(channel, 8, ""));
                      selectApp.viewPic("app_" + data.app.id, data.app.pic);
                    }
                  }
                });
              });
            }
          },
          buttons : {
            "关闭" : function() {
              $("#selectAppDiv").dialog("close");
            },
            "保存" : function() {
              if(selectApp.funName != '') {
                var fun = eval(selectApp.funName);
                var obj = new Array();
                $("#selectAppDiv #dropp_div .dropp_app").each(function(i) {
                  var id = this.id.replace("app_", "");
                  $.merge(obj, [id]);
                });
                fun(selectApp.relaId, obj);
                $("#selectAppDiv").dialog("close");
              }
            }
          }
        });
      }
      $('#selectAppDiv').dialog("open");
    }
  },
  getPlat : function(type, contextPath) {
    $.ajax({
      type : "POST",
      url : contextPath + "/web/support/dictionary",
      datatype : "json",
      async : false,
      contentType : "application/json",
      data : {},
      success : function(data) {
        if(data != null) {
          if(data.message == 'SUCCESS') {
            if(data.list != null && data.list.length > 0) {
              $.each(data.list, function(i) {
                if(parseInt(this.type) == type) {
                  $.merge(selectApp.platList, [this]);
                }
              });
            }
            else {
              alert("获取数据字典出错");
            }
          }
          else {
            alert("获取数据字典出错-" + data.message);
          }
        }
      }
    });
  },
  searchApp : function(page) {
    var name = commonUtils.trim($("#selectAppDiv #appName").val());
    if(commonUtils.len(name) == 0) {
      alert("请输入应用名称");
      $("#selectAppDiv #appName").focus();
      return;
    }
    $("#selectAppDiv #result td").empty();
    $("#selectAppDiv #result td").append('<img src="' + selectApp.contextPath + '/resource/image/ajax-loader.gif" />');
    var obj = {};
    obj.name = name;
    obj.status = "-1";
    if(page) {
      obj.curPage = "" + $("#selectAppDiv #curPage").val();
    }
    else {
      obj.curPage = "1";
    }
    obj.pageSize = "" + $("#selectAppDiv #pageSize").val();
    obj.plat = "" + $("#selectAppDiv #plat").val();
    $.ajax({
      type : "POST",
      url : selectApp.contextPath + "/web/support/appSoftware",
      datatype : "json",
      contentType : "application/json",
      data : $.toJSON(obj),
      success : function(data) {
        if(data != null) {
          if(data.message == 'SUCCESS') {
            if(data.page != null && data.page.content != null && data.page.content.length > 0) {
              $("#selectAppDiv #curPage").val(data.page.curPage);
              $("#selectAppDiv #result td").empty();
              $.each(data.page.content, function(i) {
            	var channel = this.channelName == null ? "":this.channelName;
                $("#selectAppDiv #result td").append('<div title="' + this.name+'('+channel + ')" id="div_' + this.id + '" class="dragg_div" style="display:inline-block; margin-left:5px;"></div>');
                $("#selectAppDiv #result td div:last").append('<img src="' + selectApp.contextPath + '/resource/image/ajax-loader.gif" /><br />' + commonUtils.subStr4view(this.name, 8, "")+'<br /> &nbsp;'+commonUtils.subStr4view(channel,8,""));
                selectApp.viewPic("div_" + this.id, this.pic);
              });
              $("#selectAppDiv #result .dragg_div").draggable({
                addClasses : false,
                containment : "#selectAppDiv",
                helper : "clone",
                revert : "invalid"
              });
              for(var i = 0; i < (4 - data.page.content.length); i ++) {
                $("#selectAppDiv #result td").append('<div style="display:inline-block; margin-left:5px;"><img src="" width="80" height="80" /><br />&nbsp;</div>');
              }
              selectApp.pageSign = false;
              $("#selectAppDiv #pagination").pagination(data.page.rowCount, {
                callback : selectApp.pageselectCallback,
                items_per_page : data.page.pageSize,
                num_display_entries : 0,
                num_edge_entries : 1,
                current_page : data.page.curPage - 1,
                prev_text : "上一页",
                next_text : "下一页"
              });
              selectApp.pageSign = true;
            }
            else {
              $("#selectAppDiv #result td").empty();
              $("#selectAppDiv #result td").append('<p>无符合条件数据记录</p>');
            }
          }
          else {
            $("#selectAppDiv #result td").empty();
            $("#selectAppDiv #result td").append('<p>获取应用列表出错-' + data.message + '</p>');
          }
        }
      }
    });
  },
  pageselectCallback : function(index, jq) {
    if(selectApp.pageSign) {
      $("#selectAppDiv #curPage").val(parseInt(index) + 1);
      selectApp.searchApp(true);
    }
  },
  viewPic : function(id, picId) {
    $.ajax({
      type : "GET",
      url : selectApp.contextPath + "/web/support/picture/" + picId,
      datatype : "json",
      success : function(data) {
        if(data != null) {
        /*  var url = "http://i1.cdn.test.17173.com/sdnsoe";
          //url += data.pic.createDate.split("-")[0];
          url += "/pic/cms/";
          url += data.pic.createDate.split(" ")[0].replaceAll("-", "/");
          url += "/" + data.pic.id;
          url += "." + data.pic.suffix;*/
          var url = data.pic.picUrl;
          $("#selectAppDiv #" + id + " img").attr("src", url).attr("width", 80).attr("height", 80);
        }
      }
    });
  }
}