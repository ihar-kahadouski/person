package com.khdsk.boost.person.repository;

import com.khdsk.boost.person.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
