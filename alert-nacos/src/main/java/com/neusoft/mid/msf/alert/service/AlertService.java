//package com.neusoft.mid.msf.alert.service;
//
//
//import com.alibaba.nacos.api.PropertyKeyConst;
//import com.alibaba.nacos.api.exception.NacosException;
//import com.alibaba.nacos.api.naming.NamingService;
//import com.alibaba.nacos.api.naming.pojo.Instance;
//import com.alibaba.nacos.client.naming.NacosNamingService;
//import com.neusoft.mid.msf.common.msg.ObjectRestResponse;
//import io.prometheus.client.CollectorRegistry;
//import io.prometheus.client.Counter;
//import io.prometheus.client.Gauge;
//import io.prometheus.client.exporter.PushGateway;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.*;
//
///**
// * @Description: AlertService$
// * @Author Tao
// * @Date: 2023/7/10$ 14:21$
// * @Version 1.0
// */
//@Slf4j
//@Service
//public class AlertService {
//
//    @Value("${spring.cloud.nacos.config.server-addr}")
//    private String nacosServerAddress;
//
//    @Value("${spring.cloud.nacos.config.username}")
//    private String username;
//
//    @Value("${spring.cloud.nacos.config.password}")
//    private String password;
//
//    public ObjectRestResponse serviceNameList(Map<String, String> alertMap) {
//        Properties properties = new Properties();
//        properties.put(PropertyKeyConst.SERVER_ADDR, nacosServerAddress);
//        properties.put(PropertyKeyConst.USERNAME, username);
//        properties.put(PropertyKeyConst.PASSWORD, password);
//        List<Instance> allInstances = null;
//        try {
//            NamingService namingService = new NacosNamingService(properties);
//            allInstances = namingService.getAllInstances("admin", "CMS");
//        } catch (NacosException e) {
//            log.info("nacos实例获取失败{}", e);
//        }
//        List<Map<String, Object>> metricsData = new ArrayList<>();
//
//        for (Instance instance : allInstances) {
//            // 创建指标数据的字典
//            Map<String, Object> metric = new LinkedHashMap<>();
//
//            // 添加指标名称和标签
//            metric.put("__name__", "serviceNameList");
//            metric.put("target_ip", instance.getIp());
//            metric.put("target_port", instance.getPort());
//
//            // 添加其他指标的标签和值
//            // metric.put("label", value);
//
//            // 将指标数据添加到列表中
//            metricsData.add(metric);
//        }
//        return new ObjectRestResponse<>().data(metricsData);
//    }
//
//
//    public static void main(String[] args) throws Exception {
//// 创建一个CollectorRegistry对象
//        CollectorRegistry registry = new CollectorRegistry();
//
//        // 创建一个Gauge指标
//        Gauge gauge = Gauge.build()
//                .name("nacos_metric")
//                .help("Nacos service down metric")
//                .register(registry);
//        // 设置指标值
//        gauge.set(1);
//
//        // 创建一个PushGateway对象，指定Pushgateway的地址
//        PushGateway pushGateway = new PushGateway("127.0.0.1:9091");
//
//        // 创建一个包含多个标签的Map
//        Map<String, String> labels = new HashMap<>();
//        labels.put("serviceName", "admin");
//        labels.put("IP", "10.10.10.10");
//        labels.put("port", "8762");
//
//        // 使用pushAdd方法将指标和标签推送到Pushgateway
//        pushGateway.pushAdd(registry, "nacos_job", labels);
//
//    }
//
//}
