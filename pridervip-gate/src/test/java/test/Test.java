/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. 
 *All Rights Reserved
 */
package test;

import java.math.BigDecimal;

/**
 * @author yanjg
 * 2015年2月8日
 */
public class Test {
    public static void main(String[] args){
        String intStr="4000.10";
        System.out.println(new BigDecimal(intStr).intValue());
    }
}
