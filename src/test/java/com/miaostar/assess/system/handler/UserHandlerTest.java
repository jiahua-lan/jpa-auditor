package com.miaostar.assess.system.handler;

import com.miaostar.assess.system.entity.User;
import com.miaostar.assess.test.BasicHandlerTest;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class UserHandlerTest extends BasicHandlerTest {
    
    @Test
    public void testFind() throws Exception {
        String token = getToken();

        MockHttpServletRequestBuilder findAll = get("/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}");

        JsonParser parser = new JacksonJsonParser();

        String content = getMock().perform(findAll).andReturn().getResponse().getContentAsString();

        User user = parser.parseList(content).stream().findFirst()
                .map(item -> getMapper().convertValue(item, User.class)).orElseThrow(IllegalArgumentException::new);

        MockHttpServletRequestBuilder get = get("/users/" + user.getId())
                .header("Authorization", "Bearer " + token);

        getMock().perform(get)
                .andDo(log())
                .andExpect(status().isOk());
    }

    @Test
    public void testCreate() throws Exception {
        String token = getToken();

        User user = new User();
        user.setUsername("TEST");
        user.setPassword("123456");

        byte[] bytes = getMapper().writeValueAsBytes(user);

        MockHttpServletRequestBuilder post = post("/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes);

        getMock().perform(post)
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").isNumber())
        ;

    }

    @Test
    public void testFindAll() throws Exception {
        String token = getToken();

        User user = new User();

        byte[] bytes = getMapper().writeValueAsBytes(user);

        MockHttpServletRequestBuilder get = get("/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes);

        getMock().perform(get)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
        ;
    }

    @Test
    public void testReplace() throws Exception {
        String token = getToken();

        User probe = new User();

        byte[] bytes = getMapper().writeValueAsBytes(probe);

        MockHttpServletRequestBuilder list = get("/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes);

        String content = getMock().perform(list)
                .andReturn().getResponse()
                .getContentAsString();

        JsonParser parser = new JacksonJsonParser();

        User user = parser.parseList(content).stream().findFirst()
                .map(item -> getMapper().convertValue(item, User.class)).orElseThrow(IllegalArgumentException::new);

        user.setUsername("TEST_USER_NAME");

        String body = getMapper().writeValueAsString(user);

        MockHttpServletRequestBuilder put = put("/users/" + user.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);

        ResultMatcher matcher = jsonPath("$.username")
                .value(Matchers.equalTo("TEST_USER_NAME"));

        getMock().perform(put)
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(matcher);
    }

    @Test
    public void delete() {
    }

    @Test
    public void testAssignRoles() {
    }

    @Test
    public void userRoles() throws Exception {
        String token = getToken();

        User probe = new User();

        byte[] bytes = getMapper().writeValueAsBytes(probe);

        MockHttpServletRequestBuilder findAll = get("/users")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes);

        JsonParser parser = new JacksonJsonParser();

        String content = getMock().perform(findAll).andReturn()
                .getResponse().getContentAsString();

        User user = parser.parseList(content).stream().findFirst()
                .map(item -> getMapper().convertValue(item, User.class))
                .orElseThrow(IllegalArgumentException::new);

        String path = String.format("/users/%d/roles", user.getId());

        MockHttpServletRequestBuilder userRoles = get(path)
                .header("Authorization", "Bearer " + token);

        getMock().perform(userRoles)
                .andDo(log());
    }

    @Test
    public void testRemoveRoles() {
    }

    @Test
    public void userGroups() {
    }

    @Test
    public void addToGroup() {
    }

    @Test
    public void removeGroup() {
    }
}