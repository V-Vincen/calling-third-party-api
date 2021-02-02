package com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dao;


import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpConfigDto;
import org.springframework.stereotype.Repository;

/**
 * @author vincent
 */
@Repository
public class TpConfigDao {
    /**
     * 从数据库查询出 TpConfigDto 对象
     *
     * @param clientId 标识唯一
     * @return TpConfigDto
     */
    public TpConfigDto selectByClientId(String clientId) {
        // 当然这里你可以创建一个 TpConfigMapper, 通过 mybatis 来查询出 TpConfigDto, 并交给 spring 容器来管理（这里只是 demo 所以就简单模拟下）
        return new TpConfigDto(clientId, "client_secret", "http://localhost:8080/autho");
    }
}
