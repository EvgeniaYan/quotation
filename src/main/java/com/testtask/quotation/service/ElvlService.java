package com.testtask.quotation.service;

import com.testtask.quotation.model.Elvl;
import com.testtask.quotation.dto.QuoteDTO;
import com.testtask.quotation.repository.ElvlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ElvlService {
    @Autowired
    ElvlRepository elvlRepository;

    private static volatile Elvl elvl;

    public synchronized void createElvl(QuoteDTO quote) {
        elvl = elvlRepository.findByIsin(quote.getIsin());
        if (elvl == null) {
            elvl = new Elvl(quote.getIsin(), quote.getBid());
        } else {
            if (quote.getBid() > elvl.getValue()) {
                elvl.setValue(quote.getBid());
            }
            if (quote.getAsk() < elvl.getValue()) {
                elvl.setValue(quote.getAsk());
            }
        }
        elvlRepository.save(elvl);
    }
}
