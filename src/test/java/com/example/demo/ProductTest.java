package com.example.demo;

import com.example.demo.entity.Product;
import com.example.demo.entity.Review;
import com.example.demo.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hamcrest.core.Is;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.example.demo.TestData.*;
import static com.example.demo.controller.ProductController.REST_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ProductTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManagerFactory emf;

    @Test
    void create() throws Exception {
        String jsonProduct = objectMapper.writeValueAsString(getNew());

        mockMvc.perform(post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonProduct))
                .andExpect(status().isCreated()); // 201

        List<Product> products = productRepository.findAll();
        assertThat(products).anyMatch(p -> p.getTitle().equals(getNew().getTitle()));
    }

    @Test
    void update() throws Exception {
        String jsonProductUpdated = objectMapper.writeValueAsString(getUpdated());

        mockMvc.perform(put(REST_URL + "/" + PRODUCT_1_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonProductUpdated))
                .andExpect(status().isNoContent()); // 204

        List<Product> products = productRepository.findAll();
        assertThat(products).anyMatch(p -> p.getTitle().equals(getUpdated().getTitle()));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .delete(REST_URL + "/" + PRODUCT_1_ID))
                .andExpect(status().isNoContent());

        List<Product> products = productRepository.findAll();
        assertThat(products).noneMatch(p -> p.getTitle().equals(PRODUCT_1_NAME));
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .get(REST_URL + "/" + PRODUCT_1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(PRODUCT_1_ID))
                .andExpect(jsonPath("$.title").value(PRODUCT_1_NAME));
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()").value(productRepository.count())); // или точное количество
    }

    @Test
    void deleteNotFound() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .delete(REST_URL + "/" + PRODUCT_NOT_FOUND))
                .andExpect(status().isNotFound());
    }

    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .get(REST_URL + "/" + PRODUCT_NOT_FOUND))
                .andExpect(status().isNotFound());
    }

    @Test
    void createEmptyTitle() throws Exception {
        String jsonProduct = objectMapper.writeValueAsString(getNewEmptyTitle());

        mockMvc.perform(post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonProduct))
                .andExpect(jsonPath("$.title", Is.is("Title must not be blank")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNotValidDetails() throws Exception {
        String jsonProduct = objectMapper.writeValueAsString(getNewShortDetails());

        mockMvc.perform(post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonProduct))
                .andExpect(jsonPath("$.details", Is.is("Size of Details must be between 3 and 255")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldLoadGraphDynamically() {
        // Enable Hibernate statistics
        SessionFactory sf = emf.unwrap(SessionFactory.class);
        Statistics stats = sf.getStatistics();
        stats.setStatisticsEnabled(true);
        stats.clear();

        List<Product> products = productRepository.findAll();

        assertFalse(products.isEmpty());

        assertTrue(Hibernate.isInitialized(products.get(0).getReviews()));

        assertEquals(1, stats.getPrepareStatementCount());
    }
}