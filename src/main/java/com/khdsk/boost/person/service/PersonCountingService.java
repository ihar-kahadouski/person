package com.khdsk.boost.person.service;

import com.khdsk.boost.person.entity.Person;

import java.util.Collection;
import java.util.List;

public interface PersonCountingService {

    int countPersonsWithLastNameStartingWith(char letter, Collection<Person> persons);

    double countAverageAge(Collection<Person> persons);

    List<Long> findMissingIds(Collection<Person> persons);

    int countPersonsWithFirstName(String name, Collection<Person> persons);
}
