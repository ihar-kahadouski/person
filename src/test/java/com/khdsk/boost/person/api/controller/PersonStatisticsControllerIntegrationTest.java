package com.khdsk.boost.person.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PersonStatisticsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql("/sql/last-name-starting-with-c-init.sql")
    @Sql(value = "/sql/clear-person-table.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void countPersonsWithLastNameStartingWithTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/persons/statistics/last-name-starting-with/c"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(1));
    }

    @Test
    @Sql("/sql/average-age-init.sql")
    @Sql(value = "/sql/clear-person-table.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void countAverageAgeTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/persons/statistics/average-age"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(20.0));
    }

    @Test
    @Sql("/sql/find-missing-ids-init.sql")
    @Sql(value = "/sql/clear-person-table.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findMissingIdsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/persons/statistics/missing-ids"))
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$").isArray(),
                jsonPath("$.length()").value(4),
                jsonPath("$[0]").value(2),
                jsonPath("$[1]").value(4),
                jsonPath("$[2]").value(5),
                jsonPath("$[3]").value(6));
    }

    @Test
    @Sql("/sql/count-first-name.sql")
    @Sql(value = "/sql/clear-person-table.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void countPersonsWithFirstNameTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/persons/statistics/first-name/name"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(2));
    }

}