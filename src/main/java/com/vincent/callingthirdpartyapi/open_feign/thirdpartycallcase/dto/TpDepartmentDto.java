package com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author vincent
 */
@Data
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
     * 在父部门中的次序值。order值大的排序靠前。值范围是[0, 2^32)
     */
    private Long order;

    public TpDepartmentDto() {
    }

    public TpDepartmentDto(Long id, String name, String nameEn, Long parentId, Long order) {
        this.id = id;
        this.name = name;
        this.nameEn = nameEn;
        this.parentId = parentId;
        this.order = order;
    }
}
