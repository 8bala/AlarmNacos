package com.neusoft.mid.msf.alert.config;

import com.neusoft.mid.msf.alert.listener.NacosListener;
import com.neusoft.mid.msf.alert.utils.Func;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;

/**
 * @Description: InitConfig$
 * @Author Tao
 * @Date: 2023/7/17$ 10:25$
 * @Version 1.0
 */
@Component
@Slf4j
public class InitConfig implements ApplicationRunner {

    @Value("${alert.nacos.groups}")
    private String groupName;

    @Resource
    NacosListener nacosListener;

    public static HashSet<String> groups = new HashSet<>(16);


    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("<<nacos配置组加载开始>>");
        String[] group = Func.toStr(groupName).split(",");
        groups.addAll(Arrays.asList(group));
        log.info("监控的微服务组为{}",groups);
        log.info("<<nacos配置组加载结束>>");
        nacosListener.listenServiceChange();
    }
}
