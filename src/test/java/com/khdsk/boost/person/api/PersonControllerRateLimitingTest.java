package com.khdsk.boost.person.api;

import com.khdsk.boost.person.entity.Person;
import com.khdsk.boost.person.service.PersonService;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest({"resilience4j.ratelimiter.configs.default.limitForPeriod=1", "resilience4j.ratelimiter.configs.default.limitRefreshPeriod=10s"})
@AutoConfigureMockMvc
class PersonControllerRateLimitingTest {

    @MockBean
    private PersonService service;

    @Autowired
    private MockMvc mockMvc;

    @RepeatedTest(5)
    void getTest_tooManyRequests(RepetitionInfo repetitionInfo) throws Exception {
        Long id = 1L;

        when(service.get(id)).thenReturn(Optional.of(new Person()));
        when(service.delete(id)).thenReturn(true);
        when(service.create(any())).thenReturn(new Person());
        when(service.update(eq(id), any())).thenReturn(Optional.of(new Person()));

        ResultMatcher matcher = repetitionInfo.getCurrentRepetition() == 1 ? status().isOk() : status().isTooManyRequests();
        mockMvc.perform(get("/v1/persons/" + id))
            .andExpect(matcher);
        mockMvc.perform(delete("/v1/persons/" + id))
            .andExpect(matcher);
        mockMvc.perform(post("/v1/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"firstName\",\"lastName\":\"lastName\"}"))
            .andExpect(repetitionInfo.getCurrentRepetition() == 1 ? status().isCreated() : status().isTooManyRequests());
        mockMvc.perform(put("/v1/persons/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"firstName\",\"lastName\":\"lastName\"}"))
            .andExpect(matcher);
    }
}