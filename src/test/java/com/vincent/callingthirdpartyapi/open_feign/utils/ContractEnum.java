package com.vincent.callingthirdpartyapi.open_feign.utils;

/**
 * @author vincent
 * feign 创建实例时, 与之配套使用的注解（这里暂时只支持 feign 本身默认的注解和 springmvc 的注解）
 */
public enum ContractEnum {
    DEFAULT("feign_default"),
    SPRINGMVC("springmvc");

    private final String value;

    ContractEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
