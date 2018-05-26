/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.pridervip.gate.utils;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Administrator $Id$
 */
public class ExceptionQueue {

    public static ConcurrentLinkedQueue<Exception> exceptionQueue = new ConcurrentLinkedQueue<Exception>();

}
