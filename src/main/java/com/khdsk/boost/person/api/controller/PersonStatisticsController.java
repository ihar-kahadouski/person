package com.khdsk.boost.person.api.controller;

import com.khdsk.boost.person.service.PersonCountingService;
import com.khdsk.boost.person.service.PersonService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/persons/statistics")
@RequiredArgsConstructor
@Tag(name = "Person statistics", description = "the person statistics api")
public class PersonStatisticsController {

    private final PersonService personService;
    private final PersonCountingService countingService;

    @RateLimiter(name = "lastNameStartingWith")
    @Operation(summary = "Get number of persons with last name starting with provided letter")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count of persons", content = @Content(mediaType = "application/json")),
    })
    @GetMapping("/last-name-starting-with/{letter}")
    public Integer countPersonsWithLastNameStartingWith(@PathVariable Character letter) {
        return countingService.countPersonsWithLastNameStartingWith(letter, personService.getAll());
    }

    @RateLimiter(name = "averageAge")
    @Operation(summary = "Get average age of persons")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Average age of persons", content = @Content(mediaType = "application/json")),
    })
    @GetMapping("/average-age")
    public Double countAverageAge() {
        return countingService.countAverageAge(personService.getAll());
    }

    @RateLimiter(name = "missingIds")
    @Operation(summary = "Get missing ids of persons")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Array of missing ids", content = @Content(mediaType = "application/json")),
    })
    @GetMapping("/missing-ids")
    public List<Long> findMissingIds() {
        return countingService.findMissingIds(personService.getAll());
    }

    @RateLimiter(name = "countFirstNames")
    @Operation(summary = "Get number of persons that have provided name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count of persons", content = @Content(mediaType = "application/json")),
    })
    @GetMapping("/first-name/{name}")
    public Integer countPersonsWithFirstName(@PathVariable String name) {
        return countingService.countPersonsWithFirstName(name, personService.getAll());
    }

}
