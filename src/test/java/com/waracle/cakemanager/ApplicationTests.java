package com.waracle.cakemanager;

import com.waracle.cakemanager.db.CakeRepository;
import com.waracle.cakemanager.model.CakeEntity;
import com.waracle.cakemanager.web.RestError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static com.waracle.cakemanager.web.GlobalExceptionHandler.DUPLICATE_CAKE_ERROR_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Profile("test")
class ApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
    private String baseUrl;

    @Autowired
    private CakeRepository cakeRepository;

    @BeforeEach
    public void beforeEach() {
        baseUrl = String.format("http://localhost:%s/cakes", port);
        cakeRepository.deleteAll();
    }

    @Test
    public void saveAndGetCake() {
        ResponseEntity<RestError> createResponse = createCake(lemonCheesecake());
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());

        ResponseEntity<List<CakeEntity>> cakesResponse = getCakes();
        assertEquals(HttpStatus.OK, cakesResponse.getStatusCode());
        List<CakeEntity> cakes = cakesResponse.getBody();
        assertEquals(1, cakes.size());

        CakeEntity actualLemonCheesecake = cakes.get(0);
        assertCakeAttributes(lemonCheesecake(), actualLemonCheesecake);
    }

    @Test
    public void saveAndGetMultipleCakes() {
        createCake(lemonCheesecake());
        createCake(victoriaSponge());

        ResponseEntity<List<CakeEntity>> cakesResponse = getCakes();
        assertEquals(HttpStatus.OK, cakesResponse.getStatusCode());
        List<CakeEntity> cakes = cakesResponse.getBody();
        assertEquals(2, cakes.size());

        assertContains(cakes, lemonCheesecake());
        assertContains(cakes, victoriaSponge());
    }

    @Test
    public void createDuplicateCakeReturnsError() {
        createCake(lemonCheesecake());
        ResponseEntity<RestError> duplicateCreateResponse = createCake(lemonCheesecake());
        assertEquals(HttpStatus.BAD_REQUEST, duplicateCreateResponse.getStatusCode());
        assertEquals(DUPLICATE_CAKE_ERROR_MESSAGE, duplicateCreateResponse.getBody().getMessage());

    }

    private ResponseEntity<RestError> createCake(CakeEntity cakeEntity) {
        return testRestTemplate.postForEntity(baseUrl, cakeEntity, RestError.class);
    }

    private ResponseEntity<List<CakeEntity>> getCakes() {
        return testRestTemplate.exchange(baseUrl,
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
    }

    private void assertContains(List<CakeEntity> cakes, CakeEntity expected) {
        Optional<CakeEntity> actualOpt = cakes.stream()
                .filter(c -> expected.getTitle().equals(c.getTitle()))
                .findFirst();
        assertTrue(actualOpt.isPresent());
        CakeEntity actual = actualOpt.get();
        assertCakeAttributes(expected, actual);
    }

    private void assertCakeAttributes(CakeEntity expected, CakeEntity actual) {
        assertNotNull(actual.getCakeId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getImage(), actual.getImage());
    }

    private CakeEntity lemonCheesecake() {
        CakeEntity expected = new CakeEntity();
        expected.setTitle("Lemon cheesecake");
        expected.setDescription("A cheesecake made of lemon");
        expected.setImage("https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg");
        return expected;
    }

    private CakeEntity victoriaSponge() {
        CakeEntity expected = new CakeEntity();
        expected.setTitle("victoria sponge");
        expected.setDescription("sponge with jam");
        expected.setImage("http://www.bbcgoodfood.com/sites/bbcgoodfood.com/files/recipe_images/recipe-image-legacy-id--1001468_10.jpg");
        return expected;
    }

}
