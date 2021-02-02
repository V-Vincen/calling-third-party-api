package com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vincent
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TpDepartmentDto {
    private Long id;
    /**
     * 部门名称
     */
    private String name;

    /**
     * 英文名称
     */
    @JsonProperty("name_en")
    private String nameEn;

    /**
     * 父亲部门id。根部门为1
     */
    @JsonProperty("parent_Id")
    private Long parentId;

    /**
     * 职位
     */
    private String position;

    /**
     * 在父部门中的次序值。order值大的排序靠前。值范围是[0, 2^32)
     */
    private Long order;
}
