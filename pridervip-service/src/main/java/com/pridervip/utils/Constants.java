/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.pridervip.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version 1.0.0
 */
public class Constants {

    private static final Logger logger = LoggerFactory.getLogger(Constants.class);

    private static ResourceBundle appBundle = PropertyResourceBundle.getBundle("META-INF/conf/application");
    private static ResourceBundle futures_appBundle = PropertyResourceBundle.getBundle("META-INF/conf/futures_application");
    
    //bitvc和手机是否绑定
    public static final boolean BITVC_MOBILE_BIND=Boolean.parseBoolean(appBundle.getString("bitvc_mobile_bind").trim());
    //btc拥有的期货合约类型
    public static final String CONTRACT_TYPE_BTC = appBundle.getString("contractTypeBtc");
    //ltc拥有的期货合约类型
    public static final String CONTRACT_TYPE_LTC = appBundle.getString("contractTypeLtc");

    public static final BigDecimal GLOBAL_PRECISION = new BigDecimal("10000000000");
    public static final BigInteger GLOBAL_PRECISION_5 = new BigInteger("100000");
    //默认精度返回，小数点后保留10位
    public static final int DEFAULT_PRECISION_SCALE_10=10;
    public static final int BTC_PRECISION_SCALE_4=4;

    public static final long TIME_OUT_SECONDS = 100L;

    public static String BTC_NEWESTPRICE_KEY;
    public static String BTC_NEWESTPRICE_SUBKEY;
    public static String LTC_NEWESTPRICE_KEY;
    public static String LTC_NEWESTPRICE_SUBKEY;
    // 期货 redis.index=0
    public static String F_BTC_PRICE_WEEK_KEY = appBundle.getString("f_btc_price_week_key").trim();
    public static String F_BTC_PRICE_WEEK_SUBKEY = appBundle.getString("f_btc_price_week_subkey").trim();
    public static String F_BTC_PRICE_WEEK2_KEY = appBundle.getString("f_btc_price_week2_key").trim();
    public static String F_BTC_PRICE_WEEK2_SUBKEY = appBundle.getString("f_btc_price_week2_subkey").trim();
    public static String F_BTC_PRICE_QUARTER_KEY = appBundle.getString("f_btc_price_quarter_key").trim();
    public static String F_BTC_PRICE_QUARTER_SUBKEY = appBundle.getString("f_btc_price_quarter_subkey").trim();
    public static String F_BTC_PRICE_QUARTER2_KEY = appBundle.getString("f_btc_price_quarter2_key").trim();
    public static String F_BTC_PRICE_QUARTER2_SUBKEY = appBundle.getString("f_btc_price_quarter2_subkey").trim();

    public static String F_LTC_PRICE_WEEK_KEY = appBundle.getString("f_ltc_price_week_key").trim();
    public static String F_LTC_PRICE_WEEK_SUBKEY = appBundle.getString("f_ltc_price_week_subkey").trim();
    public static String F_LTC_PRICE_QUARTER_KEY = appBundle.getString("f_ltc_price_quarter_key").trim();
    public static String F_LTC_PRICE_QUARTER_SUBKEY = appBundle.getString("f_ltc_price_quarter_subkey").trim();
    public static String F_LTC_PRICE_QUARTER2_KEY = appBundle.getString("f_ltc_price_quarter2_key").trim();
    public static String F_LTC_PRICE_QUARTER2_SUBKEY = appBundle.getString("f_ltc_price_quarter2_subkey").trim();
    
    public static BigDecimal MARKET_ORDER_BUY_AMOUNT_MIN;
    public static BigDecimal MARKET_ORDER_BUY_AMOUNT_MAX;
    public static BigDecimal MARKET_ORDER_BTC_SELL_AMOUNT_MIN;
    public static BigDecimal MARKET_ORDER_BTC_SELL_AMOUNT_MAX;
    public static BigDecimal MARKET_ORDER_LTC_SELL_AMOUNT_MIN;
    public static BigDecimal MARKET_ORDER_LTC_SELL_AMOUNT_MAX;
    public static BigDecimal LIMIT_ORDER_BTC_AMOUNT_MIN;
    public static BigDecimal LIMIT_ORDER_BTC_AMOUNT_MAX;
    public static BigDecimal LIMIT_ORDER_BTC_PRICE_MIN;
    public static BigDecimal LIMIT_ORDER_BTC_PRICE_MAX;
    public static BigDecimal LIMIT_ORDER_LTC_AMOUNT_MIN;
    public static BigDecimal LIMIT_ORDER_LTC_AMOUNT_MAX;
    public static BigDecimal LIMIT_ORDER_LTC_PRICE_MIN;
    public static BigDecimal LIMIT_ORDER_LTC_PRICE_MAX;
    /**
     * 限价单数量的一个中间值,目前是0.1,以此来判断价格浮动区间
     */
    public static BigDecimal LIMIT_ORDER_AMOUNT_MID = new BigDecimal("0.1");
    /**
     * 限价单买入 数量>0.1 BTC 价格不能高于现价*1.05
     */
    public static BigDecimal LIMIT_ORDER_BTC_BUY_PRICE_LIMIT_UP;
    /**
     * 限价单买入 数量<=0.1 BTC 价格不能高于现价*1.01
     */
    public static BigDecimal LIMIT_ORDER_BTC_BUY_PRICE_LIMIT_DOWN;
    /**
     * 限价单卖出 数量>0.1 BTC 价格不能低于现价*0.95
     */
    public static BigDecimal LIMIT_ORDER_BTC_SELL_PRICE_LIMIT_UP;
    /**
     * 限价单卖出 数量<=0.1 BTC 价格不能低于现价*0.99
     */
    public static BigDecimal LIMIT_ORDER_BTC_SELL_PRICE_LIMIT_DOWN;
    /**
     * 限价单买入 数量>0.1 LTC 价格不能高于现价*1.10
     */
    public static BigDecimal LIMIT_ORDER_LTC_BUY_PRICE_LIMIT_UP;
    /**
     * 限价单买入 数量<=0.1 LTC 价格不能高于现价*1.05
     */
    public static BigDecimal LIMIT_ORDER_LTC_BUY_PRICE_LIMIT_DOWN;
    /**
     * 限价单卖出 数量>0.1 LTC 价格不能低于现价*0.90
     */
    public static BigDecimal LIMIT_ORDER_LTC_SELL_PRICE_LIMIT_UP;
    /**
     * 限价单卖出 数量<=0.1 LTC 价格不能低于现价*0.95
     */
    public static BigDecimal LIMIT_ORDER_LTC_SELL_PRICE_LIMIT_DOWN;

    static {
        try {
            BTC_NEWESTPRICE_KEY = appBundle.getString("btc_newestprice_key");
            BTC_NEWESTPRICE_SUBKEY = appBundle.getString("btc_newestprice_subkey");
            LTC_NEWESTPRICE_KEY = appBundle.getString("ltc_newestprice_key");
            LTC_NEWESTPRICE_SUBKEY = appBundle.getString("ltc_newestprice_subkey");
            MARKET_ORDER_BUY_AMOUNT_MIN = new BigDecimal(appBundle.getString("market_order_buy_amount_min"));
            MARKET_ORDER_BUY_AMOUNT_MAX = new BigDecimal(appBundle.getString("market_order_buy_amount_max"));
            MARKET_ORDER_BTC_SELL_AMOUNT_MIN = new BigDecimal(appBundle.getString("market_order_btc_sell_amount_min"));
            MARKET_ORDER_BTC_SELL_AMOUNT_MAX = new BigDecimal(appBundle.getString("market_order_btc_sell_amount_max"));
            MARKET_ORDER_LTC_SELL_AMOUNT_MIN = new BigDecimal(appBundle.getString("market_order_ltc_sell_amount_min"));
            MARKET_ORDER_LTC_SELL_AMOUNT_MAX = new BigDecimal(appBundle.getString("market_order_ltc_sell_amount_max"));
            LIMIT_ORDER_BTC_AMOUNT_MIN = new BigDecimal(appBundle.getString("limit_order_btc_amount_min"));
            LIMIT_ORDER_BTC_AMOUNT_MAX = new BigDecimal(appBundle.getString("limit_order_btc_amount_max"));
            LIMIT_ORDER_BTC_PRICE_MIN = new BigDecimal(appBundle.getString("limit_order_btc_price_min"));
            LIMIT_ORDER_BTC_PRICE_MAX = new BigDecimal(appBundle.getString("limit_order_btc_price_max"));
            LIMIT_ORDER_LTC_AMOUNT_MIN = new BigDecimal(appBundle.getString("limit_order_ltc_amount_min"));
            LIMIT_ORDER_LTC_AMOUNT_MAX = new BigDecimal(appBundle.getString("limit_order_ltc_amount_max"));
            LIMIT_ORDER_LTC_PRICE_MIN = new BigDecimal(appBundle.getString("limit_order_ltc_price_min"));
            LIMIT_ORDER_LTC_PRICE_MAX = new BigDecimal(appBundle.getString("limit_order_ltc_price_max"));
            LIMIT_ORDER_AMOUNT_MID = new BigDecimal("0.1");
            LIMIT_ORDER_BTC_BUY_PRICE_LIMIT_UP = new BigDecimal(appBundle.getString("limit_order_btc_buy_price_limit_up"));
            LIMIT_ORDER_BTC_BUY_PRICE_LIMIT_DOWN = new BigDecimal(appBundle.getString("limit_order_btc_buy_price_limit_down"));
            LIMIT_ORDER_BTC_SELL_PRICE_LIMIT_UP = new BigDecimal(appBundle.getString("limit_order_btc_sell_price_limit_up"));
            LIMIT_ORDER_BTC_SELL_PRICE_LIMIT_DOWN = new BigDecimal(appBundle.getString("limit_order_btc_sell_price_limit_down"));
            LIMIT_ORDER_LTC_BUY_PRICE_LIMIT_UP = new BigDecimal(appBundle.getString("limit_order_ltc_buy_price_limit_up"));
            LIMIT_ORDER_LTC_BUY_PRICE_LIMIT_DOWN = new BigDecimal(appBundle.getString("limit_order_ltc_buy_price_limit_down"));
            LIMIT_ORDER_LTC_SELL_PRICE_LIMIT_UP = new BigDecimal(appBundle.getString("limit_order_ltc_sell_price_limit_up"));
            LIMIT_ORDER_LTC_SELL_PRICE_LIMIT_DOWN = new BigDecimal(appBundle.getString("limit_order_ltc_sell_price_limit_down"));
        } catch (Exception e) {
            logger.error("加载现货配置文件application.properties出错, Error:{}", e);
        }
    }

    
    public static String BTC_MAX_TRADE_MONEY;
    public static String BTC_MIN_TRADE_MONEY;
    public static String LTC_MAX_TRADE_MONEY;
    public static String LTC_MIN_TRADE_MONEY;


    static {
        try {
            BTC_MAX_TRADE_MONEY = futures_appBundle.getString("btc_max_trade_money");
            BTC_MIN_TRADE_MONEY = futures_appBundle.getString("btc_min_trade_money");
            LTC_MAX_TRADE_MONEY = futures_appBundle.getString("ltc_max_trade_money");
            LTC_MIN_TRADE_MONEY = futures_appBundle.getString("ltc_min_trade_money");
        } catch (Exception e) {
            logger.error("加载期货配置文件futures_application.properties出错, Error:{}", e);
        }
    }
    
    //期货相关常量，从php代码导入
    public static int F_FUTURES_INPUT=40001;   //转入期货资产类型
    public static int F_FUTURES_OUTPUT=40002;  //转出期货资产类型
    public static int F_FUTURES_PASS=40003;    //转嫁期货资产类型
    public static int F_FUTURES_FEE=40004; //期货手续费
    
    //借贷用户转出资产来源定义
    public static int LOAN_TRANSFER_TYPE_FUTURES=1;// 转出到期货资产
    public static int LOAN_TRANSFER_TYPE_COINSAVE=2;// 转出到余币宝
    public static int LOAN_TRANSFER_TYPE_VC=3;// 转出到VC理财
    
    public static int F_STATIC_RIGHTS = 1; // 静态权益
    public static int F_CLOSE_P_L_M = 2;    // 月合约平仓盈亏
    public static int F_CLOSE_P_L_Q = 3;    // 季合约平仓盈亏
    public static int F_FEE = 4;    // 总手续费
    public static int F_MARGIN = 5; // 保证金(余额)
    public static int F_USED_MARGIN = 6;//已用保证金
    public static int F_FROZEN_MARGIN = 7;//冻结保证金
    public static int F_CLOSE_P_L_W = 8;    // 周合约平仓盈亏
    public static int F_TOTAL_P_L = 9;  // 总平仓盈亏
    public static int F_FEE_WEEK = 10;  // 周合约手续费
    public static int F_FEE_MONTH = 11; // 月合约手续费
    public static int F_FEE_QUARTER = 12; // 季合约手续费
    public static int F_INPUT_SUM = 13; // 转入总资产
    public static int F_OUTPUT_SUM = 14; // 转出总资产

    public static int F_CLOSE_P_L_Q2 = 15;  // 第二季合约平仓盈亏
    public static int F_FEE_QUARTER2 = 16; // 第二季合约手续费
    public static int S_TRANSFER_IN = 1001; //转入
    public static int S_TRANSFER_OUT = 1002; //转出
    //#币种的一个定义，之后会用到
    public static int CNY_INT=1;// 人民币
    public static int USD_INT=2;//   # 美元
    public static int BTC_INT=3;// 比特币
    public static int LTC_INT=4;//   # 莱特币
    
 // 不记录财务历史与记录坏账的字段
    public int[] NOT_WRITE_LOG_FIELDS ={F_CLOSE_P_L_M, F_CLOSE_P_L_Q,F_CLOSE_P_L_Q2, F_CLOSE_P_L_W,
           F_FEE_WEEK,F_FEE_MONTH,F_FEE_QUARTER,F_FEE_QUARTER2,F_INPUT_SUM,F_OUTPUT_SUM,F_FROZEN_MARGIN,F_USED_MARGIN};
    
    public static void main(String[] args){
        System.out.println("this is main");
    }


}
