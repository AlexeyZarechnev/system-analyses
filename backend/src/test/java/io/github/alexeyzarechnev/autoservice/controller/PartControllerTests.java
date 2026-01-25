package io.github.alexeyzarechnev.autoservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
    properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.h2.console.enabled=false"
    }
)
@AutoConfigureMockMvc
@Sql(
    statements = "TRUNCATE TABLE parts",
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class PartControllerTests {

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    private String partJson;

    @BeforeEach
    void setUp() {
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
        mockMvc.perform(post("/v1/storage/parts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("hguhb"))
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
