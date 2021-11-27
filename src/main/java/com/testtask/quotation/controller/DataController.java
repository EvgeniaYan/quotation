package com.testtask.quotation.controller;

import com.testtask.quotation.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.transaction.Transactional;

@Controller
public class DataController {
    @Autowired
    private QuoteService quoteService;

    @GetMapping("/")
    @Transactional
    public void loadQuotes(String isin, String bit, String ask){
        try {
            //quoteService.saveQuoteToDatabase(isin, bit, ask);
            quoteService.saveQuoteToDatabase("RU000A0JX0J2", "100.2", "101.9");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
