package com.ogc.boardingschedule.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardingDetail {
    Long id;

    Long employeeId;
    LocalDateTime initDate;
    LocalDateTime endDate;

    String employeeName;
    String role;

    Long enterpriseId;
    String enterpriseName;
}
