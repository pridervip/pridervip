package com.bighuobi.api.gate.mobile.utils;

import com.bighuobi.api.gate.filter.SessionAware;
import com.bighuobi.api.gate.mobile.annotation.MobileDataCoat;
import com.bighuobi.api.gate.mobile.entity.MobileData;
import com.bighuobi.api.gate.utils.Constants;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2014/12/4.
 */
@Component
@Aspect
public class MobileDataGenerator {

    private static final Logger logger = LoggerFactory.getLogger(MobileDataGenerator.class);

    @Pointcut("execution(* com.bighuobi.api.gate.controller.mobile.*.*(..)) || execution(* com.bighuobi.api.gate.controller.api.*.*(..)) || @annotation(com.bighuobi.api.gate.mobile.annotation.MobileDataCoat)")
    public void pointcut() {

    }

    @Around("pointcut() && !@annotation(com.bighuobi.api.gate.mobile.annotation.IgnoreMobileDataCoat)")
    public Object generatorData(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        int status = 0;
        String version = Constants.MOBILE_DATA_VERSION;
        if (method.isAnnotationPresent(MobileDataCoat.class)) {
            MobileDataCoat mobileDataCoat = method.getAnnotation(MobileDataCoat.class);
            status = mobileDataCoat.status();
            version = mobileDataCoat.version();
        }
        MobileData data = new MobileData();
        data.setStatus(status);
        data.setMessage("success");
        if(SessionAware.getRequest()!=null){//为了兼容测试用例
            data.setPath(SessionAware.getRequest().getServletPath().replaceAll("/", "-"));
        }
        data.setVersion(version);
        data.setData(pjp.proceed());
        return data;
    }
}
