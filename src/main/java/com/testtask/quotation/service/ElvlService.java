package com.testtask.quotation.service;

import com.testtask.quotation.model.Elvl;
import com.testtask.quotation.dto.QuoteDTO;
import com.testtask.quotation.repository.ElvlRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class ElvlService {

    private final ElvlRepository elvlRepository;

    private final Logger logger;

    @Transactional
    public synchronized void createElvl(QuoteDTO quote) {
        Elvl elvl = elvlRepository.findByIsin(quote.getIsin()).orElse(null);
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
        logger.info("Elvl updated: isin " + elvl.getIsin() + " value " + elvl.getValue());
    }
}
