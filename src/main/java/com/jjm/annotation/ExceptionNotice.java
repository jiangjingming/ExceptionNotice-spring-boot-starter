package com.jjm.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

@Target({ TYPE, METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExceptionNotice {

    /** 代码相关负责人： **/
    String noticeNickName() default "all";

    /** 通知消息的备注内容： **/
    String noticeRemarks() default "无";

}