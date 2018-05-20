package com.pridervip.gate.mobile.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2014/12/4.
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface MobileDataCoat {
    int status() default 0;

    String version() default "0.0.1";
}
