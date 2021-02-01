package com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.enums;

import com.vincent.callingthirdpartyapi.commons.ResultCode;

/**
 * @author vincent
 */
public enum ResultCodeErrorEnum implements ResultCode {
    /**
     * 业务类型错误枚举
     */
    AUTH_TYPE_ERROR(18900, "权限校验类型错误"),
    AUTH_ERROR(18901, "权限校验错误"),
    TOKEN_ERROR(18902, "Token 校验错误"),
    ;

    private final int code;
    private final String desc;

    ResultCodeErrorEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
