package com.ogc.boardingschedule.controllers;

import com.ogc.boardingschedule.domain.Employee;
import com.ogc.boardingschedule.domain.EmployeeDetail;
import com.ogc.boardingschedule.domain.Enterprise;
import com.ogc.boardingschedule.service.EmployeeService;
import com.ogc.boardingschedule.service.EnterpriseService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@Slf4j
@RestController
@RequestMapping("/v1/ogc/")
public class OGCFormController {

    private final EnterpriseService enterpriseService;
    private final EmployeeService employeeService;

    public OGCFormController(EnterpriseService enterpriseService,
                             EmployeeService employeeService) {
        this.enterpriseService = enterpriseService;
        this.employeeService = employeeService;
    }

    @ApiOperation(value = "Cadastra uma empresa")
    @ResponseStatus(CREATED)
    @PostMapping("/company/{enterprisename}")
    public Mono<Void> setEnterprise( @PathVariable String enterprisename) {
        log.info("Recebendo solicitação para gravar a empresa {}",enterprisename);
        return enterpriseService.insertCompany(enterprisename);
    }

    @ApiOperation(value = "Lista todas as empresas")
    @ResponseStatus(OK)
    @GetMapping("/companies")
    public Flux<Enterprise> getCompanies() {
        log.info("Recebendo solicitação para obter todas as empresas.");
        return enterpriseService.getCompanies();

    }

    @ApiOperation(value = "Busca uma empresa pelo nome")
    @ResponseStatus(OK)
    @GetMapping("/company/name/{name}")
    public Mono<Enterprise> getCompanyByName(@PathVariable String name) {
        log.info("Recebendo solicitação para buscar a empresa de nome {}.", name);
        return enterpriseService.getCompanyByName(name);

    }

    @ApiOperation(value = "Busca uma empresa pelo ID")
    @ResponseStatus(OK)
    @GetMapping("/company/id/{id}")
    public Mono<Enterprise> getCompanyById(@PathVariable Long id) {
        log.info("Recebendo solicitação para buscar a empresa de id {}.", id);
        return enterpriseService.getCompanyById(id);

    }

    @ApiOperation(value = "Apaga uma empresa existente")
    @ResponseStatus(OK)
    @DeleteMapping("/company/id/{id}")
    public Mono<Void> deleteCompanyById(@PathVariable Long id) {
        log.info("Recebendo solicitação para apagar a empresa de id {}.", id);
        return enterpriseService.deleteCompanyById(id);
    }

    @ApiOperation(value = "Atualiza uma empresa existente")
    @ResponseStatus(OK)
    @PutMapping("/company/id/{id}/name/{name}")
    public Mono<Void> deleteCompanyById(@PathVariable Long id, @PathVariable String name) {
        log.info("Recebendo solicitação para atualizar a empresa de id {} e nome {}.", id, name);
        return enterpriseService.updateCompany(id,name);
    }

    @ApiOperation(value = "Cadastra um empregado")
    @ResponseStatus(CREATED)
    @PostMapping("/employee/name/{name}/role/{role}/enterpriseId/{enterpriseId}")
    public Mono<Void> setEmployee( @PathVariable String name, @PathVariable String role, @PathVariable Long enterpriseId) {
        log.info("Recebendo solicitação para gravar o empregado {}, {}, {}",name, role, enterpriseId);
        return employeeService.insertEmployee(name, role, enterpriseId);
    }

    @ApiOperation(value = "Lista todos os empregados")
    @ResponseStatus(OK)
    @GetMapping("/employees")
    public Flux<Employee> getEmployees() {
        log.info("Recebendo solicitação para obter todos os empregados.");
        return employeeService.getEmployees();

    }

    @ApiOperation(value = "Lista todos os detalhes do empregado")
    @ResponseStatus(OK)
    @GetMapping("/employeesdetail")
    public Flux<EmployeeDetail> getEmployeesDetail(){
        log.info("Buscando todos os empregados com detalhes.");
        return employeeService.getEmployeesDetail();
    }

    @ApiOperation(value = "Busca o empregado pelo nome")
    @ResponseStatus(OK)
    @GetMapping("/employeesdetail/name/{name}")
    public Flux<EmployeeDetail> getEmployeeDetailByName(@PathVariable String name){
        log.info("Buscando o empregado com nome {}.", name);
        return employeeService.getEmployeeDetailByName(name);
    }

    @ApiOperation(value = "Busca o empregado pelo ID")
    @ResponseStatus(OK)
    @GetMapping("/employeesdetail/id/{id}")
    public Mono<EmployeeDetail> getEmployeeDetailById(@PathVariable Long id){
        log.info("Buscando o empregado com id = {}.", id);
        return employeeService.getEmployeeDetailById(id);
    }

    @ApiOperation(value = "Apaga um empregado existente")
    @ResponseStatus(OK)
    @DeleteMapping("/employee/id/{id}")
    public Mono<Void> deleteEmployeeById( @PathVariable Long id){
        log.info("Apagando empregado de id {}.", id);
        return employeeService.deleteEmployeeById(id);
    }

    @ApiOperation(value = "Atualiza um empregado existente")
    @ResponseStatus(OK)
    @PutMapping("/employee/id/{id}/name/{name}/role/{role}/enterpriseId/{enterpriseId}")
    public Mono<Void> updateCompany(@PathVariable Long id, @PathVariable String name, @PathVariable String role, @PathVariable Long enterpriseId){
        log.info("Recebendo solicitação para atualizar a empregado de id {} nome {} role {} enterpriseId {}.",
                id, name, role, enterpriseId);
        return employeeService.updateEmployee(id,name, role, enterpriseId);
    }

}
