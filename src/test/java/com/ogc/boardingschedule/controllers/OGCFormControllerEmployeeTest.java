package com.ogc.boardingschedule.controllers;

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

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class OGCFormControllerEmployeeTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createEmployeeTest() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";

        String createEmployee = "http://localhost:" + this.port + "v1/ogc/employee/name/Teste/role/Funcionario/enterpriseId/1";

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createEmployee, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

    }

    @Test
    void getEmployeeByName() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";
        String createEmployee = "http://localhost:" + this.port + "v1/ogc/employee/name/Teste/role/Funcionario/enterpriseId/1";
        String getEmployee = "http://localhost:" + this.port + "/v1/ogc/employeesdetail/name/Teste";

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createEmployee, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        EmployeeDetail []employeeDetail =
                this.restTemplate.getForObject(getEmployee, EmployeeDetail[].class);

        assertThat(employeeDetail[0].getName(), equalTo("Teste"));

    }

    @Test
    void getEmployeeById() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";
        String createEmployee = "http://localhost:" + this.port + "v1/ogc/employee/name/Teste/role/Funcionario/enterpriseId/1";
        String getEmployee = "http://localhost:" + this.port + "/v1/ogc/employeesdetail/id/1";

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createEmployee, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        EmployeeDetail employeeDetail =
                this.restTemplate.getForObject(getEmployee, EmployeeDetail.class);

        assertThat(employeeDetail.getId(), equalTo(1L));

    }

    @Test
    void getEmployeeByWrongId() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";
        String createEmployee = "http://localhost:" + this.port + "v1/ogc/employee/name/Teste/role/Funcionario/enterpriseId/1";
        String getEmployee = "http://localhost:" + this.port + "/v1/ogc/employeesdetail/id/2";

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createEmployee, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        EmployeeDetail employeeDetail =
                this.restTemplate.getForObject(getEmployee, EmployeeDetail.class);

        assertThat(employeeDetail.getId(), equalTo(null));

    }

    @Test
    void getAllEmployees() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";
        String createEmployee = "http://localhost:" + this.port + "v1/ogc/employee/name/Teste/role/Funcionario/enterpriseId/1";
        String createEmployee2 = "http://localhost:" + this.port + "v1/ogc/employee/name/Teste2/role/Funcionario/enterpriseId/1";
        String getEmployees = "http://localhost:" + this.port + "/v1/ogc/employeesdetail";

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createEmployee, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createEmployee2, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        List<EmployeeDetail> employeeDetails =
                this.restTemplate.getForObject(getEmployees, List.class);

        assertThat(employeeDetails.size(), equalTo(2));

    }

    @Test
    void deleteEmployee(){
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";
        String createEmployee = "http://localhost:" + this.port + "v1/ogc/employee/name/Teste/role/Funcionario/enterpriseId/1";
        String deleteEmployee = "http://localhost:" + this.port + "v1/ogc/employee/id/1";
        String getEmployees = "http://localhost:" + this.port + "/v1/ogc/employeesdetail";

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createEmployee, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        this.restTemplate.delete(deleteEmployee);

        List<EmployeeDetail> employeeDetails =
                this.restTemplate.getForObject(getEmployees, List.class);

        assertThat(employeeDetails.size(), equalTo(0));

    }

    @Test
    void updateEmployee(){
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";
        String createEmployee = "http://localhost:" + this.port + "v1/ogc/employee/name/Teste/role/Funcionario/enterpriseId/1";
        String updateEmployee = "http://localhost:" + this.port + "/v1/ogc/employee/id/1/name/Teste1/role/Funcionario1/enterpriseId/1";
        String getEmployee = "http://localhost:" + this.port + "/v1/ogc/employeesdetail/id/1";

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response =
                this.restTemplate.postForEntity(createEmployee, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        this.restTemplate.put(updateEmployee, null);

        EmployeeDetail employeeDetail =
                this.restTemplate.getForObject(getEmployee, EmployeeDetail.class);

        assertThat(employeeDetail.getName(), equalTo("Teste1"));
        assertThat(employeeDetail.getRole(), equalTo("Funcionario1"));

    }

}
