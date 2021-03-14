package com.ogc.boardingschedule.service;

import com.ogc.boardingschedule.domain.BoardingDetail;
import com.ogc.boardingschedule.domain.EmployeeDetail;
import com.ogc.boardingschedule.exceptions.*;
import com.ogc.boardingschedule.repositories.BoardingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@Slf4j
public class BoardingService {

    private final BoardingRepository boardingRepository;

    public BoardingService(BoardingRepository boardingRepository) {
        this.boardingRepository = boardingRepository;
    }

    public Mono<Void> insertNewBoarding(Long employeeId, LocalDate initDate) {
        LocalDate endDate = initDate.plusDays(15);
        log.info("Preparando agendando empregado id = {} para embarque no dia {} e saída {}.", employeeId, initDate, endDate);
        EmployeeDetail empty = new EmployeeDetail(null, null, null, null, null);
        BoardingDetail boardingEmpty = new BoardingDetail();


        if (initDate.isBefore(LocalDate.now())){
            log.info("Data de agendamento inválida {}", initDate);
            return Mono.error(new WrongInitDateException("Não pode agendar embarque para o passado data = "+initDate));
        }

        return boardingRepository
                .getEmployeeInDayOff(employeeId, initDate)
                .defaultIfEmpty(empty)
                .flatMap(item -> {
                    if (item.getId() == null){
                        return boardingRepository.getEmployeeOnBoard(employeeId,initDate)
                               .defaultIfEmpty(boardingEmpty)
                               .flatMap(onboard -> {
                                   if(onboard.getId() == null){
                                       log.info("Agendando empregado id = {} para embarque no dia {} e saída {}.", employeeId, initDate, endDate);
                                       return boardingRepository
                                               .getEmployeeRepository()
                                               .getEmployeeDetailById(employeeId)
                                               .defaultIfEmpty(empty)
                                               .flatMap( emp -> {
                                                   if (emp.getId() == null){
                                                       log.info("Empregado com id = {} não existe.", employeeId);
                                                       return Mono.error(new EmployeeNotExistsException("Empregado com id = "+employeeId+" não existe."));
                                                   }
                                                   return boardingRepository.insertNewBoarding(employeeId, initDate, endDate);
                                               });
                                   }
                                   return Mono.error(new EmployeeOnBoardException("Empregado id = "+employeeId+" já  está embarcado."));
                               });
                    }

                    return Mono.error(new EmployeeInDayOffException("Empregado id = "+employeeId+" está em período de folga"));

                });
    }

    public Flux<BoardingDetail> getBoardings() {
        log.info("Buscando todos os agendamentos.");
        return boardingRepository.getBoardings();
    }

    public Flux<BoardingDetail> getBoardingsByEmployeeId(Long id) {
        log.info("Buscando todos os agendamentos do empregado id = {}.", id);
        return boardingRepository.getBoardingByEmployeeId(id);
    }

    public Flux<BoardingDetail> getBoardingsByEnterpriseId(Long id) {
        log.info("Buscando todos os agendamentos da empresa id = {}.", id);
        return boardingRepository.getBoardingByEnterpriseId(id);
    }

    public Mono<Void> deleteBoardingScheduleById(Long id){
        log.info("Apagando agendamento de embarque de id {}.", id);
        return boardingRepository.deleteBordingScheduleById(id);
    }

    public Mono<Void> updateBoardingSchedule(Long id, Long employeeId, LocalDate initDate){
        log.info("Atualizando agendamento de embarque de id {}.", id);
        LocalDate endDate = initDate.plusDays(15);

        EmployeeDetail empty = new EmployeeDetail();
        BoardingDetail emptyBoarding = new BoardingDetail();

        if (initDate.isBefore(LocalDate.now())){
            log.info("Data de agendamento inválida {}", initDate);
            return Mono.error(new WrongInitDateException("Não pode agendar embarque para o passado data = "+initDate));
        }

        return boardingRepository
                .getEmployeeRepository()
                .getEmployeeDetailById(employeeId)
                .defaultIfEmpty(empty)
                .flatMap(emp -> {
                    if (emp.getId() == null){
                        log.info("Empregado com id = {} não existe.", employeeId);
                        return Mono.error(new EmployeeNotExistsException("Empregado com id = "+employeeId+" não existe."));
                    }
                    return boardingRepository
                            .getEmployeeInDayOff(employeeId, initDate)
                            .defaultIfEmpty(empty)
                            .flatMap(item -> {
                                if (item.getId() == null){

                                    return boardingRepository.getEmployeeOnBoard(employeeId,initDate)
                                            .defaultIfEmpty(emptyBoarding)
                                            .flatMap(onboard -> {
                                                if(onboard.getId() == null || onboard.equals(id)){
                                                    log.info("Agendando empregado id = {} para embarque no dia {} e saída {}.", employeeId, initDate, endDate);
                                                    return boardingRepository.updateBoardingSchedule(id, employeeId, initDate, endDate)
                                                            .doOnError(e -> {
                                                                log.info("Erro no agendamento do empregado id = {} para embarque no dia {} e saída {}.", employeeId, initDate, endDate);
                                                            })
                                                            .onErrorResume(e -> {
                                                                return Mono.error(e);
                                                            });
                                                }
                                                return Mono.error(new EmployeeOnBoardException("Empregado id = "+employeeId+" já  está embarcado."));
                                            });
                                }

                                return Mono.error(new EmployeeInDayOffException("Empregado id = "+employeeId+" está em período de folga"));
                    });


                });
    }

    public Flux<BoardingDetail> getBoardingScheduleBetweenDates(LocalDate initDate, LocalDate endDate){
        if (initDate.isAfter(endDate)){
            return Flux.error(new BoardingException("Data de início "+initDate+" maior que data fim "+endDate));
        }
        log.info("Buscando todos os empregados embarcados no período entre {} e {}.", initDate, endDate);
        return boardingRepository.getBoardingScheduleBetweenDates(initDate, endDate);
    }
    /*
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

    public void deleteEmployeeById(Long id){
        log.info("Apagando empregado de id {}.", id);
        employeeRepository.deleteEmployeeById(id);
    }

    public void updateEmployee(Long id, String name, String role, Long enterpriseId){
        log.info("Atualizando empregado de id {}.", id);
        employeeRepository.updateEmployee(id, name, role, enterpriseId);
    }

     */
}
