package com.testtask.quotation.repository;

import com.testtask.quotation.model.QuoteHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteHistoryRepository extends JpaRepository<QuoteHistory, Long > {
}
