package com.khdsk.boost.person.api.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PersonRequest {

    @NotBlank
    @Size(min = 1)
    private String firstName;

    @NotBlank
    @Size(min = 1)
    private String lastName;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer age;
}
