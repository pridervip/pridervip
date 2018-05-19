/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package my;

import java.math.BigInteger;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bighuobi.api.enums.CoinType;
import com.bighuobi.api.exception.CustomerException;
import com.bighuobi.api.service.impl.BaseTest;
import com.bighuobi.api.utils.RedisUtils;

/**
 * @author yanjg
 * 2015年1月5日
 */
public class Test22{
    public static void main(String[] args){
        String theStr="500500000000";
        System.out.println(new BigInteger(theStr).longValue());
    }

}
