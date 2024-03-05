package com.khdsk.boost.person.api;

import com.khdsk.boost.person.api.mapper.PersonResponseMapper;
import com.khdsk.boost.person.api.model.PersonRequest;
import com.khdsk.boost.person.api.model.PersonResponse;
import com.khdsk.boost.person.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.khdsk.boost.person.api.util.Messages.NOT_FOUND_MSG;
import static com.khdsk.boost.person.api.util.Messages.SUCCESSFUL_DELETE_MSG;
import static com.khdsk.boost.person.api.util.Messages.buildMessage;

@RestController
@RequestMapping("/v1/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService service;
    private final PersonResponseMapper mapper;

    @Operation(summary = "Get a person by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the person", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Person not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> get(@Parameter(description = "id of person") @PathVariable Long id) {
        return service.get(id)
            .map(mapper::map)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create person")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Person created", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    })
    @PostMapping
    public ResponseEntity<PersonResponse> create(@RequestBody @Validated @NotNull PersonRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(service.create(request)));
    }

    @Operation(summary = "Update a person by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Person updated", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
        @ApiResponse(responseCode = "404", description = "Person not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PersonResponse> update(@Parameter(description = "id of person") @PathVariable Long id,
                                                 @RequestBody @Validated @NotNull PersonRequest request) {
        return service.update(id, request)
            .map(mapper::map).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a person by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Person deleted", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Person not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@Parameter(description = "id of person") @PathVariable Long id) {
        return service.delete(id)
            ? ResponseEntity.ok(buildMessage(SUCCESSFUL_DELETE_MSG, id))
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildMessage(NOT_FOUND_MSG, id));
    }
}
