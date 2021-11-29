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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {QuotationApplication.class})
public class QuotationApplicationTests {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private DataController controller;

    Gson gson = new Gson();

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    //некорректные данные - bid больше ask
    @Test
    public void incorrectDataCheckBidBiggerAsk() throws Exception {
        DataClass data = new DataClass("INCORRECTBID", "100.9", "100.8");
        String json = gson.toJson(data);

        mockMvc.perform(post("/api/load")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isExpectationFailed());
    }

    //некорректные данные  isin не 12 символов
    @Test
    public void incorrectDataTooBigIsin() throws Exception {
        DataClass data = new DataClass("INCORRECTISIN", "100.8", "100.9");
        String json = gson.toJson(data);

        mockMvc.perform(post("/api/load")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isExpectationFailed());
    }

    //Если значение elvl для данной бумаги отсутствует, то elvl = bid
    @Test
    public void newElvl() throws Exception {
        DataClass data = new DataClass("RU000A0JX0J2", "100.2", "101.9");
        String json = gson.toJson(data);

        mockMvc.perform(post("/api/load")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
        Thread.sleep(2000);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/getElvlByIsin")
                .param("isin", data.isin)).andReturn();
        String content = result.getResponse().getContentAsString();
        assertEquals("100.2", content);
    }

    //Если ask < elvl, то elvl = ask
    @Test
    public void askBiggerElvl() throws Exception {

    }


    //Если bid > elvl, то elvl = bid
	/*@Test
	public void correctBidBi*/


/*    @Test
	public void uploadTestData() throws Exception {
        //controller.loadQuotes("RU000A0JX0J2", "100.2", "101.9");

		for (int i = 0; i <= 20; i++){
			String bid = System.currentTimeMillis() % 2 > 0 ? "100." + i : "99." + i;
			String ask = System.currentTimeMillis() % 2 > 0 ? "101." + i : "100." + i;
			String isin = System.currentTimeMillis() % 2 > 0 ? "RU000A0JX0J" + i : "RU000A0JX0J1";
			DataClass data = new DataClass(isin, bid, ask);
			String json = gson.toJson(data);
			mockMvc.perform(post("/api/load")
							.contentType(MediaType.APPLICATION_JSON).content(json))
					.andExpect(status().isCreated());
		}

		mockMvc.perform(MockMvcRequestBuilders.get("/api/getAllData"))
				.andDo(print());
	}*/

    private static class DataClass {
        String isin, bid, ask;

        public DataClass(String s1, String s2, String s3) {
            isin = s1;
            bid = s2;
            ask = s3;
        }
    }


    //Если bid отсутствует, то elvl = ask


}
