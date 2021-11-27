package com.testtask.quotation.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@RequiredArgsConstructor
public class Quote {
    private String isin;
    private Double bid;
    private Double ask;

    public Quote(String isin, Double bid, Double ask) {
        this.isin = isin;
        this.bid = bid;
        this.ask = ask;
    }
}
