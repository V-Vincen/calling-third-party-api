package com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto;

import lombok.Data;

/**
 * @author vincent
 */
@Data
public class TpDepartmentQueryDto {
    private Long idOrParentId;
    private String position;
}
