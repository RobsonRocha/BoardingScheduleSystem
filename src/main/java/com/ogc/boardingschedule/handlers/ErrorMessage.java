package com.ogc.boardingschedule.handlers;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessage {
    String code;
    String message;
}