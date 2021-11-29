package com.testtask.quotation.service;

import com.testtask.quotation.dto.QueueWithElvlDTO;
import com.testtask.quotation.dto.QuoteDTO;
import com.testtask.quotation.model.QuoteHistory;
import com.testtask.quotation.repository.ElvlRepository;
import com.testtask.quotation.repository.QuoteHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuoteService {
    static final int ISIN_SIZE = 12;

    @Autowired
    private QuoteHistoryRepository quoteHistoryRepository;
    @Autowired
    private ElvlRepository elvlRepository;
    @Autowired
    private ProcessingService processingService;

    public void saveQuoteToDatabase(String isin, Double bit, Double ask) throws Exception{
        QuoteDTO quote = quoteValidation(isin, bit, ask);
        if(quote == null)
            throw new Exception("Not valid data");
        QuoteHistory history = new QuoteHistory(quote.getIsin(), "created");
        quoteHistoryRepository.save(history);
        processingService.addToQueue(quote);
    }

    private QuoteDTO quoteValidation(String isin, Double bit, Double ask){
        if (bit >= ask || isin.length() != ISIN_SIZE)
            return null;
        return new QuoteDTO(isin, bit, ask);
    }

    public List<QueueWithElvlDTO> findQueueWithElvl(){
        return elvlRepository.findQueueWithElvl();
        //return elvlRepository.findAll();
    }
}
