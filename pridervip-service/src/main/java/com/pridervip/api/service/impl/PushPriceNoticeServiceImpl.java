/*
package com.pridervip.api.service.impl;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import com.pridervip.api.enums.CoinType;
import com.pridervip.api.service.*;

*/
/**
 * 现货业务逻辑类
 *//*

@Service("pushPriceNoticeService")
public class PushPriceNoticeServiceImpl implements PushPriceNoticeService {

    private static final Logger logger = LoggerFactory.getLogger(PushPriceNoticeServiceImpl.class);


//    @Resource
//    private ExceptionUtils exceptionUtils;
//
//    @Resource
//    private RedisUtils redisUtils;

//    @Resource
//    private Cid2UserIdDao cid2UserIdDao;

    @Resource
    private PushPriceNoticeDao pushPriceNoticeDao;


    @Override
    public long insertPriceNotice(PushPriceNotice tempItem) throws Exception
    {
    	return pushPriceNoticeDao.insertPriceNotice(tempItem);
    }
    
    @Override
    public long insertPriceNotice(List<PushPriceNotice> list) throws Exception
    {
    	return pushPriceNoticeDao.insertPriceNotice(list);
    }

    @Override
    public Integer deletePriceNotice(Integer id) throws Exception
    {
    	return pushPriceNoticeDao.deletePriceNotice(id);
    }

    @Override
    public Integer deletePriceNoticeByCid(String cid) throws Exception
    {
    	return pushPriceNoticeDao.deletePriceNoticeByCid(cid);
    }
    
    @Override
    public int updatePriceNoticeStatus(Map<String, Object> dataField, List<Integer> idList) throws Exception
    {
    	return pushPriceNoticeDao.updatePriceNoticeStatus(dataField, idList);
    }
    
    @Override
    public int updatePriceNoticeStatus(Integer status, List<Integer> idList) throws Exception
    {
    	return pushPriceNoticeDao.updatePriceNoticeStatus(status, idList);
    }
    
    @Override
    public List<PushPriceNotice> getPriceNoticeListByUserId(Integer userId) throws Exception
    {
    	return pushPriceNoticeDao.getPriceNoticeListByUserId(userId);
    }
    
    @Override
    public int getCountPriceNoticeListByUserId(Integer userId) throws Exception
    {
    	return pushPriceNoticeDao.getCountPriceNoticeListByUserId(userId);
    }
    
    @Override
    public List<PushPriceNotice> getPriceNoticeListByCid(String cid) throws Exception
    {
    	return pushPriceNoticeDao.getPriceNoticeListByCid(cid);
    }

    @Override
    public int getCountPriceNoticeListByCid(String cid) throws Exception
    {
    	return pushPriceNoticeDao.getCountPriceNoticeListByCid(cid);
    }
    
    
    @Override
    public List<PushPriceNotice> getWaitingPriceNoticeList(Integer taskIndex, String symbolId, Integer status, Long price, Integer currency, Integer priceType, Integer priceCondition, Integer timeMax, Integer limitSize) throws Exception
    {
    	return pushPriceNoticeDao.getWaitingPriceNoticeList(taskIndex, symbolId, status, price, currency, priceType, priceCondition, timeMax, limitSize);
    }
    
    @Override
    public boolean updateLastPushStatus(Integer taskIndex, String symbolId, Integer status, Long price, Integer currency, Integer priceType, Integer priceCondition) throws Exception
    {
    	return pushPriceNoticeDao.updateLastPushStatus(taskIndex, symbolId, status, price, currency, priceType, priceCondition);
    }
    
//    @Override
//    public Integer insertOrUpdateCid2UserIdItem(Cid2UserId tempItem) throws Exception
//    {
//    	return cid2UserIdDao.insertOrUpdateCid2UserIdItem(tempItem);
//    }
//    
//    @Override
//    public List<Cid2UserId> getCid2UserIdListByUserId(Integer userId)
//    {
//    	return cid2UserIdDao.getCid2UserIdListByUserId(userId);
//    }
//	
//    @Override
//    public long insertPushMessage(PushMessage tempItem) throws Exception
//    {
//    	return pushMessageDao.insertPushMessage(tempItem);
//    }
//    
//    @Override
//    public List<PushMessage> getPushMessageSending(int pushStatus) throws Exception
//    {
//    	return pushMessageDao.getPushMessageSending(pushStatus);
//    }
//
//    @Override
//    public List<PushMessage> getPushMessageByStatus(int pushStatus, int limitSize) throws Exception
//    {
//    	return pushMessageDao.getPushMessageByStatus(pushStatus, limitSize);
//    }
//    
//    
//    @Override
//    public int updateMessageStatus(Map<String, Object> dataField, List<Integer> idList) throws Exception
//    {
//    	return pushMessageDao.updateMessageStatus(dataField, idList);
//    }
//    
//    
//    @Override
//    public int updateMessageStatus(Integer status, List<Integer> idList) throws Exception
//    {
//    	return pushMessageDao.updateMessageStatus(status, idList);
//    }
//    
//    @Override
//    public int moveMessageToHistory() throws Exception
//    {
//    	return pushMessageDao.moveMessageToHistory();
//    }
    

}
*/
