package com.testtask.quotation;

import com.google.gson.Gson;
import com.testtask.quotation.controller.DataController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {QuotationApplication.class})
public class QuotationApplicationTests {
    /*@LocalServerPort
    private int port;*/

    private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

    @Autowired
    private DataController controller;

	@Before
	public void setUp() {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

    /*@Test
    void contextLoads() throws Exception {
    }*/

    @Test
	public void uploadTestData() throws Exception {
        //controller.loadQuotes("RU000A0JX0J2", "100.2", "101.9");
		DataClass data = new DataClass("RU000A0JX0J2", "100.2", "101.9");
		Gson gson = new Gson();
		String json = gson.toJson(data);

		for (int i = 0; i <= 20; i++){
			mockMvc.perform(post("/api/load")
							.contentType(MediaType.APPLICATION_JSON).content(json))
					.andExpect(status().isCreated());
		}

		mockMvc.perform(MockMvcRequestBuilders.get("/api/getAllData"))
				.andDo(print());
	}

	private static class DataClass {
		String isin, bid, ask;
		public DataClass(String s1, String s2, String s3) {
			isin= s1;
			bid = s2;
			ask = s3;
		}
	}
}
