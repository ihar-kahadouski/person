package com.khdsk.boost.person.api.mapper;

import com.khdsk.boost.person.api.model.PersonRequest;
import com.khdsk.boost.person.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonRequestMapper {

    @Mapping(target = "id", ignore = true)
    Person map(PersonRequest request);
}
