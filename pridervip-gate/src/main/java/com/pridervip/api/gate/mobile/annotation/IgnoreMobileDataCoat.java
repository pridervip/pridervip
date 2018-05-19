package com.bighuobi.api.gate.mobile.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2014/12/4.
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface IgnoreMobileDataCoat {
}
