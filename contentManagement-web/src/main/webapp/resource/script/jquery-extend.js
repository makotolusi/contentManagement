jQuery.extend({
  dataToContainer : function(container, data_html, data_json) {
    var html = $('<div></div>').append(data_html.clone().show()).show().html();
    html = html.replace(/%7B/gi, "{").replace(/%7D/gi, "}"); //img中src的处理,%7B表示{,%7D表示}
    var result = html.match(/\{([^}]+)\}/gi);
    if(result) {
      var result = result.unique();
      for(var i = 0; i < result.length; i ++) {
        var key = result[i].replace(/^\{/, "").replace(/\}$/, "");
        var value = '';
        if(/\./.test(key)) {
          var key_temp = key.replace(/([^\.]+)\.([^\.]+)/, "[\"$1\"][\"$2\"]");
          value = $.toJSON(eval("data_json" + key_temp));
        }
        else {
          value = $.toJSON(data_json[key]);
        }
        if(typeof(value) != "undefined") {
          var regx = new RegExp("\{" + key.toLowerCase() + "\}", "ig");
          html = html.replace(regx, $.toHTML(value).replace(/^"/, "").replace(/"$/, "").replace(/\\"/gi, '"'));
        }
      }
    }
    container.append(html);
  }
});

jQuery.extend({
  /**
   * @see 将javascript数据类型转换为json字符串
   * @param 待转换对象,支持object,array,string,function,number,boolean,regexp
   * @return 返回json字符串
   */
  toJSON : function(object) {
	if(object==null) return '';
    var type = typeof object;
    if('object' == type) {
      if(Array == object.constructor) {
        type = 'array';
      }
      else if(RegExp == object.constructor) {
        type = 'regexp';
      }
      else {
        type = 'object';
      }
    }
    switch(type) {
      case 'undefined' :
      case 'unknown' :
        return;
        break;
      case 'function' :
      case 'boolean' :
      case 'regexp' :
        return object.toString();
        break;
      case 'number' :
        return isFinite(object) ? object.toString() : 'null';
        break;
      case 'string' :
        return '"' + object.replace(/(\\|\")/g, "\\$1").replace(/\n|\r|\t/g, function() {
          var a = arguments[0];
          return (a == '\n') ? '\\n' : (a == '\r') ? '\\r' : (a == '\t') ? '\\t' : ""}) + '"';
        break;
      case 'object' :
        if(object === null) {
          return 'null';
        }
        var results = [];
        for(var property in object) {
          if(object[property]) {
            var value = jQuery.toJSON(object[property]);
            if(value != undefined) {
              results.push(jQuery.toJSON(property) + ':' + value);
            }
          }
          else if(object[property] == '0') {
            results.push('"' + property + '"' + ':0');
          }
          else {
            results.push('"' + property + '"' + ':null');
          }
        }
        return '{' + results.join(',') + '}';
        break;
      case 'array' :
        var results = [];
        for(var i = 0; i < object.length; i ++) {
          var value = jQuery.toJSON(object[i]);
          if(value !== undefined) {
            results.push(value);
          }
        }
        return '[' + results.join(',') + ']';
        break;
    }
  }
});

jQuery.extend({
  /**
   * @see 将json字符串转换为对象
   * @param json字符串
   * @return 返回object,array,string等对象
   */
  evalJSON : function(strJson) {
    return eval("(" + strJson + ")");
  }
});

jQuery.extend({
  toHTML : function(str) {
    return str.replace(/</gi, "&lt;").replace(/>/gi, "&gt;");
  }
});

Array.prototype.unique = function() {
  var a = {};
  for(var i = 0; i < this.length; i ++) {
    if(typeof a[this[i]] == "undefined") {
      a[this[i]] = 1;
    }
  }
  this.length = 0;
  for(var i in a) {
    this[this.length] = i;
  }
  return this;
};