package com.neusoft.mid.msf.alert.listener;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.neusoft.mid.msf.alert.config.InitConfig;
import com.neusoft.mid.msf.alert.service.CategrafPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description: NacosConfig$
 * @Author Tao
 * @Date: 2023/7/3$ 10:11$
 * @Version 1.0
 */
@Slf4j
@Configuration
public class NacosListener {
    @Value("${spring.cloud.nacos.config.server-addr}")
    private String nacosServerAddress;

    @Value("${spring.cloud.nacos.config.username}")
    private String username;

    @Value("${spring.cloud.nacos.config.password}")
    private String password;



    @Resource
    private CategrafPushService pushService;

    public static Map<String, List<Instance>> instanceMap = new HashMap<>(16);

    @Scheduled(cron = "0 0 * * * *")
    public void listenServiceChange() throws NacosException {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, nacosServerAddress);
        properties.put(PropertyKeyConst.USERNAME, username);
        properties.put(PropertyKeyConst.PASSWORD, password);
        NamingService namingService = new NacosNamingService(properties);
        for (String group : InitConfig.groups) {
            List<String> serviceNames = namingService.getServicesOfServer(1, Integer.MAX_VALUE, group).getData();
            for (String serviceName : serviceNames) {
                namingService.subscribe(serviceName, group, new EventListener() {
                    @Override
                    public void onEvent(Event event) {
                        if (event instanceof NamingEvent) {
                            NamingEvent namingEvent = (NamingEvent) event;
                            List<Instance> instances = namingEvent.getInstances();

                            List<Instance> target = instanceMap.get(serviceName);

                            if (ObjectUtils.isEmpty(target) && !ObjectUtils.isEmpty(instances)) {
                                log.info("{}服务上线", serviceName);
                                log.info("实例消息为{}", instances.toString());
                                pushService.push(serviceName, instances, 0);
                            } else {
                                diff(instances, target, serviceName);
                            }
                            instanceMap.put(serviceName, instances);
                            // 处理服务上线和下线事件的逻辑
                        }
                    }
                });
            }
        }
    }

    private void diff(List<Instance> instances, List<Instance> target, String serviceName) {
        List<Instance> diffSourList;
        List<Instance> diffTarList;
        if (!ObjectUtils.isEmpty(instances)) {
            diffSourList = new ArrayList<>(instances);
        } else {
            diffSourList = new ArrayList<>();
        }
        if (!ObjectUtils.isEmpty(target)) {
            diffTarList = new ArrayList<>(target);
        } else {
            diffTarList = new ArrayList<>();
        }

        diffSourList.removeAll(target);
        diffTarList.removeAll(instances);

        if (ObjectUtils.isEmpty(diffSourList) && !ObjectUtils.isEmpty(diffTarList)) {
            log.info("{}服务下线", serviceName);
            log.info("实例消息为{}", diffTarList.toString());
            pushService.push(serviceName, diffTarList, 1);
        }
        if (ObjectUtils.isEmpty(diffTarList)&& !ObjectUtils.isEmpty(diffSourList)) {
            log.info("{}服务上线", serviceName);
            log.info("实例消息为{}", diffSourList.toString());
            pushService.push(serviceName, diffSourList, 0);
        }
    }

}
