package com.testtask.quotation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
@RequiredArgsConstructor
public class QuoteDTO {
    private String isin;
    private Double bid;
    private Double ask;

    public QuoteDTO(String isin, Double bid, Double ask) {
        this.isin = isin;
        this.bid = bid;
        this.ask = ask;
    }
}
