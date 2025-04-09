package com.credit.client;

import feign.Client;
import feign.Request;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class SalaryCertificateClientConfig {

    @Bean
    public Client feignClient() {
        return new ApacheHttpClient(HttpClientBuilder.create()
            .disableRedirectHandling()
            .build());
    }

    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(
            10, TimeUnit.SECONDS, // connectTimeout
            10, TimeUnit.SECONDS, // readTimeout
            true // followRedirects
        );
    }
} 