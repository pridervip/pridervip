package com.bighuobi.api.gate.controller.api;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.spi.CurrencyNameProvider;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
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

import org.json.*;

@Controller
public class PushPriceNoticeController {

	Logger logger = LoggerFactory.getLogger(PushPriceNoticeController.class);

	@Resource
	private RedisUtils redisUtils;
	
	@Autowired
	private PushPriceNoticeService pushPriceNoticeService;

	
    private static final int PRICE_NOTICE_TASK_INDEX = Integer.valueOf(Constants.getMessage("PRICE_NOTICE_TASK_INDEX"));
    
    
//	private static SimpleDateFormat sdf = new SimpleDateFormat("MMdd");

	
//	@RequestMapping(value = "/mobile/bindcid", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	@RequestMapping(value = "/push/addpricenotice")
	@ResponseBody
	public Object addPriceNotice(@RequestParam String lang, 
			@RequestParam String cid, 
			@RequestParam Integer deviceType,
			@RequestParam(required = false, defaultValue="") String deviceToken, 
			@RequestParam(required = false, defaultValue="") Integer userId, 
			@RequestParam String priceListJson) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		JSONArray tempArray = JsonUtil.toJsonArray(priceListJson);
		if(tempArray == null)
		{
			throw new CustomerException("priceListJson", -1000, ExceptionType.MOBILE);
		}

		if(lang == null || lang.equals(""))
		{
			throw new CustomerException("lang", -1000, ExceptionType.MOBILE);
		}
		
		if(lang.equals("cn"))
		{
			lang = "zh_CN";
		}
		else if(lang.equals("en"))
		{
			lang = "en_US";
		}
		
		if(cid == null || cid.equals(""))
		{
			throw new CustomerException("cid", -1000, ExceptionType.MOBILE);
		}
		
		// 如果数据为空，则删除所有的价格提醒
		if(tempArray.length() <= 0)
		{
			// 删除之前的的价格提醒设置
			long count = pushPriceNoticeService.deletePriceNoticeByCid(cid);
			if(count < 0)
			{
				throw new CustomerException(-1002, ExceptionType.MOBILE);
			}
			else
			{
				map.put("result", 2);
			}
			
			return map;
		}

		// deviceType
		if(deviceType == null || deviceType.equals("") 
				|| deviceType == PushDeviceType.UNKNOWN.getValue())
		{
			throw new CustomerException("deviceType", -1000, ExceptionType.MOBILE);
		}
		
		if(deviceType == PushDeviceType.IPHONE.getValue())
		{
			if(deviceToken == null || deviceToken.equals(""))
			{
				throw new CustomerException("deviceToken", -1000, ExceptionType.MOBILE);
			}
		}
		else
		{
			deviceToken = "";
		}
		
		// userId
		if(userId == null || userId.equals(""))
		{
			userId = 0;
		}
		
		
//		// 获取已经设置的价格提醒
//		List<PushPriceNotice> listPriceOld = pushPriceNoticeService.getPriceNoticeListByCid(cid);
//		if(listPriceOld != null && listPriceOld.size() > 0)
//		{
//			//
//		}
		
		// 删除之前的的价格提醒设置
		long count = pushPriceNoticeService.deletePriceNoticeByCid(cid);
		if(count < 0)
		{
			throw new CustomerException(-1002, ExceptionType.MOBILE);
		}
		
		List<PushPriceNotice> listPriceNotice = new ArrayList<PushPriceNotice>();
		JSONObject tempJson = null;
		
		PushPriceNotice tempItem = null;
		for(int i = 0; i < tempArray.length(); i++)
		{
			tempJson = JsonUtil.getNodeJSonItem(tempArray, i);
			
			tempItem = new PushPriceNotice();
			
			tempItem.setCid(cid);
			tempItem.setDeviceToken(deviceToken);
			tempItem.setDeviceType(deviceType);
			tempItem.setUserId(userId);
			tempItem.setTaskIndex(PRICE_NOTICE_TASK_INDEX);
			tempItem.setStatus(PushPriceStatus.OPEN.getValue());
			tempItem.setTimeLastPush(0);
			
			String symbolId = JsonUtil.getNodeJSonValue(tempJson, "symbolId");
			BigDecimal price = JsonUtil.getNodeJSonValueBigDecimal(tempJson, "price");
			
			Integer currency = JsonUtil.getNodeJSonValueInt(tempJson, "currency");
			Integer priceType = JsonUtil.getNodeJSonValueInt(tempJson, "priceType");
			Integer priceCondition = JsonUtil.getNodeJSonValueInt(tempJson, "priceCondition");
			
			if( symbolId == null || symbolId.equals("") )
			{
				throw new CustomerException("symbolId", -1000, ExceptionType.MOBILE);
			}
			
			if( price == null || price.floatValue() <= 0.0 )
			{
				throw new CustomerException("price", -1000, ExceptionType.MOBILE);
			}
			
			if( currency == null || currency.equals(""))
			{
				throw new CustomerException("currency", -1000, ExceptionType.MOBILE);
			}
			
			if( priceType == null || priceType.equals(""))
			{
				throw new CustomerException("priceType", -1000, ExceptionType.MOBILE);
			}
			
			if( priceCondition == null || priceCondition.equals(""))
			{
				throw new CustomerException("priceCondition", -1000, ExceptionType.MOBILE);
			}
			
			if( ( priceType != PushPriceType.LAST_PRICE.getValue() 
				     && priceType != PushPriceType.FIRST_ASK.getValue()
				     && priceType != PushPriceType.FIRST_BID.getValue()
				     && priceType != PushPriceType.HIGHEST_24.getValue()
				     && priceType != PushPriceType.LOWEST_24.getValue()
				     )
				)
			{
				throw new CustomerException("priceType", -1000, ExceptionType.MOBILE);
			}
			
			if( ( currency != CurrencyType.CNY.getValue() 
			         && currency != CurrencyType.USD.getValue()
			         )
				)
			{
				throw new CustomerException("currency", -1000, ExceptionType.MOBILE);
			}
			
			
			if(priceType == PushPriceType.LAST_PRICE.getValue() 
			     || priceType == PushPriceType.FIRST_ASK.getValue()
			     || priceType == PushPriceType.FIRST_BID.getValue())
			{
				if(priceCondition != PushPriceCondition.GREATER_THEN.getValue() 
						&& priceCondition != PushPriceCondition.LESS_THEN.getValue())
				{
					throw new CustomerException("priceCondition", -1000, ExceptionType.MOBILE);
				}
			}
			else if(priceType == PushPriceType.HIGHEST_24.getValue()
				 || priceType == PushPriceType.LOWEST_24.getValue())
			{
				priceCondition = PushPriceCondition.UNKNOWN.getValue();
			}
			
			tempItem.setSymbolId(symbolId);
			
			BigDecimal tempTimes = new BigDecimal(PushPriceType.PRICE_UNIT_TIMES);
			BigDecimal tempPrice = price.multiply(tempTimes);
			
			tempItem.setPrice(tempPrice.longValue());
			tempItem.setCurrency(currency);
			tempItem.setLang(lang);
			tempItem.setPriceType(priceType);
			tempItem.setPriceCondition(priceCondition);
			
			listPriceNotice.add(tempItem);
			
			// 判断是否已经存在
		}
		
		map.put("cid", cid);
		map.put("deviceToken", deviceToken);
		map.put("userId", userId);
		map.put("deviceType", deviceType);
		long nId = pushPriceNoticeService.insertPriceNotice(listPriceNotice);
		if(nId > 0)
		{
			map.put("result", 1);
		}
		else
		{
			map.put("result", 0);
			
			throw new CustomerException(-1005, ExceptionType.MOBILE);
		}
		
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
	
}
