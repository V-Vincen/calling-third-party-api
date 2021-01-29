package com.vincent.callingthirdpartyapi.commons;

/**
 * @author vincent
 */
public interface ResultCode {
    /**
     * 获取 code
     *
     * @return int
     */
    int getCode();

    /**
     * 获取 desc
     *
     * @return String
     */
    String getDesc();

    BaseResultCode SUCCESS = BaseResultCode.SUCCESS;
    BaseResultCode ERROR = BaseResultCode.ERROR;
    BaseResultCode EXCEPTION = BaseResultCode.EXCEPTION;
}
