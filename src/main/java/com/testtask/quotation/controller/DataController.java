package com.testtask.quotation.controller;

import com.testtask.quotation.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DataController {
    @Autowired
    private QuoteService quoteService;

    @GetMapping("/")
    public void loadQuotes(String isin, String bit, String ask){
        try {
            quoteService.saveQuoteToDatabase(isin, bit, ask);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
