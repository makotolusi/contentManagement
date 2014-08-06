var selectChannel = {
  pageSign : false,
  funName : '',
  contextPath : '',
  resId : 0,
  platList : new Array(),
  show : function(funName, contextPath, channelId, resId, plat, appType) {
    selectChannel.funName = funName;
    selectChannel.resId = resId;
    selectChannel.contextPath = contextPath;
    if(selectChannel.platList.length == 0) {
    	selectChannel.getPlat(3, contextPath);
    }
    if(selectChannel.platList.length > 0) {
      if(! $("#selectChannelDiv").length > 0) {
        $("body").append('<div id="selectChannelDiv" align="center"></div>');
        $("#selectChannelDiv").append('<table width="100%" border="0" cellspacing="0" cellpadding="0" style="text-align:left; font-size:12px;"></table>');
        $("#selectChannelDiv table").append('<tr><td class="left_txt2" align="center" colspan="4"></td></tr>');
        $("#selectChannelDiv table tr:last td:last").append('渠道名称：<input id="channelName" type="text" size="10" />&nbsp;');
        $("#selectChannelDiv table tr:last td:last").append('平台：<select id="plat"></select>&nbsp;');
        $("#selectChannelDiv table tr:last td:last").append('app类型：<select id="appType"><option value="1">173App</option><option value="2">最强攻略</option></select>&nbsp;');
        $("#selectChannelDiv #plat").empty();
        $.each(selectChannel.platList, function(i) {
          $("#selectChannelDiv #plat").append('<option value="' + this.id + '">' + this.value + '</option>');
        });
        $("#selectChannelDiv table tr:last td:last").append('<input type="button" value="查找" onClick="selectChannel.searchChannel(false)" />');
        $("#selectChannelDiv table").append('<tr><td class="left_bt2" colspan="4"><hr style="color:#FFFFFF;" /><input type="hidden" id="curPage" value="1" /><input type="hidden" id="pageSize" value="5" /></td></tr>');
        $("#selectChannelDiv table").append('<tr id="result"><td class="left_bt2" colspan="4" align="center">点击”查询”，获取渠道列表</td></tr>');
        $("#selectChannelDiv table").append('<tr><td class="left_bt2" colspan="4"><hr style="color:#FFFFFF;" /></td></tr>');
        $("#selectChannelDiv table").append('<tr><td class="left_txt2" colspan="4" align="center">拖拽下方添加，拖拽垃圾箱删除<div style="float:right;" id="pagination" class="pagination"></div></td></tr>');
        $("#selectChannelDiv table tr:last td:last").append('<div id="recycle" style="float:left; margin-top:3px; margin-left:10px;"><img src="' + contextPath + '/resource/image/recycle.png" width="30" height="30" /></div>');
        $("#selectChannelDiv").append('<table id="container" width="450px" height="130px" border="0" cellspacing="0" cellpadding="0" style="font-size:12px;"></table>');
        $("#selectChannelDiv #container").append('<tr height="1px"><td class="left_bt2"><hr style="color:#FFFFFF;" /></td></tr>');
        $("#selectChannelDiv #container").append('<tr height="100%"><td class="left_bt2" style="text-align:center;"><div id="dropp_div" style="text-align:center; height:100%; list-style-type:none; margin:0; padding:0;"><ul id="sortable1" class="ui-sortable" unselectable="on" style="list-style-type:none;margin:0px;padding:0px;"/><br /><br /><br /></div></td></tr>');
        $('#selectChannelDiv').dialog({
          title : '相关渠道',
          width : 500,
          height : 360,
          focus : true,
          modal : true,
          draggable : false,
          autoOpen : false,
          resizable : false,
          close : function() {
            $("#selectChannelDiv").dialog('destroy');
            $("#selectChannelDiv").remove();
          },
          open : function() {
            $("#selectChannelDiv #dropp_div").droppable({
              addClasses : false,
              accept : '.dragg_div',
              drop : function(event, ui) {
                var chId = ui.draggable.attr("id").replace("li_", "");
                if($("#selectChannelDiv #dropp_div ul").html() == ""){
                	if(! $("#selectChannelDiv #dropp_div #sortable1 #chID_" + chId).length > 0) {
                        var channelName = ui.draggable.attr("title");
                        $("#selectChannelDiv #dropp_div ul").append('<li title="' + channelName + '" id="chID_' + chId  + '" class="ui-state-default dragg_channel dropp_channel"  style="width: 86px;height: 50px;list-style: none;text-align:center;float:left;"><span>'+commonUtils.subStr4view(channelName,10,"..")+'</span><br/><span>'+commonUtils.subStr4view( ""+chId+"",15,"..")+'</span></li>');
                      }
                }else{
                	alert("只可以选择一个渠道绑定");
                }
              }
            });
            $("#selectChannelDiv #recycle").droppable({
              addClasses : false,
              accept : '.dropp_channel',
              drop : function(event, ui) {
                if(confirm("您确定删除关联渠道“" + ui.draggable.attr("title") + "”吗？")) {
                  ui.draggable.remove();
              }
              }
            });
            $("#selectChannelDiv #dropp_div #sortable1 .dropp_channel").draggable({
              addClasses : false
            });
            $("#selectChannelDiv #dropp_div  #sortable1").sortable();
            if(channelId !=null && channelId !="") {//绑定的渠道
            	$.ajax({
            	      type : "GET",
            	      url : selectChannel.contextPath+"/web/support/channel/" + channelId + "/get",
            	      datatype : "json",
            	      async : false,
            	      success : function(data) {
            	        if(data != null && data.channel) {
            	        	$("#selectChannelDiv #dropp_div ul").append('<li id="chID_' + data.channel.channelId + '" title="' + data.channel.channelName + '('+data.channel.channelId+')" class="ui-state-default dragg_channel dropp_channel" style="width: 86px;height: 50px;list-style: none;text-align:center;float:left;"><span>'+commonUtils.subStr4view(data.channel.channelName,10,"..")+'</span><br/><span>'+commonUtils.subStr4view( ""+data.channel.channelId+"",15,"..")+'</span></li>');

            	        }
            	      }
            	});
             // $.each(aelaChannel, function(i) {
            	//$("#selectChannelDiv #dropp_div ul").append('<li id="chID_' + this.channelId + '" title="' + relaChannel.channelName + '('+relaChannel.channelId+')"  class="ui-state-default dragg_channel dropp_channel"  style="width: 86px;height: 50px;list-style: none;text-align:center;float:left;">'+commonUtils.subStr4view(relaChannel.channelName+ "("+relaChannel.channelId+")", 20, "..")+'</li>');
              //});
            }
          },
          buttons : {
            "关闭" : function() {
              $("#selectChannelDiv").dialog("close");
            },
            "保存" : function() {
              if(selectChannel.funName != '') {
                var fun = eval(selectChannel.funName);
                var obj = new Array();
                $("#selectChannelDiv #dropp_div .dropp_channel").each(function(i) {
                  var id = this.id.replace("chID_", "");
                  $.merge(obj, [id]);
                });
                fun(selectChannel.resId, obj);
                $("#selectChannelDiv").dialog("close");
              }
            }
          }
        });
        if(plat > 0){
        	$("#selectChannelDiv #plat option[value='"+plat+"']").attr("selected","selected");
            $("#selectChannelDiv #plat").attr("disabled", "disabled"); 
        }
        if(appType > 0){
        	$("#selectChannelDiv #appType option[value='"+appType+"']").attr("selected","selected");
            $("#selectChannelDiv #appType").attr("disabled", "disabled"); 
        }
      }
      $('#selectChannelDiv').dialog("open");
    }
  },

  searchChannel : function(page) {
    var name = commonUtils.trim($("#selectChannelDiv #channelName").val());
    if(commonUtils.len(name) == 0) {
      alert("请输入渠道名称");
      $("#selectChannelDiv #channelName").focus();
      return;
    }
    $("#selectChannelDiv #result td").empty();
    $("#selectChannelDiv #result td").append('<img src="' + selectChannel.contextPath + '/resource/image/ajax-loader.gif" />');
    var obj = {};
    obj.channelName = name;
    obj.status = 1;
    obj.plat = $("#selectChannelDiv #plat").val();
    if(page) {
      obj.curPage = "" + $("#selectChannelDiv #curPage").val();
    }
    else {
      obj.curPage = "1";
    }
    obj.status = 1;
    obj.pageSize = "" + $("#selectChannelDiv #pageSize").val();
    obj.appType = $("#selectChannelDiv #appType").val();
    $.ajax({
      type : "POST",
      url : selectChannel.contextPath + "/web/support/channel",
      datatype : "json",
      contentType : "application/json",
      data : $.toJSON(obj),
      success : function(data) {
        if(data != null) {
            if(data.page != null && data.page.content != null && data.page.content.length > 0) {
              $("#selectChannelDiv #curPage").val(data.page.curPage);
              $("#selectChannelDiv #result td").empty();
              $("#selectChannelDiv #result td").append('<ul id="sortable1" class="ui-sortable" unselectable="on" style="list-style-type: none;margin:0px;padding:0px;"/>');
              $.each(data.page.content, function(i) {
                $("#selectChannelDiv #result td ul").append('<li title="' + this.channelName + '('+this.channelId+')" id="li_' + this.channelId + '" class="ui-state-default dragg_div"  style="width: 86px;height: 50px;list-style: none;text-align:center;float:left;"><span>'+commonUtils.subStr4view(this.channelName,10,"..")+'</span><br/><span>'+commonUtils.subStr4view( ""+this.channelId+"",15,"..")+'</span></li>');
              });
              for(var i = 0; i < (5 - data.page.content.length); i ++) {
                  $("#selectChannelDiv #result td ul").append('<li class="ui-state-default"  style="width: 86px;height: 50px;list-style: none;text-align:center;float:left;"></li>');
              }
              $("#selectChannelDiv #result .dragg_div").draggable({
                addClasses : false,
                containment : "#selectChannelDiv",
                helper : "clone",
                revert : "invalid"
              });
              selectChannel.pageSign = false;
              $("#selectChannelDiv #pagination").pagination(data.page.rowCount, {
                callback : selectChannel.pageselectCallback,
                items_per_page : data.page.pageSize,
                num_display_entries : 0,
                num_edge_entries : 1,
                current_page : data.page.curPage - 1,
                prev_text : "上一页",
                next_text : "下一页"
              });
              selectChannel.pageSign = true;
            }
            else {
              $("#selectChannelDiv #result td").empty();
              $("#selectChannelDiv #result td").append('<p>无符合条件数据记录</p>');
            }
        }
      }
    });
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
	                  $.merge(selectChannel.platList, [this]);
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
  pageselectCallback : function(index, jq) {
    if(selectChannel.pageSign) {
      $("#selectChannelDiv #curPage").val(parseInt(index) + 1);
      selectChannel.searchChannel(true);
    }
  },
}