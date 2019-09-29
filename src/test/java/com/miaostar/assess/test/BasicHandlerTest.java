package com.miaostar.assess.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public abstract class BasicHandlerTest {

    private Logger logger = LoggerFactory.getLogger(BasicHandlerTest.class);

    @Value("${info.security.username}")
    private String username;

    @Value("${info.security.password}")
    private String password;

    @Value("${info.security.clientId}")
    private String clientId;

    @Value("${info.security.clientSecret}")
    private String clientSecret;

    @Value("${info.security.grantType}")
    private String grantType;

    private MockMvc mock;

    private ObjectMapper mapper;

    protected MockMvc getMock() {
        return mock;
    }

    @Autowired
    public void setMock(MockMvc mock) {
        this.mock = mock;
    }

    protected ObjectMapper getMapper() {
        return mapper;
    }

    @Autowired
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String getToken() throws Exception {
        MockHttpServletRequestBuilder post = post("/oauth/token")
                .param("grant_type", grantType)
                .param("client_id", clientId)
                .param("client_secret", clientSecret)
                .param("username", username)
                .param("password", password);
        String content = mock.perform(post).andReturn().getResponse().getContentAsString();
        JsonParser parser = new JacksonJsonParser();
        Map<String, Object> result = parser.parseMap(content);
        logger.info("{}", result);
        return result.get("access_token").toString();

    }
}
