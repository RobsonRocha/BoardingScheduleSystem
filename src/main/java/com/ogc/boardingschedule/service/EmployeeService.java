package com.ogc.boardingschedule.service;

import com.ogc.boardingschedule.domain.Employee;
import com.ogc.boardingschedule.domain.EmployeeDetail;
import com.ogc.boardingschedule.domain.Enterprise;
import com.ogc.boardingschedule.exceptions.EnterpriseNotExistsException;
import com.ogc.boardingschedule.repositories.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    public Mono<Void> insertEmployee(String name, String role, Long enterpriseId) {
        log.info("Inserindo empregado {}.", name);
        Enterprise empty = new Enterprise(0L, null);
        return employeeRepository
                .getEnterpriseRepository()
                .getCompanyById(enterpriseId)
                .flatMap(item -> {
                    if(item.getName() != null){
                        return employeeRepository.insertNewEmployee(name, role, enterpriseId)
                                .doOnError(e -> {
                                    log.info("Erro ao inserir empregado {} para empresa id = {} ",name,  enterpriseId);
                                })
                                .onErrorResume(e -> {
                                    return Mono.error(e);
                                });
                    }else{
                        log.info("Empresa com id = {} não existe.",  enterpriseId);
                        return Mono.error(new EnterpriseNotExistsException("Empresa com id = "+enterpriseId+" não existe."));
                    }
                });
    }

    public Flux<Employee> getEmployees() {
        log.info("Buscando todos os empregados.");
        return employeeRepository.getEmployees();
    }

    public Flux<EmployeeDetail> getEmployeesDetail(){
        log.info("Buscando todos os empregados com detalhes.");
        return employeeRepository.getEmployeesDetail();
    }

    public Mono<EmployeeDetail> getEmployeeDetailByName(String name){
        log.info("Buscando o empregado com nome {}.", name);
        return employeeRepository.getEmployeeDetailByName(name);
    }

    public Mono<EmployeeDetail> getEmployeeDetailById(Long id){
        log.info("Buscando o empregado com id = {}.", id);
        return employeeRepository.getEmployeeDetailById(id);
    }

    public Mono<Void> deleteEmployeeById(Long id){
        log.info("Apagando empregado de id {}.", id);
        return employeeRepository.deleteEmployeeById(id);
    }

    public Mono<Void> updateEmployee(Long id, String name, String role, Long enterpriseId){
        log.info("Atualizando empregado de id {}.", id);

        Enterprise empty = new Enterprise(0L, null);
        return employeeRepository
                .getEnterpriseRepository()
                .getCompanyById(enterpriseId)
                .flatMap(item -> {
                    if(item.getName() != null){
                        return employeeRepository.updateEmployee(id,name, role, enterpriseId)
                                .doOnError(e -> {
                                    log.info("Erro ao atualizar empregado {} id = {} ",name,  id);
                                })
                                .onErrorResume(e -> {
                                    return Mono.error(e);
                                });
                    }else{
                        log.info("Empresa com id = {} não existe.",  enterpriseId);
                        return Mono.error(new EnterpriseNotExistsException("Empresa com id = "+enterpriseId+" não existe."));
                    }
                });
    }
}
