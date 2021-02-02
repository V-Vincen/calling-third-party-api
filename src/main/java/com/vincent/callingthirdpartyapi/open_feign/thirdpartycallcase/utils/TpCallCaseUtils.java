package com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincent.callingthirdpartyapi.commons.ResponseDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.enums.ResultCodeErrorEnum;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author vincent
 */
public class TpCallCaseUtils {

    /**
     * 返回错误信息
     *
     * @param errorMsg 错误信息
     * @param response HttpServletResponse
     */
    public static void writeJson(String errorMsg, HttpServletResponse response) {
        try {
            String json = new ObjectMapper().writeValueAsString(ResponseDto.error(ResultCodeErrorEnum.TOKEN_ERROR, errorMsg));
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter writer = response.getWriter();
            writer.print(json);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 毫秒值转时间
     *
     * @param currentTimeMillis 毫秒值
     * @return 时间格式（yyyy-MM-dd HH:mm:ss.SSS）
     */
    public static String millisConvertToDate(Long currentTimeMillis) {
        Instant instant = Instant.ofEpochMilli(currentTimeMillis);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneId.SHORT_IDS.get("CTT")));
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }
}
