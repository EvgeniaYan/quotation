package com.testtask.quotation.repository;

import com.testtask.quotation.model.Elvl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElvlRepository extends JpaRepository<Elvl, Long> {
    Elvl findByIsin(String isin);
}
