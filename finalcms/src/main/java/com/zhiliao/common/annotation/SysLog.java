package com.zhiliao.common.annotation;

import java.lang.annotation.*;

/**
 * Created by binary on 2017/4/10.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SysLog {

    String value() default "";

}
