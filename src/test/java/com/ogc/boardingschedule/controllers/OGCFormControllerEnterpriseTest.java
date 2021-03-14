package com.ogc.boardingschedule.controllers;

import com.ogc.boardingschedule.domain.Enterprise;
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
public class OGCFormControllerEnterpriseTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createEnterpriseTest() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";


        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

    }

    @Test
    void createEqualEnterpriseTest() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";


        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        response = this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @Test
    void getEnterpriseByName() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";
        String getCompany = "http://localhost:" + this.port + "/v1/ogc/company/name/Teste";

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));


        Enterprise enterprise =
                this.restTemplate.getForObject(getCompany, Enterprise.class);

        assertThat(enterprise.getName(), equalTo("Teste"));

    }

    @Test
    void getEnterpriseByWrongName() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";
        String getCompany = "http://localhost:" + this.port + "/v1/ogc/company/name/Teste1";

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));


        Enterprise enterprise =
                this.restTemplate.getForObject(getCompany, Enterprise.class);

        assertThat(enterprise.getName(), equalTo(null));

    }

    @Test
    void getEnterpriseById() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";
        String getCompany = "http://localhost:" + this.port + "/v1/ogc/company/id/1";

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));


        Enterprise enterprise =
                this.restTemplate.getForObject(getCompany, Enterprise.class);

        assertThat(enterprise.getId(), equalTo(1L));

    }

    @Test
    void getEnterpriseByWrongId() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";
        String getCompany = "http://localhost:" + this.port + "/v1/ogc/company/id/5";

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));


        Enterprise enterprise =
                this.restTemplate.getForObject(getCompany, Enterprise.class);

        assertThat(enterprise.getId(), equalTo(null));

    }

    @Test
    void getAllEnterprises() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";
        String createCompany2 = "http://localhost:" + this.port + "/v1/ogc/company/Teste2";
        String getCompanies = "http://localhost:" + this.port + "/v1/ogc/companies";

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        ResponseEntity<String> response2 =
                this.restTemplate.postForEntity(createCompany2, null, String.class);

        assertThat(response2.getStatusCode(), equalTo(HttpStatus.CREATED));


        List<Enterprise> enterprise =
                this.restTemplate.getForObject(getCompanies, List.class);

        assertThat(enterprise.size(), equalTo(2));

    }

    @Test
    void deleteEnterpriseTest() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";
        String deleteCompany = "http://localhost:" + this.port + "/v1/ogc/company/id/1";
        String getCompany = "http://localhost:" + this.port + "/v1/ogc/company/id/1";

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        this.restTemplate.delete(deleteCompany);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        Enterprise enterprise =
                this.restTemplate.getForObject(getCompany, Enterprise.class);

        assertThat(enterprise.getId(), equalTo(null));

    }

    @Test
    void updateEnterpriseTest() {
        String createCompany = "http://localhost:" + this.port + "/v1/ogc/company/Teste";
        String updateCompany = "http://localhost:" + this.port + "/v1/ogc/company/id/1/name/Teste1";
        String getCompany = "http://localhost:" + this.port + "/v1/ogc/company/id/1";

        ResponseEntity<String> response =
                this.restTemplate.postForEntity(createCompany, null, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        this.restTemplate.put(updateCompany, null);

        Enterprise enterprise =
                this.restTemplate.getForObject(getCompany, Enterprise.class);

        assertThat(enterprise.getName(), equalTo("Teste1"));

    }


}
