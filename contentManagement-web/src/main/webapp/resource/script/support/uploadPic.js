var uploadPic = {
  pageSign : false,
  funName : '',
  contextPath : '',
  show : function(funName, contextPath, id) {
	uploadPic.funName = funName;
	uploadPic.contextPath = contextPath;
    if(!$("#selectPicDiv").length > 0) {
      $("body").append('<div id="selectPicDiv"></div>');
      $("#selectPicDiv").append('<div id="tabs_1" style="text-align:center;"><table width="100%" border="0" cellspacing="0" cellpadding="0" style="text-align:center;"></table></div><form id="subForm" enctype="multipart/form-data" method="POST" ><div id="tabs_0" style="text-align:center;"></div></form>');
      uploadPic.editPic(id);
      $('#selectPicDiv').dialog({
        title : '图片选择',
        width : 440,
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
  
  addFile : function(imgName) {
      var id = $(".fileText").length + 1;
      if($("#tabs_0 table tr").length % 2 == 1) {
        $("#tabs_0 table tr:last").before('<tr bgcolor="#f2f2f2"><td class="left_txt2"></td></tr>');
      }
      else {
        $("#tabs_0 table tr:last").before('<tr><td class="left_txt2"></td></tr>');
      }
      $("#tabs_0 table tr:last").prev("tr").children("td").append('图片' + id + '：<input class="fileText" type="text"  size="40" id="fileText_' + id + '" value="' + imgName + '" />');
  },
  editPic : function(id) {
	  $.getScript(uploadPic.contextPath+"/resource/script/jquery.form.js");
      $("#tabs_0").empty();
      if(id !=null && id > 0){
      $("#subForm").attr("action", uploadPic.contextPath+"/web/support/picture/" + id + "/picUpdate");
      var picName ='';
      var picSuffix ='';
      $.ajax({
          type : "GET",
          url : uploadPic.contextPath+"/web/support/picture/" + id,
          datatype : "json",
          async : false,
          data : {},
          success : function(data) {
            if(data != null) {
              if(data.message == 'SUCCESS') {
            	  picName = data.pic.name;
            	  picSuffix = data.pic.suffix;
              }
            }
          }
      });
      $("#tabs_0").append('<table width="100%" border="0" cellspacing="0" cellpadding="0" style="text-align:center;"></table>');
      $("#tabs_0 table").append('<tr bgcolor="#f2f2f2"><td class="left_txt2">图片名称：<input name="picName" id="newName" type="text" size="15" value = '+picName+' /><input name="picSuffix" id="picSuffix" type="hidden"  value = '+picSuffix+'  /></td></tr>');
      $("#tabs_0 table tr:last td:last").append('&nbsp;&nbsp;<input type="button" value="浏览" class="fileButton" id="fileButton_0" /><input name="picFile" type="file" style="display:none;" class="file" id="file_0" multiple />');
      $.ajax({
          type : "GET",
          url : uploadPic.contextPath+"/web/support/picture/" + id + "/files",
          datatype : "json",
          async : false,
          success : function(data) {
            if(data != null) {
              if(data.message == 'SUCCESS') {
                $("#tabs_0 table").append('<tr><td class="left_txt2"><input type="button" value="重置" onClick="uploadPic.resetPic()" /><input type="button" value="保存" onClick="uploadPic.updatePic(' + id + ')" /></td></tr>');
                if(data.list != null && data.list.length > 0) {
                  $.each(data.list, function(i) {
                    if($("#tabs_0 table tr").length % 2 == 1) {
                      $("#tabs_0 table tr:last").before('<tr id="' + this.replace("." + $("#picSuffix").val(), "") + '" bgcolor="#f2f2f2"><td class="left_txt2"></td></tr>');
                    }
                    else {
                      $("#tabs_0 table tr:last").before('<tr id="' + this.replace("." + $("#picSuffix").val(), "") + '"><td class="left_txt2"></td></tr>');
                    }
                    $("#tabs_0 table tr:last").prev("tr").children("td").append('<span class="fileText1">图片' + (i + 1) + '：' + this + '</span>&nbsp;&nbsp;&nbsp;&nbsp;');
                    $("#tabs_0 table tr:last").prev("tr").children("td").append('<a style="color:blue;" href="javascript:uploadPic.deletePic(' + id + ', \'' + this + '\');">删除</a>');
                  });
                }
              }
              else {
                alert("获取图片文件列表失败-" + data.message);
              }
            }
          }
      	});
      }else{
    	  $("#subForm").attr("action", uploadPic.contextPath+"/web/support/picture/picCreate");
    	  $("#tabs_0").append('<table width="100%" border="0" cellspacing="0" cellpadding="0" style="text-align:center;"></table>');
          $("#tabs_0 table").append('<tr bgcolor="#f2f2f2"><td class="left_txt2">图片名称：<input name="picName" id="newName" type="text" size="15" /><input name="picSuffix" id="picSuffix" type="hidden" /></td></tr>');
          $("#tabs_0 table tr:last td:last").append('&nbsp;&nbsp;<input type="button" value="浏览" class="fileButton" id="fileButton_0" /><input name="picFile" type="file" style="display:none;" class="file" id="file_0" multiple />');
    	  $("#tabs_0 table").append('<tr><td class="left_txt2"><input type="button" value="重置" onClick="uploadPic.resetPic()" /><input type="button" value="保存" onClick="uploadPic.submitPic()" /></td></tr>');
      };
	  $(".fileButton").live("click", function() {
          $("#file_" + this.id.replace("fileButton_", "")).click();
          return false;
      });
	  $(".file").change(function(){
	   for(var i = 0; i < this.files.length; i++) {
        	 uploadPic.addFile(this.files[i].name);
          }
          return false;
	  });
  },
  updatePic : function(id) {
	  var name = commonUtils.trim($("#newName").val());
       if(commonUtils.len(name) == 0) {
         alert("请输入图片名称");
         $("#newName").focus();
         return;
      }
      if($(".fileText").length < 1) {
        alert("请至少选择一个图片文件");
        return;
      }
      var suffix = $("#picSuffix").val();
      var check = true;
      var nameArr = new Array();
      $(".fileText").each(function(i) {
          if($(this).val().indexOf(".") == -1) {
            alert("图片文件名错误");
            check = false;
            return;
          }
          if(suffix != $(this).val().split(".").reverse()[0]) {
            alert("图片文件格式不统一");
            check = false;
            return;
          }
          if($.inArray($(this).val(), nameArr) > -1) {
            alert("图片名称重复");
            check = false;
            return;
          }
          else {
            $.merge(nameArr, [$(this).val()]);
          }
      });
      if(check) {
	   	$("#subForm").ajaxSubmit(function(data){
    		var d = eval("("+data.replace("<pre>","").replace("</pre>","")+")"); 
    	    if(d.message == 'SUCCESS') {
                alert("修改图片成功");
                uploadPic.picSelected(d.pic);
               // $("#selectPicDiv").dialog("close");
              }
              else {
                alert("修改图片失败-" + d.message);
              }
    		
    	});
      }
  },
  deletePic: function(id, fileName) {
        var str = "您确定删除图片“" + fileName + "”吗？";
        if(confirm(str)) {
          $.ajax({
            type : "POST",
            url :  uploadPic.contextPath+"/web/support/picture/" + id + "/delete",
            datatype : "json",
            async : false,
            data : {fileName : fileName},
            success : function(data) {
              if(data != null) {
                if(data.message == 'SUCCESS') {
                  alert("删除图片成功");
                  $("#" + fileName.replace("." + $("#picSuffix").val(), "")).remove();
                }
                else {
                  alert("删除图片失败-" + data.message);
                }
              }
            }
          });
        }
   },
   resetPic:function(){
	 $("#tabs_0").empty();
	 $("#subForm").attr("action", uploadPic.contextPath+"/web/support/picture/picCreate");
	  $("#tabs_0").append('<table width="100%" border="0" cellspacing="0" cellpadding="0" style="text-align:center;"></table>');
     $("#tabs_0 table").append('<tr bgcolor="#f2f2f2"><td class="left_txt2">图片名称：<input name="picName" id="newName" type="text" size="15" /><input name="picSuffix" id="picSuffix" type="hidden" /></td></tr>');
     $("#tabs_0 table tr:last td:last").append('&nbsp;&nbsp;<input type="button" value="浏览" class="fileButton" id="fileButton_0" /><input name="picFile" type="file" style="display:none;" class="file" id="file_0" multiple />');
	  $("#tabs_0 table").append('<tr><td class="left_txt2"><input type="button" value="重置" onClick="uploadPic.resetPic()" /><input type="button" value="保存" onClick="uploadPic.submitPic()" /></td></tr>');
	  $(".fileButton").live("click", function() {
          $("#file_" + this.id.replace("fileButton_", "")).click();
          return false;
      });
	  $(".file").change(function(){
	   for(var i = 0; i < this.files.length; i++) {
        	 uploadPic.addFile(this.files[i].name);
          }
          return false;
	  });
   },
  submitPic: function() {
       var name = commonUtils.trim($("#newName").val());
       if(commonUtils.len(name) == 0) {
         alert("请输入图片名称");
         $("#newName").focus();
         return;
       }
       if($(".file").length < 1) {
         alert("请至少选择一个图片文件");
         return;
       }
       var suffix = "";
       var check = true;
       var nameArr = new Array();
       $(".fileText").each(function(i) {
         if($(this).val().indexOf(".") == -1) {
           alert("图片文件名错误");
           check = false;
           return;
         }
         if(i == 0) {
           suffix = $(this).val().split(".").reverse()[0];
         }
         else {
           if(suffix != $(this).val().split(".").reverse()[0]) {
             alert("图片文件格式不统一");
             check = false;
             return;
           }
         }
         if($.inArray($(this).val(), nameArr) > -1) {
           alert("图片名称重复");
           check = false;
           return;
         }
         else {
           $.merge(nameArr, [$(this).val()]);
         }
       });
       var imgCheck = /^(jpg|png|gif|jpeg|bmp)$/i;
       if(! imgCheck.test(suffix)) {
         alert("请上传常见格式的图片");
         check = false;
         return;
       }
       if(check) {
    	$("#picSuffix").val(suffix);
    	$("#subForm").ajaxSubmit(function(data){
    		var d = eval("("+data.replace("<pre>","").replace("</pre>","")+")"); 
    	    if(d.message == 'SUCCESS') {
                alert("创建图片成功");
                uploadPic.picSelected(d.pic);
              }
              else {
                alert("创建图片失败-" + d.message);
              }
    		
    	});
       }
     },   
  picSelected : function(imgObj) {
    if(uploadPic.funName != '') {
      var fun = eval(uploadPic.funName);
      fun(imgObj.id, imgObj.picUrl);
      $("#selectPicDiv").dialog("close");
    }
  }
}