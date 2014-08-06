package com.cyou.video.mobile.server.cms.web.controller.sys;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.service.sys.ManagerLogService;
import com.cyou.video.mobile.server.common.Constants;

/**
 * 用户日志controller
 * 
 * @author zs
 */
@Controller
@RequestMapping("/web/sys/managerLog")
public class ManagerLogController {
  private Logger logger = LoggerFactory.getLogger(ManagerLogController.class);
  
  private ManagerLogService userLogInfoService;
  
  @RequestMapping(method = RequestMethod.GET)
  public ModelAndView listUserLogPage() {
    return new ModelAndView("/system/managerLog");
  }
  
  @RequestMapping(method=RequestMethod.POST)
  @ResponseBody
  public ModelMap listUserLog(@RequestBody Map<String, String> params, ModelMap model) {
    try {
      String userName = params.get("userName");
      String modelName = params.get("modelName");
      int type = Integer.parseInt(params.get("type"));
      int curPage = Integer.parseInt(params.get("curPage"));
      int pageSize = Pagination.PAGESIZE;
      if(params.get("pageSize") != null) {
        pageSize = Integer.parseInt(params.get("pageSize"));
      }
      Date from = null;
      if(params.get("from") != null) {
        from = Constants.parseDate(Constants.SDF.YYYYMMDD.toString(), params.get("from"));
      }
      Date to = null;
      if(params.get("to") != null) {
        to = Constants.parseDate(Constants.SDF.YYYYMMDD.toString(), params.get("to"));
      }
      logger.info("[method: listUserLog()] Get user log list by params:{userName="+userName+",modelName="+modelName+",type="+type+"," +
      		"curPage="+curPage+",Date from="+params.get("from")+",to="+params.get("to")+"}");
      Pagination pagination = userLogInfoService.listLogInfo(curPage, pageSize, type, userName, modelName, from, to);
      model.addAttribute("page", pagination);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: listUserLog()] Get user log list : error!"+e.getMessage(),e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }
}
