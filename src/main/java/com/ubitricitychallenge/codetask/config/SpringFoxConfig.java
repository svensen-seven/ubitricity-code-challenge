package com.ubitricitychallenge.codetask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ubitricitychallenge.codetask"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "CarparkUbi API",
                "Sample API for CarparkUbi",
                "1.0",
                "Free to use",
                new Contact("Sergey Sindeev", "https://www.ubitricity.com/", "svsindeev@gmail.com"),
                "API License",
                "https://www.ubitricity.com/",
                Collections.emptyList());
    }

}
