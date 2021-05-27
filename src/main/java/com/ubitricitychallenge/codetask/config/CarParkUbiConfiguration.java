package com.ubitricitychallenge.codetask.config;

import com.ubitricitychallenge.codetask.domain.BasicCarparkUbi;
import com.ubitricitychallenge.codetask.domain.CarparkUbi;
import com.ubitricitychallenge.codetask.services.ThreadSafeCarparkUbiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CarParkUbiConfiguration {

    @Bean
    public CarparkUbi carparkUbi() {
        return new BasicCarparkUbi();
    }
}
