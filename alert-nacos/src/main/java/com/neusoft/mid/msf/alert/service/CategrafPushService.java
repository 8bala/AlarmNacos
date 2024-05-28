package com.neusoft.mid.msf.alert.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.neusoft.mid.msf.alert.utils.RestTemplateUtil;
import com.neusoft.mid.msf.alert.vo.PushEntity;
import com.neusoft.mid.msf.common.util.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: CategrafPushService$
 * @Author Tao
 * @Date: 2023/7/11$ 15:10$
 * @Version 1.0
 */
@Slf4j
@Component
public class CategrafPushService {

    @Value("${push.address}")
    String address;

    @Resource
    private RestTemplateUtil restTemplateUtil;

    /**
     * @param serviceName 服务名
     * @param list        服务实例
     * @param value       指标值
     */
    public void push(String serviceName, List<Instance> list, int value) {
        String host = "http://" + address + "/api/push/openfalcon";
        Map header = new HashMap();
        header.put("sid", UUIDUtils.getUUID());
        for (Instance instance : list) {
            PushEntity entity = new PushEntity();
            entity.setMetric("nacos_metric");
            entity.setTimestamp(DateTime.now().getMillis());
            entity.setValue((long) value);
            entity.setTags("serviceName="+serviceName+",ip="+instance.getIp()+",port="+instance.getPort());
           restTemplateUtil.post(host, header,JSON.toJSON(entity), String.class);
           log.info("微服务采集信息推送成功");
        }
    }

}
