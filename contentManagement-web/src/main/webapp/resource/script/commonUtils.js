var commonUtils = {
  /* 截取字符串后加上.. */
  subStr4view : function(str, len, ext) {
    try {
      if(str == null || len == null || this.trim(str).length == 0 || len <= 0) {
        return str;
      }
      if(this.len(str) <= len) {
        return str;
      }
      for(var i = 0; i < str.length; i ++) {
        if(this.len(str.substr(0, i)) >= (len - this.len(ext))) {
          if(typeof ext != "undefined") {
            return str.substr(0, i + (len - this.len(ext) - this.len(str.substr(0, i)))) + ext;
          }
          return str.substr(0, i + (len - this.len(ext) - this.len(str.substr(0, i)))) + "..";
        }
      }
      return str;
    }
    catch(e) {
      return str;
    }
  },

  /* 字符长度 */
  len : function(str) {
    try {
      if(str == null || this.trim(str).length == 0) {
        return 0;
      }
      var reg = /[^\x00-\xff]/g;
      return str.replace(reg, "xx").length;
    }
    catch(e) {
      return str;
    }
  },

  /* 去除头尾空格 */
  trim : function(str) {
    if(str == null) {
      return str;
    }
    return str.replace(/(^[\s\xa1]*)|([\s\xa1]*$)/g, "");
  },

  /* 去除头空格 */
  lTrim : function(str) {
    if(str == null) {
      return str;
    }
    return str.replace(/(^[\s\xa1]*)/g, "");
  },

  /* 去除尾空格 */
  rTrim : function(str) {
    if(str == null) {
      return str;
    }
    return str.replace(/([\s\xa1]*$)/g, "");
  },

  /* 获取url传参 */
  getUrlParam : function(key) {
    try {
      if(this.trim(key) == null || this.trim(key) == '') {
        return '';
      }
      var url = window.location.href;
      var paraStr = url.substr(url.indexOf("?") + 1);
      if(this.trim(paraStr) == null || this.trim(paraStr) == '') {
        return '';
      }
      else {
        if(paraStr.indexOf(key + '=') == -1) {
          return '';
        }
        if(paraStr.indexOf(key + '=&') != -1) {
          return '';
        }
        var arr = paraStr.split(key + '=');
        return arr[arr.length - 1].split(/&+/)[0];
      }
      return '';
    }
    catch(e) {
      return '';
    }
  },

  /* 日期格式校验 */
  checkDatetime : function(str) {
    if(str == null || this.trim(str).length == 0) {
      return false;
    }
    var reg = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\d):[0-5]?\d:[0-5]?\d$/;
    return reg.exec(str);
  },

  getSystemDate : function(offsetDays) {
    var t = new Date();
    var d;
    if(offsetDays) {
      d = new Date(t.getFullYear(), t.getMonth(), t.getDate() + offsetDays + 1);
    }
    else {
      d = new Date();
    }
    s = d.getUTCFullYear() + "-";
    s += ("00" + (d.getUTCMonth() + 1)).slice(-2) + "-";
    s += ("00" + d.getUTCDate()).slice(-2);
    return s;
  },

  getBrowserInfo : function() {
    var Sys = {};
    var ua = navigator.userAgent.toLowerCase();
    var s;
    (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
    (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
    (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
    (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
    (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
    if(Sys.ie) return('IE: ' + Sys.ie);
    if(Sys.firefox) return('Firefox: ' + Sys.firefox);
    if(Sys.chrome) return('Chrome: ' + Sys.chrome);
    if(Sys.opera) return('Opera: ' + Sys.opera);
    if(Sys.safari) return('Safari: ' + Sys.safari);
  },

  checkIp : function(ip) {
    var reg = /^(25[0-4]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
    if(reg.test(ip)) {
      return true;
    }
    return false;
  },

  checkMask : function(mask) {
    var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
    if(reg.test(mask)) {
      return true;
    }
    return false;
  }
};

String.prototype.replaceAll = function(s1, s2) {
  return this.replace(new RegExp(s1, "gm"), s2);
};