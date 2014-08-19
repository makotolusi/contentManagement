package com.cyou.video.mobile.server.cms.service.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {


 public static Date toDate(String d){
   DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
   try {
    return fmt.parse(d);
  }
  catch(ParseException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
    return null;
  }
 }
}
