package com.vincent.callingthirdpartyapi.feign.dto;

import lombok.Data;

/**
 * @author vincent
 */
@Data
public class TpDepartmentQueryDto {
    private String accessToken;
    private Long id;
}
