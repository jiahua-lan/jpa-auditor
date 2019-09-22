package com.miaostar.auditor.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaostar.auditor.entity.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class DocumentHandlerTest {

    private MockMvc mock;

    private ObjectMapper mapper;

    @Autowired
    public void setMock(MockMvc mock) {
        this.mock = mock;
    }

    @Autowired
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String getToken() throws Exception {
        MockHttpServletRequestBuilder post = post("/oauth/token")
                .param("grant_type", "password")
                .param("client_id", "client")
                .param("client_secret", "123456")
                .param("username", "lan")
                .param("password", "123456");
        String content = mock.perform(post).andReturn().getResponse().getContentAsString();
        JacksonJsonParser parser = new JacksonJsonParser();
        return parser.parseMap(content).get("access_token").toString();
    }

    @Test
    public void create() throws Exception {

        String token = getToken();

        Document document = new Document();

        document.setTitle("TEST");
        document.setContent("TEST_CONTENT");

        byte[] bytes = mapper.writeValueAsBytes(document);

        MockHttpServletRequestBuilder post = post("/documents")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes);

        mock.perform(post)
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.author").isNotEmpty());

    }

    @Test
    public void createError() throws Exception {
        String token = getToken();
        Document document = new Document();
        document.setContent("TEST_CONTENT");

        byte[] bytes = mapper.writeValueAsBytes(document);

        MockHttpServletRequestBuilder post = post("/documents")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes);

        mock.perform(post)
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    public void find() {
    }
}