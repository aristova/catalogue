package com.example.demo;

import com.example.demo.jwt.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class AuthAndAccessTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void loginSuccess_shouldReturnToken() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(matchesPattern("^[\\w-]+\\.[\\w-]+\\.[\\w-]+$")));

    }

    @Test
    void loginFail_shouldReturn401() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\", \"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void accessProtectedEndpoint_withValidToken_shouldReturn200() throws Exception {
        String token = jwtUtil.generateToken("admin");

        mockMvc.perform(get("/api/products")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void accessProtectedEndpoint_withoutToken_shouldReturn401() throws Exception {
        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void healthCheck_shouldReturnOkWithoutAuth() throws Exception {
        mockMvc.perform(get("/v1/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }
}