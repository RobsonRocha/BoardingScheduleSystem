package com.ogc.boardingschedule.repositories;

import com.ogc.boardingschedule.domain.EmployeeDetail;
import com.ogc.boardingschedule.exceptions.EmployeeInDayOffException;
import com.ogc.boardingschedule.exceptions.EmployeeNotExistsException;
import com.ogc.boardingschedule.exceptions.EmployeeOnBoardException;
import com.ogc.boardingschedule.service.BoardingService;
import com.ogc.boardingschedule.service.EmployeeService;
import com.ogc.boardingschedule.service.EnterpriseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class BoardingScheduleServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private BoardingService boardingService;

    @Test
    void insertBoardingScheduleTest(){
        enterpriseService.insertCompany("Teste");
        employeeService.insertEmployee("Teste", "Funcionario", 1L);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String initDate = LocalDate.now().toString();
        LocalDate startDate = LocalDate.parse(initDate, formatter);

        boardingService.insertNewBoarding(1L, startDate);

        StepVerifier
                .create(boardingService.getBoardingsByEmployeeId(1L))
                .expectNext()
                .assertNext(e -> {e.getEmployeeName().equals("Teste");})
                .assertNext(e -> e.getInitDate().format(formatter).equals(startDate.format(formatter)));

    }

    @Test
    void insertBoardingWrongEmployeeTest(){
        enterpriseService.insertCompany("Teste");
        employeeService.insertEmployee("Teste", "Funcionario", 1L);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String initDate = LocalDate.now().toString();
        LocalDate startDate = LocalDate.parse(initDate, formatter);

        StepVerifier
                .create(boardingService.insertNewBoarding(2L, startDate))
                .expectErrorMatches(throwable -> throwable instanceof EmployeeNotExistsException)
                .verify();

    }

    @Test
    void insertBoardingEmployeeInDayOffTest(){
        enterpriseService.insertCompany("Teste")
        .doOnSuccess(u -> employeeService.insertEmployee("Teste", "Funcionario", 1L)
                .doOnSuccess(e -> {
                    employeeService.getEmployeeDetailByName("Teste")
                            .doOnSuccess( emp -> {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                String initDate = LocalDate.now().toString();
                                LocalDate startDate = LocalDate.parse(initDate, formatter);

                                StepVerifier
                                        .create(boardingService.insertNewBoarding(emp.getId(), startDate.plusDays(17)))
                                        .expectErrorMatches(throwable -> throwable instanceof EmployeeInDayOffException)
                                        .verify();
                            });
                })
        );
    }

    @Test
    void insertBoardingEmployeeThatIsOnBoardTest(){
        enterpriseService.insertCompany("Teste")
                .doOnSuccess(u -> employeeService.insertEmployee("Teste", "Funcionario", 1L)
                        .doOnSuccess(e -> {
                            employeeService.getEmployeeDetailByName("Teste")
                                    .doOnSuccess( emp -> {
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                        String initDate = LocalDate.now().toString();
                                        LocalDate startDate = LocalDate.parse(initDate, formatter);

                                        StepVerifier
                                                .create(boardingService.insertNewBoarding(emp.getId(), startDate.plusDays(10)))
                                                .expectErrorMatches(throwable -> throwable instanceof EmployeeOnBoardException)
                                                .verify();
                                    });
                        })
                );
    }

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
