package com.ogc.boardingschedule.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetail {

    Long id;

    String name;
    String role;

    Long enterpriseId;

    String enterpriseName;


}