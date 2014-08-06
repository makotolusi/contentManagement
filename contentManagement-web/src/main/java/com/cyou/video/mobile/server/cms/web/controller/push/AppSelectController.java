package com.cyou.video.mobile.server.cms.web.controller.push;

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

import com.cyou.video.mobile.server.cms.service.push.AppSelectService;
import com.cyou.video.mobile.server.common.Constants;

/**
 * 
 * 
 * @author LUSI
 */
@Controller
@RequestMapping("/web/activity/appSelect")
public class AppSelectController {

	private Logger logger = LoggerFactory.getLogger(AppSelectController.class);

	@Autowired
	private AppSelectService appSelectService;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView pushListPage(ModelMap model) {
		try {
			return new ModelAndView("/activity/appselect", "appList", model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}


	@RequestMapping(value = "/listApp", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap listApp(@RequestBody
      Map<String, Object> params, ModelMap model) {
		try {
//		  model.addAttribute("total", 100);
			model.put("page",appSelectService.listApp(params));
			model.addAttribute("message",
					Constants.CUSTOM_ERROR_CODE.SUCCESS.toString());

		} catch (Exception e) {
			logger.error(
					"[method: addApp()]  : error! "
							+ e.getMessage(), e);
			model.addAttribute("message", e.getMessage());
			e.printStackTrace();
		}
		return model;
	}
}
