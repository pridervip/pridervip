package com.pridervip.gate.controller.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;


@Controller
public class PushBindController {

	Logger logger = LoggerFactory.getLogger(PushBindController.class);

	@RequestMapping(value = "/push/bindcid2userid")
	@ResponseBody
	public Object bindCid2UserId(@RequestParam String cid,
			@RequestParam(required = false, defaultValue="") String deviceToken,
			@RequestParam(required = false, defaultValue="") Integer userId,
			@RequestParam Integer deviceType) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();


		return map;
	}
}
