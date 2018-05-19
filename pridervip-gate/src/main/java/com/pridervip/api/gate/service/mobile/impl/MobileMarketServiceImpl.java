package com.bighuobi.api.gate.service.mobile.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.bighuobi.api.entity.PushMessage;
import com.bighuobi.api.entity.PushPriceNotice;
import com.bighuobi.api.enums.CurrencyType;
import com.bighuobi.api.enums.PushActionType;
import com.bighuobi.api.enums.PushDeviceType;
import com.bighuobi.api.enums.PushPriceCondition;
import com.bighuobi.api.enums.PushPriceStatus;
import com.bighuobi.api.enums.PushPriceType;
import com.bighuobi.api.enums.PushRecvIdType;
import com.bighuobi.api.enums.PushStatus;
import com.bighuobi.api.enums.RedisIndex;
import com.bighuobi.api.gate.mobile.entity.Code;
import com.bighuobi.api.gate.mobile.entity.CodeConfig;
import com.bighuobi.api.gate.mobile.entity.Exchange;
import com.bighuobi.api.gate.mobile.entity.PriceNoticeFormatConfig;
import com.bighuobi.api.gate.service.mobile.ThreadEx;
import com.bighuobi.api.gate.service.mobile.impl.GeTuiPushServiceImpl.PushWorker;
import com.bighuobi.api.gate.trade.entity.MarketBitstampEntity;
import com.bighuobi.api.gate.trade.entity.MarketEntity;
import com.bighuobi.api.gate.trade.entity.MarketHuobiEntity;
import com.bighuobi.api.gate.trade.entity.UsdMarketEntity;
import com.bighuobi.api.gate.utils.ConstantDef;
import com.bighuobi.api.gate.utils.Constants;
import com.bighuobi.api.gate.utils.HttpUtil;
import com.bighuobi.api.gate.utils.JsonUtil;
import com.bighuobi.api.gate.utils.RedisUtils;
import com.bighuobi.api.gate.utils.XMLUtils;
import com.bighuobi.api.service.PushPriceNoticeService;
import com.bighuobi.api.service.PushService;

@Service("mobileMarketService")
public class MobileMarketServiceImpl {

	private Logger logger = LoggerFactory
			.getLogger(MobileMarketServiceImpl.class);

//	@Resource
//	private RedisUtils redisUtils;


	@Autowired
	private PushPriceNoticeService pushPriceNoticeService;

	@Autowired
	private List<CodeConfig> config_home_code_list;
	
	private Map<String, CodeConfig> m_config_home_code_map;

	@Autowired
	private List<PriceNoticeFormatConfig> config_price_notice_format_list;

	private Map<String, PriceNoticeFormatConfig> m_config_price_notice_format_map;
	

	// 美元转人民币
	private static final String USD_TO_CNY = StringUtils.trimToEmpty(Constants
			.getMessage("usd_to_cny"));
	private static String USD_CNY_KEY = "USD_CNY_RATE";

    private static final int PRICE_NOTICE_TASK_INDEX = Integer.valueOf(Constants.getMessage("PRICE_NOTICE_TASK_INDEX"));
	
	private static final int PRICE_NOTICE_INTERVAL_TIMEOUT = Integer.valueOf(Constants
			.getMessage("PRICE_NOTICE_INTERVAL_TIMEOUT"));
	
	private static final int PRICE_NOTICE_PAGE_SIZE = Integer.valueOf(Constants
			.getMessage("PRICE_NOTICE_PAGE_SIZE"));
	
	private static final String PUSH_URL_PUSHMESSAGE = Constants
			.getMessage("PUSH_URL_PUSHMESSAGE");

	private static final String PRICE_NOTICE_SOURCE = Constants
			.getMessage("PRICE_NOTICE_SOURCE");
	

	private static boolean m_bInit = false;
	private Map<Integer, MarketWorker> m_workers = new HashMap<Integer, MarketWorker>();

	private static final int MARKET_THREAD_TIMEOUT_MIN = 2000;

	@PostConstruct
	public void poll() {
		if (m_bInit) {
			return;
		}

		m_bInit = true;


		// String[] code = Constants.MOBILE_HOME_CODE.split(",");
		// service = Executors.newScheduledThreadPool(code.length + 1);
		// // 汇率较稳定
		// service.scheduleAtFixedRate(new RateThread(), 0, 60,
		// TimeUnit.SECONDS);
		// // 行情 2秒一次 与客户端相同
		// for (String key : code) {
		// service.scheduleAtFixedRate(new MarketThread(key), 0, 2,
		// TimeUnit.SECONDS);
		// }

		// config
		m_config_home_code_map = new HashMap<String, CodeConfig>();

		int index = 0;
		for (CodeConfig tempCode : config_home_code_list) {
			if (m_config_home_code_map.containsKey(tempCode.getCode())) {
				continue;
			}

			if(tempCode.getIsStop())
			{
				continue;
			}
			
			m_config_home_code_map.put(tempCode.getCode(), tempCode);

			m_workers.put(index, new MarketWorker(index, "MarketThread",
					tempCode));
			
			index++;
		}
		
		for (int i = 0; i < m_workers.size(); i++) {
			MarketWorker worker = m_workers.get(i);
			worker.start();
		}

		// language config
		m_config_price_notice_format_map = new HashMap<String, PriceNoticeFormatConfig>();

		for (PriceNoticeFormatConfig tempCode : config_price_notice_format_list) {
			if (m_config_price_notice_format_map.containsKey(tempCode.getLang())) {
				continue;
			}

			m_config_price_notice_format_map.put(tempCode.getLang(), tempCode);
		}
	}

	@PreDestroy
	public void destory() {
		for (MarketWorker worker : m_workers.values()) {
			worker.stopRunning();
		}
		
		m_bInit = false;
	}

	// ////////////////////////////
	// 行情获取线程开始
	// ////////////////////////////

	public class MarketWorker extends ThreadEx {
		private int id;
		private String name;

		private CodeConfig m_codeConfig;
		private int m_currency = 0;

		public MarketWorker(int id, String name, CodeConfig codeConfig) {
			super();

			this.id = id;
			this.name = name;
			this.m_codeConfig = codeConfig;
			
			if(CurrencyType.CNY.getName().equals(this.m_codeConfig.getCurrency()))
			{
				this.m_currency = CurrencyType.CNY.getValue();
			}
			else if(CurrencyType.USD.getName().equals(this.m_codeConfig.getCurrency()))
			{
				this.m_currency = CurrencyType.USD.getValue();
			}
		}

		public String getName() {
			return this.name + this.id;
		}

		@Override
		protected void runProcess() {

			try {
				try {
					Thread.sleep(200);
				} catch (Exception e) {
//					e.printStackTrace();
//					Thread.currentThread().interrupt();
				}

				long timeStart = 0;
				
				while (isRunning == true) {
					try {
						try {
							long timeCurrent = System.currentTimeMillis();
							
							long timeSleep = MARKET_THREAD_TIMEOUT_MIN - (timeCurrent - timeStart);
							if(timeSleep > 0)
							{
								Thread.sleep(timeSleep);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
						    logger.error("{}",e);;
							logger.error(e.getMessage());
						}
						
						timeStart = System.currentTimeMillis();
						
						marketProcess();
					} catch (Exception e) {
					    logger.error("{}",e);;
					}

				}

				System.out.println(this.name + this.id + " stopped!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
			    logger.error("{}",e);;
			}
		}

		private void marketProcess() {
			try {
				Exchange e = null;
				switch (m_codeConfig.getCode()) {
				case "btccny":
					e = getMarkert(m_codeConfig.getUrl(), false);
					break;
				case "ltccny":
					e = getMarkert(m_codeConfig.getUrl(), false);
					break;
                case "btcusd":
//                    e = getUsdMarket(m_codeConfig.getUrl());
					e = getMarkert(m_codeConfig.getUrl(), false);
                    break;    
				case "btccnyquarter":
					e = getMarkert(m_codeConfig.getUrl(), false);
					break;
				case "btccnyquarter2":
					e = getMarkert(m_codeConfig.getUrl(), false);
					break;
				case "btccnyweek":
					e = getMarkert(m_codeConfig.getUrl(), false);
					break;
				case "btccnyweek2":
					e = getMarkert(m_codeConfig.getUrl(), false);
					break;
				case "huobibtc":
					e = getMarkert(m_codeConfig.getUrl(), false);
					break;
				case "huobiltc":
					e = getMarkert(m_codeConfig.getUrl(), false);
					break;
				case "okcoinbtc":
					e = getOKMarket(m_codeConfig.getUrl());
					break;
				case "okcoinltc":
					e = getOKMarket(m_codeConfig.getUrl());
					break;
				case "chinabtc":
					e = getOKMarket(m_codeConfig.getUrl());
					break;
				case "chinaltc":
					e = getOKMarket(m_codeConfig.getUrl());
					break;
				case "bitstampbtc":
					e = getBitStamMarket(m_codeConfig.getUrl());
					break;
				case "btc-ebtc":
					e = getBtcE_Markert(m_codeConfig.getUrl());
					break;
				case "btc-eltc":
					e = getBtcE_Markert(m_codeConfig.getUrl());
					break;
				case "bitfinex_btc":
					e = getBitfinex_Markert(m_codeConfig.getUrl());
					break;
				case "bitfinex_ltc":
					e = getBitfinex_Markert(m_codeConfig.getUrl());
					break;
				default:
					logger.info("no key :{}", m_codeConfig.getCode());
				}
				
				if (e != null
						&& e.getCprice() != null
						&& new BigDecimal(e.getCprice())
								.compareTo(BigDecimal.ZERO) > 0) {
					e.setCode(m_codeConfig.getCode());
					if (new BigDecimal(e.getHprice())
							.compareTo(BigDecimal.ZERO) == 0) {
						e.setHprice(e.getCprice());
					}
					if (new BigDecimal(e.getLprice())
							.compareTo(BigDecimal.ZERO) == 0) {
						e.setLprice(e.getCprice());
					}
					
//					redisUtils.set(RedisIndex.COMMON.getValue(),
//							m_codeConfig.getCode(), JsonUtil.toString(e), 0L);
					
					// 开始进行价格提醒
					priceNoticeProcess(e);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
			    logger.error("{}",e);;
			}
		}
		
		public void priceNoticeProcess(Exchange priceTicker) {
			try {
				// 根据当前价格轮询
				
				int limitSize = PRICE_NOTICE_PAGE_SIZE;
				BigDecimal currentPrice = new BigDecimal(priceTicker.getCprice());
				
				BigDecimal tempTimes = new BigDecimal(PushPriceType.PRICE_UNIT_TIMES);
				BigDecimal tempPrice = currentPrice.multiply(tempTimes);
				
				long price = tempPrice.longValue();
				
				int currency = m_currency;
				int priceType = PushPriceType.LAST_PRICE.getValue();
				
				// 大于最新成交价
				int priceCondition = PushPriceCondition.GREATER_THEN.getValue();
				long timeMax = System.currentTimeMillis() / 1000 - PRICE_NOTICE_INTERVAL_TIMEOUT;
				
				// 更新最后推送状态
			    pushPriceNoticeService.updateLastPushStatus(PRICE_NOTICE_TASK_INDEX, 
			    		m_codeConfig.getCode(), PushPriceStatus.OPEN.getValue(), price, currency, priceType, priceCondition);
				
				while(true)
				{
				    List<PushPriceNotice> noticeList = pushPriceNoticeService.getWaitingPriceNoticeList(PRICE_NOTICE_TASK_INDEX, 
				    		m_codeConfig.getCode(), PushPriceStatus.OPEN.getValue(), price, currency, priceType, priceCondition, (int)timeMax, limitSize);
				    if(noticeList == null || noticeList.size() <= 0)
				    {
				    	break;
				    }
				    
				    // 发送价格提醒消息
				    sendPriceNotice(price, priceTicker, priceType, priceCondition, noticeList);
				}
				
				// 小于最新成交价
				priceCondition = PushPriceCondition.LESS_THEN.getValue();
				timeMax = System.currentTimeMillis() / 1000 - PRICE_NOTICE_INTERVAL_TIMEOUT;
				
				// 更新最后推送状态
			    pushPriceNoticeService.updateLastPushStatus(PRICE_NOTICE_TASK_INDEX, 
			    		m_codeConfig.getCode(), PushPriceStatus.OPEN.getValue(), price, currency, priceType, priceCondition);

			    while(true)
				{
				    List<PushPriceNotice> noticeList = pushPriceNoticeService.getWaitingPriceNoticeList(PRICE_NOTICE_TASK_INDEX, 
				    		m_codeConfig.getCode(), PushPriceStatus.OPEN.getValue(), price, currency, priceType, priceCondition, (int)timeMax, limitSize);
				    if(noticeList == null || noticeList.size() <= 0)
				    {
				    	break;
				    }
				    
				    // 发送价格提醒消息
				    sendPriceNotice(price, priceTicker, priceType, priceCondition, noticeList);
				}
			    
			} catch (Exception e) {
				// TODO Auto-generated catch block
			    logger.error("{}",e);;
			}
		}
		
		public boolean sendPriceNotice(long price, Exchange priceTicker, int priceType, int priceCondition, List<PushPriceNotice> noticeList) {
			boolean bRet = false;
			
			try {
				if(priceTicker == null || noticeList == null || noticeList.size() <= 0)
				{
					return bRet;
				}
				
				String recvId = "";
				Integer recvIdType = 0;
				Integer deviceType = 0;
				Integer actionType = 0;
				String source = "";
				String title = "";
				String content = "";
				
				Map<String, Object> dataField = new HashMap<String, Object>();
				List<Integer> idList = new ArrayList<Integer>();

				for(PushPriceNotice tempItem : noticeList)
				{
					deviceType = tempItem.device_type;
					if(deviceType == PushDeviceType.IPHONE.getValue())
					{
						// ios APN push
//						actionType = PushActionType.IOS_APN.getValue();
//						recvId = tempItem.device_token;
//						recvIdType = PushRecvIdType.DEVICE_TOKEN.getValue();
						
						// iOS Transmission Push
						actionType = PushActionType.TRANSMISSION.getValue();
						recvId = tempItem.cid;
						recvIdType = PushRecvIdType.CID.getValue();
					}
					else
					{
						actionType = PushActionType.OPEN_APP.getValue();
						recvId = tempItem.cid;
						recvIdType = PushRecvIdType.CID.getValue();
					}
					
					source = PRICE_NOTICE_SOURCE;
					
					PriceNoticeFormatConfig tempConfig = m_config_price_notice_format_map.get(tempItem.getLang());
					
					String strName = m_codeConfig.getName();
					
					String strLang = tempItem.getLang();
					if(strLang != null && strLang.equals("en_US"))
					{
						strName = m_codeConfig.getNameEn();
					}
					
					BigDecimal tempTimes = new BigDecimal(PushPriceType.PRICE_UNIT_TIMES);
					BigDecimal tempPrice = new BigDecimal(tempItem.getPrice());
					
					BigDecimal priceSet = tempPrice.divide(tempTimes).setScale(2, BigDecimal.ROUND_UP);
					
					content = formatNoticeMessage(tempConfig, strLang, priceType, priceCondition, strName, 
							priceTicker.getCprice(), priceSet.toString());
					
					if(content == null || content.equals(""))
					{
						logger.error("price notice content is null error:item id = {}", tempItem.id);
					    continue;	
					}
					title = tempConfig.getTitle();
					if(deviceType == PushDeviceType.IPHONE.getValue())
					{
						title = content;
					}
					
					Map<String, Object> postMap = new HashMap<String, Object>();
					
					postMap.put(ConstantDef.FIELD_RECVID, recvId);
					postMap.put(ConstantDef.FIELD_RECVIDTYPE, recvIdType);
					postMap.put(ConstantDef.FIELD_DEVICETYPE, deviceType);
					postMap.put(ConstantDef.FIELD_ACTIONTYPE, actionType);
					postMap.put(ConstantDef.FIELD_SOURCE, source);
					postMap.put(ConstantDef.FIELD_TITLE, title);
					postMap.put(ConstantDef.FIELD_CONTENT, content);
					
					try {
						String strRet = HttpUtil.post(PUSH_URL_PUSHMESSAGE, postMap, HttpUtil.DEFAULT_ENCODING);
						
						JSONObject objRet = JsonUtil.toJsonObj(strRet);
						
						Integer status = JsonUtil.getNodeJSonValueIntNumber(objRet, "status");
						if(status == null || status != 0)
						{
							// 出错，记录
							logger.error("price notice post error:item id = {}, error = {}", tempItem.id, strRet);
						}

						// 更新最后发送的时间
						dataField.clear();
						idList.clear();
						
						long nTiemNow = System.currentTimeMillis() / 1000;
						dataField.put("time_last_push", (int)nTiemNow);
						dataField.put("status_last_push", 1);
						dataField.put("price_last_push", price);
						idList.add(tempItem.id);
						
					    int nRet = pushPriceNoticeService.updatePriceNoticeStatus(dataField, idList);
					    if(nRet <= 0)
					    {
							// 出错，记录
							logger.error("price notice update time last push error:item id = {}", tempItem.id);
					    }
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("price notice post error:{}", e.getMessage());
					}
				}
				
				bRet = true;
			} catch (Exception e) {
				bRet = true;
				logger.error("price notice post error:{}", e.getMessage());
			}
			
			return bRet;
		}
	}

	public String formatNoticeMessage(PriceNoticeFormatConfig tempConfig, String lang, int priceType, int priceCondition, String codeName, String priceNotice, String priceFocus) {
		String strTemp = "";
		
		try {
			if(tempConfig == null)
			{
				return strTemp;
			}
			
			if(lang == null || lang.equals(""))
			{
				return strTemp;
			}
			
			if(PushPriceType.LAST_PRICE.getValue() == priceType)
			{
				if(PushPriceCondition.GREATER_THEN.getValue() == priceCondition)
				{
					strTemp = String.format(tempConfig.getFormatLastPriceGreat(), codeName, priceNotice, priceFocus);
				}
				else if(PushPriceCondition.LESS_THEN.getValue() == priceCondition)
				{
					strTemp = String.format(tempConfig.getFormatLastPriceLess(), codeName, priceNotice, priceFocus);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
		    logger.error("{}",e);;
			strTemp = "";
		}
		
		return strTemp;
	}
	
	// ////////////////////////////
	// 行情获取线程结束
	// ////////////////////////////


	/**
	 * 比特币期货周
	 * 
	 * @return
	 * @throws IOException
	 */
	public Exchange getMarkert(String url, boolean isFuture) {
		Exchange e = new Exchange();
		try {
			String result = HttpUtil.doGet(url);
			if (StringUtils.isEmpty(result)) {
				return null;
			}
			logger.debug("{}", result);
			if (isFuture) {
				MarketEntity entity = JsonUtil.toObject(result,
						MarketEntity.class);
				if (entity == null) {
					return null;
				}
				e.setAmount(entity.getVol());
				e.setLprice(entity.getLow());
				e.setHprice(entity.getHigh());
				e.setCprice(entity.getLast());
				e.setOprice(entity.getOpen());
			} else {
				MarketHuobiEntity entity = JsonUtil.toObject(result,
						MarketHuobiEntity.class);
				if (entity == null) {
					return null;
				}
				e.setAmount(entity.getAmount());
				e.setLprice(entity.getP_low());
				e.setHprice(entity.getP_high());
				e.setCprice(entity.getP_new());
				e.setOprice(entity.getP_open());
			}
		} catch (SocketTimeoutException ex) {
			logger.debug("time out {}", url);
		} catch (Exception ex) {
			logger.debug("{} {}", url, ex);
		}
		return e;
	}
    
    public Exchange getUsdMarket(String url) {
        Exchange e = new Exchange();
        try {
            String result = HttpUtil.doGet(url);
            if (StringUtils.isEmpty(result)) {
                return null;
            }
            logger.debug("{}", result);
            UsdMarketEntity entity = JsonUtil.toObject(JsonUtil.getFieldString(result, "ticker"), UsdMarketEntity.class);
            e.setAmount(entity.getVol());
            e.setLprice(entity.getLow());
            e.setHprice(entity.getHigh());
            e.setCprice(entity.getLast());
            e.setOprice(entity.getOpen());
        } catch (SocketTimeoutException ex) {
            logger.debug("time out {}", url);
        } catch (Exception ex) {
            logger.debug("{} {}", url, ex);
        }
        return e;
    }

	public Exchange getOKMarket(String url) {
		Exchange e = new Exchange();
		// e.setCode(code);
		// e.setName(name);
		try {
			String result = HttpUtil.doGet(url);
			if (StringUtils.isEmpty(result)) {
				return null;
			}
			logger.debug("{}", result);
			MarketEntity entity = JsonUtil.toObject(
					JsonUtil.getFieldString(result, "ticker"),
					MarketEntity.class);
			e.setAmount(entity.getVol());
			e.setLprice(entity.getLow());
			e.setHprice(entity.getHigh());
			e.setCprice(entity.getLast());
			// e.setOprice(entity.getOpen());
		} catch (SocketTimeoutException ex) {
			logger.debug("time out {}", url);
		} catch (Exception ex) {
			logger.debug("{} {}", url, ex);
		}
		return e;
	}

	public Exchange getBitStamMarket(String url) {
		Exchange e = new Exchange();
		try {
			String result = HttpUtil.doGet(url);
			if (StringUtils.isEmpty(result)) {
				return null;
			}
			logger.debug("{}", result);
			MarketBitstampEntity entity = JsonUtil.toObject(result,
					MarketBitstampEntity.class);
			e.setAmount(entity.getVolume());
			e.setLprice(entity.getLow());
			e.setHprice(entity.getHigh());
			e.setCprice(entity.getLast());
			// e.setCprice2(usd2Cny(entity.getLast()));
		} catch (SocketTimeoutException ex) {
			logger.debug("time out {}", url);
		} catch (Exception ex) {
			logger.debug("{} {}", url, ex);
		}
		return e;
	}

	public Exchange getBtcE_Markert(String url) {
		Exchange e = new Exchange();
		try {
			String result = HttpUtil.doGet(url);
			if (StringUtils.isEmpty(result)) {
				return null;
			}
			logger.debug("{}", result);
			MarketEntity entity = JsonUtil.toObject(
					JsonUtil.getFieldString(result, "ticker"),
					MarketEntity.class);
			if (entity == null) {
				return null;
			}
			e.setAmount(entity.getVol_cur());
			e.setLprice(entity.getLow());
			e.setHprice(entity.getHigh());
			e.setCprice(entity.getLast());
			e.setOprice(entity.getOpen());

			// e.setCprice2(usd2Cny(entity.getLast()));
		} catch (SocketTimeoutException ex) {
			logger.debug("time out {}", url);
		} catch (Exception ex) {
			logger.debug("{} {}", url, ex);
		}
		return e;
	}

	/**
	 * 获取bitfinex的行情
	 * 
	 * @param bitfinexBtc
	 * @return
	 */
	public Exchange getBitfinex_Markert(String url) {
		Exchange e = new Exchange();
		try {
			String result = HttpUtil.doGet(url);
			if (StringUtils.isEmpty(result)) {
				return null;
			}
			logger.debug("{}", result);
			Map<String, Object> entity = JsonUtil.toObject(result, Map.class);
			if (entity == null) {
				return null;
			}
			e.setAmount(StringUtils
					.trimToEmpty(entity.get("volume").toString()));
			e.setLprice(StringUtils.trimToEmpty(entity.get("low").toString()));
			e.setHprice(StringUtils.trimToEmpty(entity.get("high").toString()));
			e.setCprice(StringUtils.trimToEmpty(entity.get("last_price")
					.toString()));
			e.setOprice(StringUtils.trimToEmpty(entity.get("last_price")
					.toString()));

			// e.setCprice2(usd2Cny(StringUtils.trimToEmpty(entity.get(
			// "last_price").toString())));
		} catch (SocketTimeoutException ex) {
			logger.debug("time out {}", url);
		} catch (Exception ex) {
			logger.debug("{} {}", url, ex);
		}
		return e;
	}

	// private String usd2Cny(String usd) throws IOException,
	// ParserConfigurationException, DocumentException {
	// Object rate = redisUtils.get(RedisIndex.COMMON.getValue(), USD_CNY_KEY);
	// if (rate == null) {
	// return null;
	// }
	// BigDecimal r = new BigDecimal(usd).multiply(
	// new BigDecimal(rate.toString())).setScale(4,
	// RoundingMode.HALF_DOWN);
	// return r.toPlainString();
	// }
	//
	// private class RateThread implements Runnable {
	//
	// @Override
	// public void run() {
	// try {
	// String price = getRate();
	// if (StringUtils.isNotEmpty(price)) {
	// redisUtils.set(RedisIndex.COMMON.getValue(), USD_CNY_KEY,
	// price, 0L);
	// }
	// } catch (Exception e) {
	//
	// }
	// }
	//
	// }
	//
	// private String getRate() throws IOException,
	// ParserConfigurationException,
	// DocumentException {
	// String result = HttpUtil.doGet(USD_TO_CNY);
	// if (StringUtils.isNotEmpty(result)) {
	// return XMLUtils.getNodeValue(result);
	// }
	// logger.info("Can't get rate {}", USD_TO_CNY);
	// return null;
	// }

}
