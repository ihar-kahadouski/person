package com.khdsk.boost.person.service;

import com.khdsk.boost.person.api.mapper.PersonRequestMapper;
import com.khdsk.boost.person.api.model.PersonRequest;
import com.khdsk.boost.person.entity.Person;
import com.khdsk.boost.person.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository repository;
    private final PersonRequestMapper mapper;

    @Override
    public Optional<Person> get(Long id) {
        return ofNullable(id).map(repository::getReferenceById);
    }

    @Override
    public List<Person> getAll() {
        return repository.findAll();
    }

    @Override
    public Person create(PersonRequest request) {
        return repository.save(mapper.map(request));
    }

    @Override
    public Optional<Person> update(Long id, PersonRequest request) {
        return get(id).map(it -> it
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName()))
            .map(repository::save);
    }

    @Override
    public boolean delete(Long id) {
        return get(id).map(it -> {
            repository.delete(it);
            return true;
        }).orElse(false);
    }
}
