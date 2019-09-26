package com.miaostar.assess.system.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaostar.assess.system.entity.Group;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
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

    /**
     * 获取Token
     */
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
        //0.准备阶段：获取token
        String token = getToken();

        //1.获取用户组列表，并取得第一位的用户组
        Group probe = new Group();

        byte[] value = mapper.writeValueAsBytes(probe);

        MockHttpServletRequestBuilder findAll = get("/groups")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(value);

        String content = mock.perform(findAll).andReturn().getResponse().getContentAsString();

        JsonParser parser = new JacksonJsonParser();

        Group group = parser.parseList(content).stream().findFirst()
                .map(item -> mapper.convertValue(item, Group.class))
                .orElseThrow(IllegalArgumentException::new);

        //测试开始
        MockHttpServletRequestBuilder get = get("/groups/" + group.getId())
                .header("Authorization", "Bearer " + token);

        mock.perform(get)
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Matchers.equalTo(group.getId())));
    }

    @Test
    public void findAll() throws Exception {
        //准备阶段：获取Token，构建请求
        String token = getToken();

        Group group = new Group();

        byte[] bytes = mapper.writeValueAsBytes(group);

        MockHttpServletRequestBuilder get = get("/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes)
                .header("Authorization", "Bearer " + token);

        //开始测试
        mock.perform(get)
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void create() throws Exception {
        //准备阶段：获取Token，构建请求
        String token = getToken();

        Group group = new Group();
        group.setName("TEST_GROUP");
        group.setCode("T0001");

        byte[] bytes = mapper.writeValueAsBytes(group);

        MockHttpServletRequestBuilder post = post("/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes)
                .header("Authorization", "Bearer " + token);
        //开始测试
        mock.perform(post)
                .andDo(log())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void replace() throws Exception {
        //准备阶段：获取Token，获取用户组列表，并取得第一位的用户组
        String token = getToken();

        Group probe = new Group();

        byte[] value = mapper.writeValueAsBytes(probe);

        MockHttpServletRequestBuilder get = get("/groups")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(value);

        String content = mock.perform(get).andReturn().getResponse().getContentAsString();

        JsonParser parser = new JacksonJsonParser();

        Group group = parser.parseList(content).stream().findFirst()
                .map(item -> mapper.convertValue(item, Group.class))
                .orElseThrow(IllegalArgumentException::new);

        //修改用户组的备注
        group.setRemark("REPLACE");

        byte[] bytes = mapper.writeValueAsBytes(group);

        MockHttpServletRequestBuilder put = put("/groups/" + group.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes)
                .header("Authorization", "Bearer " + token);

        //发送请求
        mock.perform(put)
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remark").value(Matchers.equalTo("REPLACE")));
    }

    @Test
    public void delete() throws Exception {
        //准备阶段：获取Token，获取用户组列表，并取得第一位的用户组
        String token = getToken();

        MockHttpServletRequestBuilder get = get("/groups")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}");

        JsonParser parser = new JacksonJsonParser();

        String content = mock.perform(get).andDo(log()).andReturn().getResponse().getContentAsString();

        Group group = parser.parseList(content).stream()
                .findFirst()
                .map(item -> mapper.convertValue(item, Group.class))
                .orElseThrow(IllegalArgumentException::new);

        //构建请求
        MockHttpServletRequestBuilder delete = MockMvcRequestBuilders.delete("/groups/" + group.getId())
                .header("Authorization", "Bearer " + token);

        //开始测试
        mock.perform(delete)
                .andDo(log())
                .andExpect(status().isNoContent());

        //完成后的验证
        mock.perform(get).andDo(log());
    }

    @Test
    public void allocation() {
    }

    @Test
    public void remove() {
    }
}