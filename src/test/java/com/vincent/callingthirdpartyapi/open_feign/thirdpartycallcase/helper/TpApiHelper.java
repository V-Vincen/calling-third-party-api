package com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.helper;

import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpDepartmentDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpDepartmentQueryDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpUserDto;

import java.util.List;

/**
 * @author vincent
 */
public interface TpApiHelper {

    TpUserDto getUserDto(String userId);

    List<TpDepartmentDto> getDepartmentDtos(TpDepartmentQueryDto queryDto);
}
