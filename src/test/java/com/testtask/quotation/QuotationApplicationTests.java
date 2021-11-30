package com.testtask.quotation;

import com.google.gson.Gson;
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

import java.util.concurrent.CompletableFuture;

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

    //некорректные данные - isin не 12 символов
    @Test
    public void incorrectDataTooBigIsin() throws Exception {
        DataClass data = new DataClass("INCORRECTISIN", "100.8", "100.9");
        mockMvc.perform(post("/api/load")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                .andExpect(status().isExpectationFailed());
    }

    //некорректные данные - isin не 12 символов
    @Test
    public void incorrectDataEmptyIsinOrAsk() throws Exception {
        DataClass data = new DataClass("INCORRECTISIN", "100.8", null);
        mockMvc.perform(post("/api/load")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                .andExpect(status().isExpectationFailed());

        DataClass data2 = new DataClass(null, "100.8", "100.9");
        mockMvc.perform(post("/api/load")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data2)))
                .andExpect(status().isExpectationFailed());
    }

    //Если значение elvl для данной бумаги отсутствует, то elvl = bid
    @Test
    public void newElvl() throws Exception {
        cleanDatabase();
        DataClass data = new DataClass("RU000A0JY0J2", "100.2", "101.9");
        mockMvc.perform(post("/api/load")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
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
        cleanDatabase();
        DataClass data = new DataClass("RU000A0JX0J3", "100.2", "100.9");
        mockMvc.perform(post("/api/load")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                .andExpect(status().isCreated());
        Thread.sleep(2000);
        data = new DataClass("RU000A0JX0J3", "100.0", "100.1");
        mockMvc.perform(post("/api/load")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                .andExpect(status().isCreated());
        Thread.sleep(2000);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/getElvlByIsin")
                .param("isin", data.isin)).andReturn();
        String content = result.getResponse().getContentAsString();
        assertEquals("100.1", content);

    }

    //Если bid > elvl, то elvl = bid
    @Test
    public void bidBiggerElvl() throws Exception {
        cleanDatabase();
        DataClass data = new DataClass("RU000A0JX0J4", "100.2", "100.9");
        mockMvc.perform(post("/api/load")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                .andExpect(status().isCreated());
        Thread.sleep(2000);
        data = new DataClass("RU000A0JX0J4", "100.5", "101.0");
        mockMvc.perform(post("/api/load")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                .andExpect(status().isCreated());
        Thread.sleep(2000);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/getElvlByIsin")
                .param("isin", data.isin)).andReturn();
        String content = result.getResponse().getContentAsString();
        assertEquals("100.5", content);

    }

    //Если bid отсутствует, то elvl = ask
    @Test
    public void emptyBid() throws Exception {
        cleanDatabase();
        DataClass data = new DataClass("RU000A0JX0J3", null, "100.9");
        mockMvc.perform(post("/api/load")
                        .contentType(MediaType.APPLICATION_JSON).content(gson.toJson(data)))
                .andExpect(status().isCreated());
        Thread.sleep(2000);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/getElvlByIsin")
                .param("isin", data.isin)).andReturn();
        String content = result.getResponse().getContentAsString();
        assertEquals("100.9", content);
    }

    //много запросов одновременно
    @Test
    public void uploadTestData() throws Exception {
        cleanDatabase();
        for (int i = 0; i < 100; i++) {
            String bid = System.currentTimeMillis() % 2 > 0 ? "99." + i : "100." + i;
            String ask = System.currentTimeMillis() % 2 > 0 ? "100." + i : "101." + i;
            String isin = ("RU000A0JX0J" + i).substring(0, 12);
            DataClass data = new DataClass(isin, bid, ask);
            String json = gson.toJson(data);
            mockMvc.perform(post("/api/load")
                            .contentType(MediaType.APPLICATION_JSON).content(json))
                    .andExpect(status().isCreated());
        }
        Thread.sleep(3000);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/getAllData"))
                .andReturn();
        String[] split = result.getResponse().getContentAsString().split(",");
        assertEquals(Integer.parseInt(split[0]), 100);
        assertEquals(Integer.parseInt(split[1]), 10);
    }

    //несколько запросов одновременно через потоки
    @Test
    public void uploadTestDataThreads() throws Exception {
        cleanDatabase();
        for (int i = 0; i < 100; i++) {
            String bid = System.currentTimeMillis() % 2 > 0 ? "99." + i : "100." + i;
            String ask = System.currentTimeMillis() % 2 > 0 ? "100." + i : "101." + i;
            String isin = ("RU000A0JX0J" + i).substring(0, 12);
            CompletableFuture.runAsync(() -> {
                DataClass data = new DataClass(isin, bid, ask);
                String json = gson.toJson(data);
                try {
                    mockMvc.perform(post("/api/load")
                                    .contentType(MediaType.APPLICATION_JSON).content(json))
                            .andExpect(status().isCreated());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        Thread.sleep(2000);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/getAllData"))
                .andReturn();
        String[] split = result.getResponse().getContentAsString().split(",");
        assertEquals(Integer.parseInt(split[0]), 100);
        assertEquals(Integer.parseInt(split[1]), 10);
    }

    private void cleanDatabase() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/removeAllForTests"));
        Thread.sleep(2000);
    }

    private static class DataClass {
        String isin, bid, ask;

        public DataClass(String s1, String s2, String s3) {
            isin = s1;
            bid = s2;
            ask = s3;
        }
    }
}
