package com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.helper.iml;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpConfigDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpDepartmentDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpDepartmentQueryDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.dto.TpUserDto;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.feign.TpApi;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.feign.TpFeignClient;
import com.vincent.callingthirdpartyapi.open_feign.thirdpartycallcase.helper.TpApiHelper;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author vincent
 */
@Slf4j
public class TpApiHelperImpl implements TpApiHelper {
    private final TpApi tpApi;
    private final TpConfigDto tpConfigDto;
    private static volatile TpApiHelperImpl instance;
    private final LoadingCache<String, String> loadingCache;
    private static final Lock reentrantLock = new ReentrantLock();
    private static final String GRANT_TYPE = "client_credentials";

    public static TpApiHelperImpl getInstance(TpConfigDto tpConfigDto) {
        if (instance == null) {
            reentrantLock.lock();
            try {
                if (instance == null) {
                    instance = new TpApiHelperImpl(tpConfigDto);
                }
            } finally {
                reentrantLock.unlock();
            }
        }
        return instance;
    }

    private TpApiHelperImpl(TpConfigDto tpConfigDto) {
        Objects.requireNonNull(tpConfigDto);
        this.tpConfigDto = tpConfigDto;
        tpApi = TpFeignClient.getClient(TpApi.class, tpConfigDto.getHostName());
        loadingCache = CacheBuilder
                .newBuilder()
                .maximumSize(1)
                .expireAfterWrite(178, TimeUnit.SECONDS)
                .expireAfterAccess(180, TimeUnit.SECONDS)
                .removalListener((RemovalListener<String, String>) notification -> log.info("[ Token: {} ] is removed...", notification.getValue()))
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(@NonNull String key) {
                        String accessToken = tpApi.getAccessToken(GRANT_TYPE, key, tpConfigDto.getClientSecret()).getData().getAccessToken();
                        log.info("Get access Token with feign client first time: [{}]...", accessToken);
                        return accessToken;
                    }
                });
    }

    @Override
    public TpUserDto getUserDto(String userId) {
        try {
            String accessToken = loadingCache.get(tpConfigDto.getClientId());
            log.info("LoadingCache get access Token: [{}]...", accessToken);
            return tpApi.getUserDto(accessToken, userId).getData();
        } catch (ExecutionException e) {
            log.error("GetUserDto is error...");
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TpDepartmentDto> getDepartmentDtos(TpDepartmentQueryDto queryDto) {
        try {
            String accessToken = loadingCache.get(tpConfigDto.getClientId());
            log.info("LoadingCache get access Token: [{}]...", accessToken);
            return tpApi.getDepartmentDtos(accessToken, queryDto).getData();
        } catch (ExecutionException e) {
            log.error("GetDepartmentDtos is error...");
            throw new RuntimeException(e);
        }
    }
}
