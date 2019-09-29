package com.miaostar.assess.system.handler;

import com.miaostar.assess.system.entity.Group;
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
public class GroupHandlerTest extends BasicHandlerTest {

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
        String content = getMock().perform(post).andReturn().getResponse().getContentAsString();
        JacksonJsonParser parser = new JacksonJsonParser();
        return parser.parseMap(content).get("access_token").toString();
    }

    @Test
    public void testFind() throws Exception {
        //0.准备阶段：获取token
        String token = getToken();

        //1.获取用户组列表，并取得第一位的用户组
        Group probe = new Group();

        byte[] value = getMapper().writeValueAsBytes(probe);

        MockHttpServletRequestBuilder findAll = get("/groups")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(value);

        String content = getMock().perform(findAll).andReturn().getResponse().getContentAsString();

        JsonParser parser = new JacksonJsonParser();

        Group group = parser.parseList(content).stream().findFirst()
                .map(item -> getMapper().convertValue(item, Group.class))
                .orElseThrow(IllegalArgumentException::new);

        //测试开始
        MockHttpServletRequestBuilder get = get("/groups/" + group.getId())
                .header("Authorization", "Bearer " + token);

        getMock().perform(get)
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Matchers.equalTo(group.getId())));
    }

    @Test
    public void testFindAll() throws Exception {
        //准备阶段：获取Token，构建请求
        String token = getToken();

        Group group = new Group();

        byte[] bytes = getMapper().writeValueAsBytes(group);

        MockHttpServletRequestBuilder get = get("/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes)
                .header("Authorization", "Bearer " + token);

        //开始测试
        getMock().perform(get)
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testCreate() throws Exception {
        //准备阶段：获取Token，构建请求
        String token = getToken();

        Group group = new Group();
        group.setName("TEST_GROUP");
        group.setCode("T0001");

        byte[] bytes = getMapper().writeValueAsBytes(group);

        MockHttpServletRequestBuilder post = post("/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes)
                .header("Authorization", "Bearer " + token);
        //开始测试
        getMock().perform(post)
                .andDo(log())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void testReplace() throws Exception {
        //准备阶段：获取Token，获取用户组列表，并取得第一位的用户组
        String token = getToken();

        Group probe = new Group();

        byte[] value = getMapper().writeValueAsBytes(probe);

        MockHttpServletRequestBuilder get = get("/groups")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(value);

        String content = getMock().perform(get).andReturn().getResponse().getContentAsString();

        JsonParser parser = new JacksonJsonParser();

        Group group = parser.parseList(content).stream().findFirst()
                .map(item -> getMapper().convertValue(item, Group.class))
                .orElseThrow(IllegalArgumentException::new);

        //修改用户组的备注
        group.setRemark("REPLACE");

        byte[] bytes = getMapper().writeValueAsBytes(group);

        MockHttpServletRequestBuilder put = put("/groups/" + group.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(bytes)
                .header("Authorization", "Bearer " + token);

        //发送请求
        getMock().perform(put)
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remark").value(Matchers.equalTo("REPLACE")));
    }

    @Test
    public void testDelete() throws Exception {
        //准备阶段：获取Token，获取用户组列表，并取得第一位的用户组
        String token = getToken();

        MockHttpServletRequestBuilder get = get("/groups")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}");

        JsonParser parser = new JacksonJsonParser();

        String content = getMock().perform(get).andDo(log()).andReturn().getResponse().getContentAsString();

        Group group = parser.parseList(content).stream()
                .findFirst()
                .map(item -> getMapper().convertValue(item, Group.class))
                .orElseThrow(IllegalArgumentException::new);

        //构建请求
        MockHttpServletRequestBuilder delete = MockMvcRequestBuilders.delete("/groups/" + group.getId())
                .header("Authorization", "Bearer " + token);

        //开始测试
        getMock().perform(delete)
                .andDo(log())
                .andExpect(status().isNoContent());

        //完成后的验证
        getMock().perform(get).andDo(log());
    }

    @Test
    public void testAllocation() {
    }

    @Test
    public void testRemove() {
    }

    @Test
    public void list() throws Exception {
        String token = getToken();

        MockHttpServletRequestBuilder get = get("/groups")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}");

        JsonParser parser = new JacksonJsonParser();

        String content = getMock().perform(get).andDo(log()).andReturn().getResponse().getContentAsString();

        Group group = parser.parseList(content).stream()
                .findFirst()
                .map(item -> getMapper().convertValue(item, Group.class))
                .orElseThrow(IllegalArgumentException::new);


        String uri = String.format("/groups/%d/resources", group.getId());

        MockHttpServletRequestBuilder list = get(uri)
                .header("Authorization", "Bearer " + token);

        getMock().perform(list)
                .andDo(log())
                .andExpect(jsonPath("$").isArray())
        ;
    }
}