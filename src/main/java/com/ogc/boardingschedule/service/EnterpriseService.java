package com.ogc.boardingschedule.service;

import com.ogc.boardingschedule.domain.Enterprise;
import com.ogc.boardingschedule.exceptions.EnterpriseExistsException;
import com.ogc.boardingschedule.repositories.EnterpriseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;

    public EnterpriseService(EnterpriseRepository enterpriseRepository) {
        this.enterpriseRepository = enterpriseRepository;
    }


    public Mono<Void> insertCompany(String name) {
        if (enterpriseRepository.alreadyExists(name)) {
            log.info("Empresa {} já existe.", name);
            throw new EnterpriseExistsException("Empresa "+name+" já existe.");
        }
        log.info("Inserindo empresa {}.", name);
        return enterpriseRepository.insertNewCompany(name);
    }

    public Flux<Enterprise> getCompanies() {
        log.info("Buscando todas as empresas.");
        return enterpriseRepository.getCompanies();
    }

    public Mono<Enterprise> getCompanyByName(String name) {
        log.info("Buscando empresa {}.", name);
        return enterpriseRepository.getCompanyByName(name);
    }

    public Mono<Enterprise> getCompanyById(Long id) {
        log.info("Buscando empresa de id {}.", id);
        return enterpriseRepository.getCompanyById(id);
    }

    public Mono<Void> deleteCompanyById(Long id) {
        log.info("Apagando empresa de id {}.", id);
        return enterpriseRepository.deleteCompanyById(id);
    }

    public Mono<Void> updateCompany(Long id, String name) {
        log.info("Atualizando empresa de id {} e nome {}.", id, name);
        return enterpriseRepository.updateCompany(id, name);
    }
}
