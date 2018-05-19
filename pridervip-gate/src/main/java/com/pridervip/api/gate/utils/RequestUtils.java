/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.bighuobi.api.gate.utils;

import java.util.Enumeration;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.bighuobi.api.enums.OrderSource;
import com.bighuobi.api.enums.OrderType;
import com.bighuobi.api.enums.TradeType;

/**
 * @author 任培伟
 * @version 1.0.0
 */
@Component
public class RequestUtils {

    @Resource
    private MessageSource messageSource;

    @SuppressWarnings("unchecked")
    public void generatePostMap(HttpServletRequest request, Map<String, Object> paramMap) {
        Enumeration<String> paraNames = request.getParameterNames();
        String paramName = StringUtils.EMPTY;
        while (paraNames.hasMoreElements()) {
            paramName = paraNames.nextElement();
            paramMap.put(paramName, request.getParameter(paramName));
        }
    }

//    public Order generateOrder(Integer userId, String amount, String price, Integer trade_type, Integer order_type) {
//        Order order = new Order();
//        order.setUser_id(userId);
//        order.setOrder_amount(amount);
//        order.setOrder_price(price);
//        order.setTrade_type(TradeType.findByValue(trade_type));
//        order.setOrder_type(OrderType.findByValue(order_type));
//        order.setOrder_source(OrderSource.MOBILE);
//        return order;
//    }
//
//    public FuturesOrderInfo generateFuturesOrder() {
//        FuturesOrderInfo futuresOrder = new FuturesOrderInfo();
//        return futuresOrder;
//    }

}
