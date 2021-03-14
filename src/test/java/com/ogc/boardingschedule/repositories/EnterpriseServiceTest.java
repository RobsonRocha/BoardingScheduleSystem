package com.ogc.boardingschedule.repositories;

import com.ogc.boardingschedule.domain.Enterprise;
import com.ogc.boardingschedule.exceptions.EnterpriseExistsException;
import com.ogc.boardingschedule.exceptions.EnterpriseNotExistsException;
import com.ogc.boardingschedule.service.EnterpriseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class EnterpriseServiceTest {

    @Autowired
    private EnterpriseService enterpriseService;

    @Test
    void insertEnterpriseTest(){
        enterpriseService.insertCompany("Teste");
        Enterprise e =  enterpriseService.getCompanyById(1L).block();

        assertThat(e.getName(), equalTo("Teste"));

    }

    @Test
    void insertDuplicatedEnterpriseTest(){
        enterpriseService.insertCompany("Teste");

        StepVerifier
                .create(enterpriseService.insertCompany("Teste"))
                .expectErrorMatches(throwable -> throwable instanceof EnterpriseExistsException &&
                        throwable.getMessage().equals("Empresa Teste já existe."))
                .verify();

    }

    @Test
    void getEnterpriseByIdTest(){
        enterpriseService.insertCompany("Teste");

        StepVerifier
                .create(enterpriseService.getCompanyById(1L))
                .expectNext()
                .assertNext(e -> {e.getName().equals("Teste");});

    }

    @Test
    void getEnterpriseByWrongIdTest(){
        enterpriseService.insertCompany("Teste");

        StepVerifier
                .create(enterpriseService.getCompanyById(2L))
                .expectErrorMatches(throwable -> throwable instanceof EnterpriseNotExistsException)
                .verify();

    }

    @Test
    void getEnterpriseByNameTest(){
        enterpriseService.insertCompany("Teste");

        StepVerifier
                .create(enterpriseService.getCompanyByName("Teste"))
                .expectNext()
                .assertNext(e -> {e.getName().equals("Teste");});

    }

    @Test
    void getEnterpriseByWrongNameTest(){
        enterpriseService.insertCompany("Teste");

        StepVerifier
                .create(enterpriseService.getCompanyByName("Teste1"))
                .expectErrorMatches(throwable -> throwable instanceof EnterpriseNotExistsException)
                .verify();

    }
}
