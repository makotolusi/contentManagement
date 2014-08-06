package com.cyou.video.mobile.server.cms.service.common;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.cyou.video.mobile.server.cms.common.Consts;
import com.cyou.video.mobile.server.common.Constants;
import com.cyou.video.mobile.server.common.utils.ImageUtil;
import com.cyou.video.mobile.server.common.utils.JacksonUtil;
import com.cyou.video.mobile.server.common.utils.S3Util;
import com.cyou.video.mobile.server.common.utils.SecurityUtil;
import com.fasterxml.jackson.databind.JsonNode;

public class PrivateUtil {

  public static final String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJz/J1OgkpF3ONcB7kHcWoJzqVMNs/cc" + "\r"
      + "aao79tPodAGXlN/K2jub2rliZnjIKxSIuAcM4PK4HpznXRPpNGjHplECAwEAAQ==" + "\r";

  public static final String privateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAnP8nU6CSkXc41wHu" + "\r"
      + "QdxagnOpUw2z9xxpqjv20+h0AZeU38raO5vauWJmeMgrFIi4Bwzg8rgenOddE+k0" + "\r"
      + "aMemUQIDAQABAkAYqEZP7xc1+4iU1DvMYkRg2rOMVMmgFPlPgE166LZXjW0c3Fbg" + "\r"
      + "JUD1MtNaZpy2WKLwRDiPtzAZLCy2RmNtTi2xAiEAywvgExwpoGEzidgsYnyRMPew" + "\r"
      + "MRDJSZPEdjPGIKQMVy0CIQDF8NTF93mP9wqPmG0/IZIriUCP2ZUZ646/HrxKqsBC" + "\r"
      + "NQIgc99bCIzR1Iyj9M5AxhOAaAlxqw6BUFPbkfkJ4Ca+RCECIQCHbgemi3Q49CXd" + "\r"
      + "qcTVdPq1nur1gUFqwqigSz85NyrkIQIgLeUNfPLdNDuIWJi7IpPFII1HN+Y5DSPC" + "\r" + "Do+1Oth8Pyk=" + "\r";

  public static final int[] WIDTH_LIST = {604, 480, 320, 280, 250};

  public static final int[] FIRST_WIDTH_LIST = {162, 108, 140, 604, 480, 320, 280, 250};


  public volatile static Map<Integer, List<Integer>> mapCnIdList = new HashMap<Integer, List<Integer>>();


  public volatile static Map<Integer, List<Integer>> mapMnIdList = new HashMap<Integer, List<Integer>>();

  public static final String[] IMAGE_TYPE = {"jpg", "gif", "png", "bmp", "jpeg"};

  /**
   * 为新闻资讯提供图片缩放并存储的功能
   * 
   * @param url
   * @param flag
   *          是否是第一个图片 true->是
   * @return
   * @throws Exception
   */
  public static Map<String, String> getThumbnailImage(String url, boolean flag) throws Exception {
    java.util.regex.Pattern p1 = java.util.regex.Pattern.compile("[\u4e00-\u9fa5]+");
    if(p1.matcher(url).find()) {
      throw new IOException("param url contains chiness.");
    }
    url = java.net.URLDecoder.decode(url, "UTF-8");
    String year = "";
    String month = "";
    String day = "";
    String date = "";
    String imageName = "";
    String imageType = "";
    url = url.trim();
    java.util.regex.Pattern p = java.util.regex.Pattern
        .compile("^http://.+/(\\d{4})/(\\d{2})/(\\d{2})/[^/]*?/?([^/]+)\\.(.+)$");
    java.util.regex.Matcher m = p.matcher(url);
    int[] widths = flag ? FIRST_WIDTH_LIST : WIDTH_LIST;
    if(m.find()) { // 正常url
      year = m.group(1);
      month = m.group(2);
      day = m.group(3);
      imageName = m.group(4);
      imageType = m.group(5).toLowerCase();
      for(int i = 0; i < IMAGE_TYPE.length; i++) {
        if(imageType.indexOf(IMAGE_TYPE[i]) > -1) {
          imageType = IMAGE_TYPE[i];
          break;
        }
      }
      // url = url.replace("images.17173.com",
      // "i9.17173.itc.cn");//替换后服务器上wget访问不到
    }
    else {
      java.util.regex.Pattern er_p = java.util.regex.Pattern.compile("^http://.+/[^/]*?/?([^/]+)\\.(.+)$");
      java.util.regex.Matcher er_m = er_p.matcher(url);
      if(er_m.find()) { // 非规定的url
        Calendar now = Calendar.getInstance(); // 判断url中图片名称前三位是否是年月日，若不是，则取当前时间
        year = String.valueOf(now.get(Calendar.YEAR));
        month = String.valueOf(now.get(Calendar.MONTH) + 1);
        day = String.valueOf(now.get(Calendar.DAY_OF_MONTH));
        imageName = er_m.group(1);
        imageType = er_m.group(2).toLowerCase();
        for(int i = 0; i < IMAGE_TYPE.length; i++) {
          if(imageType.indexOf(IMAGE_TYPE[i]) > -1) {
            imageType = IMAGE_TYPE[i];
            break;
          }
        }
        String count = "";
        int num = 0;
        while(new File(Consts.CMS_PIC_PATH + "/" + year + "/" + month + "/" + day + "/" + imageName + count + "."
            + imageType).exists()) {
          num++;
          count = String.valueOf(num);
        }
        imageName = imageName + count;
      }
      else {
        throw new IOException("param url Format is error");
      }
    }
    imageType = imageType.replaceAll("/[^\\.]+$", "");// 处理url中图片结尾还有内容的
    date = year + "/" + month + "/" + day;
    String originalAddress = Consts.CMS_PIC_PATH + "/" + date + "/" + imageName + "." + imageType;

    List<String> list = searchFileByPath(Consts.CMS_PIC_PATH + "/" + date + "/", imageName);
    Map<String, String> map = new HashMap<String, String>();
    StringBuffer urlResult = new StringBuffer("http://");// 拼接缩放后图片的互联网访问地址
    urlResult.append(Constants.PIC_DOMAIN.DOMAIN_1.getValue());
    urlResult.append("/");
    urlResult.append(year);
    urlResult.append(Consts.CMS_PIC_AGREED);
    urlResult.append("pic/cms");
    urlResult.append("/" + date + "/");
    urlResult.append(imageName + "." + imageType);
    map.put("src", urlResult.toString());

    boolean originalFlag = false;
    if(list != null && list.size() > 0) {
      if(list.contains(imageName + "." + imageType)) {
        originalFlag = true;
      }
    }
    if(!originalFlag) {// 下载原图
      Map<String, String> mapFlag = judgeOriginalSize(url);
      if(mapFlag.containsKey("noFind")) {
        throw new IOException("save Original Image error ! not find image. :" + url + ":");
      }
      else if(mapFlag.containsKey("bigSize")) {
        throw new IOException("Original Image size exceed 4M ! image size is :" + mapFlag.get("bigSize"));
      }
      else {
        ImageUtil.saveOriginalImage(url, new File(originalAddress));// 在指定位置保存原图
      }
    }
    BufferedImage originalImage = ImageIO.read(new FileInputStream(originalAddress));
    for(int i = 0; i < widths.length; i++) { // 判断要切割的图片是否存储
      String image = imageName + "_" + widths[i] + "." + imageType;
      if((list == null || list.size() == 0) || !list.contains(image)) {
        int newHeight = createNewImage(originalImage, date, imageName, imageType, widths[i], originalAddress, url);
        map.put(String.valueOf(widths[i]), String.valueOf(newHeight));
      }
      else {
        Image original = (Image) originalImage;
        int w = original.getWidth(null);
        int h = original.getHeight(null);
        if(new File(Consts.CMS_PIC_PATH + "/" + date + "/" + image).length() >= 4 * 1024 * 1024) {
          copyImage(originalAddress, Consts.CMS_PIC_PATH + "/" + date + "/" + image);
        }
        map.put(String.valueOf(widths[i]), String.valueOf((h * widths[i] / w)));
      }
    }
    return map;
  }

  /**
   * 
   * @param url
   * @param localUrlJson
   *          用于新闻更新是比对图片是否上传过
   * @return
   * @throws Exception
   */
  public static Map<String, String> putPictureByS3(String url, String localUrlJson) throws Exception {
    java.util.regex.Pattern p1 = java.util.regex.Pattern.compile("[\u4e00-\u9fa5]+");
    if(p1.matcher(url).find()) {
      throw new IOException("param url contains chiness.");
    }
    url = java.net.URLDecoder.decode(url, "UTF-8");
    String year = "";
    String month = "";
    String day = "";
    String date = "";
    String imageName = "";
    String imageType = "";
    url = url.trim();
    java.util.regex.Pattern p = java.util.regex.Pattern
        .compile("^http://.+/(\\d{4})/(\\d{2})/(\\d{2})/[^/]*?/?([^/]+)\\.(.+)$");
    java.util.regex.Matcher m = p.matcher(url);
    if(m.find()) { // 正常url
      year = m.group(1);
      month = m.group(2);
      day = m.group(3);
      imageName = m.group(4);
      imageType = m.group(5).toLowerCase();
      for(int i = 0; i < IMAGE_TYPE.length; i++) {
        if(imageType.indexOf(IMAGE_TYPE[i]) > -1) {
          imageType = IMAGE_TYPE[i];
          break;
        }
      }
    }
    else {
      java.util.regex.Pattern er_p = java.util.regex.Pattern.compile("^http://.+/[^/]*?/?([^/]+)\\.(.+)$");
      java.util.regex.Matcher er_m = er_p.matcher(url);
      if(er_m.find()) { // 非规定的url
        Calendar now = Calendar.getInstance(); // 判断url中图片名称前三位是否是年月日，若不是，则取当前时间
        year = String.valueOf(now.get(Calendar.YEAR));
        month = String.valueOf(now.get(Calendar.MONTH) + 1);
        day = String.valueOf(now.get(Calendar.DAY_OF_MONTH));
        imageName = er_m.group(1);
        imageType = er_m.group(2).toLowerCase();
        for(int i = 0; i < IMAGE_TYPE.length; i++) {
          if(imageType.indexOf(IMAGE_TYPE[i]) > -1) {
            imageType = IMAGE_TYPE[i];
            break;
          }
        }
      }
      else {
        throw new IOException("param url Format is error");
      }
    }
    imageType = imageType.replaceAll("/[^\\.]+$", "");// 处理url中图片结尾还有内容的
    date = year + "/" + month + "/" + day;
    String ts = String.valueOf(System.currentTimeMillis());
    String oldName = imageName.replace("_", "").replace(".", "");
    imageName = imageName.replace("_", "").replace(".", "") + ts.substring(2, 10);
    Map<String, String> map = new HashMap<String, String>();
    StringBuffer urlResult = new StringBuffer();// 拼接缩放后图片的互联网访问地址
    urlResult.append("http://" + Consts.cdnDomain + "/");
    // urlResult.append(Consts.CMS_PIC_AGREED);
    urlResult.append("mobileme/pic/cms");
    urlResult.append("/" + date + "/");
    urlResult.append(imageName + "." + imageType);
    try {
      if(StringUtils.isNotBlank(localUrlJson)) {
        Iterator<JsonNode> i = JacksonUtil.getJsonMapper().readTree(localUrlJson).elements();
        while(i.hasNext()) {
          String s = i.next().asText();
          if(s.contains(oldName)) {
            map.put("src", s);
            break;
          }
        }
      }
      if(!map.containsKey("src")) {
        S3Util s3 = S3Util.getInstance(Consts.endPoint, Consts.accessKey, Consts.secretKey);
        PutObjectResult result = s3.putRemotePicture(url, Consts.cdnDomainKey + "mobileme/pic/cms/" + date + "/" + imageName + "."
            + imageType);
        if(result != null && StringUtils.isNotBlank(result.getETag())) {
          map.put("src", urlResult.toString());
        }
        else {
          throw new Exception("put pic error ! url : " + url);
        }
      }
    }
    catch(Exception e) {
      throw e;
    }
    return map;
  }

  public static void putLocalPic(InputStream inStream, long length, String key) throws Exception {
    try {
      S3Util s3 = S3Util.getInstance(Consts.endPoint, Consts.accessKey, Consts.secretKey);
      PutObjectResult result = s3.putLocalPictureInStream(inStream, length, key);
      if(result == null || StringUtils.isBlank(result.getETag())) {
        throw new Exception("put pic error file:" + key);
      }
    }
    catch(Exception e) {
      throw e;
    }
  }

  private static int createNewImage(BufferedImage originalImage, String date, String imageName, String imageType,
      int newWidth, String originalAddress, String url) throws Exception {/*
    String newResult = Consts.CMS_PIC_PATH + "/" + date + "/" + imageName + "_" + newWidth + "." + imageType; // 拼接缩放后图片的物理地址
    int newHeight = 0;
    if(imageType.equals("gif")) {
      if(newWidth >= originalImage.getWidth()) {
        copyImage(originalAddress, newResult);
        newHeight = originalImage.getHeight() * newWidth / originalImage.getWidth();
      }
      else {
        newHeight = ImageUtil.createGifThumbnail(originalImage, originalAddress, newWidth, newResult);// gif图片缩放功能
        if(new File(newResult).length() >= 4 * 1024 * 1024) {
          copyImage(originalAddress, newResult);
          newHeight = originalImage.getHeight() * newWidth / originalImage.getWidth();
        }
      }
    }
    else {
      if(newWidth >= originalImage.getWidth()) {
        copyImage(originalAddress, newResult);
        newHeight = originalImage.getHeight() * newWidth / originalImage.getWidth();
      }
      else {
        BufferedImage newImage = null;
        newImage = ImageUtil.createThumbnail(originalImage, newWidth, ImageUtil.ZOOM_TYPE.WIDTH.getValue());// 返回计算等比例缩放后的图片对象
        if(newImage == null) {
          throw new IOException("original image not exist or size error");
        }
        FileOutputStream out = new FileOutputStream(newResult);
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(newImage);
        out.flush();
        out.close();
        newHeight = newImage.getHeight();
        if(new File(newResult).length() >= 4 * 1024 * 1024) {
          copyImage(originalAddress, newResult);
          newHeight = originalImage.getHeight() * newWidth / originalImage.getWidth();
        }
      }
    }
    return newHeight;
  */  return 0;
    }

  private static List<String> searchFileByPath(String localPath, final String imageName) throws Exception {
    File path = new File(localPath);
    if(!path.exists()) {// 创建目录
      path.mkdirs();
    }
    List<String> list = Arrays.asList(new File(localPath).list(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.startsWith(imageName);
      }
    }));
    return list;
  }

  public static boolean checkCntVideoUrl(String url) {
    try {
      StringBuffer checkCntVideoUrl = new StringBuffer();
      checkCntVideoUrl.append("http://17173.tv.sohu.com/[vz]_\\d+_\\d+/([a-zA-Z0-9=]+).html");
      checkCntVideoUrl.append("|http://17173.tv.sohu.com/v/\\d+/\\d+/\\d+/([a-zA-Z0-9=]+)");
      checkCntVideoUrl.append("|http://17173.tv.sohu.com/seq/\\w+/([a-zA-Z0-9=]+).html");
      checkCntVideoUrl.append("|http://17173.tv.sohu.com/playlist_\\d+/([a-zA-Z0-9=]+).html");
      java.util.regex.Pattern p = java.util.regex.Pattern.compile(checkCntVideoUrl.toString());
      java.util.regex.Matcher m = p.matcher(url);
      while(m.find()) {
        return true;
      }
      return false;
    }
    catch(Exception e) {
      return false;
    }
  }

  public static String getVlogIdByUrl(String url) {
    String vlogId = "";
    try {
      StringBuffer checkCntVideoUrl = new StringBuffer();
      checkCntVideoUrl.append("http://17173.tv.sohu.com/[vz]_\\d+_\\d+/([a-zA-Z0-9=]+).html");
      checkCntVideoUrl.append("|http://17173.tv.sohu.com/v/\\d+/\\d+/\\d+/([a-zA-Z0-9=]+)");
      checkCntVideoUrl.append("|http://17173.tv.sohu.com/seq/\\w+/([a-zA-Z0-9=]+).html");
      checkCntVideoUrl.append("|http://17173.tv.sohu.com/playlist_\\d+/([a-zA-Z0-9=]+).html");
      java.util.regex.Pattern p = java.util.regex.Pattern.compile(checkCntVideoUrl.toString());
      java.util.regex.Matcher m = p.matcher(url);
      while(m.find()) {
        if(m.group(1) == null) {
          if(m.group(2) == null) {
            if(m.group(3) == null) {
              if(m.group(4) == null) {
              }
              else {
                vlogId = new String(SecurityUtil.decryptBASE64(m.group(4)+"=="));
              }
            }
            else {
              vlogId = new String(SecurityUtil.decryptBASE64(m.group(3)+"=="));
            }
          }
          else {
            vlogId = new String(SecurityUtil.decryptBASE64(m.group(2)));
          }
        }
        else {
          vlogId = new String(SecurityUtil.decryptBASE64(m.group(1)+"=="));
        }
        return vlogId;
      }
      return vlogId;
    }
    catch(Exception e) {
      return vlogId;
    }
  }

  private static Map<String, String> judgeOriginalSize(String urls) throws IOException {
    Map<String, String> map = new HashMap<String, String>();
    map.put("true", "0");
    HttpURLConnection conn = null;
    try {
      URL url = new URL(urls);// 打开链接
      conn.setFollowRedirects(false);// 去掉url重定向功能
      conn = (HttpURLConnection) url.openConnection();
      if(conn.getResponseCode() != 200) {
        map.put("noFind", "0");
      }
      int limitSize = 4 * 1024 * 1024;
      if(conn.getContentLength() >= limitSize) {
        map.put("bigSize", conn.getContentLength() + "");
      }
    }
    finally {
      if(conn != null) {// 关闭连接
        conn.disconnect();
      }
    }
    return map;
  }

  private static void copyImage(String sourceDir, String targetDir) throws IOException {
    FileOutputStream outStream = null;
    try {
      File file = new File(sourceDir);
      FileInputStream inStream = new FileInputStream(file);
      ByteArrayOutputStream bOutStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int len = 0;
      while((len = inStream.read(buffer)) != -1) {
        bOutStream.write(buffer, 0, len);
      }
      inStream.close();
      byte[] data = bOutStream.toByteArray();
      outStream = new FileOutputStream(new File(targetDir));
      outStream.write(data);
    }
    catch(IOException e) {
      e.printStackTrace();
    }
    finally {
      if(outStream != null) {
        outStream.close();
      }
    }
  }
  
  public static void main(String[] args) {
    try {
      //System.out.println(getThumbnailImage("http://images.17173.com/2014/bns//2014/03/14/2014031.410.5042630.png",
       //   false));
      // System.out.println(putPictureByS3("http://images.17173.com/2014/bns//2014/03/14/20140314105042630.png",
      // null));
      System.out.println(StringUtils.length("0 ".trim()));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
//  	public static String getPicUrl(Picture pic) {
//	    if(pic == null) {
//	      return "";
//	    }
//	    StringBuilder url = new StringBuilder("http://");
////	    url.append(Constants.PIC_DOMAIN.DOMAIN_3.getValue());
//	    url.append(Consts.cdnDomain);
//	    url.append("/");
//	    Calendar calendar = Calendar.getInstance();
//	    calendar.setTime(pic.getCreateDate());
////	    url.append(calendar.get(Calendar.YEAR));
////	    url.append(Consts.CMS_PIC_AGREED);
//	    url.append(Consts.CMS_PIC_FOLDER);
//	    url.append("/" + Constants.formatDate(Constants.SDF.YYYYMMDD.toString(), pic.getCreateDate()).replaceAll("-", "/") + "/");
//	    url.append(pic.getId());
//	    url.append("." + pic.getSuffix());
//	    url.append("?" + pic.getTs());
//	    return url.toString();
//  	}
  	
  	public static String decryptToken(HttpServletRequest request) {
      String token = null;
      String keyStr = request.getHeader("k");
      String tokenStr = request.getHeader("c");
      if (StringUtils.isNotBlank(tokenStr) && StringUtils.isNotBlank(keyStr)) {
          try {
              String keyAndMd5 = SecurityUtil.rsaDecode(keyStr, privateKey);
              String key = keyAndMd5.substring(0, keyAndMd5.length() - 16);
              token = SecurityUtil.desDecode(tokenStr, key);
          } catch (Exception e) {
              //logger.error(e.getMessage());
            e.printStackTrace();
          }
      } else {
          //logger.warn("forbid access for unexpected k: {} and c {}", keyStr, tokenStr);
          return null;
      }

      return StringUtils.isNotBlank(token) ? token.trim() : token;
  }

}
