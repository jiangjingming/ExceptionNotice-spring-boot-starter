package com.jjm.aspect;

import com.alibaba.fastjson.JSON;
import com.jjm.annotation.ExceptionNotice;
import com.jjm.exception.NoticeException;
import com.jjm.model.MessageMarkDownModel;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 异常通知切面服务
 * Created by jiangjingming on 2017/6/28.
 */
@Aspect
@Component
@Slf4j
public class ExceptionNoticeAspect {

    /*@Autowired
    private RedisTemplate redisTemplate;*/

    @Pointcut("@annotation(com.jjm.annotation.ExceptionNotice)")
    public void exceptionNoticeAspect() {
    }

    @AfterThrowing(pointcut = "exceptionNoticeAspect()", throwing = "e")
    @Async
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("-------------------******异步线程开始************-------------------");

        /*//异常通知控制次数
        if (!redisCount(joinPoint)) {
            return;
        }*/
            log.error("异常开始捕捉=============================================");
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method me = methodSignature.getMethod();
            Object[] args = joinPoint.getArgs();
            ExceptionNotice exceptionNotice = me.getAnnotation(ExceptionNotice.class);
            // 获取用户请求方法的参数并序列化为JSON格式字符串
            String params = JSON.toJSONString(args);

            log.error("-------------------afterThrowing.handler.start-------------------");

            StringBuffer bufferErrorMsg = new StringBuffer();
            bufferErrorMsg
                    .append("代码相关负责人：")
                    .append(exceptionNotice.noticeNickName()).append(",").append("\n")
                    .append("异常自定义关键字：")
                    .append(exceptionNotice.noticeRemarks()).append(",").append("\n")
                    .append("请求方法：")
                    .append((joinPoint.getTarget().getClass().getName() + "."+ joinPoint.getSignature().getName() + "(),")).append("\n")
                    .append("请求参数：")
                    .append(params).append(",").append("\n")
                    .append("异常名称：")
                    .append(e.getClass().toString()).append(",").append("\n")
                    .append("异常栈信息：")
                    .append(e).append("\n");
            log.error(bufferErrorMsg.toString());

            try {
                //发送钉钉机器人消息
                sendDingDingMsg(exceptionNotice, joinPoint,params, (Exception) e);
            } catch (Exception e1) {
                log.error("发送钉钉机器人信息失败，异常信息：{}", e1);
            }
        log.error("-------------------afterThrowing.handler.end-------------------");

    }

    private void sendDingDingMsg(ExceptionNotice exceptionNotice, JoinPoint joinPoint, String params, Exception e ) {

        MessageMarkDownModel messageMarkDownModel = new MessageMarkDownModel();
        messageMarkDownModel.setMsgtype("markdown");
        MessageMarkDownModel.Markdown markdown = new MessageMarkDownModel.Markdown();
        markdown.setTitle("异常通知");
        markdown.setText(">### 异常详情\n" +
                "> 代码相关负责人:" + exceptionNotice.noticeNickName() + "\n" +
                "> 异常自定义关键字:" + exceptionNotice.noticeRemarks() + "\n" +
                "> 请求方法:" + joinPoint.getTarget().getClass().getName() + "."+ joinPoint.getSignature().getName() + "()" + "\n" +
                "> 请求参数:" + params + "\n" +
                "> 异常名称:" + e.getClass().toString() + "\n" +
                "> 异常栈信息:" + e
                );
        MessageMarkDownModel.At at = new MessageMarkDownModel.At();
        at.setAtAll(true);
        messageMarkDownModel.setMarkdown(markdown);
        messageMarkDownModel.setAt(at);
        String textMsg = JSON.toJSONString(messageMarkDownModel);
        NoticeException.sendMarkDownErrorMsgDingDingRobot(textMsg);
    }

    /**
     * 同一方法异常通知次数控制
     * @param joinPoint
     * @return
     *//*
    private boolean redisCount(JoinPoint joinPoint) {
        String redisKey = String.valueOf((joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName()).hashCode());
        BoundValueOperations<String, Integer> operations = redisTemplate.boundValueOps(redisKey);
        redisTemplate.expire(redisKey, 5, TimeUnit.MINUTES);
        operations.increment(1);
        if (operations.get() > 20) {
            log.error("同一个请求5分钟之内异常超过20次,不再通知了");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }*/
}
