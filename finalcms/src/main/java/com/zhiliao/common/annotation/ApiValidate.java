package com.zhiliao.common.annotation;

import java.lang.annotation.*;

/**
 * Created by binary on 2017/4/10.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiValidate {

    boolean checkSignature() default true;

}
