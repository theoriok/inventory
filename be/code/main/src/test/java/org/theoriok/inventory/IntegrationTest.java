package org.theoriok.inventory;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.theoriok.inventory.persistence.entities.BookEntity;
import org.theoriok.inventory.persistence.entities.CapEntity;
import org.theoriok.inventory.persistence.entities.CountryEntity;

@SpringBootTest
@ActiveProfiles("test")
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
abstract class IntegrationTest {

    @ServiceConnection
    static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:%s-alpine".formatted(System.getProperty("postgresVersion")));

    static {
        POSTGRES.start();
    }

    @Autowired
    MockMvc mvc;

    @Autowired
    JdbcAggregateTemplate jdbcAggregateTemplate;

    @BeforeEach
    void setUp() {
        jdbcAggregateTemplate.deleteAll(CapEntity.class);
        jdbcAggregateTemplate.deleteAll(BookEntity.class);
        jdbcAggregateTemplate.deleteAll(CountryEntity.class);
    }
}
