package com.bighuobi.api.gate.service.mobile.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bighuobi.api.entity.PushMessage;
import com.bighuobi.api.enums.PushActionType;
import com.bighuobi.api.enums.PushDeviceType;
import com.bighuobi.api.enums.PushRecvIdType;
import com.bighuobi.api.enums.PushStatus;
import com.bighuobi.api.gate.service.mobile.ThreadEx;
import com.bighuobi.api.gate.utils.Constants;
import com.bighuobi.api.gate.utils.RedisUtils;
import com.bighuobi.api.service.PushService;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.ITemplate;
import com.gexin.rp.sdk.base.em.EPushResult;
import com.gexin.rp.sdk.base.impl.*;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.exceptions.PushAppException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.APNTemplate;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;

@Service("getTuiPushService")
public class GeTuiPushServiceImpl {
	private static Logger logger = LoggerFactory.getLogger(GeTuiPushServiceImpl.class);
	@Resource
	private RedisUtils redisUtils;
	@Autowired
	private PushService pushService;
	private static final int THREAD_TIMEOUT_MIN = 50;
	private static final int QUEUE_SIZE_MIN = 5;
	private static final int GETUI_TASK_INDEX = Integer.valueOf(Constants
			.getMessage("GETUI_TASK_INDEX"));
	private static final int GETUI_PUSH_THREAD_COUNT = Integer
			.valueOf(Constants.getMessage("GETUI_PUSH_THREAD_COUNT"));
	private static final int GETUI_MAX_QUEUE_SIZE = Integer.valueOf(Constants
			.getMessage("GETUI_MAX_QUEUE_SIZE"));
	private static final String GETUI_URL = Constants.getMessage("GETUI_URL");
	private static final String[] GETUI_APPID = Constants
			.getMessage("GETUI_APPID").split(";");
	private static final String[] GETUI_APPKEY = Constants
			.getMessage("GETUI_APPKEY").split(";");
	private static final String[] GETUI_MASTERSECRET = Constants
			.getMessage("GETUI_MASTERSECRET").split(";");
	private static final int GETUI_OFFLINE_TIMEOUT = Integer.valueOf(Constants
			.getMessage("GETUI_OFFLINE_TIMEOUT"));
	// 消息队列
	private static boolean m_bInit = false;
	private static ConcurrentLinkedQueue<PushMessage> m_msgQueue;
	private static Map<Integer, Integer> m_mapKeyId;
	private Map<Integer, PushWorker> m_workers = new HashMap<Integer, PushWorker>();
	private MessageLoadWorker m_threadLoad = null;
	@PostConstruct
	public void poll() {
	  System.setProperty("http.proxyHost",Constants.PROXY_HOST);
	  System.setProperty("http.proxyPort",""+Constants.PROXY_PORT);
		if (m_bInit) {
			return;
		}
		m_bInit = true;
		m_msgQueue = new ConcurrentLinkedQueue<PushMessage>();
		m_mapKeyId = new HashMap<Integer, Integer>();
		initLoadMessageFromDB();
		for (int i = 0; i < GETUI_PUSH_THREAD_COUNT; i++) {
			m_workers.put(i, new PushWorker(i, "PushThread"));
		}
		for (int i = 0; i < m_workers.size(); i++) {
			PushWorker worker = m_workers.get(i);
			worker.start();
		}
		m_threadLoad = new MessageLoadWorker(0, "LoadThread");
		m_threadLoad.start();
	}

	@PreDestroy
	public void destory() {
		for (PushWorker worker : m_workers.values()) {
			worker.stopRunning();
		}
		m_threadLoad.stopRunning();
		m_bInit = false;
	}
	// 消息队列操作开始
	public synchronized static void putMessage(PushMessage tempItem) {
		if (tempItem == null || tempItem.getId() <= 0) {
			return;
		}
		if (m_mapKeyId.containsKey(tempItem.getId())) {
			return;
		}
		m_msgQueue.add(tempItem);
		m_mapKeyId.put(tempItem.getId(), 1);
	}
	public synchronized static PushMessage popMessage() {
		PushMessage tempItem = null;
		try {
			tempItem = m_msgQueue.poll();
			if (tempItem == null) {
				return tempItem;
			}
			m_mapKeyId.remove(tempItem.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
		  logger.error("{}",e);;
		}
		return tempItem;
	}
	public synchronized static int getQueueSize() {
		return m_msgQueue.size();
	}
	// ////////////////////////////
	// 消息队列操作结束
	// ////////////////////////////
	// ////////////////////////////
	// 消息加载线程开始
	// ////////////////////////////
	// 初始化加载消息，所有task_index相同的正在发送的消息都要加载。
	public synchronized boolean initLoadMessageFromDB() {
		try {
			List<PushMessage> tempList = pushService.getPushMessageSending(
					GETUI_TASK_INDEX, PushStatus.SENDING.getValue());
			if (tempList != null && tempList.size() > 0) {
				// 加入发送队列
				for (PushMessage tempItem : tempList) {
					putMessage(tempItem);
				}
			}
			// 将已经发送的消息移动到历史表
		} catch (Exception e) {
			// TODO Auto-generated catch block
		    logger.error("{}",e);
		}
		return true;
	}
	public class MessageLoadWorker extends ThreadEx {
		private int id;
		private String name;
		public MessageLoadWorker(int id, String name) {
			super();
			this.id = id;
			this.name = name;
		}
		public String getName() {
			return this.name + this.id;
		}
		@Override
		protected void runProcess() {
			try {
				Thread.sleep(200);
				while (isRunning == true) {
					Thread.sleep(THREAD_TIMEOUT_MIN);
					int queueSize = getQueueSize();
					if (queueSize >= GETUI_MAX_QUEUE_SIZE - QUEUE_SIZE_MIN) {
						continue;
					}
					int limitSize = GETUI_MAX_QUEUE_SIZE - queueSize;
					// 从数据库加载没有发送的数据
					List<PushMessage> tempList = pushService
								.getPushMessageByStatus(GETUI_TASK_INDEX,new int[]{
										PushStatus.INIT.getValue()}, limitSize);
					Map<String, Object> tempDataField = new HashMap<String, Object>();
					tempDataField.put("status", PushStatus.SENDING.getValue());
					List<Integer> tempIdList = new ArrayList<Integer>();
					// 将数据加入消息队列
					if (tempList != null && tempList.size() > 0) {
					  // 加入发送队列
						for (PushMessage tempItem : tempList) {
							putMessage(tempItem);
							tempIdList.add(tempItem.getId());
						}
					}
					// 更新这些记录的状态
					int ret = pushService.updateMessageStatus(tempDataField,
							tempIdList);
					if (ret < 0) {
						// todo:更新出错
					}
				}
				logger.info(this.name + this.id + " stopped!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
			    logger.error("{}",e);
			}
		}
	}
	// ////////////////////////////
	// 消息加载线程结束
	// ////////////////////////////
	// ////////////////////////////
	// 消息推送线程开始
	// ////////////////////////////
	public class PushWorker extends ThreadEx {
		private int id;
		private String name;
		public PushWorker(int id, String name) {
			super();
			this.id = id;
			this.name = name;
		}
		public String getName() {
			return this.name + this.id;
		}
		@Override
		protected void runProcess() {
			try {
				Thread.sleep(200);
				Integer status = PushStatus.SEND_SUCCESS.getValue();
				// 更新消息状态
				List<Integer> tempIdList = null;
				PushMessage tempItem = null;
				while (isRunning == true) {
					tempIdList = new ArrayList<Integer>();
					tempItem = popMessage();
					if (tempItem == null) {
						Thread.sleep(THREAD_TIMEOUT_MIN);
						continue;
					}
					sendMessage(tempItem,GETUI_APPID,GETUI_APPKEY,GETUI_MASTERSECRET);
					// 更新这些记录的状态
					tempIdList.add(tempItem.getId());
					pushService.updateMessageStatus(status,tempIdList);
				}
				logger.info(this.name + this.id + " stopped!");
			} catch (Exception e) {
			    logger.error("{}",e);;
			}
		}
		private void sendMessage(PushMessage tempItem,String[] appIds, String[] appKeys,String[] mAppSecrets) {
			  for(int i=0;i<appIds.length;i++){
			    boolean bRet = false;
			    try {
    				IGtPush push = new IGtPush(GETUI_URL, appKeys[i],
    				    mAppSecrets[i]);
    				push.connect();
    				ITemplate template = null;
    				if (tempItem.getActionType() == PushActionType.OPEN_APP
    						.getValue()) {
    					template = getNotificationTemplateDemo(tempItem,appIds[i],appKeys[i]);
    				} else if (tempItem.getActionType() == PushActionType.OPEN_URL
    						.getValue()) {
    					template = getLinkTemplateDemo(tempItem,appIds[i],appKeys[i]);
    				} else if (tempItem.getActionType() == PushActionType.TRANSMISSION
    						.getValue()) {
    					template = getTransmissionTemplate(tempItem,appIds[i],appKeys[i]);
    				} else if (tempItem.getActionType() == PushActionType.IOS_APN
    						.getValue()) {
    					template = getAPNTemplate(tempItem);
    				} else {
    				}
    				if (tempItem.getRecvIdType() == PushRecvIdType.APP.getValue()) {
    					bRet = pushToApp(push, template, tempItem,appIds[i]);
    				}
    				else if(tempItem.getRecvIdType() == PushRecvIdType.DEVICE_TOKEN.getValue())
    				{
    					bRet = pushToUserByAPN(push, template, tempItem,appIds[i]);
    				} else {
    					bRet = pushToUser(push, template, tempItem,appIds[i]);
    				}
    				logger.info("The i:"+i+" user send "+ bRet);
			    } catch (IOException e) {
	                // TODO Auto-generated catch block
			        logger.error("{}",e);
	            } catch (Exception e) {
	                // TODO Auto-generated catch block
	                logger.error("{}",e);
	            }
  			  }
		}
		
		private boolean pushToUser(IGtPush push, ITemplate template,
				PushMessage tempItem,String appId) {
			boolean bRet = false;
			try {
				// 推送给单个用户
				SingleMessage message = new SingleMessage();
				message.setOffline(true);
				message.setOfflineExpireTime(GETUI_OFFLINE_TIMEOUT);
				message.setData(template);
				Target target1 = new Target();
				target1.setAppId(appId);
				target1.setClientId(tempItem.getRecvId());
				IPushResult ret = push.pushMessageToSingle(message, target1);
				if (ret == null) {
					return bRet;
				}
				EPushResult retCode = ret.getResultCode();
				if (retCode != EPushResult.RESULT_OK) {
					return bRet;
				}
				Map<String, Object> tempResponse = ret.getResponse();
				if (tempResponse == null || tempResponse.size() <= 0) {
					return bRet;
				}
				logger.info(ret.getResponse().toString());
				bRet = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
			    logger.error("{}",e);
				bRet = false;
			}
			return bRet;
		}
		private boolean pushToUserByAPN(IGtPush push, ITemplate template,
				PushMessage tempItem,String appId) {
			boolean bRet = false;
			try {
				// 推送给单个用户
				SingleMessage message = new SingleMessage();
				message.setData(template);
				IPushResult ret = push.pushAPNMessageToSingle(appId, tempItem.getRecvId(), message);
				if (ret == null) {
					return bRet;
				}
				EPushResult retCode = ret.getResultCode();
				if (retCode != EPushResult.RESULT_OK) {
					return bRet;
				}
				Map<String, Object> tempResponse = ret.getResponse();
				if (tempResponse == null || tempResponse.size() <= 0) {
					return bRet;
				}
				logger.info(ret.getResponse().toString());
				bRet = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
			    logger.error("{}",e);
				bRet = false;
			}
			return bRet;
		}
		private boolean pushToApp(IGtPush push, ITemplate template,
				PushMessage tempItem,String appId) {
			boolean bRet = false;
			try {
				// 推送给app
				AppMessage message = new AppMessage();
				message.setData(template);
				message.setOffline(true);
				message.setOfflineExpireTime(GETUI_OFFLINE_TIMEOUT);
				List<String> appIdList = new ArrayList<String>();
				appIdList.add(appId);
				message.setAppIdList(appIdList);
				IPushResult ret = push.pushMessageToApp(message, "apppush");
				if (ret == null) {
					return bRet;
				}
				EPushResult retCode = ret.getResultCode();
				if (retCode != EPushResult.RESULT_OK) {
					return bRet;
				}
				Map<String, Object> tempResponse = ret.getResponse();
				if (tempResponse == null || tempResponse.size() <= 0) {
					return bRet;
				}
				logger.info(ret.getResponse().toString());
				bRet = true;
			} catch (PushAppException e) {
				// TODO Auto-generated catch block
			    logger.error("{}",e);
				bRet = false;
			}
			return bRet;
		}
	}
	// ////////////////////////////
	// 消息推送线程结束
	// ////////////////////////////
	public static LinkTemplate getLinkTemplateDemo(PushMessage tempItem,String appId,String appKey)
			throws Exception {
		LinkTemplate template = new LinkTemplate();
		template.setAppId(appId);
		template.setAppkey(appKey);
		template.setTitle(tempItem.title);
		template.setText(tempItem.content);
		template.setLogo("icon.png");
		template.setLogoUrl("");
		template.setIsRing(true);
		template.setIsVibrate(true);
		template.setIsClearable(true);
		template.setUrl(tempItem.url);
		if (tempItem.device_type == PushDeviceType.IPHONE.getValue()) {
			template.setPushInfo("", 1, tempItem.getTitle(), "default",
					tempItem.content, "", "", "");
		}
		return template;
	}
	public static NotificationTemplate getNotificationTemplateDemo(
			PushMessage tempItem,String appId,String appKey) throws Exception {
		NotificationTemplate template = new NotificationTemplate();
		template.setAppId(appId);
		template.setAppkey(appKey);
		template.setTitle(tempItem.title);
		template.setText(tempItem.content);
		template.setLogo("icon.png");
		template.setLogoUrl("");
		template.setIsRing(true);
		template.setIsVibrate(true);
		template.setIsClearable(true);
		template.setTransmissionType(1);
		template.setTransmissionContent("text");
		if (tempItem.device_type == PushDeviceType.IPHONE.getValue()) {
			template.setPushInfo("", 1, tempItem.getTitle(), "default",
					tempItem.content, "", "", "");
		}
		return template;
	}
	public static TransmissionTemplate getTransmissionTemplate(
			PushMessage tempItem,String appId,String appKey) throws Exception {
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(appId);
		template.setAppkey(appKey);
		template.setTransmissionType(1);
		template.setTransmissionContent(tempItem.content);
		if (tempItem.getDeviceType() == PushDeviceType.IPHONE.getValue()) {
			APNPayload payload = new APNPayload();
		    payload.setBadge(1);  //将应用icon上显示的数字设为1
		    //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，如10，效果和setBadge一样，
		    //应用icon上显示指定数字。不能和setBadge同时使用
		    //setAutoBadge("+1");
		    payload.setContentAvailable(1);
		    payload.setSound("default");
		    //简单模式APNPayload.SimpleMsg 
		    payload.setAlertMsg(new APNPayload.SimpleAlertMsg(tempItem.content));
		    template.setAPNInfo(payload);
		}
		return template;
	}
	
	public static APNTemplate getAPNTemplate(
			PushMessage tempItem) throws Exception {
		 APNTemplate template = new APNTemplate();
		 APNPayload apnpayload = new APNPayload();
		 com.gexin.rp.sdk.base.payload.APNPayload.SimpleAlertMsg alertMsg = new com.gexin.rp.sdk.base.payload.APNPayload.SimpleAlertMsg(tempItem.content);
		 apnpayload.setAlertMsg(alertMsg);
		 apnpayload.setBadge(5);
		 apnpayload.setContentAvailable(1);
		 apnpayload.setCategory("ACTIONABLE");
		 template.setAPNInfo(apnpayload);
		 return template;
	}

}
