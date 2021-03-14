package com.ogc.boardingschedule.repositories;

import com.ogc.boardingschedule.domain.Employee;
import com.ogc.boardingschedule.domain.EmployeeDetail;
import com.ogc.boardingschedule.exceptions.EmployeeException;
import com.ogc.boardingschedule.exceptions.EmployeeInsertException;
import com.ogc.boardingschedule.exceptions.EmployeeNotExistsException;
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

import java.util.List;


@Repository
@Slf4j
public class EmployeeRepository {
    private static final String QUERY_GET_EMPLOYEE_DETAIL_BY_NAME = "SELECT emp.ID, emp.NAME, emp.ROLE, emp.ENTERPRISE_ID, e.name as enterpriseName " +
            " FROM employee emp inner join enterprise e " +
            "     on emp.enterprise_id = e.id " +
            "WHERE trim(upper(emp.name)) like '%'|| trim(upper(:name)) || '%'";

    private static final String QUERY_GET_EMPLOYEE_DETAIL_BY_ID = "SELECT emp.ID, emp.NAME, emp.ROLE, emp.ENTERPRISE_ID, e.name as enterpriseName " +
            " FROM employee emp inner join enterprise e " +
            "     on emp.enterprise_id = e.id " +
            "WHERE emp.id = :id";

    private static final String QUERY_GET_EMPLOYEES = "SELECT ID, NAME, ROLE, ENTERPRISE_ID " +
            " FROM employee";

    private static final String QUERY_GET_EMPLOYEES_DETAIL = "SELECT emp.ID, emp.NAME, emp.ROLE, emp.ENTERPRISE_ID, e.name as enterpriseName " +
            " FROM employee emp inner join enterprise e " +
            "     on emp.enterprise_id = e.id" ;

    private static final String QUERY_INSERT_NEW_EMPLOYEE = "INSERT INTO employee (name, role, enterprise_id)  " +
            "  VALUES (:name, :role, :enterpriseId)";

    private static final String QUERY_DELETE_EMPLOYEE = "DELETE from employee " +
            "  WHERE id = :id";

    private static final String QUERY_UPDATE_EMPLOYEE = "UPDATE employee set name = :name, role = :role, enterprise_id = :enterpriseId" +
            "  WHERE id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Employee> rowMapper = BeanPropertyRowMapper.newInstance(Employee.class);
    private final BeanPropertyRowMapper<EmployeeDetail> rowDetailMapper = BeanPropertyRowMapper.newInstance(EmployeeDetail.class);
    private final EnterpriseRepository enterpriseRepository;

    public EmployeeRepository(NamedParameterJdbcTemplate jdbcTemplate,
                              EnterpriseRepository enterpriseRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.enterpriseRepository = enterpriseRepository;
    }

    public EnterpriseRepository getEnterpriseRepository(){
        return this.enterpriseRepository;
    }

    public Mono<Void> insertNewEmployee(String name, String role, Long enterpriseId) {
        try {
            Employee newEmployee = new Employee(0L, name, role, enterpriseId);
            BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(newEmployee);
            jdbcTemplate.update(QUERY_INSERT_NEW_EMPLOYEE, paramSource);
            log.info("Empregado {} inserida com sucesso.", name);
            return Mono.empty();
        } catch (Exception e) {
            log.error("Erro ao inserir a empregado {} :: Messagem - {}", name, e.getMessage());
            return Mono.error(new EmployeeInsertException("Erro ao inserir empregado "+name));
        }
    }


    public Flux<Employee> getEmployees() {
        try {
            List<Employee> employees =  jdbcTemplate.query(QUERY_GET_EMPLOYEES, rowMapper);
            return Flux.defer(() -> Flux.fromIterable(
                    employees));
        } catch (EmptyResultDataAccessException e) {
            log.info("Não há nenhum empregado cadastrado");
            return Flux.empty();
        } catch (Exception e) {
            log.error("Erro ao acessar o banco :: Message - {}", e.getMessage());
            return Flux.error(new EmployeeException(e.getMessage()));
        }
    }

    public Flux<EmployeeDetail> getEmployeesDetail() {
        try {
            List<EmployeeDetail> employees =  jdbcTemplate.query(QUERY_GET_EMPLOYEES_DETAIL, rowDetailMapper);
            return Flux.defer(() -> Flux.fromIterable(
                    employees));
        } catch (EmptyResultDataAccessException e) {
            log.info("Não há nenhuma empregado cadastrado");
            return Flux.empty();
        } catch (Exception e) {
            log.error("Erro ao acessar o banco :: Message - {}", e.getMessage());
            return Flux.error(new EmployeeException(e.getMessage()));
        }
    }

    public Flux<EmployeeDetail> getEmployeeDetailByName(String name) {
        try {

            SqlParameterSource param = new MapSqlParameterSource("name", name);

            List<EmployeeDetail> employees =  jdbcTemplate.query(QUERY_GET_EMPLOYEE_DETAIL_BY_NAME, param, rowDetailMapper);
            return Flux.defer(() -> Flux.fromIterable(
                    employees));

        } catch (EmptyResultDataAccessException e) {
            log.info("Não há nenhuma empregado cadastrado");
            return Flux.empty();
        } catch (Exception e) {
            log.error("Erro ao acessar o banco :: Message - {}", e.getMessage());
            return Flux.error(new EmployeeException(e.getMessage()));
        }
    }

    public Mono<EmployeeDetail> getEmployeeDetailById(Long id) {
        try {

            SqlParameterSource param = new MapSqlParameterSource("id", id);

            EmployeeDetail employee = jdbcTemplate.queryForObject(QUERY_GET_EMPLOYEE_DETAIL_BY_ID, param, rowDetailMapper);
            return Mono.just(employee);

        } catch (EmptyResultDataAccessException e) {
            log.info("Empregdo {} não existe", id);
            return Mono.error(new EmployeeNotExistsException("Empregado "+id+" não existe."));
        } catch (Exception e) {
            log.error("Erro ao acessar o banco :: Message - {}", e.getMessage());
            return Mono.error(new EmployeeException(e.getMessage()));
        }
    }

    public Mono<Void> deleteEmployeeById(Long id) {
        try {
            Mono<EmployeeDetail> employee = getEmployeeDetailById(id);
            SqlParameterSource param = new MapSqlParameterSource("id", id);
            jdbcTemplate.update(QUERY_DELETE_EMPLOYEE, param);
            log.info("Empregado, cujo id é {}, foi apagado com sucesso.", id);
            return Mono.empty();
        } catch (EmployeeNotExistsException e){
            log.info("Empregado de id = {} não existe.", id);
            return Mono.empty();
        } catch (Exception e) {
            log.error("Erro ao apagar a empregado id = {} :: Messagem - {}", id, e.getMessage());
            return Mono.error(new EmployeeException("Erro ao apagar empregado id = "+id));
        }
    }

    public Mono<Void> updateEmployee(Long id, String name, String role, Long enterpriseId) {
        try {
            Mono<EmployeeDetail> employee = getEmployeeDetailById(id);
            EmployeeDetail oldEmployee = employee.block();
            Employee newEmployee = new Employee(id, name, role, enterpriseId);
            BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(newEmployee);
            jdbcTemplate.update(QUERY_UPDATE_EMPLOYEE, paramSource);
            log.info("Empregado, cujo id é {}, foi atulizado de {} para {} " +
                    "de {} para {} " +
                    "de {} para {} com sucesso.", id, oldEmployee.getName(), name, oldEmployee.getRole(), role,
                    oldEmployee.getEnterpriseId(), enterpriseId);
            return Mono.empty();
        } catch (EmployeeNotExistsException e){
            log.error("Empregado de id = {} não existe.", id);
            return Mono.error(new EmployeeNotExistsException(e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao atualizar a empregado id = {} :: Messagem - {}", id, e.getMessage());
            return Mono.error(new EmployeeException("Erro ao atualizar empregado id = "+id));
        }
    }
}
