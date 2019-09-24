package com.miaostar.assess.system.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaostar.assess.system.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void findAll() {
    }

    @Test
    public void replace() {
    }

    @Test
    public void assignRoles() {
    }

    @Test
    public void removeRoles() {
    }
}