package com.neusoft.mid.msf.alert.service;

import com.alibaba.nacos.api.naming.pojo.Instance;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: PromethusPushService$
 * @Author Tao
 * @Date: 2023/7/11$ 14:52$
 * @Version 1.0
 */
@Slf4j
@Component
public class PromethusPushService {

    @Value("${push.address}")
    String address;

    /**
     * @param serviceName 服务名
     * @param list        服务实例
     * @param value       指标值
     */
    public void push(String serviceName, List<Instance> list, int value) {
        // 创建一个CollectorRegistry对象
        CollectorRegistry registry = new CollectorRegistry();

        // 创建一个Gauge指标
        Gauge gauge = Gauge.build()
                .name("nacos_metric")
                .help("Nacos service down metric")
                .register(registry);
        // 设置指标值
        gauge.set(value);

        // 创建一个PushGateway对象，指定Pushgateway的地址
        PushGateway pushGateway = new PushGateway(address);
        for (Instance instance : list) {
            // 创建一个包含多个标签的Map
            Map<String, String> labels = new HashMap<>();
            labels.put("serviceName", serviceName);
            labels.put("ip", instance.getIp());
            labels.put("port", String.valueOf(instance.getPort()));
            // 使用pushAdd方法将指标和标签推送到Pushgateway
            try {
                pushGateway.pushAdd(registry, "nacos_job", labels);
                log.info("推送消息成功！");
            } catch (IOException e) {
                log.info("推送消息失败", e);
            }
        }
    }
}