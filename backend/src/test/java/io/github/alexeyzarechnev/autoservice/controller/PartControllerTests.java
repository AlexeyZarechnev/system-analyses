package io.github.alexeyzarechnev.autoservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Sql(
  statements = "TRUNCATE TABLE parts RESTART IDENTITY CASCADE",
  executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class PartControllerTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }


    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    private String partJson;

    @BeforeEach
    void setUp() throws Exception {
        partJson = """
            {
              "name": "Brake disc",
              "articleNumber": 123456,
              "remains": 10,
              "price": 4500
            }
            """;
    }

    @Test
    void shouldCreatePart() throws Exception {
        mockMvc.perform(post("/v1/storage/parts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(partJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Brake disc"))
                .andExpect(jsonPath("$.articleNumber").value(123456))
                .andExpect(jsonPath("$.remains").value(10))
                .andExpect(jsonPath("$.price").value(4500));
    }

    @Test
    void shouldGetPartById() throws Exception {
        String response = mockMvc.perform(post("/v1/storage/parts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(partJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/v1/storage/parts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Brake disc"));
    }

    @Test
    void shouldUpdatePart() throws Exception {
        String response = mockMvc.perform(post("/v1/storage/parts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(partJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        String updatedJson = """
            {
              "name": "Brake disc updated",
              "articleNumber": 123456,
              "remains": 20,
              "price": 5000
            }
            """;

        mockMvc.perform(post("/v1/storage/parts/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Brake disc updated"))
                .andExpect(jsonPath("$.remains").value(20))
                .andExpect(jsonPath("$.price").value(5000));
    }

    @Test
    void shouldDeletePart() throws Exception {
        String response = mockMvc.perform(post("/v1/storage/parts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(partJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/v1/storage/parts/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/v1/storage/parts/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenPartNotFound() throws Exception {
        mockMvc.perform(get("/v1/storage/parts/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenCreatePartWithInvalidBody() throws Exception {
        String invalidJson = "hguhb";

        mockMvc.perform(post("/v1/storage/parts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404WhenUpdateNonExistingPart() throws Exception {
            mockMvc.perform(post("/v1/storage/parts/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(partJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenDeleteNonExistingPart() throws Exception {
        mockMvc.perform(delete("/v1/storage/parts/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdatePartInDatabase() throws Exception {
        String createResponse = mockMvc.perform(post("/v1/storage/parts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(partJson))
                .andReturn()
                .getResponse()
                .getContentAsString(); 

        Long id = objectMapper.readTree(createResponse).get("id").asLong();

        String updatedJson = """
            {
            "name": "Brake disc PRO",
            "articleNumber": 123456,
            "remains": 25,
            "price": 6000
            }
            """;

        mockMvc.perform(post("/v1/storage/parts/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/v1/storage/parts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Brake disc PRO"))
                .andExpect(jsonPath("$.remains").value(25))
                .andExpect(jsonPath("$.price").value(6000));
    }


}
