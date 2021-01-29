package com.vincent.callingthirdpartyapi.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author vincent
 */
@Data
public class TimeDto implements Serializable {

    /**
     * timestamp : 1611728644
     * datetime_1 : 2021-01-27 14:24:04
     * datetime_2 : 2021年01月27日 14时24分04秒
     * week_1 : 3
     * week_2 : 星期三
     * week_3 : 周三
     * week_4 : Wednesday
     */

    @JsonProperty("timestamp")
    private String timestamp;
    /**
     * datetime1
     */
    @JsonProperty("datetime_1")
    private String datetime1;
    /**
     * datetime2
     */
    @JsonProperty("datetime_2")
    private String datetime2;
    /**
     * week1
     */
    @JsonProperty("week_1")
    private String week1;
    /**
     * week2
     */
    @JsonProperty("week_2")
    private String week2;
    /**
     * week3
     */
    @JsonProperty("week_3")
    private String week3;
    /**
     * week4
     */
    @JsonProperty("week_4")
    private String week4;
}
