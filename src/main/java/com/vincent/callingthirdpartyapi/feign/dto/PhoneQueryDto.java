package com.vincent.callingthirdpartyapi.feign.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author vincent
 */
@Data
public class PhoneQueryDto implements Serializable {

    private String app;
    /**
     * 手机号码, 例如:13800138000
     */
    private String phone;
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
