package com.khdsk.boost.person.service;

import com.khdsk.boost.person.entity.Person;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.OptionalLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.util.Optional.ofNullable;

@Service
public class PersonCountingServiceImpl implements PersonCountingService {

    @Override
    public int countPersonsWithLastNameStartingWith(char letter, Collection<Person> persons) {
        return ofNullable(persons)
            .map(it -> it.stream()
                .filter(Objects::nonNull)
                .map(Person::getLastName)
                .filter(StringUtils::isNotBlank)
                .filter(name -> name.startsWith(String.valueOf(letter)))
                .map(name -> 1)
                .reduce(0, Integer::sum)
            ).orElse(0);
    }

    @Override
    public double countAverageAge(Collection<Person> persons) {
        return ofNullable(persons)
            .map(it -> BigDecimal.valueOf(extractAges(it)
                    .average()
                    .orElse(0d))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue()
            ).orElse(0d);
    }

    @Override
    public List<Long> findMissingIds(Collection<Person> persons) {
        return ofNullable(persons)
            .map(this::doFindMissingIds)
            .orElse(Collections.emptyList());
    }

    @Override
    public int countPersonsWithFirstName(String name, Collection<Person> persons) {
        return ofNullable(persons).map(it -> persons.stream()
                .filter(Objects::nonNull)
                .filter(person -> Objects.nonNull(person.getFirstName()))
                .filter(person -> person.getFirstName().equals(name))
                .mapToInt(person -> 1)
                .sum())
            .orElse(0);
    }

    private List<Long> doFindMissingIds(Collection<Person> it) {
        OptionalLong min = extractIds(it).min();
        OptionalLong max = extractIds(it).max();
        if (min.isPresent() && max.isPresent()) {
            List<Long> range = LongStream.rangeClosed(min.getAsLong(), max.getAsLong()).boxed().collect(Collectors.toList());
            range.removeAll(extractIds(it).boxed().toList());
            return range;
        }
        return Collections.emptyList();
    }

    private IntStream extractAges(Collection<Person> persons) {
        return persons.stream()
            .filter(Objects::nonNull)
            .filter(person -> Objects.nonNull(person.getAge()))
            .mapToInt(Person::getAge);
    }

    private LongStream extractIds(Collection<Person> persons) {
        return persons.stream()
            .filter(Objects::nonNull)
            .filter(person -> Objects.nonNull(person.getId()))
            .mapToLong(Person::getId);
    }

}
