package com.bighuobi.api.gate.controller.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;

import com.bighuobi.api.entity.Cid2UserId;
import com.bighuobi.api.gate.mobile.entity.Code;
import com.bighuobi.api.gate.mobile.entity.Exchange;
import com.bighuobi.api.gate.service.mobile.impl.GeTuiPushServiceImpl;
import com.bighuobi.api.gate.service.mobile.impl.MobileMarketServiceImpl;
import com.bighuobi.api.gate.utils.Constants;
import com.bighuobi.api.gate.utils.JsonUtil;
import com.bighuobi.api.gate.utils.RedisUtils;
import com.bighuobi.api.service.*;
import com.bighuobi.api.entity.*;
import com.bighuobi.api.enums.*;
import com.bighuobi.api.exception.CustomerException;

@Controller
public class PushBindController {

	Logger logger = LoggerFactory.getLogger(PushBindController.class);

	@Resource
	private RedisUtils redisUtils;
	
    @Resource
    private MessageSource messageSource;
    
	@Autowired
	private PushService pushService;

    private static final int GETUI_TASK_INDEX = Integer.valueOf(Constants.getMessage("GETUI_TASK_INDEX"));
    private static final int GETUI_MAX_QUEUE_SIZE = Integer.valueOf(Constants.getMessage("GETUI_MAX_QUEUE_SIZE"));
    
    
//	private static SimpleDateFormat sdf = new SimpleDateFormat("MMdd");

	
//	@RequestMapping(value = "/mobile/bindcid", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	@RequestMapping(value = "/push/bindcid2userid")
	@ResponseBody
	public Object bindCid2UserId(@RequestParam String cid, 
			@RequestParam(required = false, defaultValue="") String deviceToken, 
			@RequestParam(required = false, defaultValue="") Integer userId, 
			@RequestParam Integer deviceType) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		Cid2UserId tempItem = new Cid2UserId();
		
		if(cid == null || cid.equals(""))
		{
			// cid不能为空
			throw new CustomerException("cid", -1000, ExceptionType.MOBILE);
		}
		else
		{
			tempItem.setCid(cid);
		}
		
		if(deviceType == null || deviceType.equals("") 
				|| deviceType == PushDeviceType.UNKNOWN.getValue())
		{
		    // deviceType不能为空
			throw new CustomerException("deviceType", -1000, ExceptionType.MOBILE);
		}
		else
		{
			tempItem.setDeviceType(deviceType);
		}
		
		if(deviceType == PushDeviceType.IPHONE.getValue())
		{
			if(deviceToken == null || deviceToken.equals(""))
			{
				// cid不能为空
				throw new CustomerException("deviceToken", -1000, ExceptionType.MOBILE);
			}
		}
		else
		{
			deviceToken = "";
		}
		tempItem.setDeviceToken(deviceToken);
		
		if(userId == null || userId.equals(""))
		{
			tempItem.setUserId(0);
		}
		else
		{
			tempItem.setUserId(userId);
		}
		
		map.put("cid", cid);
		map.put("deviceToken", deviceToken);
		map.put("userId", userId);
		map.put("deviceType", deviceType);

		Integer nId = pushService.insertOrUpdateCid2UserIdItem(tempItem);
		if(nId > 0)
		{
			map.put("result", 1);
		}
		else
		{
			map.put("result", 0);
			
			throw new CustomerException(-1003, ExceptionType.MOBILE);
		}
		
		return map;
	}
	
	@RequestMapping(value = "/push/pushmessage")
	@ResponseBody
	public Object pushMessage(
			@RequestParam String recvId, 
			@RequestParam Integer recvIdType, 
			@RequestParam Integer deviceType,
			@RequestParam Integer actionType,
			@RequestParam String source, 
			@RequestParam String title, 
			@RequestParam String content,
			@RequestParam(required = false, defaultValue="") String url
			) throws Exception {
		
		// 保存数据到数据库
		
		// 将数据加入发送对列
		
		// 返回
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		PushMessage tempItem = new PushMessage();
		
		if(recvIdType == null || recvIdType.equals("") 
				|| recvIdType == PushRecvIdType.UNKNOWN.getValue())
		{
			throw new CustomerException("recvIdType", -1000, ExceptionType.MOBILE);
		}
		tempItem.recv_id_type = recvIdType;
		
		int queueSize = GeTuiPushServiceImpl.getQueueSize();
		if(queueSize >= GETUI_MAX_QUEUE_SIZE)
		{
			// 等待发送的消息太多，直接加入数据库
			tempItem.status = PushStatus.INIT.getValue();
		}
		else
		{
			// 因为会直接放入发送队列，状态标记为正在发送，发送完成后更改状态。
			tempItem.status = PushStatus.SENDING.getValue();
		}
		
		tempItem.try_times = 0;
		tempItem.time_push = 0;
		tempItem.task_index = GETUI_TASK_INDEX;
		
		if(recvId == null || recvId.equals(""))
		{
			throw new CustomerException("recvId", -1000, ExceptionType.MOBILE);
		}
		else
		{
			tempItem.setRecvId(recvId);
		}
		
		if(deviceType == null || deviceType.equals("") 
				|| deviceType == PushDeviceType.UNKNOWN.getValue())
		{
			throw new CustomerException("deviceType", -1000, ExceptionType.MOBILE);
		}
		else
		{
			tempItem.setDeviceType(deviceType);
		}
		
		if(actionType == null || actionType.equals("") 
				|| actionType == PushActionType.UNKNOWN.getValue())
		{
			throw new CustomerException("actionType", -1000, ExceptionType.MOBILE);
		}
		else
		{
			tempItem.setActionType(actionType);
		}
	
		if(actionType == PushActionType.IOS_APN.getValue())
		{
			if(recvId.length() != 64)
			{
				throw new CustomerException("recvId", -1000, ExceptionType.MOBILE);
			}
		}
		
		if(recvIdType == PushRecvIdType.APP.getValue())
		{
			recvId = "";
			tempItem.setRecvId(recvId);
		}
		
		if(source == null || source.equals(""))
		{
			throw new CustomerException("source", -1000, ExceptionType.MOBILE);
		}
		else
		{
			tempItem.setSource(source);
		}
		
		if(title == null || title.equals(""))
		{
			throw new CustomerException("title", -1000, ExceptionType.MOBILE);
		}
		else
		{
			tempItem.setTitle(title);
		}
		
		if(content == null || content.equals(""))
		{
			throw new CustomerException("content", -1000, ExceptionType.MOBILE);
		}
		else
		{
			tempItem.setContent(content);
		}
		
		if(tempItem.getActionType() == PushActionType.OPEN_URL.getValue())
		{
			if(url == null || url.equals(""))
			{
				throw new CustomerException("url", -1000, ExceptionType.MOBILE);
			}
		}
		tempItem.setUrl(url);
		
		long nId = pushService.insertPushMessage(tempItem);
		if(nId > 0)
		{
			map.put("result", 1);
			
			tempItem.setId((int)nId);
			if(tempItem.status == PushStatus.SENDING.getValue())
			{
				// 加入消息队列
				GeTuiPushServiceImpl.putMessage(tempItem);
			}
		}
		else
		{
			map.put("result", 0);
			
			throw new CustomerException(-1004, ExceptionType.MOBILE);
		}
		
		return map;
	}

	@RequestMapping(value = "/push/pushmessagebyuserid", method = RequestMethod.GET)
	@ResponseBody
	public Object pushMessageByUserId(
			@RequestParam Integer userId, 
			@RequestParam Integer actionType,
			@RequestParam String source, 
			@RequestParam String title, 
			@RequestParam String content,
			@RequestParam(required = false, defaultValue="") String url
			) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(userId <= 0)
		{
			throw new CustomerException("userId", -1000, ExceptionType.MOBILE);
		}
		
		// get cid by userId
		List<Cid2UserId> tempList = pushService.getCid2UserIdListByUserId(userId);
		if(tempList == null || tempList.size() <= 0)
		{
			throw new CustomerException(-1001, ExceptionType.MOBILE);
		}
		
		for(Cid2UserId tempCid : tempList)
		{
			if(tempCid.device_type == PushDeviceType.IPHONE.getValue())
			{
				if(actionType == PushActionType.OPEN_APP.getValue()
						|| actionType == PushActionType.IOS_APN.getValue())
				{
					pushMessage(tempCid.device_token, PushRecvIdType.DEVICE_TOKEN.getValue(), tempCid.device_type, actionType, 
							source, title, content, url);
				}
				else
				{
					pushMessage(tempCid.cid, PushRecvIdType.CID.getValue(), tempCid.device_type, actionType, 
							source, title, content, url);
				}
			}
			else
			{
				pushMessage(tempCid.cid, PushRecvIdType.CID.getValue(), tempCid.device_type, actionType, 
						source, title, content, url);
			}
		}
		
		map.put("result", 1);
		
		return map;
	}
	

	public static void main(String[] args) {
//		Jedis jedis = new Jedis("172.32.1.237", 6379);
//		jedis.select(0);
//		String mills = jedis.hget("futures_treaty", "next_delivery_week");
//		Date date = new Date(Long.valueOf(mills) * 1000);
//		System.out.println(date);
//
//		String da = sdf.format(date);
//		System.out.println(da);
//
//		String bs = jedis.hget("futures_lever", "btc_week");
//		System.out.println(bs);
//		jedis.close();
	}
	
	
	private boolean saveMessageToDB() {
		
		return true;
	}
	
}
