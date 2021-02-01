package com.vincent.callingthirdpartyapi.open_feign.defandspmvc_contract.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author vincent
 */
@Data
public class PhoneDto implements Serializable {
    /**
     * status : ALREADY_ATT
     * phone : 13800138000
     * area : 010
     * postno : 100000
     * att : 中国,北京
     * ctype : 北京移动全球通卡
     * par : 1380013
     * prefix : 138
     * operators : 移动
     * style_simcall : 中国,北京
     * style_citynm : 中华人民共和国,北京市
     */

    @JsonProperty("status")
    private String status;
    /**
     * 查询的手机号
     */
    @JsonProperty("phone")
    private String phone;
    /**
     * 区号
     */
    @JsonProperty("area")
    private String area;
    /**
     * 邮编
     */
    @JsonProperty("postno")
    private String postno;
    /**
     * 归属地样式1
     */
    @JsonProperty("att")
    private String att;
    /**
     * 卡类型
     */
    @JsonProperty("ctype")
    private String ctype;
    /**
     * 手机号前缀
     */
    @JsonProperty("par")
    private String par;
    /**
     * 号段
     */
    @JsonProperty("prefix")
    private String prefix;
    /**
     * 所属运营商
     */
    @JsonProperty("operators")
    private String operators;
    /**
     * 归属地样式1
     */
    @JsonProperty("style_simcall")
    private String styleSimcall;
    /**
     * 归属地样式2
     */
    @JsonProperty("style_citynm")
    private String styleCitynm;
}
