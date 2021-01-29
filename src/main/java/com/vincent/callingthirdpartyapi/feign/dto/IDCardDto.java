package com.vincent.callingthirdpartyapi.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author vincent
 */
@Data
public class IDCardDto implements Serializable {
    /**
     * status : ALREADY_ATT
     * idcard : 110101199001011114
     * par : 110101
     * born : 1990年01月01日
     * sex : 男
     * att : 北京市东城区
     * postno : 100000
     * areano : 010
     * style_simcall : 中国,北京
     * style_citynm : 中华人民共和国,北京市
     */

    @JsonProperty("status")
    private String status;
    /**
     * 查询的身份证号码
     */
    @JsonProperty("idcard")
    private String idcard;
    /**
     * 身份证前缀
     */
    @JsonProperty("par")
    private String par;
    /**
     * 出生年月日
     */
    @JsonProperty("born")
    private String born;
    /**
     * 性别
     */
    @JsonProperty("sex")
    private String sex;
    /**
     * 归属地
     */
    @JsonProperty("att")
    private String att;
    /**
     * 邮编
     */
    @JsonProperty("postno")
    private String postno;
    /**
     * 区号
     */
    @JsonProperty("areano")
    private String areano;
    /**
     * 地区1
     */
    @JsonProperty("style_simcall")
    private String styleSimcall;
    /**
     * 地区2
     */
    @JsonProperty("style_citynm")
    private String styleCitynm;
}
