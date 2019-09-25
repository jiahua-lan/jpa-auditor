package com.miaostar.assess.system.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaostar.assess.system.entity.Group;
import org.hamcrest.Matchers;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class GroupHandlerTest {

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
    public void find() throws Exception {
        String token = getToken();

        MockHttpServletRequestBuilder get = get("/groups/3")
                .header("Authorization", "Bearer " + token);

        mock.perform(get)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Matchers.equalTo(3)));
    }

    @Test
    public void findAll() throws Exception {
        String token = getToken();

        Group group = new Group();

        byte[] bytes = mapper.writeValueAsBytes(group);

        MockHttpServletRequestBuilder get = get("/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes)
                .header("Authorization", "Bearer " + token);

        mock.perform(get)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void create() throws Exception {

        String token = getToken();

        Group group = new Group();
        group.setName("TEST_GROUP");
        group.setCode("T0001");

        byte[] bytes = mapper.writeValueAsBytes(group);

        MockHttpServletRequestBuilder post = post("/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes)
                .header("Authorization", "Bearer " + token);

        mock.perform(post)
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void replace() throws Exception {

        String token = getToken();

        MockHttpServletRequestBuilder get = get("/groups/3")
                .header("Authorization", "Bearer " + token);

        String content = mock.perform(get).andReturn().getResponse().getContentAsString();

        Group group = mapper.readValue(content, Group.class);
        group.setRemark("REPLACE");

        byte[] bytes = mapper.writeValueAsBytes(group);

        MockHttpServletRequestBuilder put = put("/groups/" + group.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes)
                .header("Authorization", "Bearer " + token);

        mock.perform(put)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remark").value(Matchers.equalTo("REPLACE")));
    }

    @Test
    public void delete() throws Exception {

        //TODO:
        String token = getToken();

        MockHttpServletRequestBuilder delete = MockMvcRequestBuilders.delete("/groups/3")
                .header("Authorization", "Bearer " + token);
        mock.perform(delete)
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void allocation() {
    }

    @Test
    public void remove() {
    }
}