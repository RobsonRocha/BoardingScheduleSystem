package com.ogc.boardingschedule.repositories;

import com.ogc.boardingschedule.domain.EmployeeDetail;
import com.ogc.boardingschedule.exceptions.EmployeeNotExistsException;
import com.ogc.boardingschedule.exceptions.EnterpriseNotExistsException;
import com.ogc.boardingschedule.service.EmployeeService;
import com.ogc.boardingschedule.service.EnterpriseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import reactor.test.StepVerifier;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EnterpriseService enterpriseService;

    @Test
    void getEmployeeByIdTest(){
        enterpriseService.insertCompany("Teste");
        employeeService.insertEmployee("Teste", "Funcionario", 1L);

        StepVerifier
                .create(employeeService.getEmployeeDetailById(1L))
                .expectNext()
                .assertNext(e -> {e.getName().equals("Teste");});

    }

    @Test
    void getEmployeeByWrongIdTest(){
        enterpriseService.insertCompany("Teste");
        employeeService.insertEmployee("Teste", "Funcionario", 1L);

        StepVerifier
                .create(employeeService.getEmployeeDetailById(2L))
                .expectErrorMatches(throwable -> throwable instanceof EmployeeNotExistsException)
                .verify();

    }

    @Test
    void getEmployeeByNameTest(){
        enterpriseService.insertCompany("Teste");
        employeeService.insertEmployee("Teste", "Funcionario", 1L);

        StepVerifier
                .create(employeeService.getEmployeeDetailByName("Teste"))
                .expectNext()
                .assertNext(e -> {e.getName().equals("Teste");});

    }
}
