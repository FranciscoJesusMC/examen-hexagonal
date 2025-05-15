package com.spring.examenhexagonal.infrastructure.repository;

import com.spring.examenhexagonal.infrastructure.entity.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends JpaRepository<EmpresaEntity, Long> {

    boolean existsByNumeroDocumento(String numeroDocumento);

}
