package com.testtask.quotation.service;

import com.testtask.quotation.model.Elvl;
import com.testtask.quotation.model.Quote;
import com.testtask.quotation.repository.ElvlRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ElvlService {
    @Autowired
    ElvlRepository elvlRepository;

    public void createElvl(Quote quote) {
        Elvl elvl = elvlRepository.findByIsin(quote.getIsin());
        if (elvl == null) {
            elvl = new Elvl(quote.getIsin(), quote.getBid());
            elvlRepository.save(elvl);

        } else {
            if (quote.getBid() > elvl.getValue()) {
                elvl.setValue(quote.getBid());
            }
            if (quote.getAsk() < elvl.getValue()) {
                elvl.setValue(quote.getAsk());
            }

        }
    }
}
