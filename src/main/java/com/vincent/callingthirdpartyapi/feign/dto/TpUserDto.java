package com.vincent.callingthirdpartyapi.feign.dto;

import lombok.Data;

/**
 * @author vincent
 */
@Data
public class TpUserDto {
    private String userId;
    private String userCode;
    private String userName;
}
