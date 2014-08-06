package com.cyou.video.mobile.server.cms.web.controller.push;

import java.util.HashMap;
import java.util.List;
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

import com.cyou.video.mobile.server.cms.common.Consts;
import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_ITEM_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_OPERATOR_TYPE;
import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.model.collection.StatisticJobLastUpdateTime;
import com.cyou.video.mobile.server.cms.service.collection.ClientLogCollectionService;
import com.cyou.video.mobile.server.common.Constants;
import com.cyou.video.mobile.server.common.Constants.SDF;

/**
 * 
 * 
 * @author LUSI
 */
@Controller
@RequestMapping("/web/activity/pv")
public class PVController {

  private Logger logger = LoggerFactory.getLogger(PVController.class);

  @Autowired
  ClientLogCollectionService clientLogCollectionService;

  @RequestMapping(method = RequestMethod.GET)
  public ModelAndView pushListPage(ModelMap model) {
    try {
      Map<String, Object> m = new HashMap<String, Object>();
      m.put("collectionName", Consts.COLLECTION_CLIENT_LOG_NAME);
      model.put("total", clientLogCollectionService.getTotalNum(Consts.COLLECTION_CLIENT_LOG_NAME));
      model.put("itemType", COLLECTION_ITEM_TYPE.values());
      model.put("operatorType", COLLECTION_OPERATOR_TYPE.values());
      List<StatisticJobLastUpdateTime> jobLastUpdateTimes = clientLogCollectionService.getPVLastUpdateTime(null);
      if(jobLastUpdateTimes != null && !jobLastUpdateTimes.isEmpty())
        model.addAttribute("lastUpdate",
            Constants.formatDate(SDF.YYYYMMDDHHMMSS.toString(), jobLastUpdateTimes.get(0).getLastUpdateTime()));
      return new ModelAndView("/activity/pvlist", "type", model);
    }
    catch(Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }

  }

  @RequestMapping(value = "/listClientLogCollection", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap list(@RequestBody
  Map<String, Object> params, ModelMap model) {
    try {
      params.put("pageSize", Pagination.PAGESIZE);
      Pagination pagination = clientLogCollectionService.getClientLogCollection(params);
      model.addAttribute("total", clientLogCollectionService.getTotalNum(Consts.COLLECTION_CLIENT_LOG_NAME));
      model.addAttribute("page", pagination);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: listPush()] Get Push list : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/listPvCollection", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap listPvCollection(@RequestBody
  Map<String, Object> params, ModelMap model) {
    try {
      Pagination pagination = clientLogCollectionService.getPVByName(params);
      String collectionName = params.get("collectionName").toString();
      model.addAttribute("total", clientLogCollectionService.getTotalNum(collectionName));
      model.addAttribute("page", pagination);

      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: listPush()] Get Push list : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/listTagNameAndPV", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap listTagNameAndPV(@RequestBody
  Map<String, Object> params, ModelMap model) {
    try {
      Pagination pagination = clientLogCollectionService.getTagNameAndPV(params);
      model.addAttribute("page", pagination);

      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: listPush()] Get Push list : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

}
