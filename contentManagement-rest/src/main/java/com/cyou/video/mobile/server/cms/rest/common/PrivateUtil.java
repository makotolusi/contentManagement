package com.cyou.video.mobile.server.cms.rest.common;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cyou.video.mobile.server.cms.common.Consts;
import com.cyou.video.mobile.server.common.Constants;
import com.cyou.video.mobile.server.common.utils.SecurityUtil;

public class PrivateUtil {

    private static Logger logger = LoggerFactory.getLogger(PrivateUtil.class);

    private static final String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJz/J1OgkpF3ONcB7kHcWoJzqVMNs/cc" + "\r"
            + "aao79tPodAGXlN/K2jub2rliZnjIKxSIuAcM4PK4HpznXRPpNGjHplECAwEAAQ==" + "\r";

    private static final String privateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAnP8nU6CSkXc41wHu" + "\r"
            + "QdxagnOpUw2z9xxpqjv20+h0AZeU38raO5vauWJmeMgrFIi4Bwzg8rgenOddE+k0" + "\r"
            + "aMemUQIDAQABAkAYqEZP7xc1+4iU1DvMYkRg2rOMVMmgFPlPgE166LZXjW0c3Fbg" + "\r"
            + "JUD1MtNaZpy2WKLwRDiPtzAZLCy2RmNtTi2xAiEAywvgExwpoGEzidgsYnyRMPew" + "\r"
            + "MRDJSZPEdjPGIKQMVy0CIQDF8NTF93mP9wqPmG0/IZIriUCP2ZUZ646/HrxKqsBC" + "\r"
            + "NQIgc99bCIzR1Iyj9M5AxhOAaAlxqw6BUFPbkfkJ4Ca+RCECIQCHbgemi3Q49CXd" + "\r"
            + "qcTVdPq1nur1gUFqwqigSz85NyrkIQIgLeUNfPLdNDuIWJi7IpPFII1HN+Y5DSPC" + "\r" + "Do+1Oth8Pyk=" + "\r";

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
                logger.error(e.getMessage());
            }
        } else {
            logger.warn("forbid access for unexpected k: {} and c {}", keyStr, tokenStr);
            return null;
        }

        return StringUtils.isNotBlank(token) ? token.trim() : token;
    }


    public static enum UA_TYPE {
        Android_Phone(1), Android_PAD(2), IOS_IPhone(3), IOS_IPAD(4), Windows_Phone8(5), Win8(6), Android_TV(7);
        private final int type;

        private UA_TYPE(int type) {
            this.type = type;
        }

        public int getValue() {
            return type;
        }
    }
    public static final String APP_DOWNLOAD_URL = "http://a.17173.com/m/index.html?downloadurl=";
    
    public static String getMainChannel(int plat,String project){
      String channel = "";
      if(StringUtils.isEmpty(project) || project.equals("MOBILEME_YXKK")){
        switch(plat) {
          case 7 :
            channel = Constants.MAIN_CHANNEL.ANDROID.getValue();
            break;
          case 6 :
            channel = Constants.MAIN_CHANNEL.IOS.getValue();
            break;
          case 11 :
            channel = Constants.MAIN_CHANNEL.IOS.getValue();
            break;
          default :
            break;
        }
      }else{
        switch(plat) {
          case 7 :
            channel = "shouyou";
            break;
          case 6 :
            channel = "AppStore-Iphone";
            break;
          case 11 :
            channel = "AppStore-Iphone";
            break;
          default :
            break;
        }
      }
      return channel;
    }
}
