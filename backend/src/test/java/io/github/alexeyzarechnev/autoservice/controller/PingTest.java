package io.github.alexeyzarechnev.autoservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(PingApiController.class)
public class PingTest {
    
    @Autowired
    MockMvc mvc;

    @Test
	void pingTest() throws Exception {
		mvc.perform(get("/v1/ping"))
			.andExpect(status().isOk())
			.andExpect(content().string("pong"));
	}
}
