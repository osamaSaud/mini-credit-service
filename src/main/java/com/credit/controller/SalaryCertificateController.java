package com.credit.controller;

import com.credit.model.SalaryCertificateResponse;
import com.credit.service.SalaryCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/salary-certificate")
@RequiredArgsConstructor
public class SalaryCertificateController {
    
    private final SalaryCertificateService salaryCertificateService;
    
    @PostMapping
    public ResponseEntity<SalaryCertificateResponse> getSalaryCertificate() {
        return ResponseEntity.ok(salaryCertificateService.getAndSaveSalaryCertificate());
    }
} 