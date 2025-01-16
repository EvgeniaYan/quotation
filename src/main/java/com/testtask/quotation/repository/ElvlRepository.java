package com.testtask.quotation.repository;

import com.testtask.quotation.model.Elvl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ElvlRepository extends JpaRepository<Elvl, Long> {
    Optional<Elvl> findByIsin(String isin);
}
