package com.ogc.boardingschedule.controllers;

import com.ogc.boardingschedule.domain.BoardingDetail;
import com.ogc.boardingschedule.service.BoardingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@Slf4j
@RestController
@RequestMapping("/v1/ogc/")
public class OGCBoardingController {

    private final BoardingService boardingService;

    public OGCBoardingController(BoardingService boardingService) {
        this.boardingService = boardingService;
    }

    @ResponseStatus(CREATED)
    @PostMapping("/boarding/employeeid/{employeeId}/initdate/{initDate}")
    public Mono<Void> insertBoarding(@PathVariable Long employeeId, @PathVariable String initDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(initDate, formatter);

        log.info("Recebendo solicitação para agendar o embarque do empregado de id {} na data {} .",
                employeeId, initDate);
        return boardingService.insertNewBoarding(employeeId, startDate);
    }

    @ResponseStatus(OK)
    @GetMapping("/boardings")
    public Flux<BoardingDetail> getBoarding() {
        log.info("Recebendo solicitação para listar todas as agendas");
        return boardingService.getBoardings();
    }

    @ResponseStatus(OK)
    @GetMapping("/boardings/employeeid/{employeeId}")
    public Flux<BoardingDetail> getBoardingByEmployeeId(@PathVariable Long employeeId) {
        log.info("Recebendo solicitação para listar toda a agenda do empregado id = {}", employeeId);
        return boardingService.getBoardingsByEmployeeId(employeeId);
    }

    @ResponseStatus(OK)
    @GetMapping("/boardings/enterpriseid/{enterpriseId}")
    public Flux<BoardingDetail> getBoardingByEnterpriseId(@PathVariable Long enterpriseId) {
        log.info("Recebendo solicitação para listar toda a agenda da empresa id = {}", enterpriseId);
        return boardingService.getBoardingsByEnterpriseId(enterpriseId);
    }

    @ResponseStatus(OK)
    @PutMapping("/boarding/id/{boardingId}/employeeid/{employeeId}/initdate/{initDate}")
    public Mono<Void> updateBoardingSchedule(@PathVariable Long boardingId, @PathVariable Long employeeId, @PathVariable String initDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(initDate, formatter);
        log.info("Recebendo solicitação para atualizar a agenda de id = {}", boardingId);
        return boardingService.updateBoardingSchedule(boardingId, employeeId,startDate);
    }

    @ResponseStatus(OK)
    @DeleteMapping("/boarding/id/{id}")
    public Mono<Void> deleteBoardingSchedule(@PathVariable Long id) {
        log.info("Recebendo solicitação para apagar o agendamento de embarque de id {}.", id);
        return boardingService.deleteBoardingScheduleById(id);
    }

    @ResponseStatus(OK)
    @GetMapping("/boarding/initdate/{initDate}/enddate/{endDate}")
    public Flux<BoardingDetail> deleteBoardingSchedule(@PathVariable String initDate, @PathVariable String endDate) {
        log.info("Recebendo solicitação para buscar embarques entre {} e {}.", initDate, endDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(initDate, formatter);
        LocalDate finishDate = LocalDate.parse(endDate, formatter);
        return boardingService.getBoardingScheduleBetweenDates(startDate, finishDate);
    }

    /*

    @ResponseStatus(OK)
    @GetMapping("/companies")
    public Flux<Enterprise> getCompanies() {
        log.info("Recebendo solicitação para obter todas as empresas.");
        return enterpriseService.getCompanies();

    }

    @ResponseStatus(OK)
    @GetMapping("/company/name/{name}")
    public Mono<Enterprise> getCompanyByName(@PathVariable String name) {
        log.info("Recebendo solicitação para buscar a empresa de nome {}.", name);
        return enterpriseService.getCompanyByName(name);

    }

    @ResponseStatus(OK)
    @GetMapping("/company/id/{id}")
    public Mono<Enterprise> getCompanyById(@PathVariable Long id) {
        log.info("Recebendo solicitação para buscar a empresa de id {}.", id);
        return enterpriseService.getCompanyById(id);

    }

    @ResponseStatus(OK)
    @DeleteMapping("/company/id/{id}")
    public void deleteCompanyById(@PathVariable Long id) {
        log.info("Recebendo solicitação para apagar a empresa de id {}.", id);
        enterpriseService.deleteCompanyById(id);
    }

    @ResponseStatus(OK)
    @PutMapping("/company/id/{id}/name/{name}")
    public void deleteCompanyById(@PathVariable Long id, @PathVariable String name) {
        log.info("Recebendo solicitação para atualizar a empresa de id {} e nome {}.", id, name);
        enterpriseService.updateCompany(id,name);
    }

    @ResponseStatus(OK)
    @PostMapping("/employee/name/{name}/role/{role}/enterpriseId/{enterpriseId}")
    public void setEmployee( @PathVariable String name, @PathVariable String role, @PathVariable Long enterpriseId) {
        log.info("Recebendo solicitação para gravar o empregado {}, {}, {}",name, role, enterpriseId);
        employeeService.insertEmployee(name, role, enterpriseId);
    }

    @ResponseStatus(OK)
    @GetMapping("/employees")
    public Flux<Employee> getEmployees() {
        log.info("Recebendo solicitação para obter todos os empregados.");
        return employeeService.getEmployees();

    }

    @ResponseStatus(OK)
    @GetMapping("/employeesdetail")
    public Flux<EmployeeDetail> getEmployeesDetail(){
        log.info("Buscando todos os empregados com detalhes.");
        return employeeService.getEmployeesDetail();
    }

    @ResponseStatus(OK)
    @GetMapping("/employeesdetail/name/{name}")
    public Mono<EmployeeDetail> getEmployeeDetailByName(@PathVariable String name){
        log.info("Buscando o empregado com nome {}.", name);
        return employeeService.getEmployeeDetailByName(name);
    }

    @ResponseStatus(OK)
    @GetMapping("/employeesdetail/id/{id}")
    public Mono<EmployeeDetail> getEmployeeDetailById(@PathVariable Long id){
        log.info("Buscando o empregado com id = {}.", id);
        return employeeService.getEmployeeDetailById(id);
    }

    @ResponseStatus(OK)
    @DeleteMapping("/employee/id/{id}")
    public void deleteEmployeeById( @PathVariable Long id){
        log.info("Apagando empregado de id {}.", id);
        employeeService.deleteEmployeeById(id);
    }

    @ResponseStatus(OK)
    @PutMapping("/employee/id/{id}/name/{name}/role/{role}/enterpriseId/{enterpriseId}")
    public void updateCompany(@PathVariable Long id, @PathVariable String name, @PathVariable String role, @PathVariable Long enterpriseId){
        log.info("Recebendo solicitação para atualizar a empregado de id {} nome {} role {} enterpriseId {}.",
                id, name, role, enterpriseId);
        employeeService.updateEmployee(id,name, role, enterpriseId);
    }

    @ResponseStatus(OK)
    @PostMapping("/boarding/employeeid/{employeeId}/initdate/{initDate}")
    public void insertBoarding(@PathVariable Long employeeId, @PathVariable LocalDateTime initDate){
        log.info("Recebendo solicitação para agendar o embarque do empregado de id {} entre {} e {}.",
                employeeId, initDate, role, enterpriseId);
        boaremployeeService.updateEmployee(id,name, role, enterpriseId);
    }
    */
}
