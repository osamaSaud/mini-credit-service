package com.credit.service;

import com.credit.client.SalaryCertificateClient;
import com.credit.model.SalaryCertificate;
import com.credit.model.SalaryCertificateRequest;
import com.credit.model.SalaryCertificateResponse;
import com.credit.repository.SalaryCertificateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SalaryCertificateService {
    
    private final SalaryCertificateClient salaryCertificateClient;
    private final SalaryCertificateRepository salaryCertificateRepository;
    
    @Transactional
    public SalaryCertificateResponse getAndSaveSalaryCertificate() {
        // Create a sample request
        SalaryCertificateRequest request = new SalaryCertificateRequest();
        request.setNationalId("1234567890");
        request.setDateOfBirth("1990-01-01");
        
        // Get data from external API
        SalaryCertificateResponse response = salaryCertificateClient.getSalaryCertificate(request);
        
        // Save data to database if available
        if (response.isSuccess() && response.getData() != null && 
            response.getData().getPrivateSector() != null && 
            response.getData().getPrivateSector().getEmploymentStatusInfo() != null &&
            response.getData().getPrivateSector().getEmploymentStatusInfo().length > 0) {
            
            // Get the first employment status info
            var employmentInfo = response.getData().getPrivateSector().getEmploymentStatusInfo()[0];
            
            // Create and save salary certificate entity
            SalaryCertificate certificate = new SalaryCertificate();
            certificate.setFullName(employmentInfo.getFullName());
            certificate.setBasicWage(employmentInfo.getBasicWage());
            certificate.setHousingAllowance(employmentInfo.getHousingAllowance());
            certificate.setOtherAllowance(employmentInfo.getOtherAllowance());
            certificate.setFullWage(employmentInfo.getFullWage());
            certificate.setEmployerName(employmentInfo.getEmployerName());
            certificate.setDateOfJoining(employmentInfo.getDateOfJoining());
            certificate.setWorkingMonths(employmentInfo.getWorkingMonths());
            certificate.setEmploymentStatus(employmentInfo.getEmploymentStatus());
            certificate.setSalaryStartingDate(employmentInfo.getSalaryStartingDate());
            certificate.setEstablishmentActivity(employmentInfo.getEstablishmentActivity());
            certificate.setCommercialRegistrationNumber(employmentInfo.getCommercialRegistrationNumber());
            certificate.setLegalEntity(employmentInfo.getLegalEntity());
            certificate.setDateOfBirth(employmentInfo.getDateOfBirth());
            certificate.setNationality(employmentInfo.getNationality());
            certificate.setGosinumber(employmentInfo.getGosinumber());
            
            salaryCertificateRepository.save(certificate);
        }
        
        return response;
    }
} 