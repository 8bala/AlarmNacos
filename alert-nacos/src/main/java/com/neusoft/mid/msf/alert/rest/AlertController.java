//package com.neusoft.mid.msf.alert.rest;
//
//import com.alibaba.fastjson.JSON;
//import com.neusoft.mid.msf.alert.service.AlertService;
//import com.neusoft.mid.msf.common.msg.ObjectRestResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import java.util.Map;
//
///**
// * @Description: AlertController$
// * @Author Tao
// * @Date: 2023/7/10$ 14:18$
// * @Version 1.0
// */
//@Slf4j
//@RestController
//@RequestMapping("/nacos/instance/list")
//public class AlertController {
//
//    @Resource
//    AlertService service;
//
//    @GetMapping
//    public ObjectRestResponse receiveAlert( Map<String, String> alertMap) {
//
//        log.info("请求微服务信息为{}", alertMap);
//        return service.serviceNameList(alertMap);
//
//    }
//}
