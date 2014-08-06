package com.cyou.video.mobile.server.cms.job.push;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cyou.video.mobile.server.common.Constants;

@Controller()
@RequestMapping("/job/push")
public class PushJobController {

	private Logger logger = LoggerFactory.getLogger(PushJobController.class);

	@Autowired
	private PushScheduler pushScheduler;

	@RequestMapping(value = "/newJob", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap newJobAndTriggerInfo(@RequestParam("pushId") String pushId,
			HttpServletRequest request, ModelMap model) {
		if (request.getParameter("expression") == null) {
			logger.warn("push id  and expression is null ");
			model.put("success", "false");
			return model;
		} else {
			try {
				pushScheduler.initScheduler();
				model.putAll(pushScheduler.newPushJob(pushId + "", request
						.getParameter("expression").toString()));
				model.put("success", "true");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.warn(e.getMessage());
				model.put("success", "false");
				model.put("msg", e.getMessage());
				return model;
			}

			return model;
		}
	}

	/**
	 * 得到trigger信息
	 * 
	 * @param map
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getTriggerInfo", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap getTrigerInfo(@RequestParam("pushId") String pushId, ModelMap model) {
		if (pushId == null)
			logger.warn("push id  and expression is null ");
		else {
			try {
				pushScheduler.initScheduler();
				JobDetail job = pushScheduler.getJobByPushId(pushId);
				Trigger[] triggers = pushScheduler.getTriggerByPushId(pushId);
				if (job == null) {
					logger.info("job is null ");
					return model;
				} else {
					List<Map<String, String>> triggerList = new ArrayList<Map<String, String>>();
					for (int i = 0; i < triggers.length; i++) {
						Trigger t = triggers[i];
						CronTrigger ct = (CronTrigger) triggers[i];
						Map<String, String> tri = new HashMap<String, String>();
						tri.put("name", t.getName());
						if (t.getStartTime() != null)
							tri.put("startTime", Constants.formatDate(
									Constants.SDF.YYYYMMDDHHMMSS.toString(),
									t.getStartTime()));
						if (t.getNextFireTime() != null)
							tri.put("nextFireTime", Constants.formatDate(
									Constants.SDF.YYYYMMDDHHMMSS.toString(),
									t.getNextFireTime()));
						if (t.getPreviousFireTime() != null)
							tri.put("previousFireTime", Constants.formatDate(
									Constants.SDF.YYYYMMDDHHMMSS.toString(),
									t.getPreviousFireTime()));
						tri.put("triggerState",
								pushScheduler.getTrigerState(pushId) + "");
						triggerList.add(tri);
						model.put("cronExpression", ct.getCronExpression());
					}
					model.put("isStateful", job.isStateful());
					model.put("isDurable", job.isDurable());
					model.put("isVolatile", job.isVolatile());

					if (triggerList != null && !triggerList.isEmpty()) {
						Map<String, String> map = triggerList.get(0);
						model.putAll(map);
					}

				}
				// pushScheduler
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return model;
	}

	@RequestMapping(value = "/updateTrigger", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap updateTrigger(HttpServletRequest request, ModelMap model) {
		if (request.getParameter("pushId") == null
				|| request.getParameter("expression") == null) {
			logger.warn("push triggerName  and expression is null ");
			model.put("success", "false");
		} else {
			try {
				pushScheduler.initScheduler();
				System.out.println(request.getParameter("pushId")+"-----------"+
            request.getParameter("expression"));
				pushScheduler.updateTriger(request.getParameter("pushId"),
						request.getParameter("expression"));
				model.put("success", "true");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				model.put("success", "false");
			}

		}
		return model;
	}

	@RequestMapping(value = "/deleteJob", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap deleteJob(@RequestParam("pushId") String pushId, ModelMap model) {
		if (pushId == null) {
			logger.warn("push id  is null , deleteJob failed ");
			model.put("success", "false");
			return model;
		} else {
			try {
				pushScheduler.initScheduler();
				pushScheduler.deleteJob(pushId);
//				if (t)
//					model.put("success", "true");
//				else
					model.put("success", "true");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				model.put("success", "false");
			}

			return model;
		}
	}

	/**
	 * pause job
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pauseJob", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap pauseJob(@RequestParam("pushId") String pushId, ModelMap model) {
		if (pushId == null) {
			logger.warn("push id  is null , deleteJob failed ");
			model.put("success", "false");
			return model;
		} else {
			try {
				pushScheduler.initScheduler();
				pushScheduler.pauseJob(pushId);
				model.put("success", "true");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				model.put("success", "false");
			}

			return model;
		}
	}

	/**
	 * resume job
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/resumeJob", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap resumeJob(@RequestParam("pushId") String pushId, ModelMap model) {
		if (pushId == null) {
			logger.warn("push id  is null , deleteJob failed ");
			model.put("success", "false");
			return model;
		} else {
			try {
				pushScheduler.initScheduler();
				pushScheduler.resumeJob(pushId);
				model.put("success", "true");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				model.put("success", "false");
			}

			return model;
		}
	}

}
