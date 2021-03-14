package com.ogc.boardingschedule.repositories;

import com.ogc.boardingschedule.domain.BoardSchedule;
import com.ogc.boardingschedule.domain.BoardingDetail;
import com.ogc.boardingschedule.domain.EmployeeDetail;
import com.ogc.boardingschedule.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;


@Repository
@Slf4j
public class BoardingRepository {
    private static final String QUERY_GET_BOARDING_SCHEDULE_BY_ID = "SELECT bs.id, bs.employee_id as employeeId, " +
            "bs.init_date as initDate, bs.end_date as endDate " +
            " FROM board_schedule bs " +
            "WHERE bs.id = :id";

    private static final String QUERY_GET_ALL_SCHEDULES = "SELECT bs.ID, bs.init_date as initDate, " +
            " bs.end_date as endDate, bs.employee_id employeeId, emp.name as employeeName, emp.role," +
            " e.id as enterpriseId, e.name enterpriseName " +
            " FROM board_schedule bs " +
            "     inner join employee emp on bs.employee_id = emp.id " +
            "     inner join enterprise e on emp.enterprise_id = e.id ";

    private static final String QUERY_GET_SCHEDULE_BY_EMPLOYEE_ID = "SELECT bs.ID, bs.init_date as initDate, " +
            " bs.end_date as endDate, bs.employee_id employeeId, emp.name as employeeName, emp.role," +
            " e.id as enterpriseId, e.name enterpriseName " +
            " FROM board_schedule bs " +
            "     inner join employee emp on bs.employee_id = emp.id " +
            "     inner join enterprise e on emp.enterprise_id = e.id " +
            "WHERE bs.employee_id = :id ";

    private static final String QUERY_GET_SCHEDULE_BETWEEN_DATES = "SELECT bs.ID, bs.init_date as initDate, " +
            " bs.end_date as endDate, bs.employee_id employeeId, emp.name as employeeName, emp.role," +
            " e.id as enterpriseId, e.name enterpriseName " +
            " FROM board_schedule bs " +
            "     inner join employee emp on bs.employee_id = emp.id " +
            "     inner join enterprise e on emp.enterprise_id = e.id " +
            "WHERE bs.init_date between :initDate and :endDate " +
            "      or bs.end_date between :initDate and :endDate";

    private static final String QUERY_GET_SCHEDULE_BY_ENTERPRISE_ID = "SELECT bs.ID, bs.init_date as initDate, " +
            " bs.end_date as endDate, bs.employee_id employeeId, emp.name as employeeName, emp.role," +
            " e.id as enterpriseId, e.name enterpriseName " +
            " FROM board_schedule bs " +
            "     inner join employee emp on bs.employee_id = emp.id " +
            "     inner join enterprise e on emp.enterprise_id = e.id " +
            "WHERE e.id = :id ";

    private static final String QUERY_GET_EMPLOYEE_IN_DAY_OFF = "SELECT  emp.ID, emp.name, emp.ROLE, emp.ENTERPRISE_ID, " +
            "e.name as enterpriseName " +
            "FROM board_schedule bs " +
            "   inner join employee emp on bs.employee_id = emp.id " +
            "   inner join enterprise e on emp.enterprise_id = e.id " +
            "WHERE :initDate between bs.end_date and dateadd('DAY',7,bs.end_date) " +
            "AND emp.id = :employeeId";

    private static final String QUERY_GET_EMPLOYEE_ON_BOARD = "SELECT bs.ID, bs.init_date as initDate, " +
            " bs.end_date as endDate, bs.employee_id employeeId, emp.name as employeeName, emp.role," +
            " e.id as enterpriseId, e.name enterpriseName " +
            "FROM board_schedule bs " +
            "   inner join employee emp on bs.employee_id = emp.id " +
            "   inner join enterprise e on emp.enterprise_id = e.id " +
            "WHERE :initDate between bs.init_date and bs.end_date " +
            "AND emp.id = :employeeId";

    private static final String QUERY_INSERT_NEW_BOARDING = "INSERT INTO board_schedule (employee_id, init_date, end_date)  " +
            "  VALUES (:employeeId, :initDate, :endDate)";

    private static final String QUERY_DELETE_SCHEDULE = "DELETE from board_schedule " +
            "  WHERE id = :id";

    private static final String QUERY_UPDATE_SCHEDULE = "UPDATE board_schedule set employee_id = :employeeId, init_date = :initDate, end_date = :endDate" +
            "  WHERE id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<BoardSchedule> rowMapper = BeanPropertyRowMapper.newInstance(BoardSchedule.class);
    private final BeanPropertyRowMapper<BoardingDetail> rowDetailMapper = BeanPropertyRowMapper.newInstance(BoardingDetail.class);
    private final BeanPropertyRowMapper<EmployeeDetail> rowEmployeeDetailMapper = BeanPropertyRowMapper.newInstance(EmployeeDetail.class);
    private final EmployeeRepository employeeRepository;

    public BoardingRepository(NamedParameterJdbcTemplate jdbcTemplate,
                              EmployeeRepository employeeRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.employeeRepository = employeeRepository;
    }

    public EmployeeRepository getEmployeeRepository(){
        return this.employeeRepository;
    }

    public Mono<BoardSchedule> getBoardingScheduleById(Long id){
        try{
            SqlParameterSource param = new MapSqlParameterSource("id", id);

            BoardSchedule boardSchedule = jdbcTemplate.queryForObject(QUERY_GET_BOARDING_SCHEDULE_BY_ID, param, rowMapper);
            return Mono.just(boardSchedule);

        } catch (EmptyResultDataAccessException e) {
            log.info("Não há nenhuma embarque agendado com o id {}.", id);
            return Mono.error(new BoardingScheduleNotExistsException("Não há nenhum embarque agendado com id "+id+"."));
        } catch (Exception e) {
            log.error("Erro ao acessar o banco :: Message - {}", e.getMessage());
            return Mono.error(new BoardingException(e.getMessage()));
        }
    }

    public Mono<Void> insertNewBoarding(Long employeeId, LocalDate initDate, LocalDate endDate) {
        try {
            BoardSchedule newBoarding = new BoardSchedule(0L, employeeId, initDate, endDate);
            BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(newBoarding);
            jdbcTemplate.update(QUERY_INSERT_NEW_BOARDING, paramSource);
            log.info("Empregado agendado para o intervalo entre {} e {}.", initDate, endDate);
            return Mono.empty();
        } catch (Exception e) {
            log.error("Erro ao agendar embarque :: Messagem - {}", e.getMessage());
            return Mono.error(new BoardingInsertException("Erro ao agendar embarque "+e.getMessage()));
        }
    }


    public Flux<BoardingDetail> getBoardings() {
        try {
            List<BoardingDetail> boardingDetails =  jdbcTemplate.query(QUERY_GET_ALL_SCHEDULES, rowDetailMapper);
            return Flux.defer(() -> Flux.fromIterable(
                    boardingDetails));
        } catch (EmptyResultDataAccessException e) {
            log.info("Não há nenhum agendamento cadastrado.");
            return Flux.empty();
        } catch (Exception e) {
            log.error("Erro ao acessar o banco :: Message - {}", e.getMessage());
            return Flux.error(new BoardingException(e.getMessage()));
        }
    }

    public Flux<BoardingDetail> getBoardingByEmployeeId(Long id) {
        try {
            SqlParameterSource param = new MapSqlParameterSource("id", id);
            List<BoardingDetail> boardingDetails =  jdbcTemplate.query(QUERY_GET_SCHEDULE_BY_EMPLOYEE_ID, param, rowDetailMapper);
            return Flux.defer(() -> Flux.fromIterable(
                    boardingDetails));
        } catch (EmptyResultDataAccessException e) {
            log.info("Não há nenhum agendamento cadastrado.");
            return Flux.empty();
        } catch (Exception e) {
            log.error("Erro ao acessar o banco :: Message - {}", e.getMessage());
            return Flux.error( new BoardingException(e.getMessage()));
        }
    }

    public Flux<BoardingDetail> getBoardingByEnterpriseId(Long id) {
        try {
            SqlParameterSource param = new MapSqlParameterSource("id", id);
            List<BoardingDetail> boardingDetails =  jdbcTemplate.query(QUERY_GET_SCHEDULE_BY_ENTERPRISE_ID, param, rowDetailMapper);
            return Flux.defer(() -> Flux.fromIterable(
                    boardingDetails));
        } catch (EmptyResultDataAccessException e) {
            log.info("Não há nenhum agendamento cadastrado.");
            return Flux.empty();
        } catch (Exception e) {
            log.error("Erro ao acessar o banco :: Message - {}", e.getMessage());
            return Flux.error(new BoardingException(e.getMessage()));
        }
    }

    public Mono<EmployeeDetail> getEmployeeInDayOff(Long id, LocalDate initDate) {
        try {
            BoardSchedule param = new BoardSchedule(0L, id, initDate, null);
            BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(param);
            EmployeeDetail employeesDetail = jdbcTemplate.queryForObject(QUERY_GET_EMPLOYEE_IN_DAY_OFF, paramSource, rowEmployeeDetailMapper);
            log.info("Há embarque agendado para o empregado id = {} no dia {}",id, initDate);
            return Mono.just(employeesDetail);
        }
        catch (EmptyResultDataAccessException e) {
            log.info("Não há embarque agendado para o empregado id = {} no dia {}",id, initDate);
            return Mono.empty();
        } catch (Exception e) {
            log.error("Erro ao acessar o banco :: Message - {}", e.getMessage());
            return Mono.error(new BoardingException(e.getMessage()));
        }
    }

    public Mono<BoardingDetail> getEmployeeOnBoard(Long id, LocalDate initDate) {
        try {
            BoardSchedule param = new BoardSchedule(0L, id, initDate, null);
            BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(param);
            BoardingDetail boardingDetail = jdbcTemplate.queryForObject(QUERY_GET_EMPLOYEE_ON_BOARD, paramSource, rowDetailMapper);
            log.info("Empregado já embarcado id = {} .",id);
            return Mono.just(boardingDetail);
        }
        catch (EmptyResultDataAccessException e) {
            log.info("Empregado não está embarcado id = {}",id);
            return Mono.empty();
        } catch (Exception e) {
            log.error("Erro ao acessar o banco :: Message - {}", e.getMessage());
            return Mono.error(new BoardingException(e.getMessage()));
        }
    }

    public Mono<Void> deleteBordingScheduleById(Long id) {
        try {
            SqlParameterSource param = new MapSqlParameterSource("id", id);
            jdbcTemplate.update(QUERY_DELETE_SCHEDULE, param);
            log.info("Embarque, cujo id é {}, foi apagado com sucesso.", id);
            return Mono.empty();
        } catch (Exception e) {
            log.error("Erro ao apagar a agendamento de embarque id = {} :: Messagem - {}", id, e.getMessage());
            return Mono.error(new BoardingException("Erro ao apagar agendamento de embarque id = "+id));
        }
    }

    public Mono<Void> updateBoardingSchedule(Long id, Long employeeId, LocalDate initDate, LocalDate endDate) {
        try {
            Mono<BoardSchedule> board = getBoardingScheduleById(id);
            Mono<EmployeeDetail> employee = employeeRepository.getEmployeeDetailById(id);
            BoardSchedule oldBoard = board.block();
            BoardSchedule newBoard = new BoardSchedule(id, employeeId, initDate, endDate);
            BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(newBoard);
            jdbcTemplate.update(QUERY_UPDATE_SCHEDULE, paramSource);
            log.info("Agendamento de embarque, cujo id é {}, foi atualizado de {} para {} " +
                            "de {} para {} " +
                            "de {} para {} com sucesso.", id, oldBoard.getEmployeeId(), employeeId,
                    oldBoard.getInitDate(), initDate,
                    oldBoard.getEndDate(), endDate);
            return Mono.empty();
        }  catch (BoardingScheduleNotExistsException e ){
            log.error(e.getMessage());
            return Mono.error(new BoardingScheduleNotExistsException(e.getMessage()));
        }
        catch (EmployeeNotExistsException e){
            log.error(e.getMessage());
            return Mono.error(new EmployeeNotExistsException(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao atualizar a agendamento de embarque id = {} :: Messagem - {}", id, e.getMessage());
            return Mono.error(new BoardingException("Erro ao atualizar agendamento de embarque id = "+id));
        }
    }

    public Flux<BoardingDetail> getBoardingScheduleBetweenDates(LocalDate initDate, LocalDate endDate) {
        try {
            BoardSchedule newBoard = new BoardSchedule(null, null, initDate, endDate);
            BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(newBoard);
            List<BoardingDetail> boardingDetails =  jdbcTemplate.query(QUERY_GET_SCHEDULE_BETWEEN_DATES, paramSource, rowDetailMapper);
            return Flux.defer(() -> Flux.fromIterable(
                    boardingDetails));
        } catch (EmptyResultDataAccessException e) {
            log.info("Não há nenhum agendamento cadastrado.");
            return Flux.empty();
        } catch (Exception e) {
            log.error("Erro ao acessar o banco :: Message - {}", e.getMessage());
            return Flux.error(new BoardingException(e.getMessage()));
        }
    }

}
