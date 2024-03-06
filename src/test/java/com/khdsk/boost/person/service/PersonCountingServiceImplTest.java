package com.khdsk.boost.person.service;

import com.khdsk.boost.person.entity.Person;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonCountingServiceImplTest {

    private final PersonCountingService service = new PersonCountingServiceImpl();

    @ParameterizedTest
    @MethodSource
    void countPersonsWithLastNameStartingWithTest(int expected, char letter, Collection<Person> persons) {
        assertEquals(expected, service.countPersonsWithLastNameStartingWith(letter, persons));
    }

    private static Stream<Arguments> countPersonsWithLastNameStartingWithTest() {
        return Stream.of(
            Arguments.of(0, 'c', null),
            Arguments.of(0, 'c', Collections.emptyList()),
            Arguments.of(2, 'c', List.of(
                new Person().setLastName("c"),
                new Person(), new Person().setLastName("ccc"),
                new Person().setLastName("C"),
                new Person().setLastName("acc"))),
            Arguments.of(2, 'a', List.of(
                new Person().setLastName(null),
                new Person().setLastName("b"),
                new Person().setLastName(""),
                new Person().setLastName("Aaaa"),
                new Person().setLastName("aqwerty"),
                new Person().setLastName("a name")
            )));
    }

    @ParameterizedTest
    @MethodSource
    void countAverageAgeTest(double expected, Collection<Person> persons) {
        assertEquals(expected, service.countAverageAge(persons));
    }

    private static Stream<Arguments> countAverageAgeTest() {
        return Stream.of(
            Arguments.of(0d, null),
            Arguments.of(0d, Collections.emptyList()),
            Arguments.of(0d, List.of(new Person().setAge(null))),
            Arguments.of(10d, List.of(
                new Person().setAge(10),
                new Person().setAge(null),
                new Person().setAge(10))),
            Arguments.of(20d, List.of(
                new Person().setAge(null),
                new Person().setAge(10),
                new Person().setAge(30),
                new Person().setAge(20)))
        );
    }

    @ParameterizedTest
    @MethodSource
    void findMissingIdsTest(List<Long> expected, Collection<Person> persons) {
        assertThat(service.findMissingIds(persons)).containsExactlyElementsOf(expected);
    }

    private static Stream<Arguments> findMissingIdsTest() {
        return Stream.of(
            Arguments.of(Collections.emptyList(), null),
            Arguments.of(Collections.emptyList(), Collections.emptyList()),
            Arguments.of(Collections.emptyList(), List.of(new Person().setId(null))),
            Arguments.of(
                List.of(2L, 3L, 4L),
                List.of(
                    new Person().setId(1L),
                    new Person().setId(5L)
                )),
            Arguments.of(
                List.of(2L, 3L, 4L, 6L, 8L, 9L),
                List.of(
                    new Person().setId(1L),
                    new Person().setId(5L),
                    new Person().setId(7L),
                    new Person().setId(10L)
                )
            )
        );
    }

    @ParameterizedTest
    @MethodSource
    void countPersonsWithSameNameTest(int expected, Collection<Person> persons, String name) {
        assertEquals(expected, service.countPersonsWithFirstName(name, persons));
    }

    public static Stream<Arguments> countPersonsWithSameNameTest() {
        return Stream.of(
            Arguments.of(0, null, null),
            Arguments.of(0, Collections.emptyList(), null),
            Arguments.of(0, Collections.emptyList(), ""),
            Arguments.of(1, List.of(new Person(), new Person().setFirstName("aaa"), new Person().setFirstName("bbb")), "aaa"),
            Arguments.of(2, List.of(new Person(), new Person().setFirstName("aaa"), new Person().setFirstName("aaa")), "aaa"),
            Arguments.of(0, List.of(new Person(), new Person().setFirstName("aaa"), new Person().setFirstName("aaa")), "bbb")
        );
    }

}