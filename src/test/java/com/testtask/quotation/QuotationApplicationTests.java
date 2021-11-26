package com.testtask.quotation;

import com.testtask.quotation.controller.DataController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest
class QuotationApplicationTests {
	@LocalServerPort
	private int port;

	@Autowired
	private DataController controller;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() throws Exception{
		controller.loadQuotes("RU000A0JX0J2", "100.2", "101.9");
	}

}
