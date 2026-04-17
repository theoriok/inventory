package org.theoriok.inventory;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.theoriok.inventory.client.ApiClient;
import org.theoriok.inventory.client.ApiException;
import org.theoriok.inventory.client.JSON;
import org.theoriok.inventory.client.api.BookControllerApi;
import org.theoriok.inventory.client.model.Problem;

import java.io.IOException;
import java.io.UncheckedIOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class ContractTest extends IntegrationTest {

    private static final JSON JSON_MAPPER = new JSON();

    @LocalServerPort
    int port;

    BookControllerApi bookApi;

    @BeforeEach
    void setUpApis() {
        var client = new ApiClient();
        client.updateBaseUri("http://localhost:" + port);
        bookApi = new BookControllerApi(client);
    }

    Problem parseProblem(ApiException exception) {
        try {
            return JSON_MAPPER.getMapper().readValue(exception.getResponseBody(), Problem.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
