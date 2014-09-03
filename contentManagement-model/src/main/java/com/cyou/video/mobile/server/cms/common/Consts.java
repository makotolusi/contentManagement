package com.cyou.video.mobile.server.cms.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.cyou.video.mobile.server.common.IEnumDisplay;

/**
 * CMS常量类
 * 
 * @author jyz
 */
public class Consts {

  public static final String SESSION_MANAGER = "cms_session_manager";

  // public static final String CID_URL =
  // "http://10.5.121.118:8090/index/getCidByLiveRoomId.action?liveRoomId=";

  public static final String CID_URL = "http://v.17173.com/live/index/getCidByLiveRoomId.action?liveRoomId=";

  public static final String COLLECTION_CLIENT_LOG_NAME = "ClientLogCollection"; // 原始数据

  public static final String COLLECTION_CLIENT_LOG_WALKTHROUGH_NAME = "ClientLogBestWalkthroughCollection"; // 攻略收集手机app
  
  public static final String COLLECTION_BEST_WALKTHROUGH_INSTALLED_NAME = "BestWalkthroughInstalledGames"; // 攻略收集手机app
  
  public static final String COLLECTION_USER_ITEM_OPERATE_PV_NAME = "UserItemOperatePv"; // 用户行文

  public static final String COLLECTION_USER_ITEM_OPERATE_TAG_STATE = "UserItemOperateTagState"; // mongo

  public static final String COLLECTION_USER_SUBSCRIBE = "UserSubScribe"; // 用户订阅

  public static final String COLLECTION_USER_TOP = "UserTop"; // 置顶

  public static final String COLLECTION_USER_FAVORITE = "UserFavorate"; // 收藏

  public static final String COLLECTION_ITEM_OPERATE_PV_NAME = "ItemOperatePv"; // 行为

  public static final String COLLECTION_ITEM_PV_NAME = "ItemPv"; // 内容

  public static final String COLLECTION_USER_GAME_PV = "UserGamePv"; // gamecode访问量

  public static final String COLLECTION_ITEM_TAG = "PushTagCollection"; // mongo

  public static final String COLLECTION_KEYWORD_PV_NAME = "SearchKeyWordPv"; // mongo

  public static final String COLLECTION_SHARE_PV_NAME = "ShareKeyWordPv"; // mongo

  public static final String COLLECTION_JOB_MAKE_TAG = "makeTag"; // mongo

  public static final String COLLECTION_USER_KEYWORD_PV_NAME = "UserSearchKeyWordPv"; // mongo

  public static final String COLLECTION_MY_SUBSCRIBE = "-102"; // mongo

  public static final int LIST_MAX_SIZE = 10000;//list存储最大条数

  public static final String TABOO_FILTER_RESULT_KEY_CONTENT = "result";

  public static final String TABOO_FILTER_RESULT_KEY_FOUND = "words";

  public static final List<Character> TABOO_SPECIAL_CHARACTER = new ArrayList<Character>() {
    private static final long serialVersionUID = 6775633504910929562L;
    {
      add('.');
      add('$');
      add('^');
      add('{');
      add('[');
      add('(');
      add('|');
      add(')');
      add(']');
      add('}');
      add('*');
      add('+');
      add('?');
      add('\\');
    }
  };

  public static enum SCORE_RULE_TYPE {
    FREE(0), ONEOFF(1), DAILY(2), TOTAL(3);
    private final int type;

    private SCORE_RULE_TYPE(int type) {
      this.type = type;
    }

    public int getValue() {
      return type;
    }
  }

  public static enum SCORE_RECORD_TYPE {
    PERSONAL(0), PERSONAL_VIP(1), CORPORATE(2), CORPORATE_VIP(3);
    private final int type;

    private SCORE_RECORD_TYPE(int type) {
      this.type = type;
    }

    public int getValue() {
      return type;
    }
  }

  public static enum SCORE_DETAIL_TYPE {
    GAIN(1), LOSS(-1);
    private final int type;

    private SCORE_DETAIL_TYPE(int type) {
      this.type = type;
    }

    public int getValue() {
      return type;
    }
  }
  
  public static enum APP_TYPE {
    GAMETOPLINE(1), ZQSTRATEGY(2), SHOUYOUCENTER(3), MSITE(4);
    private final int type;

    private APP_TYPE(int type) {
      this.type = type;
    }

    public int getValue() {
      return type;
    }
  }


	public static enum AD_MEMCACHE_KEY {
		AD_STRATEGY_INDEX("ad_strategy_index"),AD_STRATEGY_INDEX1("ad_strategy_index1"), AD_STRATEGY_DETAIL(
				"ad_strategy_detail"), AD_NEWS_INDEX("ad_news_index"), AD_NEWS_DETAIL(
				"ad_news_detail"), AD_NEWS_COMMON("ad_news_common"), AD_STARTPAGE(
				"ad_start_page"), AD_NEWS_LIST("ad_news_list"), AD_VIDEO_LIST("ad_video_list"), AD_PIC_LIST("ad_pic_list"), AD_PIC_BANNER("ad_pic_banner"),
				AD_NEWS_BANNER("ad_news_banner"), AD_VIDEO_BANNER("ad_video_banner"),
				//AD_STRATEGY_COMMON("ad_strategy_common"), 
				AD_VIDEO_INDEX(
				"ad_video_index"), AD_VIDEO_DETAIL("ad_video_detail"), AD_DATA(
				"ad_data"), AD_VIDEO_COMMON("ad_video_common"), 
				AD_DB("ad_db"), AD_SUDOKU("ad_sudoku"), AD_TURNS_PIC("ad_turns_pic"), 
				AD_WORD_LINK("word_link"), AD_PRIMARY_GUIDE("primary_guide"), AD_NOTICE_WORD_LINK("notice_word_link"), INDEX_TURNS_PIC("index_turns_pic");
		private final String key;


    private AD_MEMCACHE_KEY(String key) {
      this.key = key;
    }

    public String getValue() {
      return key;
    }
  }

  public static enum SCORE_MEMCACHE_KEY {
    RULE("score_rule_map"), RULE_CONDITION("score_rule_condition_map");
    private final String key;

    private SCORE_MEMCACHE_KEY(String key) {
      this.key = key;
    }

    public String getValue() {
      return key;
    }
  }

  public static enum POP_GAME_MEMCACHE_KEY {
    TOP_CATE_LIST("popgame_top_cate_list"), TOP_GAME_MAP("popgame_top_game_map"), GAME_INFO_MAP("popgame_game_info_map"), GAME_NEWS_LIST(
        "popgame_news_list_"), GAME_VIDEO_LIST("popgame_video_list_"), GAME_PIC_LIST("popgame_pic_list_"), GAME_NEWS_DETAIL(
        "popgame_newsRela_list_"), GAME_VIDEO_DETAIL("popgame_videoRela_list_");
    private final String key;

    private POP_GAME_MEMCACHE_KEY(String key) {
      this.key = key;
    }

    public String getValue() {
      return key;
    }
  }

  public enum CONTENT_TYPE implements IEnumDisplay {
    NEWS("新闻", 1), VIDEO("视频", 2), PIC("图片", 3);
    public String name;

    public int index;

    private CONTENT_TYPE(String name, int index) {
      this.name = name;
      this.index = index;
    }

    public String getName() {
      return this.name;
    }

    public int getIndex() {
      return this.index;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }

  public static enum STRATEGY_MENU_MEMCACHE_KEY {
    MENU("strategyMenuById_"), SUBMENULIST("strategySubMenuListByPid_"), ENABLE_SUBMENULIST("enableStrategySubMenuListByPid_"),FMENULISTBYSID(
        "strategyFMenuListByStrategyId_"), STRATEGYPICGROUP("strategyPicGroupById_"), STRATEGYMENUNEWSLIST(
        "strategyMenuNewsListByMenuId_"), STRATEGYMENUNEWSRELALIST("strategyMenuNewsRelaByMenuId_"), STRATEGYMENUVIDEOLIST(
        "strategyMenuVideoListByMenuId_"), STRATEGYMENUVIDEORELALIST("strategyMenuVideoRelaByMenuId_"), STRATEGYNEWS(
        "strategyNewsById_"), STRATEGYVIDEO("strategyVideoById_"), STRATEGYMENUPICGROUPRELALIST(
        "strategyMenuPicGroupRelaByMenuId_"), STRATEGYMENUPICGROUPLIST("strategyMenuPicGroupListByMenuId_"), LISTBUBBLEMENUFORTREE(
        "listBubbleMenuForTree_"), LISTFOCUSIMAGEBYSTRATEGYID("listFocusImageByStrategyId_"), LISTMENUBUBBLEBYSTRATEGYID(
        "listMenuBubbleByStrategyId_"), STRATEGYPICLIST("strategyPicListByPicGroupId_");
    private final String key;

    private STRATEGY_MENU_MEMCACHE_KEY(String key) {
      this.key = key;
    }

    public String getValue() {
      return key;
    }
  }

  /**
   * 内容来源
   * 
   * @author lusi
   */
  public enum CONTENT_SOURCE implements IEnumDisplay {
    STRATEGY_VIDEO("攻略视频", "/cont/strategy/video/", 1), STRATEGY_NEWS("攻略新闻", "/cont/strategy/news/", 2), POP_GAME_VIDEO(
        "新游视频", "/cont/popgame/video/", 3), POP_GAME_NEWS("新游新闻", "/cont/popgame/news/", 4), COLUMN_NEWS("栏目新闻",
        "/ops/news/", 5), COLUMN_VIDEO("栏目视频", "/ops/video/", 6);
    public String name;

    public String url;

    public int index;

    private CONTENT_SOURCE(String name, String url, int index) {
      this.name = name;
      this.url = url;
      this.index = index;
    }

    public String getName() {
      return this.name;
    }

    public int getIndex() {
      return this.index;
    }

    public String getUrl() {
      return this.url;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }

  /**
   * 收集枚举 项目类型 推送展现为 视频，攻略，新闻，活动，囧图，直播
   * 
   * @author lusi
   */
  public enum COLLECTION_ITEM_TYPE implements IEnumDisplay {
    COLUMN("栏目", 0), VIDEO("视频", 1), JIONG("囧图", 2), NEWS("新闻", 3), PIC("图片集", 4), SEARCH("搜索", 5), CHANNEL("渠道", 6), PLATFORM(
        "平台", 7), VERSION("版本", 8), LIVE("直播", 9), APP("应用", 10), WALKTHROUGH("攻略", 11), PICTURE("图片", 12), GIFT("礼包",
        13), ADV("广告", 14), RANK("排行榜", 15), FEEDBACK("反馈", 16), EXTERNAL_LINK("外联", 17), TOOL("工具", 18), PHONE_BIND(
        "手机绑定", 19), APP_REC("应用推荐", 20), ACT_CENTER("活动", 21), NEWS_COLUMN("新闻专题", 22),ENCY("百科", 23),GAME("游戏", 24),NEWS_LIST_PAGE("新闻列表", 25),
        GIFT_TAO_PAGE("礼包淘号列表", 26), GIFT_QIANG_PAGE("礼包抢号列表", 27), OTHER("其它", 28), SHOW("秀场", 28),NULL("", -1), URL("URL", -2);
    public String name;

    public int index;

    private COLLECTION_ITEM_TYPE(String name, int index) {
      this.name = name;
      this.index = index;
    }

    public String getName() {
      return this.name;
    }

    public int getIndex() {
      return this.index;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }

  /**
   * 收集枚举 操作类型
   * 
   * @author lusi
   */
  public enum COLLECTION_OPERATOR_TYPE implements IEnumDisplay {
    VIEW("查看内容", 0), DOWNLOAD("下载", 1), SHARE("分享", 2), FAVORITE("收藏", 3), CANCEL_FAVORITE("取消收藏", 4), SUBSCRIBE("订阅",
        5), CANCEL_SUBSCRIBE("取消订阅", 6), LEAVE_COMMENTS("发表评论", 7), TOP("置顶", 8), CANCEL_TOP("取消置顶 ", 9), INSTALL("安装",
        10), UNINSTALL("卸载 ", 11), BIND("绑定 ", 12), UNBIND("解绑 ", 13), RECEIVE("抢号 ", 14), RANDOM("淘号", 15), DESKTOP(
        "添加至桌面 ", 16);// no pv
    public String name;

    public int index;

    private COLLECTION_OPERATOR_TYPE(String name, int index) {
      this.name = name;
      this.index = index;
    }

    public String getName() {
      return this.name;
    }

    public int getIndex() {
      return this.index;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }

  /**
   * 收集枚举 任务类型
   * 
   * @author lusi
   */
  public enum COLLECTION_PUSH_TAG_JOB_NAME implements IEnumDisplay {
    USER_CHANNEL_TAG("用户渠道标签任务", 0), COMBINATION_TAG("组合标签任务", 1), COLLECTION_UPATE("历史收集数据更新任务", 2), USER_MERGE_TAG(
        "订阅收藏置顶任务", 3), USER_REDUCE_TAG("评论下载查看等", 3),WALKTHROUGH_APP_GAME_TAG("最强攻略", 4);
    public String name;

    public int index;

    private COLLECTION_PUSH_TAG_JOB_NAME(String name, int index) {
      this.name = name;
      this.index = index;
    }

    public String getName() {
      return this.name;
    }

    public int getIndex() {
      return this.index;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }

  /**
   * 任务状态
   * 
   * @author LUSI
   */
  public enum PUSH_JOB_STATE implements IEnumDisplay {
    ENABLE("启用"), DISABLE("暂停");
    private String name;

    private PUSH_JOB_STATE(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }

  /**
   * 标记标签状态
   * 
   * @author LUSI
   */
  public enum PUSH_SEND_TAG_STATE implements IEnumDisplay {
    RUNNING("运行中"), WAITING("等待");
    private String name;

    private PUSH_SEND_TAG_STATE(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }

  /**
   * 推送类型
   * 
   * @author lusi
   */
  public enum PUSH_TYPE implements IEnumDisplay {
    IMMEDIATE("即时推送"), TIMING("定时推送"), AUTO("自动推送"), AUTO_HISTORY("自动推送历史");
    private String name;

    private PUSH_TYPE(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }

  /**
   * 发送状态
   * 
   * @author kevin
   */
  public enum PUSH_SEND_STATE implements IEnumDisplay {
    SEND("已发送"), FAIL("发送失败");
    private String name;

    private PUSH_SEND_STATE(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }

  /**
   * 用户群
   * 
   * @author kevin
   */
  // @JsonFormat(shape= JsonFormat.Shape.OBJECT)
  public enum PUSH_USER_SCOPE implements IEnumDisplay {
    ALL("所有人"), SINGLE("单播"), TAG("标签");
    private String name;

    private PUSH_USER_SCOPE(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }

  /**
   * 广告端类型
   * 
   * @author zhaogz
   */
  public enum AD_AOP_TYPE {
    STRATEGY_INDEX("STRATEGY_INDEX"), STRATEGY_DETAIL("STRATEGY_DETAIL"), NEWS_INDEX("NEWS_INDEX"), VIDEO_INDEX(
        "VIDEO_INDEX"), NEWS_DETAIL("NEWS_DETAIL"), VIDEO_DETAIL("VIDEO_DETAIL"), STRATEGY_INDEX1("STRATEGY_INDEX1"), DB("DB"), 
        SUDOKU("SUDOKU"), WORD_LINK("WORD_LINK"), NOTICE_WORD_LINK("NOTICE_WORD_LINK"), PRIMARY_GUIDE("PRIMARY_GUIDE"), TURNS_PIC("TURNS_PIC"), NEWS_LIST("NEWS_LIST"), NEWS_BANNER("NEWS_BANNER"),
        VIDEO_LIST("VIDEO_LIST"), VIDEO_BANNER("VIDEO_BANNER"), PIC_LIST("PIC_LIST"), PIC_BANNER("PIC_BANNER"), INDEX_TURNS_PIC("INDEX_TURNS_PIC");
    private String name;

    private AD_AOP_TYPE(String name) {
      this.name = name;
    }

    public String getName() {
      return this.name;
    }
  }

  /**
   * 客户端类型
   * 
   * @author lusi
   */
  public enum CLIENT_TYPE {
    IOS("IOS", 6), ANDROID("ANDROID", 7), ALL("ALL", 99);
    private String name;

    private int index;

    private CLIENT_TYPE(String name, int index) {
      this.name = name;
      this.index = index;
    }

    public String getName() {
      return this.name;
    }

    public int getIndex() {
      return this.index;
    }
  }

  /**
   * 设置标签状态
   * 
   * @author lusi
   */
  public enum PUSH_TAG_SETTING_STATE {
    SUC("已设置", 1), NUID("百度用户id不存在", 2), NTOKEN("用户token在mysql中不存在", 3), EXP("EXCEPTION", 4);
    private int index;

    private String name;

    private PUSH_TAG_SETTING_STATE(String name, int index) {
      this.name = name;
      this.index = index;
    }

    public String getName() {
      return this.name;
    }

    public int getIndex() {
      return this.index;
    }
  }

  /**
   * 使用状态
   * 
   * @author kevin
   */
  public enum USE_STATUS implements IEnumDisplay {
    ENABLED("已启用", "ENABLED"), DISABLED("已停用", "DISABLED"), DELETED("已删除", "DELETED");
    private String name;

    private String value;

    private USE_STATUS(String name, String value) {
      this.name = name;
      this.value = value;
    }

    public String getName() {
      return this.name;
    }

    public String getValue() {
      return this.value;
    }
  }

  public static final int SHOUYOU = 1;

  public static final int DUANYOU = 2;

  /**
   * 游戏平台类型
   * 
   * @author lusi
   */
  public enum GAME_PLATFORM_TYPE implements IEnumDisplay {
    MOBILE("手游", 1), PC("端游", 2);
    private String name;

    public int index;

    private GAME_PLATFORM_TYPE(String name, int index) {
      this.name = name;
      this.index = index;
    }

    public String getName() {
      return this.name;
    }

    public int getIndex() {
      return this.index;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }

  /**
   * 推送平台
   * 
   * @author lusi
   */
  public enum PUSH_PLATFORM_TYPE implements IEnumDisplay {
    XINGE("信鸽", 1), BAIDU("百度", 2);
    private String name;

    public int index;

    private PUSH_PLATFORM_TYPE(String name, int index) {
      this.name = name;
      this.index = index;
    }

    public String getName() {
      return this.name;
    }

    public int getIndex() {
      return this.index;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }
  
  /**
   * 
   * m站新闻分享cache_key模板
   * <功能详细描述>
   * 
   * @author  Administrator
   * @version  [版本号, 2014-6-11]
   * @see  [相关类/方法]
   * @since  [产品/模块版本]
   */
  public static enum M_NEWS_MEMCACHE_KEY {
     M_SHARENEWS("shareNewsById_");
    private final String key;

    private M_NEWS_MEMCACHE_KEY(String key) {
      this.key = key;
    }

    public String getValue() {
      return key;
    }
  }
  
  public static enum ACTIVITY_MEMCACHE_KEY {
    ACTIVITY("activityByName_"),ACTIVITYUSERRELA("activityUserRela_");
    private final String key;

    private ACTIVITY_MEMCACHE_KEY(String key) {
      this.key = key;
    }

    public String getValue() {
      return key;
    }
  }
  
  public static final String STRATEGY_KEY = "strategy_";

  public static final String STRATEGY_LIST_KEY = "strategy_list_";

  public static final String STRATEGY_COUNT_KEY = "strategy_count_";

  public static final String REC_TURNS_PIC_KEY = "rec_turns_pic_";

  public static final String REC_TURNS_PIC_LIST_KEY = "rec_turns_pic_list_";

  public static final String REC_TURNS_PIC_COUNT_KEY = "rec_turns_pic_count_";
  
  public static final String COLUMN_STRATEGY = "columnStrategy";
  
  public static final String STRATEGY_GAMECODE_KEY = "strategy_gamecode_";
  
  public static final String ALL_APP = "all_app";
  
  public static final String TIMETABLE_LIST_KEY = "timetable_list_";

  public static final String PLAYER_LOG_NAME = "PlayerLog"; // 播放器日志原始数据
  
  public static final String PLAYER_ADV_NAME = "PlayerAdv"; // 播放器广告
 
  public static final String PLAYER_APPS_NAME = "PlayerApps"; // 播放器广告
  
  public static final String SOHU_RSS_SUB_COLUMNS = "10,7,24,27,92,23,47,173,172"; // sohu rss数据接口提取栏目id v2
  
  public static final String SOHU_RSS_SUB_COLUMNNAMES = "端游头条,手游头条,端游新作,端游单机,端游产业,手游新游,手游评测,手游娱乐,手游硬件"; // sohu rss数据接口提取栏目id名
  
  public static final String SOHU_RSS_INDEX_COLUMNS = "10,24,23,28,27,12,93,97,92,216,47,48,96,172,173"; // sohu rss数据接口提取栏目id v2
  
  public static final String SOHU_RSS_INDEX_COLUMNNAMES = "端游头条,端游新游,手游新游,端游大观,端游单机,端游期待榜,页游,端游话题,端游产业,端游专题,手游评测,手游全球,手游产业,手游硬件,手游娱乐"; // sohu rss数据接口提取栏目id名
  
  public static final int WORD_LINK = 10;
  
  public static final int PRIMARY_GUIDE = 12;

}
