package org.theoriok.inventory;

import static java.util.Comparator.comparing;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
@EnableJpaRepositories
abstract class IntegrationTest {

    @ServiceConnection
    static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:18.2-alpine");

    static {
        POSTGRES.start();
    }

    @Autowired
    MockMvc mvc;

    @Autowired
    private List<JpaRepository<?, ?>> repositories;

    @BeforeEach
    void setUp() {
        repositories.stream()
            .sorted(comparing(jpaRepository -> jpaRepository.getClass().getSimpleName()))
            .forEach(CrudRepository::deleteAll);
    }
}
