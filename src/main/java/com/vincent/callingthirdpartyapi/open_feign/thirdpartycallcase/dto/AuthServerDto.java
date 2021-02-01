package com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vincent
 */
@NoArgsConstructor
@Data
public class AuthServerDto {
    /**
     * access_token : 2e63f6f5-c546-427e-8a1c-f4db48671bf8
     * token_type : bearer
     * expires_in : 7199
     */
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private Integer expiresIn;
}
