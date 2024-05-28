package com.neusoft.mid.msf.alert.vo;

import lombok.Data;

/**
 * @Description: PushEntity$
 * @Author Tao
 * @Date: 2023/7/11$ 15:15$
 * @Version 1.0
 */
@Data
public class PushEntity {
    /**
     * 采集指标
     */
    private String metric;

    /**
     * 标签
     */
    private String tags;
    /**
     * 采集时间
     */
    private Long timestamp;

    /**
     * 指标值
     */
    private Long value;

}
