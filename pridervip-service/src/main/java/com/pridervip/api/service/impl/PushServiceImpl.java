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







import com.pridervip.api.entity.Cid2UserId;
import com.pridervip.api.entity.PushMessage;
import com.pridervip.api.enums.CoinType;
import com.pridervip.api.enums.PushStatus;
import com.pridervip.api.service.*;
import com.pridervip.api.dao.*;

*/
/**
 * 现货业务逻辑类
 *//*

@Service("pushService")
public class PushServiceImpl implements PushService {

    private static final Logger logger = LoggerFactory.getLogger(PushServiceImpl.class);


//    @Resource
//    private ExceptionUtils exceptionUtils;
//
//    @Resource
//    private RedisUtils redisUtils;

    @Resource
    private Cid2UserIdDao cid2UserIdDao;

    @Resource
    private PushMessageDao pushMessageDao;

    
    @Override
    public Integer insertOrUpdateCid2UserIdItem(Cid2UserId tempItem) throws Exception
    {
    	return cid2UserIdDao.insertOrUpdateCid2UserIdItem(tempItem);
    }
    
    @Override
    public List<Cid2UserId> getCid2UserIdListByUserId(Integer userId)
    {
    	return cid2UserIdDao.getCid2UserIdListByUserId(userId);
    }
	
    @Override
    public long insertPushMessage(PushMessage tempItem) throws Exception
    {
    	return pushMessageDao.insertPushMessage(tempItem);
    }
    
    @Override
    public List<PushMessage> getPushMessageSending(int task_index, int pushStatus) throws Exception
    {
    	return pushMessageDao.getPushMessageSending(task_index, pushStatus);
    }

    @Override
    public List<PushMessage> getPushMessageByStatus(int task_index, int[] pushStatus, int limitSize) throws Exception
    {
    	return pushMessageDao.getPushMessageByStatus(task_index, pushStatus, limitSize);
    }
    
    
    @Override
    public int updateMessageStatus(Map<String, Object> dataField, List<Integer> idList) throws Exception
    {
    	return pushMessageDao.updateMessageStatus(dataField, idList);
    }
    
    
    @Override
    public int updateMessageStatus(Integer status, List<Integer> idList) throws Exception
    {
    	return pushMessageDao.updateMessageStatus(status, idList);
    }
    
    @Override
    public int moveMessageToHistory(int task_index) throws Exception
    {
    	return pushMessageDao.moveMessageToHistory(task_index);
    }
    

}
*/
