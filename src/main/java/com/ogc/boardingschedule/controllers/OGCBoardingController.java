package com.ogc.boardingschedule.controllers;

import com.ogc.boardingschedule.domain.BoardingDetail;
import com.ogc.boardingschedule.service.BoardingService;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "Cadastra um agendamento de embarque")
    @ResponseStatus(CREATED)
    @PostMapping("/boarding/employeeid/{employeeId}/initdate/{initDate}")
    public Mono<Void> insertBoarding(@PathVariable Long employeeId, @PathVariable String initDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(initDate, formatter);

        log.info("Recebendo solicitação para agendar o embarque do empregado de id {} na data {} .",
                employeeId, initDate);
        return boardingService.insertNewBoarding(employeeId, startDate);
    }

    @ApiOperation(value = "Busca todos os agendamentos")
    @ResponseStatus(OK)
    @GetMapping("/boardings")
    public Flux<BoardingDetail> getBoarding() {
        log.info("Recebendo solicitação para listar todas as agendas");
        return boardingService.getBoardings();
    }

    @ApiOperation(value = "Busca agendamento por empregado")
    @ResponseStatus(OK)
    @GetMapping("/boardings/employeeid/{employeeId}")
    public Flux<BoardingDetail> getBoardingByEmployeeId(@PathVariable Long employeeId) {
        log.info("Recebendo solicitação para listar toda a agenda do empregado id = {}", employeeId);
        return boardingService.getBoardingsByEmployeeId(employeeId);
    }

    @ApiOperation(value = "Busca agendamento por empresa")
    @ResponseStatus(OK)
    @GetMapping("/boardings/enterpriseid/{enterpriseId}")
    public Flux<BoardingDetail> getBoardingByEnterpriseId(@PathVariable Long enterpriseId) {
        log.info("Recebendo solicitação para listar toda a agenda da empresa id = {}", enterpriseId);
        return boardingService.getBoardingsByEnterpriseId(enterpriseId);
    }

    @ApiOperation(value = "Atualiza um agendamento existente")
    @ResponseStatus(OK)
    @PutMapping("/boarding/id/{boardingId}/employeeid/{employeeId}/initdate/{initDate}")
    public Mono<Void> updateBoardingSchedule(@PathVariable Long boardingId, @PathVariable Long employeeId, @PathVariable String initDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(initDate, formatter);
        log.info("Recebendo solicitação para atualizar a agenda de id = {}", boardingId);
        return boardingService.updateBoardingSchedule(boardingId, employeeId,startDate);
    }

    @ApiOperation(value = "Apaga um agendamento")
    @ResponseStatus(OK)
    @DeleteMapping("/boarding/id/{id}")
    public Mono<Void> deleteBoardingSchedule(@PathVariable Long id) {
        log.info("Recebendo solicitação para apagar o agendamento de embarque de id {}.", id);
        return boardingService.deleteBoardingScheduleById(id);
    }

    @ApiOperation(value = "Busca agendamentos entre a datas")
    @ResponseStatus(OK)
    @GetMapping("/boarding/initdate/{initDate}/enddate/{endDate}")
    public Flux<BoardingDetail> deleteBoardingSchedule(@PathVariable String initDate, @PathVariable String endDate) {
        log.info("Recebendo solicitação para buscar embarques entre {} e {}.", initDate, endDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(initDate, formatter);
        LocalDate finishDate = LocalDate.parse(endDate, formatter);
        return boardingService.getBoardingScheduleBetweenDates(startDate, finishDate);
    }
}
