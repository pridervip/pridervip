package com.pridervip.api.service;


import com.pridervip.api.entity.Cid2UserId;
import com.pridervip.api.entity.PushMessage;
import com.pridervip.api.enums.CoinType;
import com.pridervip.api.entity.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/11/18.
 */
public interface PushService {
	
    /**
     * 添加或者更新cid对用户id的映射记录，如果有就更新，没有就添加
     *
     * @param tempItem      Cid2UserId
     * @return id
     * @throws Exception
     */
    public Integer insertOrUpdateCid2UserIdItem(Cid2UserId tempItem) throws Exception;

    /**
     * 通过用户id获取cid
     *
     * @param userId      Integer
     * @return id
     * @throws Exception
     */
    public List<Cid2UserId> getCid2UserIdListByUserId(Integer userId) throws Exception;
    
    /**
     * 添加推送消息
     *
     * @param tempItem      PushMessage
     * @return id
     * @throws Exception
     */
    public long insertPushMessage(PushMessage tempItem) throws Exception;
    
//    /**
//     * 修改推送消息状态
//     *
//     * @param tempItem      PushMessage
//     * @return id
//     * @throws Exception
//     */
//    public boolean updatePushMessageStatus(Integer id, Integer status) throws Exception;

    /**
     * 获取正在发送的消息
     *
     * @param pushStatus      PushStatus
     * @return id
     * @throws Exception
     */
    public List<PushMessage> getPushMessageSending(int task_index, int pushStatus) throws Exception;

    /**
     * 根据状态获取发送的消息
     *
     * @param pushStatus      PushStatus
     * @return id
     * @throws Exception
     */
    public List<PushMessage> getPushMessageByStatus(int task_index, int[] pushStatus, int limitSize) throws Exception;
   
    /**
     * 更新消息状态
     *
     * @return id
     * @throws Exception
     */
    public int updateMessageStatus(Map<String, Object> dataField, List<Integer> idList) throws Exception;
    public int updateMessageStatus(Integer status, List<Integer> idList) throws Exception;
    
    /**
     * 将已经发送的消息移动到历史表
     *
     * @return id
     * @throws Exception
     */
    public int moveMessageToHistory(int task_index) throws Exception;
    
}
