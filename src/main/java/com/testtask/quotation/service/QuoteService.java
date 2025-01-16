package com.testtask.quotation.service;

import com.testtask.quotation.dto.QuoteDTO;
import com.testtask.quotation.model.Elvl;
import com.testtask.quotation.model.QuoteHistory;
import com.testtask.quotation.repository.ElvlRepository;
import com.testtask.quotation.repository.QuoteHistoryRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.zip.DataFormatException;

@Service
@AllArgsConstructor
public class QuoteService {
    static final int ISIN_SIZE = 12;

    private final QuoteHistoryRepository quoteHistoryRepository;
    private final ElvlRepository elvlRepository;
    private final ProcessingService processingService;

    private final Logger logger;

    @Transactional
    public void saveQuoteToDatabase(String isin, Double bid, Double ask) throws Exception {
        QuoteDTO quote = quoteValidation(isin, bid, ask);
        if (quote == null)
            throw new DataFormatException("Not valid data");
        QuoteHistory history = new QuoteHistory(quote.getIsin(), "created");
        quoteHistoryRepository.save(history);
        logger.info("QuoteHistory updated: isin " + history.getIsin() + " value " + history.getIsin());
        processingService.addToQueue(quote);
    }

    private QuoteDTO quoteValidation(String isin, Double bid, Double ask) {
        if (bid == null)
            bid = ask;
        if (isin == null || ask == null || bid > ask || isin.length() != ISIN_SIZE)
            return null;
        return new QuoteDTO(isin, bid, ask);
    }

    public List<Elvl> findAllElvls() {
        return elvlRepository.findAll();
    }

    public void removeAll() {
        elvlRepository.deleteAll();
        quoteHistoryRepository.deleteAll();
    }

    public List<QuoteHistory> findAllQuotes() {
        return quoteHistoryRepository.findAll();
    }

    public Double findElvlByIsin(String isin) {
        return Objects.requireNonNull(elvlRepository.findByIsin(isin).orElse(null)).getValue();
    }
}
