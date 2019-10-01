package com.miaostar.assess.system.handler;

import com.miaostar.assess.system.entity.Role;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class RoleHandlerHandlerTest extends BasicHandlerTest {

    @Test
    public void find() throws Exception {
        String token = getToken();

        MockHttpServletRequestBuilder get = get("/roles")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}");
        String content = getMock().perform(get).andReturn()
                .getResponse().getContentAsString();

        JsonParser parser = new JacksonJsonParser();

        Role role = parser.parseList(content).stream().findFirst()
                .map(object -> getMapper().convertValue(object, Role.class))
                .orElseThrow(IllegalArgumentException::new);

        MockHttpServletRequestBuilder find = get("/roles/" + role.getId())
                .header("Authorization", "Bearer " + token);

        getMock().perform(find)
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(Matchers.equalTo(role.getId()), Long.class));

    }

    @Test
    public void findAll() throws Exception {

        String token = getToken();

        MockHttpServletRequestBuilder get = get("/roles")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}");

        getMock().perform(get)
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void create() throws Exception {

        String token = getToken();

        Role role = new Role();
        role.setCode("TEST_ROLE");
        role.setName("TEST_ROLE");

        byte[] bytes = getMapper().writeValueAsBytes(role);

        MockHttpServletRequestBuilder post = post("/roles")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes);

        getMock().perform(post)
                .andDo(log())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void replace() throws Exception {
        String token = getToken();

        MockHttpServletRequestBuilder get = get("/roles")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}");
        String content = getMock().perform(get).andReturn()
                .getResponse().getContentAsString();

        JsonParser parser = new JacksonJsonParser();

        Role role = parser.parseList(content).stream().findFirst()
                .map(object -> getMapper().convertValue(object, Role.class))
                .orElseThrow(IllegalArgumentException::new);

        role.setName("TEST_TEST");

        byte[] bytes = getMapper().writeValueAsBytes(role);

        MockHttpServletRequestBuilder put = put("/roles/" + role.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes);

        getMock().perform(put)
                .andDo(log()).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(Matchers.equalTo("TEST_TEST")));
    }

    @Test
    public void delete() throws Exception {
        String token = getToken();

        MockHttpServletRequestBuilder get = get("/roles")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}");
        String content = getMock().perform(get).andReturn()
                .getResponse().getContentAsString();

        JsonParser parser = new JacksonJsonParser();

        Role role = parser.parseList(content).stream().findFirst()
                .map(object -> getMapper().convertValue(object, Role.class))
                .orElseThrow(IllegalArgumentException::new);

        MockHttpServletRequestBuilder delete = MockMvcRequestBuilders.delete("/roles/" + role.getId())
                .header("Authorization", "Bearer " + token);

        getMock().perform(delete)
                .andDo(log())
                .andExpect(status().isNoContent());
    }

    @Test
    public void listResources() throws Exception {
        String token = getToken();

        MockHttpServletRequestBuilder get = get("/roles")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}");
        String content = getMock().perform(get).andReturn()
                .getResponse().getContentAsString();

        JsonParser parser = new JacksonJsonParser();

        Role role = parser.parseList(content).stream().findFirst()
                .map(object -> getMapper().convertValue(object, Role.class))
                .orElseThrow(IllegalArgumentException::new);

        MockHttpServletRequestBuilder list = get("/roles/" + role.getId() + "/resources")
                .header("Authorization", "Bearer " + token);

        getMock().perform(list)
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());


    }

    @Test
    public void allocation() {
    }

    @Test
    public void remove() {
    }
}