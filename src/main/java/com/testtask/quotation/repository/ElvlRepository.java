package com.testtask.quotation.repository;

import com.testtask.quotation.dto.QueueWithElvlDTO;
import com.testtask.quotation.model.Elvl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ElvlRepository extends JpaRepository<Elvl, Long> {
    Elvl findByIsin(String isin);

    @Query(value = "SELECT DISTINCT new com.testtask.quotation.dto.QueueWithElvlDTO(q.CREATE_WHEN, q.status, e.isin, e.value) " +
            "FROM Elvl e inner join QuoteHistory q ON e.isin = q.isin")
    List<QueueWithElvlDTO> findQueueWithElvl();

}
