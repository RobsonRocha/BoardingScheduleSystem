package com.ogc.boardingschedule.repositories;

import com.ogc.boardingschedule.domain.Enterprise;
import com.ogc.boardingschedule.exceptions.EnterpriseException;
import com.ogc.boardingschedule.exceptions.EnterpriseInsertException;
import com.ogc.boardingschedule.exceptions.EnterpriseNotExistsException;
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
public class EnterpriseRepository {
    private static final String QUERY_GET_COMPANY_BY_NAME = "SELECT id, name " +
            "FROM enterprise WHERE trim(upper(name))=trim(upper(:name))";

    private static final String QUERY_GET_COMPANY_BY_ID = "SELECT id, name " +
            "FROM enterprise WHERE id = :id";

    private static final String QUERY_GET_COMPANIES = "SELECT ID, NAME " +
            " FROM enterprise";

    private static final String QUERY_INSERT_NEW_COMPANY = "INSERT INTO enterprise (name)  " +
            "  VALUES (:name)";

    private static final String QUERY_DELETE_COMPANY = "DELETE from enterprise " +
            "  WHERE id = :id";

    private static final String QUERY_UPDATE_COMPANY = "UPDATE enterprise set name = :name" +
            "  WHERE id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Enterprise> rowMapper = BeanPropertyRowMapper.newInstance(Enterprise.class);

    public EnterpriseRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean alreadyExists(String name) {

        SqlParameterSource param = new MapSqlParameterSource("name", name);

        try {
            jdbcTemplate.queryForObject(QUERY_GET_COMPANY_BY_NAME, param, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }

        return true;

    }

    public Mono<Void> insertNewCompany(String name) {
        try {
            SqlParameterSource param = new MapSqlParameterSource("name", name);
            jdbcTemplate.update(QUERY_INSERT_NEW_COMPANY, param);
            log.info("Empresa {} inserida com sucesso.", name);
            return Mono.empty();
        } catch (Exception e) {
            log.error("Erro ao inserir a empresa {} :: Messagem - {}", name, e.getMessage());
            return Mono.error(new EnterpriseInsertException("Erro ao inserir empresa "+name));
        }
    }


    public Flux<Enterprise> getCompanies() {
        try {
            List<Enterprise> enterprises =  jdbcTemplate.query(QUERY_GET_COMPANIES, rowMapper);
            return Flux.defer(() -> Flux.fromIterable(
                    enterprises));
        } catch (EmptyResultDataAccessException e) {
            log.info("Não há nenhuma empresa no cadastrada");
            return Flux.empty();
        } catch (Exception e) {
            log.error("Erro ao acessar o banco :: Message - {}", e.getMessage());
            return Flux.error(new EnterpriseException(e.getMessage()));
        }
    }

    public Mono<Enterprise> getCompanyByName(String name) {

        SqlParameterSource param = new MapSqlParameterSource("name", name);

        try {
            Enterprise enterprise = jdbcTemplate.queryForObject(QUERY_GET_COMPANY_BY_NAME, param, rowMapper);
            return Mono.just(enterprise);
        } catch (EmptyResultDataAccessException e) {
            log.info("Empresa {} não existe", name);
            return Mono.error(new EnterpriseNotExistsException("Empresa "+name+" não existe."));
        } catch (Exception e) {
            log.error("Erro ao acessar o banco :: Message - {}", e.getMessage());
            return Mono.error(new EnterpriseException(e.getMessage()));
        }
    }

    public Mono<Enterprise> getCompanyById(Long id) {
        SqlParameterSource param = new MapSqlParameterSource("id", id);

        try {
            Enterprise enterprise = jdbcTemplate.queryForObject(QUERY_GET_COMPANY_BY_ID, param, rowMapper);
            return Mono.just(enterprise);
        } catch (EmptyResultDataAccessException e) {
            log.info("Empresa, cujo id é {} não existe", id);
            return Mono.error(new EnterpriseNotExistsException("Empresa, cujo id é  "+id+" não existe."));
        } catch (Exception e) {
            log.error("Erro ao acessar o banco :: Message - {}", e.getMessage());
            return Mono.error(new EnterpriseException(e.getMessage()));
        }
    }

    public Mono<Void> deleteCompanyById(Long id) {
        try {
            getCompanyById(id);
            SqlParameterSource param = new MapSqlParameterSource("id", id);
            jdbcTemplate.update(QUERY_DELETE_COMPANY, param);
            log.info("Empresa, cujo id é {}, foi apagada com sucesso.", id);
            return Mono.empty();
        } catch (EnterpriseNotExistsException e){
            log.info("Empresa de id = {} não existe.", id);
            return Mono.empty();
        } catch (Exception e) {
            log.error("Erro ao apagar a empresa id = {} :: Messagem - {}", id, e.getMessage());
            return Mono.error(new EnterpriseException("Erro ao apagar empresa id = "+id));
        }
    }

    public Mono<Void> updateCompany(Long id, String name) {
        try {
            Mono<Enterprise> enterprise = getCompanyById(id);
            Enterprise oldEnterprise = enterprise.block();
            Enterprise newEnterprise = new Enterprise(id, name);
            BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(newEnterprise);
            jdbcTemplate.update(QUERY_UPDATE_COMPANY, paramSource);
            log.info("Empresa, cujo id é {}, foi autulizada de {} para {} com sucesso.", id, oldEnterprise.getName(), name);
            return Mono.empty();
        } catch (EnterpriseNotExistsException e){
            log.error("Empresa de id = {} não existe.", id);
            return Mono.error(new EnterpriseNotExistsException("Empresa de id = "+id+" e nome = "+name+" não existe."));
        } catch (Exception e) {
            log.error("Erro ao atualizar a empresa id = {} :: Messagem - {}", id, e.getMessage());
            return Mono.error(new EnterpriseException("Erro ao atualizar empresa id = "+id));
        }
    }
}
