package com.khdsk.boost.person.service;

import com.khdsk.boost.person.api.model.PersonRequest;
import com.khdsk.boost.person.entity.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    Optional<Person> get(Long id);

    List<Person> getAll();

    Person create(PersonRequest request);

    Optional<Person> update(Long id, PersonRequest request);

    boolean delete(Long id);

}
