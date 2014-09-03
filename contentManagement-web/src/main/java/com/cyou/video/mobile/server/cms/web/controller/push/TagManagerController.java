package com.cyou.video.mobile.server.cms.web.controller.push;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.model.push.PushTagCollection;
import com.cyou.video.mobile.server.cms.model.sys.ContentType;
import com.cyou.video.mobile.server.cms.service.utils.HttpUtils;
import com.cyou.video.mobile.server.common.Constants;
import com.cyou.video.mobile.server.common.utils.HttpUtil;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

/**
 * 
 * 推送一些手动设置项 只有管理员可以看到的页面
 * 
 * @author LUSI
 */
@Controller
@RequestMapping("/web/tagprovider")
public class TagManagerController {

  private Logger logger = LoggerFactory.getLogger(TagManagerController.class);

  @Autowired
  private MongoOperations mongoTemplate;

  @RequestMapping(value = "/get", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap get(@RequestParam
  String index, HttpServletRequest request, ModelMap model) {
    try {
      String serviceName = request.getParameter("serviceName");
      ContentType contentType = mongoTemplate.findOne(new Query(new Criteria("index").is(index)), ContentType.class);
      JSONObject p = new JSONObject();
      p.put("curPage", "1");
      p.put("pageSize", "10");
      if(!StringUtils.isEmpty(serviceName)) p.put("serviceName", serviceName);
      if(contentType.getResourceUri().indexOf("?") > 0) {
        String url = contentType.getResourceUri().split("\\?")[0];
        String pa = contentType.getResourceUri().split("\\?")[1];
        p.put(pa.split("=")[0], pa.split("=")[1]);
        contentType.setResourceUri(url);
      }
      String str = HttpUtils.postJosn(contentType.getResourceUri(), p);
      JSONArray array = new JSONObject(str).getJSONObject("page").getJSONArray("content");
      List<PushTagCollection> tags = new ArrayList<PushTagCollection>();
      for(int i = 0; i < array.length(); i++) {
        PushTagCollection tag = new PushTagCollection();
        JSONObject jo = (JSONObject) array.get(i);
        tag.setTagId(jo.getString("tagId"));
        tag.setTagName(jo.getString("tagName"));
        tags.add(tag);
      }
      model.addAttribute("data", tags);
      model.addAttribute("message", Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());
    }
    catch(Exception e) {
      logger.error("[method: listPush()] Get Push list : error! " + e.getMessage(), e);
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
    }
    return model;
  }

  @RequestMapping(value = "/actionTagList", method = RequestMethod.POST)
  @ResponseBody
  public ModelMap actionTagList(@RequestBody
  Map<String, String> params, HttpServletRequest request, ModelMap model) {
    try {
      List<String> id = Arrays.asList(params.get("ids").split(","));
      Pagination page = new Pagination();
      List<ContentType> contentType = mongoTemplate.find(new Query(new Criteria("index").in(id)), ContentType.class);
      List<PushTagCollection> tags = new ArrayList<PushTagCollection>();
      for(int i = 0; i < contentType.size(); i++) {
        PushTagCollection tag = new PushTagCollection();
        ContentType jo = (ContentType) contentType.get(i);
        tag.setTagId(jo.getTagConstant());
        tag.setTagName(jo.getName());
        tags.add(tag);
      }
      page.setContent(tags);
      model.addAttribute("page", page);
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
