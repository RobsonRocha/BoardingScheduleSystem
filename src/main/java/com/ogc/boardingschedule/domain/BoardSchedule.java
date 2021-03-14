package com.ogc.boardingschedule.domain;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardSchedule {
    @SerializedName("id")
    Long id;

    Long employeeId;
    LocalDate initDate;
    LocalDate endDate;
}
