package com.pridervip.api.service;


import com.pridervip.api.entity.Cid2UserId;
import com.pridervip.api.entity.PushMessage;
import com.pridervip.api.entity.PushPriceNotice;
import com.pridervip.api.enums.CoinType;
import com.pridervip.api.entity.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/11/18.
 */
public interface PushPriceNoticeService {
	
    /**
     * 添加价格提醒
     *
     * @param tempItem      PushPriceNotice
     * @return id
     * @throws Exception
     */
    public long insertPriceNotice(PushPriceNotice tempItem) throws Exception;
    public long insertPriceNotice(List<PushPriceNotice> list) throws Exception;

    /**
     * 删除价格提醒
     *
     * @param id      Integer
     * @return id
     * @throws Exception
     */
    public Integer deletePriceNotice(Integer id) throws Exception;
    public Integer deletePriceNoticeByCid(String cid) throws Exception;
    
    /**
     * 更新价格提醒状态
     *
     * @param pushStatus      PushStatus
     * @return id
     * @throws Exception
     */
    public int updatePriceNoticeStatus(Map<String, Object> dataField, List<Integer> idList) throws Exception;
    public int updatePriceNoticeStatus(Integer status, List<Integer> idList) throws Exception;
    
    /**
     * 通过用户id获取价格提醒
     *
     * @param userId      Integer
     * @return id
     * @throws Exception
     */
    public List<PushPriceNotice> getPriceNoticeListByUserId(Integer userId) throws Exception;
    public int getCountPriceNoticeListByUserId(Integer userId) throws Exception;
    
    /**
     * 通过cid获取价格提醒
     *
     * @return id
     * @throws Exception
     */
    public List<PushPriceNotice> getPriceNoticeListByCid(String cid) throws Exception;
    public int getCountPriceNoticeListByCid(String cid) throws Exception;
    
    
    /**
     * 获取等待通知的价格提醒
     *
     * @return id
     * @throws Exception
     */
    public List<PushPriceNotice> getWaitingPriceNoticeList(Integer taskIndex, String symbolId, Integer status, Long price, Integer currency, Integer priceType, Integer priceCondition, Integer timeMax, Integer limitSize) throws Exception;
    
    
    /**
     * 更新价格突破状态
     *
     * @return id
     * @throws Exception
     */
    public boolean updateLastPushStatus(Integer taskIndex, String symbolId, Integer status, Long price, Integer currency, Integer priceType, Integer priceCondition) throws Exception;
}
