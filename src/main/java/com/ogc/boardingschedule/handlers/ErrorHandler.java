package com.ogc.boardingschedule.handlers;

import com.ogc.boardingschedule.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ErrorHandler {

    private static final String COMPANY_EXISTS_ERROR_CODE = "C001";
    private static final String COMPANY_INSERT_ERROR_ID_CODE = "C002";
    private static final String COMPANY_ERROR_CODE = "C003";
    private static final String COMPANY_NOT_EXISTS_ERROR_CODE = "C004";
    private static final String EMPLOYEE_ERROR_CODE = "C005";
    private static final String EMPLOYEE_NOT_EXISTS_ERROR_CODE = "C006";
    private static final String EMPLOYEE_IN_DAY_OFF_ERROR_CODE = "C007";
    private static final String EMPLOYEE_ON_BOARD_ERROR_CODE = "C008";
    private static final String WRONG_INIT_DATE_ERROR_CODE = "C009";
    private static final String BOARDING_ERROR_CODE = "C0010";
    private static final String BOARDING_SCHEDULE_ERROR_CODE = "C0011";
    private static final String COMPANY_RESP_ERROR_ID_MESSAGE = "CNPJ inválido.";
    private static final String PERSON_RESP_ERROR_CODE = "P001";
    private static final String PERSON_RESP_ERROR_MESSAGE = "Falha ao obter dados de pessoa física";
    private static final String PERSON_RESP_ERROR_ID_CODE = "P002";
    private static final String PERSON_RESP_ERROR_ID_MESSAGE = "CPF inválido.";
    private static final String AUTHORIZATION_RESP_ERROR_CODE = "A001";
    private static final String AUTHORIZATION_RESP_ERROR_MESSAGE = "Erro interno";


    @ExceptionHandler(EnterpriseException.class)
    public ResponseEntity handleCompanyException(EnterpriseException e) {
        log.error("Erro :: messagem: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.builder().code(COMPANY_ERROR_CODE).message(e.getMessage()).build());
    }

    @ExceptionHandler(EnterpriseExistsException.class)
    public ResponseEntity handleCompanyExistsException(EnterpriseExistsException e) {
        log.error("Erro na tentativa de inserir empresa :: messagem: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.builder().code(COMPANY_EXISTS_ERROR_CODE).message(e.getMessage()).build());
    }

    @ExceptionHandler(EnterpriseInsertException.class)
    public ResponseEntity handleCompanyInsertException(EnterpriseInsertException e) {
        log.error("Erro na tentativa de inserir empresa :: messagem: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.builder().code(COMPANY_INSERT_ERROR_ID_CODE).message(e.getMessage()).build());
    }

    @ExceptionHandler(EnterpriseNotExistsException.class)
    public ResponseEntity handleCompanyNotExistsException(EnterpriseNotExistsException e) {
        log.error("Empresa buscada não existe :: messagem: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.builder().code(COMPANY_NOT_EXISTS_ERROR_CODE).message(e.getMessage()).build());
    }

    @ExceptionHandler(EmployeeException.class)
    public ResponseEntity handleEmployeeException(EmployeeException e) {
        log.error("Erro :: messagem: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.builder().code(EMPLOYEE_ERROR_CODE).message(e.getMessage()).build());
    }

    @ExceptionHandler(EmployeeNotExistsException.class)
    public ResponseEntity handleEmployeeNotExistsException(EmployeeNotExistsException e) {
        log.error("Empregado buscado não existe :: messagem: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.builder().code(EMPLOYEE_NOT_EXISTS_ERROR_CODE).message(e.getMessage()).build());
    }

    @ExceptionHandler(EmployeeInDayOffException.class)
    public ResponseEntity handleEmployeeInDayOffException(EmployeeInDayOffException e) {
        log.error("Empregado está de folga :: messagem: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.builder().code(EMPLOYEE_IN_DAY_OFF_ERROR_CODE).message(e.getMessage()).build());
    }

    @ExceptionHandler(EmployeeOnBoardException.class)
    public ResponseEntity handleEmployeeOnBoardException(EmployeeOnBoardException e) {
        log.error("Empregado já está embarcado :: messagem: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.builder().code(EMPLOYEE_ON_BOARD_ERROR_CODE).message(e.getMessage()).build());
    }

    @ExceptionHandler(WrongInitDateException.class)
    public ResponseEntity handleWrongInitDateException(WrongInitDateException e) {
        log.error("Não pode criar agenda para o passado :: messagem: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.builder().code(WRONG_INIT_DATE_ERROR_CODE).message(e.getMessage()).build());
    }

    @ExceptionHandler(BoardingInsertException.class)
    public ResponseEntity handleBoardingInsertException(BoardingInsertException e) {
        log.error("Erro no agendamento :: messagem: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.builder().code(BOARDING_ERROR_CODE).message(e.getMessage()).build());
    }

    @ExceptionHandler(BoardingScheduleNotExistsException.class)
    public ResponseEntity handleBoardingScheduleNotExistsException(BoardingScheduleNotExistsException e) {
        log.error("Agendamento de embarque não existe:: messagem: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.builder().code(BOARDING_SCHEDULE_ERROR_CODE).message(e.getMessage()).build());
    }
    /*
    @ExceptionHandler(CompanyInformationException.class)
    public ResponseEntity handleCompanyInformationException(CompanyInformationException e) {
        log.error("Erro ao tentar obter dados de pessoa jurídica  :: messagem: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.builder().code(COMPANY_RESP_ERROR_CODE).message(COMPANY_RESP_ERROR_MESSAGE).build());
    }

    @ExceptionHandler(PersonInformationException.class)
    public ResponseEntity handlePersonInformationException(PersonInformationException e) {
        log.error("Erro ao tentar obter dados de pessoa física  :: messagem: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.builder().code(PERSON_RESP_ERROR_CODE).message(PERSON_RESP_ERROR_MESSAGE).build());
    }

    @ExceptionHandler(InvalidCNPJException.class)
    public ResponseEntity handleInvalidCNPJException(InvalidCNPJException e) {
        log.error("CNPJ inválido :: messagem: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ErrorMessage.builder().code(COMPANY_RESP_ERROR_ID_CODE).message(COMPANY_RESP_ERROR_ID_MESSAGE).build());
    }

    @ExceptionHandler(InvalidCPFException.class)
    public ResponseEntity handleInvalidCPFException(InvalidCPFException e) {
        log.error("CPF inválido :: messagem: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ErrorMessage.builder().code(PERSON_RESP_ERROR_ID_CODE).message(PERSON_RESP_ERROR_ID_MESSAGE).build());
    }
*/
}

