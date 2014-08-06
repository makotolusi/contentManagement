var dictionary = {
  getAll : function(contextPath) {
    var objList = null;
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
              objList = data.list;
            }
          }
        }
      }
    });
    return objList;
  },
  getByType : function(objList, type) {
    var reObjList = null;
    if(objList != null && objList.length > 0) {
      reObjList = new Array();
      $.each(objList, function(i) {
        if(parseInt(this.type) == type) {
          $.merge(reObjList, [this]);
        }
      });
    }
    return reObjList;
  },
  columnType : function(contextPath) {
    return dictionary.getByType(dictionary.getAll(contextPath), 1);
  },
  gamePlat : function(contextPath) {
    return dictionary.getByType(dictionary.getAll(contextPath), 2);
  },
  appPlat : function(contextPath) {
    return dictionary.getByType(dictionary.getAll(contextPath), 3);
  },
  remindType : function(contextPath) {
    return dictionary.getByType(dictionary.getAll(contextPath), 4);
  }
}