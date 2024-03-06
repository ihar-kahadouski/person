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

import static net.andreinc.mockneat.unit.objects.Filler.filler;
import static net.andreinc.mockneat.unit.types.Ints.ints;
import static net.andreinc.mockneat.unit.types.Longs.longs;
import static net.andreinc.mockneat.unit.user.Names.names;
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
        when(service.get(TestData.id)).thenReturn(Optional.of(TestData.person));
        when(service.delete(TestData.id)).thenReturn(true);
        when(service.create(any())).thenReturn(TestData.person);
        when(service.update(eq(TestData.id), any())).thenReturn(Optional.of(TestData.person));

        ResultMatcher matcher = repetitionInfo.getCurrentRepetition() == 1 ? status().isOk() : status().isTooManyRequests();
        mockMvc.perform(get(TestData.pathById)).andExpect(matcher);
        mockMvc.perform(delete(TestData.pathById)).andExpect(matcher);
        mockMvc.perform(post(TestData.basePath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestData.request))
            .andExpect(repetitionInfo.getCurrentRepetition() == 1 ? status().isCreated() : status().isTooManyRequests());
        mockMvc.perform(put(TestData.pathById)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestData.request))
            .andExpect(matcher);
    }

    private interface TestData {
        Long id = longs().lowerBound(1).get();
        String basePath = "/v1/persons";
        String pathById = basePath + "/" + id;
        Person person = filler(Person::new)
            .setter(Person::setId, () -> () -> id)
            .setter(Person::setFirstName, names())
            .setter(Person::setLastName, names())
            .setter(Person::setAge, ints().range(1, 100))
            .get();
        String request = "{\"firstName\":\"firstName\",\"lastName\":\"lastName\",\"age\":\"20\"}";
    }
}