package com.credit.model;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class SalaryCertificateResponse {
    private String message;
    @JsonProperty("isSuccess")
    private boolean success;
    private SalaryCertificateData data;

    @Data
    public static class SalaryCertificateData {
        private Object governmentSector;
        private PrivateSector privateSector;
    }

    @Data
    public static class PrivateSector {
        private EmploymentStatusInfo[] employmentStatusInfo;
    }

    @Data
    public static class EmploymentStatusInfo {
        private String fullName;
        private String basicWage;
        private String housingAllowance;
        private String otherAllowance;
        private String fullWage;
        private String employerName;
        private String dateOfJoining;
        private String workingMonths;
        private String employmentStatus;
        private String salaryStartingDate;
        private String establishmentActivity;
        private String commercialRegistrationNumber;
        private String legalEntity;
        private String dateOfBirth;
        private String nationality;
        private String gosinumber;
    }
}