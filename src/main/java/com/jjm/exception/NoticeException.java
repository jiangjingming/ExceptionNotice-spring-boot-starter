package com.jjm.exception;


import com.alibaba.fastjson.JSON;
import com.jjm.consts.enums.ErrorCodeEnum;
import com.jjm.model.MessageTextModel;
import com.jjm.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * Created by baili on 17/08/30 13:09.
 */
@Slf4j
public class NoticeException extends RuntimeException {

    private static final String CONTENT_TYPE_KEY = "Content-Type";

    private static final String CONTENT_TYPE_VALUE = "application/json; charset=utf-8";

    private static final String CODE = "utf-8";

    protected String code;

    private static String DING_DING_ROBOT_URL = SpringUtil.getBean("dingDingRobotUrl").toString();
    private static Executor executor = SpringUtil.getBean("exceptionNoticeThreadPool", Executor.class);

    public NoticeException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getErrMsg());
        this.code = errorCodeEnum.getErrCode();
        this.sendDingDingRobotTextMessage(errorCodeEnum.getErrMsg());

    }

    public NoticeException(String errorMsg) {
        super(errorMsg);
        this.sendDingDingRobotTextMessage(errorMsg);
    }

    /**
     * 发送信息到钉钉机器人
     *
     * @param errorMsg
     * @return
     */
    public static void sendDingDingRobotTextMessage(String errorMsg) {
        executor.execute(() -> {

            MessageTextModel messageTextModel = new MessageTextModel();
            MessageTextModel.Text textContent = new MessageTextModel.Text();
            textContent.setContent(errorMsg);
            MessageTextModel.At at = new MessageTextModel.At();
            at.setAtAll(true);
            messageTextModel.setText(textContent);
            messageTextModel.setAt(at);
            String textMsg = JSON.toJSONString(messageTextModel);
            sendMarkDownErrorMsgDingDingRobot(textMsg);
        });

    }

    public static void sendMarkDownErrorMsgDingDingRobot(String textMsg) {
        try {
            HttpPost httppost = new HttpPost(DING_DING_ROBOT_URL);
            httppost.addHeader(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE);
            StringEntity se = new StringEntity(textMsg, CODE);
            httppost.setEntity(se);
            HttpClient httpclient = HttpClients.createDefault();
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                log.info("异常信息发送钉钉机器人成功！");
            }
        } catch (IOException e) {
            log.error("发送钉钉消息异常,异常信息为：{}", e);
        }
    }
}
