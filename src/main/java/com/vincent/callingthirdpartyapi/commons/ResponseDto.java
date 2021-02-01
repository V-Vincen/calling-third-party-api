package com.vincent.callingthirdpartyapi.commons;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Optional;

/**
 * @param <T>
 * @author vincent
 * 返回前端数据封装
 */
public class ResponseDto<T> implements Serializable {
    private int status;
    private String msg;
    private T data;

    private ResponseDto() {
    }

    private ResponseDto(int status) {
        this.status = status;
    }

    private ResponseDto(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ResponseDto(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ResponseDto(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ResponseDto(ResultCode responseCode, T data) {
        this(responseCode, null, data);
    }

    private ResponseDto(ResultCode responseCode, String detailMsg, T data) {
        this.status = responseCode.getCode();
        this.msg = Optional.ofNullable(detailMsg)
                .map(deMsg -> String.format("%s : %s", responseCode.getDesc(), deMsg))
                .orElse(responseCode.getDesc());
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResultCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public static <T> ResponseDto<T> success() {
        return success(null);
    }

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(ResultCode.SUCCESS, data);
    }

    public static <T> ResponseDto<T> success(String msg, T data) {
        return new ResponseDto<>(ResultCode.SUCCESS, msg, data);
    }

    public static <T> ResponseDto<T> error() {
        return error(ResultCode.ERROR, null);
    }

    public static <T> ResponseDto<T> error(String errorMsg) {
        return new ResponseDto<>(ResultCode.ERROR, errorMsg, null);
    }

    public static <T> ResponseDto<T> error(ResultCode responseCode) {
        return error(responseCode, null, null);
    }

    public static <T> ResponseDto<T> error(ResultCode responseCode, String errorMsg) {
        return error(responseCode, errorMsg, null);
    }

    public static <T> ResponseDto<T> error(ResultCode responseCode, String errorMsg, T data) {
        return new ResponseDto<>(responseCode, errorMsg, data);
    }

    public static <T> ResponseDto<T> exception(String exceptionMsg) {
        return new ResponseDto<>(ResultCode.EXCEPTION, exceptionMsg, null);
    }
}
