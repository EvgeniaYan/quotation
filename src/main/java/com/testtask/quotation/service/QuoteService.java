package com.testtask.quotation.service;

import com.testtask.quotation.model.Quote;
import com.testtask.quotation.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {
    static final int ISIN_SIZE = 12;

    @Autowired
    private QuoteRepository quoteRepository;

    public void saveQuoteToDatabase(String isin, String bit, String ask) throws Exception{
        Quote quote = quoteValidation(isin, bit, ask);
        if(quote == null)
            throw new Exception("Wrong data");
        quoteRepository.save(quote);
    }

    private Quote quoteValidation(String isin, String bit, String ask){
        double bitD = Double.parseDouble(bit);
        double askD = Double.parseDouble(ask);
        if (bitD >= askD || isin.length() != ISIN_SIZE)
            return null;
        return new Quote(isin, bitD, askD);
    }
}
