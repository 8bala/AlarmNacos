package com.neusoft.mid.msf.alert;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Description: AlertNacosApplication$
 * @Author Tao
 * @Date: 2023/6/30$ 15:49$
 * @Version 1.0
 */

@Slf4j
@EnableScheduling
@SpringBootApplication
public class AlertNacosApplication {
    public static void main(String[] args) {
        try {
            new SpringApplicationBuilder(AlertNacosApplication.class).run(args);
            log.info("启动成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("启动失败，原因是:{}", e.getMessage());
        }
    }
}