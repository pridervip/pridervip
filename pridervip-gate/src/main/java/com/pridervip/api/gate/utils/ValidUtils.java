/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.bighuobi.api.gate.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.ValidationUtils;

import com.bighuobi.api.enums.CoinType;
import com.bighuobi.api.enums.RedisIndex;
import com.bighuobi.api.exception.CustomerException;

/**
 * @author Administrator $Id$
 */
@Component
public class ValidUtils {
    private Logger logger=LoggerFactory.getLogger(ValidUtils.class);

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private ExceptionUtils exceptionUtils;
    public boolean validRequestParam(Object... objects) throws CustomerException {
        for (Object object : objects) {
            if (object == null || StringUtils.isEmpty(object.toString())) {
                throw exceptionUtils.getCustomerException(64);
            }
        }
        return true;
    }
    /**
     * 下单生成json串
     *
     * @param orderType
     * @param tradeType
     * @param price
     * @param money
     * @param userId
     * @param coinType
     * @param contractType
     * @param storeId
     * @return
     */
    public String genarateSaveOrderJSON(String orderType, String tradeType, String price, String money, Integer userId,
            String coinType, String contractType, String lever, Integer storeId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Long created = System.currentTimeMillis() / 1000;
        paramMap.put("orderType", orderType);// 1 开 2 平
        paramMap.put("tradeType", tradeType);// 1 买 2 卖
        paramMap.put("price", price);
        paramMap.put("money", money);
        paramMap.put("userId", userId);
        paramMap.put("orderTime", created);
        paramMap.put("lastTime", created);
        paramMap.put("coinType", coinType.equals("1") ? "btc" : "ltc");
        paramMap.put("contractType", contractType);// 合约类型
        paramMap.put("from", 3);// api下单
        paramMap.put("status", 7);// 委托状态:初始
        paramMap.put("processedAmount", 0);
        paramMap.put("processedMoney", 0);
        paramMap.put("fee", 0);
        paramMap.put("usedMargin", 0);
        paramMap.put("lever", lever);
        paramMap.put("storeId", storeId);
        return new JSONObject(paramMap).toString();
    }
    /**
     * 校验请求时间
     * 十位时间戳，单位：秒。 请求时间和服务器时间在900秒以内（15分钟）
     * @param created
     * @return
     * @throws CustomerException
     */
    public boolean validCreatedTime(String created) throws CustomerException{
        Long currentSeconds = System.currentTimeMillis() / 1000;
        if (!created.matches("^\\d{10}$") || Math.abs(currentSeconds - Long.parseLong(created))>=Constants.CREATED_TIMEL_IMITED) {
            throw exceptionUtils.getFuturesCustomerException(70);
        }
        return true;
    }
    public static void main(String[] args){
        ValidUtils validUtils=new ValidUtils();
        try {
            if(validUtils.validCreatedTime("1425916800")){
                System.out.println("yes");
            }
        } catch (CustomerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 校验订单金额 BTC:100 <= X <= 2500000 LTC:100 <= X <= 1000000
     *
     * @param money
     * @param coinType
     * @return
     * @throws CustomerException
     */
    public boolean validMoney(String money, int coinType) throws CustomerException {
        if (!money.matches("^[1-9]\\d*00(\\.0+)?$")) {
            throw exceptionUtils.getFuturesCustomerException(69);
//            throw new FuturesException(new HandleResult(69, messageSource.getMessage("error.69", null, SessionAware
//                    .getRequest().getLocale())));
        }
        if (new BigDecimal(money).remainder(new BigDecimal(100)).compareTo(new BigDecimal(0)) != 0) {
            throw exceptionUtils.getFuturesCustomerException(111);
//            throw new FuturesException(new HandleResult(111, messageSource.getMessage("error.111", null, SessionAware
//                    .getRequest().getLocale())));
        }
        if ("1".equals(coinType)) {
            if (new BigDecimal(money).compareTo(new BigDecimal(Constants.BTC_MAX_TRADE_MONEY)) > 0) {
                throw exceptionUtils.getFuturesCustomerException(47);
//                throw new FuturesException(new HandleResult(47, messageSource.getMessage("error.47", null, SessionAware
//                        .getRequest().getLocale())
//                        + Constants.BTC_MAX_TRADE_MONEY));
            }
            if (new BigDecimal(money).compareTo(new BigDecimal(Constants.BTC_MIN_TRADE_MONEY)) < 0) {
                throw exceptionUtils.getFuturesCustomerException(48);
//                throw new FuturesException(new HandleResult(48, messageSource.getMessage("error.48", null, SessionAware
//                        .getRequest().getLocale())
//                        + Constants.BTC_MIN_TRADE_MONEY));
            }
        } else {
            if (new BigDecimal(money).compareTo(new BigDecimal(Constants.LTC_MAX_TRADE_MONEY)) > 0) {
                throw exceptionUtils.getFuturesCustomerException(47);
//                throw new FuturesException(new HandleResult(47, messageSource.getMessage("error.47", null, SessionAware
//                        .getRequest().getLocale())
//                        + Constants.LTC_MAX_TRADE_MONEY));
            }
            if (new BigDecimal(money).compareTo(new BigDecimal(Constants.LTC_MIN_TRADE_MONEY)) < 0) {
                throw exceptionUtils.getFuturesCustomerException(48);
//                throw new FuturesException(new HandleResult(48, messageSource.getMessage("error.48", null, SessionAware
//                        .getRequest().getLocale())
//                        + Constants.LTC_MIN_TRADE_MONEY));
            }
        }
        return true;
    }

    /**
     * 校验订单价格:两位小数
     *
     * @param coinType
     * @param tradeType
     * @param price
     * @param contractType
     * @return
     * @throws CustomerException
     */
    public boolean validPrice(int coinType, String tradeType, String price, String contractType) throws CustomerException {
        if (!price.matches("^\\d+(\\.\\d+)?$")) {
            throw exceptionUtils.getFuturesCustomerException(68);
//            throw new FuturesException(new HandleResult(68, messageSource.getMessage("error.68", null, SessionAware
//                    .getRequest().getLocale())));
        }
        if (new BigDecimal(price).scale() > 2) {
            throw exceptionUtils.getFuturesCustomerException(110);
//            throw new FuturesException(new HandleResult(110, messageSource.getMessage("error.110", null, SessionAware
//                    .getRequest().getLocale())));
        }

        String key = CoinType.BTC.getValue()==coinType ? "futures_price" : "futures_price_ltc";
        Object object = redisUtils.hGet(RedisIndex.PRICE.getValue(),key, "limit_highest_price_" + contractType);
        Long flag = System.currentTimeMillis();
        if (object == null) {
            logger.error("{}:{}价格不存在", flag, "limit_highest_price_" + contractType);
            throw exceptionUtils.getFuturesCustomerException(1);
//            throw new FuturesException(new HandleResult(1, messageSource.getMessage("error.1", null, SessionAware
//                    .getRequest().getLocale())
//                    + ":" + flag));
        }
        BigDecimal limitHighestPrice = new BigDecimal(object.toString());

        object = redisUtils.hGet(RedisIndex.PRICE.getValue(),key, "limit_lowest_price_" + contractType);
        if (object == null) {
            logger.error("{}:{}价格不存在", flag, "limit_lowest_price_" + contractType);
            throw exceptionUtils.getFuturesCustomerException(1);
//            throw new FuturesException(new HandleResult(1, messageSource.getMessage("error.1", null, SessionAware
//                    .getRequest().getLocale())
//                    + ":" + flag));
        }
        BigDecimal limitLowestPrice = new BigDecimal(object.toString()).setScale(2,BigDecimal.ROUND_DOWN);

        logger.info("highest:{},lowest:{}", limitHighestPrice.toPlainString(), limitLowestPrice.toPlainString());
        if (tradeType.equals("1") && new BigDecimal(price).compareTo(limitHighestPrice) > 0) {
            throw exceptionUtils.getFuturesCustomerException(45);
//            throw new FuturesException(new HandleResult(45, messageSource.getMessage("error.45", null, SessionAware
//                    .getRequest().getLocale())
//                    + limitHighestPrice.toPlainString()));
        } else if (tradeType.equals("2") && new BigDecimal(price).compareTo(limitLowestPrice) < 0) {
            throw exceptionUtils.getFuturesCustomerException(46);
//            throw new FuturesException(new HandleResult(46, messageSource.getMessage("error.46", null, SessionAware
//                    .getRequest().getLocale())
//                    + limitLowestPrice.toPlainString()));
        }
        return true;
    }

    /**
     * 校验订单类型和交易类型
     *
     * @param orderType
     * @param tradeType
     * @return
     * @throws CustomerException
     */
    public boolean validType(String orderType, String tradeType) throws CustomerException {
        if (!orderType.matches("1|2")) {
            throw exceptionUtils.getFuturesCustomerException(72);
//            throw new FuturesException(new HandleResult(72, messageSource.getMessage("error.72", null, SessionAware
//                    .getRequest().getLocale())));
        }
        if (!tradeType.matches("1|2")) {
            throw exceptionUtils.getFuturesCustomerException(73);
//            throw new FuturesException(new HandleResult(73, messageSource.getMessage("error.73", null, SessionAware
//                    .getRequest().getLocale())));
        }
        return true;
    }

    /**
     * 如果下单时不设置杠杆倍数，则默认在server中设置为10;否则,比特币:5/10/20莱特币:5/10
     *
     * @param coinType
     * @param coefficient
     * @return
     * @throws CustomerException
     */
    public boolean validCoefficient(String orderType, int coinType, String contractType, String lever) throws CustomerException {
        if (orderType.equals("1")) {
            if (coinType==CoinType.BTC.getValue()) {
                String regex = StringUtils.EMPTY;
                if (contractType.equals("week")||contractType.equals("week2")) {
                    regex = "5|10|20";
                } else {
                    regex = "5|10";
                }
                if (!lever.matches(regex)) {
                    throw exceptionUtils.getFuturesCustomerException(64);
//                    throw new FuturesException(new HandleResult(64, messageSource.getMessage("error.64", null,
//                            SessionAware.getRequest().getLocale()) + " leverage"));
                }
            } else if (coinType==CoinType.LTC.getValue()) {
                if (!lever.matches("5|10")) {
                    throw exceptionUtils.getFuturesCustomerException(64);
//                    throw new FuturesException(new HandleResult(64, messageSource.getMessage("error.64", null,
//                            SessionAware.getRequest().getLocale()) + " leverage"));
                }
            }
        }
        return true;
    }
    
    /**
     * 如果下单时不设置杠杆倍数，则默认在server中设置为10;否则,比特币:5/10/20莱特币:5/10
     *
     * @param coinType
     * @param coefficient
     * @return
     * @throws CustomerException
     */
    public boolean validCoefficientForApi(String orderType, int coinType, String contractType, String lever) throws CustomerException {
        if (orderType.equals("1")) {
            if (coinType==CoinType.BTC.getValue()) {
                String regex = StringUtils.EMPTY;
                if (contractType.equals("week")||contractType.equals("next_week")) {
                    regex = "5|10|20";
                } else {
                    regex = "5|10";
                }
                if (!lever.matches(regex)) {
                    throw exceptionUtils.getFuturesCustomerException(64);
//                    throw new FuturesException(new HandleResult(64, messageSource.getMessage("error.64", null,
//                            SessionAware.getRequest().getLocale()) + " leverage"));
                }
            } else if (coinType==CoinType.LTC.getValue()) {
                if (!lever.matches("5|10")) {
                    throw exceptionUtils.getFuturesCustomerException(64);
//                    throw new FuturesException(new HandleResult(64, messageSource.getMessage("error.64", null,
//                            SessionAware.getRequest().getLocale()) + " leverage"));
                }
            }
        }
        return true;
    }

    /**
     * 校验币种
     *
     * @param coinType
     * @return
     * @throws CustomerException
     */
    public boolean validCoinType(int coinType) throws CustomerException {
        if (coinType!=CoinType.ALL.getValue() && coinType!=CoinType.BTC.getValue() && coinType!=CoinType.LTC.getValue()) {
            throw exceptionUtils.getCustomerException(75);
        }
        return true;
    }
    /**
     * api使用
     * @param coinType
     * @return
     * @throws CustomerException
     */
    public boolean validCoinTypeForApi(String coinType) throws CustomerException {
        if (!"1".equals(coinType) && !"2".equals(coinType)) {
//            throw exceptionUtils.getCustomerException(75);
            throw exceptionUtils.getFuturesCustomerException(75);
        }
        return true;
    }
    

    /**
     * 校验合约类型
     *(验证手机端请求)
     * @param contractType
     * @param coinType
     * @return
     * @throws CustomerException
     */
    public boolean validContractType(String contractType, int coinType) throws CustomerException {
        if(coinType==CoinType.BTC.getValue()){
            if (contractType != null && contractType.trim().length()>0 && !contractType.matches("week|week2|quarter|quarter2")) {
                throw exceptionUtils.getFuturesCustomerException(74);
//                throw new FuturesException(new HandleResult(74, messageSource.getMessage("error.74", null, SessionAware
//                        .getRequest().getLocale())));
            }
        }else if(coinType==CoinType.LTC.getValue()){
            if (contractType != null && contractType.trim().length()>0  && !contractType.matches("week|quarter")) {
                throw exceptionUtils.getFuturesCustomerException(74);
//                throw new FuturesException(new HandleResult(74, messageSource.getMessage("error.74", null, SessionAware
//                        .getRequest().getLocale())));
            }
        }

        return true;
    }
    
    /**
     * 校验合约类型--下单时，要求合约类型不能为空
     * @param contractType
     * @param coinType
     * @return
     * @throws CustomerException
     */
    public boolean validContractTypeForSave(String contractType, int coinType) throws CustomerException {
        if(coinType==CoinType.BTC.getValue()){
            if (contractType == null || contractType.trim().length()==0 || !contractType.matches("week|week2|quarter|quarter2")) {
                throw exceptionUtils.getFuturesCustomerException(74);
//                throw new FuturesException(new HandleResult(74, messageSource.getMessage("error.74", null, SessionAware
//                        .getRequest().getLocale())));
            }
        }else if(coinType==CoinType.LTC.getValue()){
            if (contractType == null || contractType.trim().length()==0  || !contractType.matches("week|quarter")) {
                throw exceptionUtils.getFuturesCustomerException(74);
//                throw new FuturesException(new HandleResult(74, messageSource.getMessage("error.74", null, SessionAware
//                        .getRequest().getLocale())));
            }
        }

        return true;
    }
    
    /**
     * 校验合约类型
     *(验证API请求)
     * @param contractType
     * @param coinType
     * @return
     * @throws CustomerException
     */
    public boolean validContractTypeForApi(String contractType, int coinType) throws CustomerException {
        if(coinType==CoinType.BTC.getValue()){
            if (contractType == null || (contractType.trim().length()>0 && !contractType.matches("week|next_week|quarter|next_quarter"))) {
                throw exceptionUtils.getFuturesCustomerException(74);
//                throw new FuturesException(new HandleResult(74, messageSource.getMessage("error.74", null, SessionAware
//                        .getRequest().getLocale())));
            }
        }else if(coinType==CoinType.LTC.getValue()){
            if (contractType == null || (contractType.trim().length()>0  && !contractType.matches("week|quarter"))) {
                throw exceptionUtils.getFuturesCustomerException(74);
//                throw new FuturesException(new HandleResult(74, messageSource.getMessage("error.74", null, SessionAware
//                        .getRequest().getLocale())));
            }
        }

        return true;
    }


    /**
     * 交割结算状态
     *
     * @param coinType
     * @return
     * @throws CustomerException
     */
    public boolean validSettleStatus(int coinType) throws CustomerException {
        if (coinType==CoinType.BTC.getValue()) {
            Object object = redisUtils.hGet(RedisIndex.PRICE.getValue(),"futures_set", "settle_status");
            if (object != null && "1".equals(object.toString())) {
                throw exceptionUtils.getFuturesCustomerException(2);
//                throw new FuturesException(new HandleResult(2, messageSource.getMessage("error.2", null, SessionAware
//                        .getRequest().getLocale())));
            }
        } else {
            Object object = redisUtils.hGet(RedisIndex.PRICE.getValue(),"futures_set_ltc", "settle_status");
            if (object != null && "1".equals(object.toString())) {
                throw exceptionUtils.getFuturesCustomerException(2);
//                throw new FuturesException(new HandleResult(2, messageSource.getMessage("error.2", null, SessionAware
//                        .getRequest().getLocale())));
            }
        }
        return true;
    }

    public boolean validId(String id) throws CustomerException {
        if (!id.matches("^\\d+$") || new BigDecimal(id).compareTo(new BigDecimal(Long.MAX_VALUE)) > 0) {
            throw exceptionUtils.getCustomerException(64);
        }
        return true;
    }

    /**
     * 验证仓位Id,其值在0--10之间
     * @param storeId
     * @return
     * @throws CustomerException
     */
    public boolean validStoreId(Integer storeId) throws CustomerException {
        if(storeId!=null && (storeId.intValue()>=0 && storeId.intValue()<=10)){
            return true;
        }else{
            throw exceptionUtils.getFuturesCustomerException(113);
//            throw new FuturesException(new HandleResult(113, messageSource.getMessage("error.113", null, SessionAware
//                    .getRequest().getLocale())));
        }
    }
    /**
     * 验证仓位Id,其值在0--10之间
     * 
     * @param storeId
     * @return
     * @throws CustomerException 
     */
    public boolean validStoreId(String storeIdStr, String orderType) throws CustomerException {
        Integer storeId=0;
        try{
            storeId=Integer.parseInt(storeIdStr);
        }catch(Exception e){
            logger.error("user's input storeId:{} is not number",storeIdStr);
            throw exceptionUtils.getFuturesCustomerException(64);
        }
        if (storeId != null
                && ((Integer.valueOf(orderType) == 2 && storeId.intValue() >= 0 && storeId <= 10) || (Integer
                        .valueOf(orderType) == 1 && storeId.intValue() == 0))) {
            return true;
        } else if (storeId != null && (Integer.valueOf(orderType) == 1 && storeId.intValue() != 0)) {
            throw exceptionUtils.getFuturesCustomerException(114);
//            throw new FuturesException(new HandleResult(114, messageSource.getMessage("error.114", null, SessionAware
//                    .getRequest().getLocale())));
        } else {
            throw exceptionUtils.getFuturesCustomerException(113);
//            throw new FuturesException(new HandleResult(113, messageSource.getMessage("error.113", null, SessionAware
//                    .getRequest().getLocale())));
        }
    }

    /**
     * 验证结算历史的类型
     * @param settleType
     * @return
     * @throws CustomerException
     */
    public boolean validSettleType(String settleType) throws CustomerException {
        if (settleType!= null && settleType.matches("week|week2|quarter|quarter2")) {
            return true;
        }else{
            //无效的合约类型
            throw exceptionUtils.getFuturesCustomerException(74);
        }
    }
    /**
     * 资产转账方发必须为in或out
     * @param method
     * @return
     */
    public boolean validTransferMethod(String method)throws CustomerException {
        if (method!= null && method.matches("in|out")) {
            return true;
        }else{
            //无效的转账类型
            throw exceptionUtils.getFuturesCustomerException(74);
        }
    }
    /**
     * 判断转账金额是否合法
     * @param amount
     * @return
     */
    public boolean validTransferAmount(String amountStr) throws CustomerException{
        BigDecimal amount=new BigDecimal("0.0");
        try{
            amount=new BigDecimal(amountStr).setScale(4, RoundingMode.DOWN);
        }catch(Exception e){
            throw exceptionUtils.getFuturesCustomerException(76);
        }
        if(amount.compareTo(Constants.MIN_TRADE_AMOUNT)<0){
            throw exceptionUtils.getFuturesCustomerException(201);
        }
        return true;
    }
}
