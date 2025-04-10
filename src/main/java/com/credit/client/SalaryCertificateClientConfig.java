package com.credit.client;

import feign.Client;
import feign.Request;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class SalaryCertificateClientConfig {

    @Bean
    public Client feignClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(20);

        return new ApacheHttpClient(HttpClientBuilder.create()
            .setConnectionManager(connectionManager)
            .disableRedirectHandling()
            .build());
    }

    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(
            30, TimeUnit.SECONDS,  // connectTimeout - increased from 10 to 30 seconds
            30, TimeUnit.SECONDS,  // readTimeout - increased from 10 to 30 seconds
            true // followRedirects
        );
    }
} 