package com.jjm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * 读取配置的钉钉RobotUrl
 * Created by jiangjingming on 2017/8/30.
 */
@Configuration
public class CommonConfig {

    @Value("${ambulance.dingDingRobot.url}")
    private String dingDingRobotUrl = "";

    /**
     * 配置钉钉机器人路由
     * @return
     */
    @Bean("dingDingRobotUrl")
    public String getDingDingRobotUrl() {
            return dingDingRobotUrl;
        }
}
