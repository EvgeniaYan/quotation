package com.testtask.quotation.controller;

import com.testtask.quotation.model.Quote;
import com.testtask.quotation.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@Controller
public class DataController {
    @Autowired
    private QuoteService quoteService;

    @RequestMapping(value = { "/api/load" }, method = { RequestMethod.POST })
    @Transactional
    public void loadQuotes(@RequestBody Quote quote, HttpServletResponse httpResponse,
                           WebRequest request){
        try {
            //quoteService.saveQuoteToDatabase(isin, bit, ask);
            quoteService.saveQuoteToDatabase("RU000A0JX0J2", "100.2", "101.9");
            httpResponse.setStatus(HttpStatus.CREATED.value());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
