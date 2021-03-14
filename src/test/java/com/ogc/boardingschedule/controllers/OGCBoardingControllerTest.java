package com.ogc.boardingschedule.controllers;

import com.ogc.boardingschedule.domain.BoardingDetail;
import com.ogc.boardingschedule.domain.EmployeeDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class OGCBoardingControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createBoardingScheduleTest() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";

        String createEmployee = "http://localhost:" + this.port + "v1/ogc/employee/name/Teste/role/Funcionario/enterpriseId/1";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String initDate = LocalDate.now().toString();
        LocalDate startDate = LocalDate.parse(initDate, formatter);

        String createBoarding = "http://localhost:" + this.port + "/v1/ogc/boarding/employeeid/1/initdate/"+startDate;

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createEmployee, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createBoarding, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));


    }

    @Test
    void createBoardingScheduleOnDayOffTest() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";

        String createEmployee = "http://localhost:" + this.port + "v1/ogc/employee/name/Teste/role/Funcionario/enterpriseId/1";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String initDate = LocalDate.now().toString();
        String dayoff = LocalDate.now().toString();
        LocalDate startDate = LocalDate.parse(initDate, formatter);
        LocalDate dayOffDate = LocalDate.parse(dayoff, formatter);
        dayOffDate = dayOffDate.plusDays(16);

        String createBoarding = "http://localhost:" + this.port + "/v1/ogc/boarding/employeeid/1/initdate/"+startDate;

        String dayoffBoarding = "http://localhost:" + this.port + "/v1/ogc/boarding/employeeid/1/initdate/"+dayOffDate;

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createEmployee, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createBoarding, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(dayoffBoarding, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @Test
    void createBoardingScheduleOnBoardPeriodTest() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";

        String createEmployee = "http://localhost:" + this.port + "v1/ogc/employee/name/Teste/role/Funcionario/enterpriseId/1";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String initDate = LocalDate.now().toString();
        String onBoarding = LocalDate.now().toString();
        LocalDate startDate = LocalDate.parse(initDate, formatter);
        LocalDate onBoardingDate = LocalDate.parse(onBoarding, formatter);
        onBoardingDate = onBoardingDate.plusDays(5);

        String createBoarding = "http://localhost:" + this.port + "/v1/ogc/boarding/employeeid/1/initdate/"+startDate;

        String dayoffBoarding = "http://localhost:" + this.port + "/v1/ogc/boarding/employeeid/1/initdate/"+onBoardingDate;

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createEmployee, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createBoarding, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(dayoffBoarding, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @Test
    void getBoardingDetailTest() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";

        String createEmployee = "http://localhost:" + this.port + "v1/ogc/employee/name/Teste/role/Funcionario/enterpriseId/1";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String initDate = LocalDate.now().toString();
        String nextSchedule = LocalDate.now().toString();
        LocalDate startDate = LocalDate.parse(initDate, formatter);
        LocalDate initDate2 = LocalDate.parse(nextSchedule, formatter);
        initDate2 = initDate2.plusDays(23);

        String createBoarding = "http://localhost:" + this.port + "/v1/ogc/boarding/employeeid/1/initdate/"+startDate;

        String dayoffBoarding = "http://localhost:" + this.port + "/v1/ogc/boarding/employeeid/1/initdate/"+initDate2;

        String boardDetails = "http://localhost:" + this.port + "/v1/ogc/boardings";

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createEmployee, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createBoarding, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(dayoffBoarding, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        BoardingDetail []boardingDetail =
                this.restTemplate.getForObject(boardDetails, BoardingDetail[].class);

        BoardingDetail detail = boardingDetail[0];

        assertThat(boardingDetail.length, equalTo(2));

        assertThat(detail.getInitDate().format(formatter), equalTo(startDate.format(formatter)));

        assertThat(detail.getEndDate().format(formatter), equalTo(startDate.plusDays(15).format(formatter)));

    }

    @Test
    void getBoardingDetailByEmployeeTest() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";

        String createEmployee = "http://localhost:" + this.port + "v1/ogc/employee/name/Teste/role/Funcionario/enterpriseId/1";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String initDate = LocalDate.now().toString();
        String nextSchedule = LocalDate.now().toString();
        LocalDate startDate = LocalDate.parse(initDate, formatter);
        LocalDate initDate2 = LocalDate.parse(nextSchedule, formatter);
        initDate2 = initDate2.plusDays(23);

        String createBoarding = "http://localhost:" + this.port + "/v1/ogc/boarding/employeeid/1/initdate/"+startDate;

        String dayoffBoarding = "http://localhost:" + this.port + "/v1/ogc/boarding/employeeid/1/initdate/"+initDate2;

        String boardDetails = "http://localhost:" + this.port + "/v1/ogc/boardings/employeeid/1";

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createEmployee, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createBoarding, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(dayoffBoarding, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        BoardingDetail []boardingDetail =
                this.restTemplate.getForObject(boardDetails, BoardingDetail[].class);

        BoardingDetail detail = boardingDetail[0];

        assertThat(boardingDetail.length, equalTo(2));

        assertThat(detail.getInitDate().format(formatter), equalTo(startDate.format(formatter)));

        assertThat(detail.getEndDate().format(formatter), equalTo(startDate.plusDays(15).format(formatter)));

    }

    @Test
    void getBoardingDetailByEnterpriseTest() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";

        String createEmployee = "http://localhost:" + this.port + "v1/ogc/employee/name/Teste/role/Funcionario/enterpriseId/1";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String initDate = LocalDate.now().toString();
        String nextSchedule = LocalDate.now().toString();
        LocalDate startDate = LocalDate.parse(initDate, formatter);
        LocalDate initDate2 = LocalDate.parse(nextSchedule, formatter);
        initDate2 = initDate2.plusDays(23);

        String createBoarding = "http://localhost:" + this.port + "/v1/ogc/boarding/employeeid/1/initdate/"+startDate;

        String dayoffBoarding = "http://localhost:" + this.port + "/v1/ogc/boarding/employeeid/1/initdate/"+initDate2;

        String boardDetails = "http://localhost:" + this.port + "/v1/ogc/boardings/enterpriseid/1";

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createEmployee, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createBoarding, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(dayoffBoarding, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        BoardingDetail []boardingDetail =
                this.restTemplate.getForObject(boardDetails, BoardingDetail[].class);

        BoardingDetail detail = boardingDetail[0];

        assertThat(boardingDetail.length, equalTo(2));

        assertThat(detail.getInitDate().format(formatter), equalTo(startDate.format(formatter)));

        assertThat(detail.getEndDate().format(formatter), equalTo(startDate.plusDays(15).format(formatter)));

    }

    @Test
    void deleteBoardingTest() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";

        String createEmployee = "http://localhost:" + this.port + "v1/ogc/employee/name/Teste/role/Funcionario/enterpriseId/1";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String initDate = LocalDate.now().toString();
        String nextSchedule = LocalDate.now().toString();
        LocalDate startDate = LocalDate.parse(initDate, formatter);
        LocalDate initDate2 = LocalDate.parse(nextSchedule, formatter);
        initDate2 = initDate2.plusDays(23);

        String createBoarding = "http://localhost:" + this.port + "/v1/ogc/boarding/employeeid/1/initdate/"+startDate;

        String dayoffBoarding = "http://localhost:" + this.port + "/v1/ogc/boarding/employeeid/1/initdate/"+initDate2;

        String delete = "http://localhost:" + this.port + "/v1/ogc/boarding/id/1";

        String boardDetails = "http://localhost:" + this.port + "/v1/ogc/boardings";

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createEmployee, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createBoarding, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(dayoffBoarding, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        this.restTemplate.delete(delete);

        BoardingDetail []boardingDetail =
                this.restTemplate.getForObject(boardDetails, BoardingDetail[].class);

        assertThat(boardingDetail.length, equalTo(1));


    }

    @Test
    void updateBoardingTest() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";

        String createEmployee = "http://localhost:" + this.port + "v1/ogc/employee/name/Teste/role/Funcionario/enterpriseId/1";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String initDate = LocalDate.now().toString();
        String nextSchedule = LocalDate.now().toString();
        LocalDate startDate = LocalDate.parse(initDate, formatter);
        LocalDate initDate2 = LocalDate.parse(nextSchedule, formatter);
        initDate2 = initDate2.plusDays(33);

        LocalDate initDate3 = startDate.plusDays(23);

        String createBoarding = "http://localhost:" + this.port + "/v1/ogc/boarding/employeeid/1/initdate/"+startDate;

        String dayoffBoarding = "http://localhost:" + this.port + "/v1/ogc/boarding/employeeid/1/initdate/"+initDate2;

        String update = "http://localhost:" + this.port + "/v1/ogc/boarding/id/2/employeeid/1/initdate/"+initDate3;

        String boardDetails = "http://localhost:" + this.port + "/v1/ogc/boardings";

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createEmployee, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createBoarding, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(dayoffBoarding, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        this.restTemplate.put(update, null);

        BoardingDetail []boardingDetail =
                this.restTemplate.getForObject(boardDetails, BoardingDetail[].class);

        BoardingDetail detail = boardingDetail[1];

        assertThat(boardingDetail.length, equalTo(2));

        assertThat(detail.getInitDate().format(formatter), equalTo(startDate.plusDays(23).format(formatter)));

    }

    @Test
    void getBoardingDetailBetweenInitDateAndEndDateTest() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";

        String createEmployee = "http://localhost:" + this.port + "v1/ogc/employee/name/Teste/role/Funcionario/enterpriseId/1";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String initDate = LocalDate.now().toString();
        String nextSchedule = LocalDate.now().toString();
        LocalDate startDate = LocalDate.parse(initDate, formatter);
        LocalDate initDate2 = LocalDate.parse(nextSchedule, formatter);
        initDate2 = initDate2.plusDays(23);

        LocalDate init = startDate.plusDays(5);
        LocalDate end = initDate2.plusDays(5);

        String createBoarding = "http://localhost:" + this.port + "/v1/ogc/boarding/employeeid/1/initdate/"+startDate;

        String dayoffBoarding = "http://localhost:" + this.port + "/v1/ogc/boarding/employeeid/1/initdate/"+initDate2;

        String boardDetails = "http://localhost:" + this.port + "/v1/ogc/boarding/initdate/"+init+"/enddate/"+end;

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createEmployee, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createBoarding, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(dayoffBoarding, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        BoardingDetail []boardingDetail =
                this.restTemplate.getForObject(boardDetails, BoardingDetail[].class);

        BoardingDetail detail = boardingDetail[0];

        assertThat(boardingDetail.length, equalTo(2));

        assertThat(detail.getInitDate().format(formatter), equalTo(startDate.format(formatter)));

        assertThat(detail.getEndDate().format(formatter), equalTo(startDate.plusDays(15).format(formatter)));

    }


}
