package com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.vincent.callingthirdpartyapi.commons.ResponseDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.enums.ResultCodeErrorEnum;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.utils.TpCallCaseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vincent
 * 一个简单的校验 token 的过滤器
 */
@Component
@WebFilter(filterName = "AccessTokenFilter", urlPatterns = "/*")
@Order(value = 1)
@Slf4j
public class AccessTokenFilter implements Filter {
    private static final List<String> NOT_ALLOWED_PATHS = ImmutableList.of("/autho/user/get", "/autho/department/list");

    public static final Map<String, List<String>> TOKEN_MAP = new ConcurrentHashMap<>(2);

    private static final String KEY = "token";

    /**
     * 假设 token 有效时间为 3 分钟
     */
    public static final Long EXPIRES_IN = 3 * 60 * 1000L;

    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println();
        log.info("AccessTokenFilter init...");
        // 初始化 token
        getTokenMap();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if (NOT_ALLOWED_PATHS.contains(path)) {
            System.out.println();
            log.info("AccessTokenFilter doFilter...");
            log.info("AccessTokenFilter check Token start !!!!!!");
            String accessToken = request.getParameter("accessToken");
            if (StringUtils.isEmpty(accessToken)) {
                log.warn("Parameter accessToken is empty...");
                TpCallCaseUtils.writeJson("Parameter accessToken is empty...", response);
                return;
            }
            log.info("Parameter accessToken is [{}]...", accessToken);

            List<String> tokenList = TOKEN_MAP.get(KEY);
            if (MapUtils.isNotEmpty(TOKEN_MAP) && TOKEN_MAP.containsKey(KEY)) {
                long expiresTime = Long.parseLong(tokenList.get(0));
                long currentTimeMillis = System.currentTimeMillis();
                if (expiresTime < currentTimeMillis) {
                    log.warn("Access Token has expired...");
                    TOKEN_MAP.clear();
                    log.info("Clear access Token...");
                    getTokenMap();
                    TpCallCaseUtils.writeJson("Access Token has expired...", response);
                    return;
                }
            }

            if (!StringUtils.equals(tokenList.get(1), accessToken)) {
                log.warn("Access Token is error...");
                TpCallCaseUtils.writeJson("Access Token is error...", response);
                return;
            }
            log.info("AccessTokenFilter check Token end !!!!!!\n");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        log.info("AccessTokenFilter destroy...");
    }

    /**
     * 生成 token
     */
    private void getTokenMap() {
        long currentTimeMillis = System.currentTimeMillis();
        long expiresTime = currentTimeMillis + EXPIRES_IN;
        String token = UUID.randomUUID().toString() + "_" + Base64.getEncoder().encodeToString("token".getBytes(StandardCharsets.UTF_8));
        List<String> linkedList = Lists.newLinkedList();
        linkedList.add(String.valueOf(expiresTime));
        linkedList.add(token);
        TOKEN_MAP.put(KEY, linkedList);
        log.info("Generate access Token: [{}], Expires Time: [{}] ...\n", token, TpCallCaseUtils.millisConvertToDate(expiresTime));
    }


}
