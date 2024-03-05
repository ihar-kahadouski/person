package com.khdsk.boost.person.api.mapper;

import com.khdsk.boost.person.api.model.PersonResponse;
import com.khdsk.boost.person.entity.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonResponseMapper {

    PersonResponse map(Person entity);

}
