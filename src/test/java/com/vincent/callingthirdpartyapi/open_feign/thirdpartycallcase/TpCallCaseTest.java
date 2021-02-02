package com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase;

import com.vincent.callingthirdpartyapi.CallingThirdPartyApiApplicationTests;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dao.TpConfigDao;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpConfigDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpDepartmentDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpDepartmentQueryDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpUserDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.helper.iml.TpApiHelperImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author vincent
 */
@SpringBootTest(classes = CallingThirdPartyApiApplicationTests.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class TpCallCaseTest {

    @Autowired
    private TpConfigDao tpConfigDao;

    @Test
    public void tpCallTest() {
        TpConfigDto tpConfigDto = tpConfigDao.selectByClientId("client_id");
        TpApiHelperImpl instance = TpApiHelperImpl.getInstance(tpConfigDto);

        log.info("GetUserDto method start...");
        TpUserDto tpUserDto = instance.getUserDto("userId");
        log.info("tpUserDto: {}...", tpUserDto);
        log.info("GetUserDto method end...\n");

        log.info("GetDepartmentDtos method start...");
        TpDepartmentQueryDto queryDto = new TpDepartmentQueryDto();
        queryDto.setIdOrParentId(19000L);
        queryDto.setPosition("员工");
        List<TpDepartmentDto> departmentDtos = instance.getDepartmentDtos(queryDto);
        departmentDtos.forEach(departmentDto -> log.info("departmentDto: {}...", departmentDto));
        log.info("GetDepartmentDtos method end...");
    }
}











