package org.theoriok.inventory;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EnableJpaRepositories
abstract class IntegrationTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    private List<JpaRepository> repositories;

    @BeforeEach
    void setUp() {
        repositories.forEach(CrudRepository::deleteAll);
    }
}
