package com.cyou.video.mobile.server.cms.rest.intercept;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 请求校验拦截器
 * 
 * @author jyz
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {
//
//    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
//
//    @Autowired
//    private UserTokenService userTokenService;
//
//    @Autowired
//    private ChannelService channelService;
//
//    private static final String ANDROID = "android";
//    private static final String IOS = "iOS";
//    private static final String defaultChannel = "A0010001001";
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        
//        String ua = request.getHeader("User-Agent");
//
//        if (StringUtils.isBlank(ua) || (!ua.contains("17173") && !ua.contains("shouyoucentre"))) {
//            String i = request.getHeader("i");
//            if(StringUtils.isBlank(i)) {
//                logger.warn("access for unknown User-Agent : {}",ua);
//            } else {
//                ua = i;
//            }
//        }
//
//        String token = PrivateUtil.decryptToken(request);
//
//        try {
//            ByteUtils.checkKey(token);
//        } catch (Exception e) {
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            logger.warn("msg: {} for c {} and k: {}", e.getMessage(), request.getHeader("c"), request.getHeader("k"));
//            return false;
//        }
//
//        int appType = Consts.APP_TYPE.GAMETOPLINE.getValue();
//        
//        if (StringUtils.isBlank(ua) || ua.contains("17173_")){
//          appType = Consts.APP_TYPE.GAMETOPLINE.getValue();
//          request.setAttribute("appName", "MOBILEME_YXKK");// 173app
//        }else if (ua.contains("17173gl@")) {// 是最强攻略的请求
//            appType = Consts.APP_TYPE.ZQSTRATEGY.getValue();
//            String appName = ua.substring(ua.indexOf("@") + 1, ua.indexOf("_"));
//            request.setAttribute("appName", appName);// 最强攻略的app名称
//        }else if(ua.contains("shouyoucentre")){
//            appType = Consts.APP_TYPE.SHOUYOUCENTER.getValue();
//        }
//
//        UserToken userToken = userTokenService.getToken(token);
//        if (userToken == null) {
//            userToken = new UserToken();
//            userToken.setToken(token);
//            String os = getDefaultOSByUA(ua);
//            userToken.setOs(os);
//            userToken.setUa(os);
//            userToken.setRes("NA");
//            userToken.setChannel(getChannelByUA(ua));
//            userToken.setPlat(getDefaultPlatByUA(ua));
//            logger.warn("never find user token {} ,create new one...", token);
//            userTokenService.createToken(userToken);
//        }
//
//        if (userToken.getStatus() == STATUS.OFF.getValue()) {
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            return false;
//        }
//
//        userTokenService.updateTokenById(userToken.getId());
//        request.setAttribute("plat", userToken.getPlat());
//        String channel = getChannelByUA(ua);
//        request.setAttribute("qianChannel", channel);//仅供检查版本升级用
//        if (StringUtils.isNotBlank(channel) && channelService.getChannelById(channel, Constants.STATUS.TRUE.getValue(), appType) == null) {
//          channel = "";
//        }
//        request.setAttribute("channel", channel);
//        request.setAttribute("appType", appType);
//        request.setAttribute("userToken", userToken);
//        return true;
//    }
//
//    private String getDefaultOSByUA(final String ua) {
//        if (ua == null)
//            return ANDROID;
//        return ua.indexOf(ANDROID) > -1 ? ANDROID : IOS;
//    }
//
//    private int getDefaultPlatByUA(final String ua) {
//        if (ua == null)
//            return PLAT.ANDROID.getValue();
//        return ua.indexOf(ANDROID) > -1 ? PLAT.ANDROID.getValue() : PLAT.IOS.getValue();
//    }
//
//    private String getChannelByUA(final String ua) throws Exception {
//        if (ua == null) {
//            return defaultChannel;
//        }
//        String channel = "";
//        String[] list = ua.split("_");
//        if (list != null && list.length >= 3) {
//            channel = list[2].replace(" ", "");
//        }
//        return channel;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
//            throws Exception {
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//    }

}
