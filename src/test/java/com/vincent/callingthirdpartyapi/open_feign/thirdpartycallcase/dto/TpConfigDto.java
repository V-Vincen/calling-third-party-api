package com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vincent
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TpConfigDto {
    private String clientId;
    private String clientSecret;
    private String hostName;
}
