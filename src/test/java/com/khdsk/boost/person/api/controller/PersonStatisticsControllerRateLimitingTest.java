package com.khdsk.boost.person.api.controller;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest({"resilience4j.ratelimiter.configs.default.limitForPeriod=1", "resilience4j.ratelimiter.configs.default.limitRefreshPeriod=10s"})
@AutoConfigureMockMvc
class PersonStatisticsControllerRateLimitingTest {

    @Autowired
    private MockMvc mockMvc;

    @RepeatedTest(5)
    void tooManyRequestsTest(RepetitionInfo repetitionInfo) throws Exception {
        ResultMatcher matcher = repetitionInfo.getCurrentRepetition() == 1 ? status().isOk() : status().isTooManyRequests();

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/persons/statistics/last-name-starting-with/c")).andExpect(matcher);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/persons/statistics/average-age")).andExpect(matcher);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/persons/statistics/missing-ids")).andExpect(matcher);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/persons/statistics/first-name/name")).andExpect(matcher);
    }
}