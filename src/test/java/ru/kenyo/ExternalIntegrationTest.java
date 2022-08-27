package ru.kenyo;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.kenyo.dto.LoginDTO;
import ru.kenyo.dto.UpdateFilenameDTO;
import ru.kenyo.dto.WebTokenDTO;
import ru.kenyo.entity.FileEntity;

import java.nio.file.Path;
import java.util.Objects;

@ExtendWith(SpringExtension.class)
@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExternalIntegrationTest {
    private static final String CONTAINER_NAME = "keny0cell/cloud:back";
    private static final int PORT = 8080;
    private static final String URI = "http://127.0.0.1:%d";
    private static final String INITIAL_NAME = "test.txt";
    private static final String UPDATED_NAME = "blah.yaml";

    private final ObjectMapper mapper = new ObjectMapper();
    private final GenericContainer<?> container = new GenericContainer<>(CONTAINER_NAME)
            .withExposedPorts(PORT);

    private String token = null;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    private void startUp() throws JsonProcessingException {
        container.start();
        performLogin();
    }

    @AfterAll
    private void stopContainer() {
        container.stop();
    }

    private HttpHeaders initHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", "Bearer %s".formatted(token));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private String getUrl(String path) {
        return URI.formatted(container.getMappedPort(PORT)) + path;
    }

    private void performLogin() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String authSerialized = mapper.writeValueAsString(new LoginDTO("ADMIN", "ADMIN"));

        final ResponseEntity<WebTokenDTO> response = restTemplate
                .postForEntity(getUrl("/login"), new HttpEntity<>(authSerialized, headers), WebTokenDTO.class);

        token = Objects.requireNonNull(response.getBody()).token();
    }

    @Test
    @Order(1)
    public void successPostOperationTest() {
        HttpHeaders headers = initHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE);

        LinkedMultiValueMap<String, Object> multipart = new LinkedMultiValueMap<>();
        multipart.add("file", new FileSystemResource(Path.of("src", "test", "resources", "test.txt")));

        final ResponseEntity<Void> response = restTemplate
                .postForEntity(
                        getUrl("/file?filename=" + INITIAL_NAME),
                        new HttpEntity<>(multipart, headers),
                        Void.class
                );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(2)
    public void successPutOperationTest() throws JsonProcessingException {
        String filenameSerialized = mapper.writeValueAsString(new UpdateFilenameDTO(UPDATED_NAME));

        final ResponseEntity<Void> response = restTemplate
                .exchange(
                        getUrl("/file?filename=" + INITIAL_NAME),
                        HttpMethod.PUT,
                        new HttpEntity<>(filenameSerialized, initHeaders()),
                        Void.class
                );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(3)
    public void successGetOperationTest() {
        final ResponseEntity<FileEntity> response = restTemplate
                .exchange(
                        getUrl("/file?filename=" + UPDATED_NAME),
                        HttpMethod.GET,
                        new HttpEntity<>(initHeaders()),
                        FileEntity.class
                );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    @Order(4)
    public void successDeleteOperationTest() {
        final ResponseEntity<Void> response = restTemplate
                .exchange(
                        getUrl("/file?filename=" + UPDATED_NAME),
                        HttpMethod.DELETE,
                        new HttpEntity<>(initHeaders()),
                        Void.class
                );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
