package com.vincent.callingthirdpartyapi.open_feign.spring_cloud_open_feign.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@Configuration
public class CircuitBreakerConfig {
    @Bean
    MyCircuitBreaker myCircuitBreaker() {
        return new MyCircuitBreaker();
    }

    @SuppressWarnings("rawtypes")
    @Bean
    CircuitBreakerFactory circuitBreakerFactory(MyCircuitBreaker myCircuitBreaker) {
        return new CircuitBreakerFactory() {
            @Override
            public CircuitBreaker create(String id) {
                log.info("Creating a circuit breaker with id [" + id + "]");
                return myCircuitBreaker;
            }

            @Override
            protected ConfigBuilder configBuilder(String id) {
                return Object::new;
            }

            @Override
            public void configureDefault(Function defaultConfiguration) {

            }
        };
    }

    static class MyCircuitBreaker implements CircuitBreaker {

        AtomicBoolean runWasCalled = new AtomicBoolean();

        @Override
        public <T> T run(Supplier<T> toRun) {
            try {
                this.runWasCalled.set(true);
                return toRun.get();
            }
            catch (Throwable throwable) {
                throw new NoFallbackAvailableException("No fallback available.", throwable);
            }
        }

        @Override
        public <T> T run(Supplier<T> toRun, Function<Throwable, T> fallback) {
            try {
                return run(toRun);
            }
            catch (Throwable throwable) {
                return fallback.apply(throwable);
            }
        }

        public void clear() {
            this.runWasCalled.set(false);
        }
    }
}
