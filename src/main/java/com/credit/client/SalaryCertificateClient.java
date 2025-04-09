package com.credit.client;

import com.credit.model.SalaryCertificateRequest;
import com.credit.model.SalaryCertificateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "salaryCertificate", 
    url = "https://mock-staging-internal.tamaratech.co",
    configuration = SalaryCertificateClientConfig.class
)
public interface SalaryCertificateClient {
    
    @PostMapping("/umock/simah-report/api/v2/enquiry/consumer/salarycertificate")
    SalaryCertificateResponse getSalaryCertificate(@RequestBody SalaryCertificateRequest request);
} 