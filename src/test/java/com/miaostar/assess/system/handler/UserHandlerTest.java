package com.miaostar.assess.system.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaostar.assess.system.entity.User;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class UserHandlerTest {

    private ObjectMapper mapper;

    private MockMvc mock;

    @Autowired
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setMock(MockMvc mock) {
        this.mock = mock;
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
    public void find() throws Exception {

        String token = getToken();

        MockHttpServletRequestBuilder get = get("/users/1")
                .header("Authorization", "Bearer " + token);

        mock.perform(get)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void create() throws Exception {
        String token = getToken();

        User user = new User();
        user.setUsername("TEST");
        user.setPassword("123456");

        byte[] bytes = mapper.writeValueAsBytes(user);

        MockHttpServletRequestBuilder post = post("/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes);

        mock.perform(post)
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").isNumber())
        ;

    }

    @Test
    public void findAll() throws Exception {
        String token = getToken();

        User user = new User();

        byte[] bytes = mapper.writeValueAsBytes(user);

        MockHttpServletRequestBuilder get = get("/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes);

        mock.perform(get)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
        ;
    }

    @Test
    public void replace() throws Exception {
        String token = getToken();

        User probe = new User();

        byte[] bytes = mapper.writeValueAsBytes(probe);

        MockHttpServletRequestBuilder list = get("/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes);

        String content = mock.perform(list)
                .andReturn().getResponse()
                .getContentAsString();

        JsonParser parser = new JacksonJsonParser();

        parser.parseList(content).stream()
                .findFirst()
                .map(object -> mapper.convertValue(object, User.class))
                .ifPresent(user -> {

                    user.setUsername("TEST_USER_NAME");

                    String body = "";
                    try {
                        body = mapper.writeValueAsString(user);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    MockHttpServletRequestBuilder put = put("/users/" + user.getId())
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body);

                    try {
                        ResultMatcher matcher = jsonPath("$.username")
                                .value(Matchers.equalTo("TEST_USER_NAME"));
                        mock.perform(put)
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(matcher);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @Test
    public void assignRoles() {
    }

    @Test
    public void removeRoles() {
    }
}