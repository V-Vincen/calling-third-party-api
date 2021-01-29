package com.vincent.callingthirdpartyapi.commons;

/**
 * @author vincent
 * 返回状态的枚举
 */
public enum BaseResultCode implements ResultCode {
    /**
     * SUCCESS: 0
     * ERROR: 1
     * Exception: -1
     */
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR"),
    EXCEPTION(-1, "Exception");

    private final int code;
    private final String desc;

    BaseResultCode(int code, String desc) {
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