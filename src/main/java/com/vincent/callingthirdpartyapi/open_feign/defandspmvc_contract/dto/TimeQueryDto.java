package com.vincent.callingthirdpartyapi.open_feign.defandspmvc_contract.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author vincent
 */
@Data
public class TimeQueryDto implements Serializable {

    private String app;

    /**
     * 使用 API 的唯一凭证
     */
    private String appkey;
    /**
     * md5 后的 32 位密文
     */
    private String sign;
    /**
     * 返回数据格式: json、xml
     */
    private String format;
}
