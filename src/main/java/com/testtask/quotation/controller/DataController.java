package com.testtask.quotation.controller;

import com.testtask.quotation.dto.QuoteDTO;
import com.testtask.quotation.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.zip.DataFormatException;

@Controller
public class DataController {
    @Autowired
    private QuoteService quoteService;

    @PostMapping(value = {"/api/load"})
    public void loadQuotes(@RequestBody QuoteDTO quote, HttpServletResponse httpResponse) {
        try {
            quoteService.saveQuoteToDatabase(quote.getIsin(), quote.getBid(), quote.getAsk());
            httpResponse.setStatus(HttpStatus.CREATED.value());
        } catch (DataFormatException e) {
            httpResponse.setStatus(HttpStatus.EXPECTATION_FAILED.value());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/api/getElvlByIsin")
    public ResponseEntity<Double> getElvlByIsin(@RequestParam(name = "isin") String isin, HttpServletResponse httpResponse) {
        Double elvlByIsin = quoteService.findElvlByIsin(isin);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(elvlByIsin);
    }

    @GetMapping("/api/getAllData")
    public ResponseEntity<String> getAllData(HttpServletResponse httpResponse) {
        int qCount = quoteService.findAllQuotes().size();
        int eCount = quoteService.findAllElvls().size();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(qCount + "," + eCount);
    }

    @GetMapping("/api/removeAllForTests")
    public void removeAllForTests(HttpServletResponse httpResponse) {
        quoteService.removeAll();
    }
}