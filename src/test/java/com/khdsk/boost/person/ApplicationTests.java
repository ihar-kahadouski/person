package com.khdsk.boost.person;

import com.khdsk.boost.person.api.controller.PersonController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private PersonController personController;

    @Test
    void contextLoads() {
        assertNotNull(personController);
    }

}
