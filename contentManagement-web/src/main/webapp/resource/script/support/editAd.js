		function getAllChildrenNodes(treeNode){			
			if(treeNode.checked){//选中
	        	var imageTextType=$('#imageTextType').val();
	        	var type=$('#type').val();
	        	if(treeNode.getParentNode()!=null&&treeNode.getParentNode().id=='strategy_child2'){
	            	if(!(type==5||(type==4&&imageTextType==1))){//不是轮播图广告或者列表页面图文广告，不能选择！
						alert('攻略首页的广告只允许轮播图广告或者列表页图文广告!');
						treeNode.checked=false;
						return ;
	            	}
	        	}
				if(treeNode.id=='video_child1'){
					var a=treeNode.getParentNode().id;
					var fl=true;
					for(var k in datainfo){
						if(k.id==a){
							fl=false;
						}
					}
					if(fl){
						var obj={};
						obj.id=a;		
						obj.type='video_index';
						datainfo.push(obj);						
					}
				}
				if(treeNode.id=='news_child1'){
					var a=treeNode.getParentNode().id;
					var fl=true;
					for(var k in datainfo){
						if(k.id==a){
							fl=false;
						}
					}
					if(fl){
						var obj={};
						obj.id=a;		
						obj.type='news_index';
						datainfo.push(obj);						
					}
				}
				if(treeNode.id=='video_child2'){
					var a=treeNode.getParentNode().id;
					var fl=true;
					for(var k in datainfo){
						if(k.id==a){
							fl=false;
						}
					}
					if(fl){
						var obj={};
						obj.id=a;		
						obj.type='video_list';
						datainfo.push(obj);						
					}
				}
				if(treeNode.id=='news_child2'){
					var a=treeNode.getParentNode().id;
					var fl=true;
					for(var k in datainfo){
						if(k.id==a){
							fl=false;
						}
					}
					if(fl){
						var obj={};
						obj.id=a;		
						obj.type='news_list';
						datainfo.push(obj);						
					}
				}	
				if(treeNode.id=='strategy_child1'){
					var a=treeNode.id;
					var fl=true;
					for(var k in datainfo){
						if(k.id==a){
							fl=false;
						}
					}
					if(fl){
						var obj={};
						obj.id=a;		
						obj.type='strategy_index';
						datainfo.push(obj);
					}
				}
				if(treeNode.id=='strategy_child3'){
					var a=treeNode.id;
					var fl=true;
					for(var k in datainfo){
						if(k.id==a){
							fl=false;
						}
					}
					if(fl){
						var obj={};
						obj.id=a;		
						obj.type='strategy_index1';
						datainfo.push(obj);
					}
				}
				if(treeNode.id=='strategy_child2'){
					var a=treeNode.id;
					var fl=true;
					for(var k in datainfo){
						if(k.id==a){
							fl=false;
						}
					}
					if(fl){
						var obj={};
						obj.id=a;		
						obj.type='strategy_list';
						datainfo.push(obj);
					}
				}				
				if(treeNode.getParentNode()!=null&&treeNode.getParentNode().id=='strategy_child2'){
					var fl=true;
					for(var k in datainfo){
						if(k.id==treeNode.id){
							fl=false;
						}
					}
					if(fl){
						var a=treeNode.id;
						var obj={};
						obj.id=a;		
						obj.type='strategy_detail';
						datainfo.push(obj);
					}
				}					
			}else{//取消选中
				if(treeNode.id=='strategy_child1'){
					 for(var i=0;i<datainfo.length;i++){ 
							if(datainfo[i]!=null&&datainfo[i].id==treeNode.id&&datainfo[i].type=='strategy_index'){
								datainfo[i]=null;
							}
					}
				}
				if(treeNode.id=='strategy_child3'){
					 for(var i=0;i<datainfo.length;i++){ 
							if(datainfo[i]!=null&&datainfo[i].id==treeNode.id&&datainfo[i].type=='strategy_index1'){
								datainfo[i]=null;
							}
					}
				}
				if(treeNode.id=='strategy_child2'){
					 for(var i=0;i<datainfo.length;i++){ 
							if(datainfo[i]!=null&&datainfo[i].id==treeNode.id&&datainfo[i].type=='strategy_list'){
								datainfo[i]=null;
							}
					}
				}			
				if(treeNode.id=='video_child1'){
					 for(var i=0;i<datainfo.length;i++){ 
							if(datainfo[i]!=null&&datainfo[i].id==treeNode.getParentNode().id&&datainfo[i].type=='video_index'){
								datainfo[i]=null;
							}
					}
				}
				if(treeNode.id=='news_child1'){
					 for(var i=0;i<datainfo.length;i++){ 
							if(datainfo[i]!=null&&datainfo[i].id==treeNode.getParentNode().id&&datainfo[i].type=='news_index'){
								datainfo[i]=null;
							}
					}					
				}
				if(treeNode.id=='video_child2'){
					 for(var i=0;i<datainfo.length;i++){ 
							if(datainfo[i]!=null&&datainfo[i].id==treeNode.getParentNode().id&&datainfo[i].type=='video_list'){
								datainfo[i]=null;
							}
					}
				}
				if(treeNode.id=='news_child2'){
					 for(var i=0;i<datainfo.length;i++){ 
							if(datainfo[i]!=null&&datainfo[i].id==treeNode.getParentNode().id&&datainfo[i].type=='news_list'){
								datainfo[i]=null;
							}
					}					
				}
				if(treeNode.getParentNode()!=null&&treeNode.getParentNode().id=='strategy_child2'){
					 for(var i=0;i<datainfo.length;i++){ 
							if(datainfo[i]!=null&&datainfo[i].id==treeNode.id&&datainfo[i].type=='strategy_detail'){
								datainfo[i]=null;
							}
					}					
				}					
			}
		    if (treeNode.isParent) {
		        var childrenNodes = treeNode.children;
		        if (childrenNodes) {
		            for (var i = 0; i < childrenNodes.length; i++) {
		                getAllChildrenNodes(childrenNodes[i]);
		            }
		        }
		    }
		    return ;
		}
		function shownews(id){			
		    var obj = {};
		    obj.columnId = ""+id;
		    obj.curPage = "" + $("#curPage").val();
		    obj.pageSize = "" + $("#pageSize").val();
		    $.ajax({
		      type : "POST",
		      url : "${contextPath}/web/operations/news",
		      datatype : "json",
		      contentType : "application/json",
		      data : $.toJSON(obj),
		      success : function(data) {
		        if(data != null) {
		            if(data.page != null && data.page.content != null && data.page.content.length > 0) {
		              newsInfo = {};
		              $("#curPage").val(data.page.curPage);
		              $("#pageSize").val(data.page.pageSize);
		              $("#pageList").html('<table width="100%" border="0" cellspacing="0" cellpadding="0" style="text-align:center;table-layout:fixed"><tr style="font-size:12px;" bgcolor="#f2f2f2"></tr></table>');
		              $("#pageList table tr").append('<th width="10%"><span>新闻ID</span></th>');
		              $("#pageList table tr").append('<th width="25%"><span>新闻标题</span></th>');
		              $("#pageList table tr").append('<th width="25%"><span>操作</span></th>');
		              $.each(data.page.content, function(i) {
		                if(i % 2 == 0) {
		                  $("#pageList table").append('<tr></tr>');
		                }
		                else {
		                  $("#pageList table").append('<tr bgcolor="#f2f2f2"></tr>');
		                }
		                $("#pageList table tr:last").append('<td  title='+ this.id+' >' + this.id + '</td>');
		                $("#pageList table tr:last").append('<td  title='+ this.title+' >' + this.title + '</td>');
		                var disable='';
						for(var k in datainfo){
							if(datainfo[k]!=null&&datainfo[k].id==this.id&&datainfo[k].type=='news_detail'){
								disable='disabled="disabled"';
							}
						}
		                $("#pageList table tr:last").append('<td  title=操作 ><button '+disable+'class=btn_dis_news'+ this.id + ' onclick=selectElement(' + this.id + ',1,this);>选择</button></td>');
		                });
		              pageSign = false;
		              $("#pagination").pagination(data.page.rowCount, {
		                callback : pageselectCallbackColumnNews,
		                items_per_page : data.page.pageSize,
		                num_display_entries : 5,
		                num_edge_entries : 1,
		                current_page:  data.page.curPage-1,
		                prev_text : "上一页",
		                next_text : "下一页"
		                });
		              pageSign = true;
		              }
		            else {
		              $("#pageTable").hide();
		              $("#pagination").empty();
		              $("#pageList").html("<p>无符合条件数据记录</p>");
		              }
		          }
		        }
		      });
	    }
		function searchVideo(id) {
		    $("#pageList").html('<img src="${contextPath}/resource/image/ajax-loader.gif" />');
		    var obj = {};
		    obj.columnId = ""+id;		    
		    obj.curPage = "" + $("#curPage").val();
		    obj.pageSize = "" + $("#pageSize").val();
		    $.ajax({
		      type : "POST",
		      url : "${contextPath}/web/operations/video",
		      datatype : "json",
		      contentType : "application/json",
		      data : $.toJSON(obj),
		      success : function(data) {
		        if(data != null) {
		            if(data.page != null && data.page.content != null && data.page.content.length > 0) {
		              videoInfo = {};
		              $("#curPage").val(data.page.curPage);
		              $("#pageSize").val(data.page.pageSize);
		              $("#pageList").html('<table width="100%" border="0" cellspacing="0" cellpadding="0" style="text-align:center;table-layout:fixed"><tr style="font-size:12px;" bgcolor="#f2f2f2"></tr></table>');
		              $("#pageList table tr").append('<th width="8%"><span></span></th>');
		              $("#pageList table tr").append('<th width="10%"><span>视频ID</span></th>');
		              $("#pageList table tr").append('<th width="25%"><span>视频标题</span></th>');
		              $.each(data.page.content, function(i) {
		                if(i % 2 == 0) {
		                  $("#pageList table").append('<tr></tr>');
		                }
		                else {
		                  $("#pageList table").append('<tr bgcolor="#f2f2f2"></tr>');
		                }
		                var disable='';
						for(var k in datainfo){
							if(datainfo[k]!=null&&datainfo[k].id==this.id&&datainfo[k].type=='video_detail'){
								disable='disabled="disabled"';
							}
						}
		                $("#pageList table tr:last").append('<td style="text-align:center;white-space:nowrap;overflow:hidden;word-break:keep-all" title='+ this.id+' >' + this.id + '</td>');
		                var vt = this.cvideoTitle=="" ? this.title:this.cvideoTitle;
		                $("#pageList table tr:last").append('<td style="text-align:center;white-space:nowrap;overflow:hidden;word-break:keep-all" title='+vt+' >' + vt + '</td>');
		                $("#pageList table tr:last").append('<td  title=操作 ><button '+disable+' class=btn_dis_video'+ this.id + ' onclick=selectElement(' + this.id + ',2,this);>选择</button></td>');
		                $("#pageList  table a").addClass("firstebox");
		                });
		              pageSign = false;
		              $("#pagination").pagination(data.page.rowCount, {
		                callback : pageselectCallbackColumn,
		                items_per_page : data.page.pageSize,
		                num_display_entries : 5,
		                num_edge_entries : 1,
		                current_page:  data.page.curPage-1,
		                prev_text : "上一页",
		                next_text : "下一页"
		                });
		              pageSign = true;
		              }
		            else {
		              $("#pageTable").hide();
		              $("#pagination").empty();
		              $("#pageList").html("<p>无符合条件数据记录</p>");
		              }
		          }
		        }
		      });
		    }	
		function selectElement(eleid,type,o){
			  var obj={};
			  obj.id=eleid;		  		  	
				 for(var i=0;i<datainfo.length;i++){ 
						if(datainfo[i]!=null&&datainfo[i].id==eleid&&type==1){
							alert('已经选择当前新闻!');
							return ;
						}
						if(datainfo[i]!=null&&datainfo[i].id==eleid&&type==2){
							alert('已经选择当前视频!');
							return ;
						}
				}
				 if(type==1){
					 	o.disabled='disable';
				 		obj.type='news_detail';
				 		$('#pushnews').append('<a href=javascript:deleteSelected('+eleid+',"news_detail") alt=删除  title=删除  class=selected1_'+eleid+'>'+eleid+'</a>&nbsp');
				     }
				  	if(type==2){
				  		o.disabled='disable';
				  		obj.type='video_detail';	 
				  		$('#pushvideo').append('<a href=javascript:deleteSelected('+eleid+',"video_detail") alt=删除  title=删除  class=selected1_'+eleid+'>'+eleid+'</a>&nbsp');
				     }
			  datainfo.push(obj);
		  }
		  function deleteSelected(s,type){
			$('.selected1_'+s).remove();
			if(type=='news_detail'){
				$('.btn_dis_news'+s).removeAttr("disabled");	
			}
			if(type=='video_detail'){
				$('.btn_dis_video'+s).removeAttr("disabled");	
			}
			
			 for(var i=0;i<datainfo.length;i++){ 
				if(datainfo[i]!=null&&datainfo[i].id==s&&datainfo[i].type==type){
					datainfo[i]=null;
				}
			}
		  }
	      function urlvalidate(url){
	    	  var prefix=url.substring(0,4);
	    	  return prefix=='http';
	      }
	      function validatetype(){
	    	var f=true;
	   		var type=$('#type').val();
	   		var app=$('#app').val();
	  		if(type==''){
	  			$('#type').focus();
	  			alert('广告类型不能为空!');
	  			f=false;
	  		}
	  		if(type==1){//启动式广告
	  	  		var showSecond=$('#showSecond').val();
	  	  		if(showSecond==''){
	  	  			$('#showSecond').focus();
	  	  			alert('全屏广告显示时间不能为空!');f=false;
	  	  		}  	
	  	  		var picId=$('#picId').val();
	  	  		if(picId==''){
	  	  			$('#picId').focus();
	  	  			alert('广告图不能为空!');f=false;
	  	  		}  	  	  		
	  		} else {
	  			var urlIos=$('#urlIos').val();
	  	  		if(urlIos==''){
	  	  			$('#urlIos').focus();
	  	  			alert('ios地址不能为空!');f=false;
	  	  		}else{
	  	  			if(!urlvalidate(urlIos)){
	  	  				alert('请输入合法ios地址!');
	  	  				f=false;	
	  	  			};
	  	  		}  	
	  	  		var urlAndroid=$('#urlAndroid').val();
	  	  		if(urlAndroid==''){
	  	  			$('#urlAndroid').focus();
	  	  			alert('android地址不能为空!');f=false;
	  	  		}else{
	  	  			if(!urlvalidate(urlAndroid)){
	  	  				alert('请输入合法android地址!');
	  	  				f=false;	
	  	  			};
	  	  		}  	 	  	
		  		if(type==2){//弹出式广告
		  	  		var action=$('#action').val();
		  	  		if(action=='0'){
		  	  			$('#action').focus();
		  	  			alert('广告动作不能为空!');f=false;
		  	  		}  	  
		  		}  	
		  		if(type==3){
		  	  		var action=$('#action').val();
		  	  		if(action=='0'){
		  	  			$('#action').focus();
		  	  			alert('广告动作不能为空!');f=false;
		  	  		}  	  
		  	  		var picId=$('#picId').val();
		  	  		if(picId==''){
		  	  			$('#picId').focus();
		  	  			alert('广告图不能为空!');f=false;
		  	  		}
		  	  		if(app==1){
			  	  		var column=$('#column').val();
				  		if(column==''){
				  			$('#column').focus();
				  			alert('广告栏目不能为空!');f=false;
				  		} 
		  			}
			  		var position=$('#position').val();
			  		if(position==''){
			  			$('#position').focus();
			  			alert('广告位不能为空!');f=false;
			  		}else{
			  	  		if($.isNaN(position)){
				  	  		alert('广告位为整数!');
				  	  		f=false;
				  	  	}else{
					  	  	if(app==1){
					  	  		if(!(position=='1'||position=='2'||position=='3')){
					  	  			alert('广告位请输入1/2/3,1代表上面,2下面第1个,3代表下面第2个!');
					  	  			f=false;
					  	  		}
					  	  	}
				  	  	}
		  	  		} 	  		
		  		}  	
		  		if(type==4){//图文
		  	  		var action=$('#action').val();
		  	  		if(action=='0'){
		  	  			$('#action').focus();
		  	  			alert('广告动作不能为空!');f=false;
		  	  		}  	  			
		  	  		var picId=$('#picId').val();
		  	  		if(picId==''){
		  	  			$('#picId').focus();
		  	  			alert('广告图不能为空!');f=false;
		  	  		}  
		  	  		var position=$('#position').val();
		  	  		if(position==''){
		  	  			$('#position').focus();
		  	  			alert('广告位不能为空!');f=false;
		  	  		}else{
			  	  		if($.isNaN(position)){
				  	  		alert('广告位为整数!');
				  	  		f=false;
				  	  	}else{
				  	  		if(app==1){
					  	  		var imageTextType=$('#imageTextType').val();
					  	  		if(imageTextType=='2'){
					  	  			if(!(position=='1'||position=='2'||position=='3')){
			                 			alert('广告位请输入1/2/3,1代表上面,2下面第1个,3代表下面第2个!');
			                 			f=false;
				           			}
			               		}
				  	  		}
				  	  	}
				  	}
			  	  	if(app==1){
				  	  	var column=$('#column').val();
				  		if(column==''){
				  			$('#column').focus();
				  			alert('广告栏目不能为空!');f=false;
				  		}   
			  	  	}
			  	  	var imageTextType=$('#imageTextType').val();
			  		if(imageTextType==''){
			  			$('#imageTextType').focus();
			  			alert('图文广告类型不能为空!');f=false;
			  		}   
		  		}  	  
		  		if(type == 5){//轮播图
		  	  		var action=$('#action').val();
		  	  		if(action=='0'){
		  	  			$('#action').focus();
		  	  			alert('广告动作不能为空!');f=false;
		  	  		}  	  			
		  	  		var picId=$('#picId').val();
		  	  		if(picId==''){
		  	  			$('#picId').focus();
		  	  			alert('广告图不能为空!');f=false;
		  	  		}  
		  	  		var turn=$('#turn').val();
		  	  		if(turn==''){
		  	  			$('#turn').focus();
		  	  			alert('广告顺序不能为空!');f=false;
		  	  		}else{
			  	  		if(isNaN(turn)){
				  	  		alert('广告顺序为整数!');
				  	  		f=false;
				  	  	}
		  	  		}    
			  	  	if(app==1){
			  	  		var column=$('#column').val();
				  		if(column==''){
				  			$('#column').focus();
				  			alert('广告栏目不能为空!');f=false;
				  		}
			  	  	}
		  		}  
		  		if(type==6){//九宫格广告
		  	  		var picId=$('#picId').val();
		  	  		if(picId==''){
		  	  			$('#picId').focus();
		  	  			alert('广告图不能为空!');f=false;
		  	  		}  
		  	  		var turn=$('#turn').val();
		  	  		if(turn==''){
		  	  			$('#turn').focus();
		  	  			alert('广告顺序不能为空!');f=false;
		  	  		}else{
			  	  		if(isNaN(turn)){
				  	  		alert('广告顺序为整数!');
				  	  		f=false;
				  	  	}
		  	  		}    
		  		}  
		  		if(type==8){//数据库广告
		  			var position=$('#position').val();
			  		if(position==''){
			  			$('#position').focus();
			  			alert('广告位不能为空!');f=false;
			  		}else{
			  	  		if($.isNaN(position)){
				  	  		alert('广告位为整数!');
				  	  		f=false;
				  	  	}else{
				  	  		if(!(position=='1'||position=='2')){
				  	  			alert('广告位请输入1/2,1代表列表页,2代表详情页');
				  	  			f=false;
				  	  		}
				  	  	}
		  	  		} 	 
		  		}  
		  		if(type == 9){//攻略大全轮播图广告
		  	  		var action=$('#action').val();
		  	  		if(action=='0'){
		  	  			$('#action').focus();
		  	  			alert('广告动作不能为空!');f=false;
		  	  		}  	  			
		  	  		var picId=$('#picId').val();
		  	  		if(picId==''){
		  	  			$('#picId').focus();
		  	  			alert('广告图不能为空!');f=false;
		  	  		}  
		  	  		var turn=$('#turn').val();
		  	  		if(turn==''){
		  	  			$('#turn').focus();
		  	  			alert('广告顺序不能为空!');f=false;
		  	  		}else{
			  	  		if(isNaN(turn)){
				  	  		alert('广告顺序为整数!');
				  	  		f=false;
				  	  	}
		  	  		}    
		  		} 
		  		if(type==10 || type==12){//文字链或原生引导
		  			var position=$('#position').val();
			  		if(position==''){
			  			$('#position').focus();
			  			alert('广告位不能为空!');f=false;
			  		}else{
			  	  		if($.isNaN(position)){
				  	  		alert('广告位为整数!');
				  	  		f=false;
				  	  	}
		  	  		} 	 
		  			var app=$('#app').val();
		  			if(app!=3){
		  				alert('应用标识只能选M站!');
			  			f=false;
		  			}
		  			var column=$('#column').val();
			  		if(column==''){
			  			$('#column').focus();
			  			alert('广告栏目不能为空!');
			  			f=false;
			  		}     
		  		}  
	  		}
	  		return f;
	     }   
	     function validatecolumn(){
	    		var column=$('#column').val();
	    		if(column==''){
	    			$('#column').focus();
	    			alert('广告栏目信息不能为空!');
	    			return false;
	    		}
	    		return true;
	     }
	     function validatename(){
	  		var name=$('#name').val();
	  		if(name==''){
	  			$('#name').focus();
	  			alert('广告名称不能为空!');
	  			return false;
	  		}
	  		return true;
		 }
		 function validateaction(){
		  		var action=$('#action').val();
		  		if(action=='0'){
		  			$('#action').focus();
		  			alert('点击动作不能为空!');
		  			return false;
		  		}
		  		return true;
		 }
	     function validatewords(){
	    		var name=$('#words').val();
	    		if(name==''){
	    			$('#words').focus();
	    			alert('广告语不能为空!');
	    			return false;
	    		}
	    		return true;
	     }   
	     function validateeffDate(){
	 		var name=$('#effDate').val();
	 		if(name==''){
	 			$('#effDate').focus();
	 			alert('生效时间不能为空!');
	 			return false;
	 		}
	 		return true;
	  	 }   
	     function validateexpDate(){
	  		var name=$('#expDate').val();
	  		if(name==''){
	  			$('#expDate').focus();
	  			alert('失效时间不能为空!');
	  			return false;
	  		}
	  		return true;
	   	 }  
//	     function actionshowfield(){
//	    	var action1=$('#action').val();
//	    	var type=$('#type').val();
//	   		if(type=='1'&&action1=='2'){//安装式
//	   			$('.urlIos').hide();
//	   		}else{
//	   			$('.urlIos').show();
//	   		} 		
//	     }
	     function adImageTextType(){
	 		zTree=null;
			zNodes=null;
			$("#pageList").html("");
			$("#pagination").empty();
			if($('#adId').val()==''){
		    	 $('#columnId').val('');
		    	 $('#column').val('');
				
			}
	     }
	     function showfield(obj){
	   		zTree=null;
	   		zNodes=null;
	   		if($('#adId').val()==''){
	   	    	 $('#columnId').val('');
	   	    	 $('#column').val('');
	   		}
	   		$("#pageList").html("");
	   		$("#pagination").empty();
	    	var action1=$('#action').val();
	   		if(obj.value==1&&action1=='2'){//安装式
	   			$('.urlIos').hide();
	      		}else{
	    		$('.urlIos').show();
	     	} 
	    	if(obj.value==1){//启动式广告
	       		 $(".position").hide();
	    		 $(".turn").hide();
	    		 $(".action").show();
	    		 $(".urlAndroid").show();
	    		 $(".showSecond").show();
	    		 $(".picId").show();
	    		 $(".columnId").hide();
	    		 $(".required").hide();
	    		 $(".imageTextType").hide();
	    		 $(".newsOrVideo").hide();
	    	 }
	    	 if(obj.value==2){//弹出式广告
	      		 $(".position").hide();
	      		 $(".picId").hide();
	    		 $(".turn").hide();
	    		 $(".action").show();
	    		 $(".urlIos").show();
	    		 $(".urlAndroid").show();
	    		 $(".showSecond").hide();
	    		 $(".columnId").hide();
	    		 $(".required").show();
	    		 $(".imageTextType").hide();
	    		 $(".newsOrVideo").hide();
	    	 }    	
	    	 if(obj.value==3){// 详情页banner广告
	      		 $(".position").show();
	    		 $(".turn").hide();
	    		 $(".action").show();
	    		 $(".urlIos").show();
	    		 $(".urlAndroid").show();
	    		 $(".picId").show();
	    		 $(".showSecond").hide();
	    		 $(".columnId").show();
	    		 $(".required").show();
	    		 $(".imageTextType").hide();
	    		 $(".newsOrVideo").show();
	    	 }    	
	    	 if(obj.value==4){//图文广告
	      		 $(".position").show();
	    		 $(".turn").hide();
	    		 $(".imageTextType").show();
	    		 $(".action").show();
	    		 $(".urlIos").show();
	    		 $(".urlAndroid").show();
	    		 $(".showSecond").hide();
	    		 $(".picId").show();
	    		 $(".columnId").show();
	    		 $(".required").show();
	    		 $("#imageTextType").empty();
	    		 $("#imageTextType").append("<option value=\"1\">列表页图文广告</option><option value=\"2\">详情页图文广告</option>");
	    		 $(".newsOrVideo").show();
	    	 }      
	    	 if(obj.value==5 || obj.value==9){//轮播图或者攻略大全轮播图
	      		 $(".position").hide();
	    		 $(".turn").show();
	    		 $(".action").show();
	    		 $(".urlIos").show();
	    		 $(".urlAndroid").show();
	    		 $(".showSecond").hide();
	    		 $(".picId").show();
	    		 $(".columnId").show();
	    		 $(".required").show();
	    		 $(".imageTextType").hide();
	    		 $(".newsOrVideo").hide();
	    	 }      
	    	 if(obj.value==6){//九宫格广告
	      		 $(".position").hide();
	    		 $(".turn").show();
	    		 $(".action").show();
	    		 $(".urlIos").show();
	    		 $(".urlAndroid").show();
	    		 $(".showSecond").hide();
	    		 $(".picId").show();
	    		 $(".columnId").hide();
	    		 $(".required").show();
	    		 $(".imageTextType").hide();
	    		 $(".newsOrVideo").hide();
	    	 }      
	    	 if(obj.value==8){//数据库广告
	      		 $(".position").show();
	    		 $(".turn").hide();
	    		 $(".action").show();
	    		 $(".urlIos").show();
	    		 $(".urlAndroid").show();
	    		 $(".showSecond").hide();
	    		 $(".picId").show();
	    		 $(".columnId").hide();
	    		 $(".required").show();
	    		 $(".imageTextType").hide();
	    		 $(".newsOrVideo").hide();
	    	 }          
	    	 if(obj.value==10){//文字链
	      		 $(".position").show();
	    		 $(".turn").hide();
	    		 $(".action").show();
	    		 $(".urlIos").show();
	    		 $(".urlAndroid").show();
	    		 $(".showSecond").hide();
	    		 $(".picId").hide();
	    		 $(".columnId").show();
	    		 $(".required").show();
	    		 $(".imageTextType").show();
	    		 $("#imageTextType").empty();
	    		 $("#imageTextType").append("<option value=\"1\">列表页文字链</option><option value=\"2\">详情页文字链</option>");
	    		 $(".newsOrVideo").hide();
	    	 }   
	    	 if(obj.value==11){//首页通知文字链
	       		 $(".position").hide();
	    		 $(".turn").hide();
	    		 $(".action").show();
	    		 $(".urlAndroid").show();
	    		 $(".showSecond").hide();
	    		 $(".picId").hide();
	    		 $(".columnId").hide();
	    		 $(".required").hide();
	    		 $(".imageTextType").hide();
	    		 $(".newsOrVideo").hide();
	    	 }
	    	 if(obj.value==12){//原生引导
	      		 $(".position").show();
	    		 $(".turn").hide();
	    		 $(".action").show();
	    		 $(".urlIos").show();
	    		 $(".urlAndroid").show();
	    		 $(".showSecond").hide();
	    		 $(".picId").show();
	    		 $(".columnId").show();
	    		 $(".required").show();
	    		 $(".imageTextType").show();
	    		 $("#imageTextType").empty();
	    		 $("#imageTextType").append("<option value=\"1\">列表页图文引导</option>");
	    		 $(".newsOrVideo").hide();
	    	 }       	 
	     } 
	     function showStrategyList(selectIndex){
	     	if(selectIndex==2){
				$("#strategyList").show();
				$(".columnId").hide();
	     	}else{
	     		$("#strategyList").hide(); 
	     		$(".columnId").show();
	     	}
	     }  
	     function checkAll(){
	     	$("input[name='strategyIds']").attr("checked",true);
	     }	      