package com.testtask.quotation.service;

import com.testtask.quotation.dto.QuoteDTO;
import com.testtask.quotation.model.Elvl;
import com.testtask.quotation.model.QuoteHistory;
import com.testtask.quotation.repository.ElvlRepository;
import com.testtask.quotation.repository.QuoteHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.zip.DataFormatException;

@Service
public class QuoteService {
    static final int ISIN_SIZE = 12;

    @Autowired
    private QuoteHistoryRepository quoteHistoryRepository;
    @Autowired
    private ElvlRepository elvlRepository;
    @Autowired
    private ProcessingService processingService;

    public void saveQuoteToDatabase(String isin, Double bid, Double ask) throws Exception{
        QuoteDTO quote = quoteValidation(isin, bid, ask);
        if(quote == null)
            throw new DataFormatException("Not valid data");
        QuoteHistory history = new QuoteHistory(quote.getIsin(), "created");
        quoteHistoryRepository.save(history);
        System.out.println("QuoteHistory updated: isin " + history.getIsin() + " value " + history.getIsin());
        processingService.addToQueue(quote);
    }

    private QuoteDTO quoteValidation(String isin, Double bid, Double ask){
        if (bid == null)
            bid = ask;
        if (bid > ask || isin.length() != ISIN_SIZE)
            return null;
        return new QuoteDTO(isin, bid, ask);
    }

    public List<Elvl> findAllElvls(){
        return elvlRepository.findAll();
    }

    public List<QuoteHistory> findAllQuotes(){
        return quoteHistoryRepository.findAll();
    }

    public Double findElvlByIsin(String isin){
        return elvlRepository.findByIsin(isin) != null ? elvlRepository.findByIsin(isin).getValue()
                : null;
    }
}
