package com.testtask.quotation.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter @Setter
@RequiredArgsConstructor
@Table(name = "quote_history")
public class QuoteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_when")
    private Date CREATE_WHEN;
    private String status;
    private String isin;

    public QuoteHistory(String isin, String status) {
        this.isin = isin;
        this.status = status;
    }
}
