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
import org.springframework.http.HttpEntity;
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
        ResponseEntity<List<RestError>> createResponse = createCake(lemonCheesecake());
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
        ResponseEntity<List<RestError>> duplicateCreateResponse = createCake(lemonCheesecake());
        assertEquals(HttpStatus.BAD_REQUEST, duplicateCreateResponse.getStatusCode());
        assertEquals(DUPLICATE_CAKE_ERROR_MESSAGE, duplicateCreateResponse.getBody().get(0).getMessage());
    }

    @Test
    public void createCakeWithNullTitleReturnsError() {
        CakeEntity cake = lemonCheesecake().toBuilder()
                .title(null)
                .build();
        ResponseEntity<List<RestError>> response = createCake(cake);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("title", response.getBody().get(0).getField());
        assertEquals("must not be null", response.getBody().get(0).getMessage());
    }

    @Test
    public void createCakeWithTooLongTitleReturnsError() {
        CakeEntity cake = lemonCheesecake().toBuilder()
                .title("X".repeat(CakeEntity.MAX_TITLE_LENGTH + 1))
                .build();
        ResponseEntity<List<RestError>> response = createCake(cake);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("title", response.getBody().get(0).getField());
        assertEquals("length must be between 0 and 100", response.getBody().get(0).getMessage());
    }

    @Test
    public void createCakeWithNullDescriptionReturnsError() {
        CakeEntity cake = lemonCheesecake().toBuilder()
                .description(null)
                .build();
        ResponseEntity<List<RestError>> response = createCake(cake);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("description", response.getBody().get(0).getField());
        assertEquals("must not be null", response.getBody().get(0).getMessage());
    }

    @Test
    public void createCakeWithTooLongDescriptionReturnsError() {
        CakeEntity cake = lemonCheesecake().toBuilder()
                .description("X".repeat(CakeEntity.MAX_DESCRIPTION_LENGTH + 1))
                .build();
        ResponseEntity<List<RestError>> response = createCake(cake);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("description", response.getBody().get(0).getField());
        assertEquals("length must be between 0 and 100", response.getBody().get(0).getMessage());
    }

    @Test
    public void createCakeWithNullImageReturnsError() {
        CakeEntity cake = lemonCheesecake().toBuilder()
                .image(null)
                .build();
        ResponseEntity<List<RestError>> response = createCake(cake);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("image", response.getBody().get(0).getField());
        assertEquals("must not be null", response.getBody().get(0).getMessage());
    }

    @Test
    public void createCakeWithTooLongImageReturnsError() {
        CakeEntity cake = lemonCheesecake().toBuilder()
                .image("http://ab/"+"X".repeat(291)) // total 301 chars
                .build();
        ResponseEntity<List<RestError>> response = createCake(cake);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("image", response.getBody().get(0).getField());
        assertEquals("length must be between 0 and 300", response.getBody().get(0).getMessage());
    }

    @Test
    public void createCakeWithInvalidUrleturnsError() {
        CakeEntity cake = lemonCheesecake().toBuilder()
                .image("not-a-url")
                .build();
        ResponseEntity<List<RestError>> response = createCake(cake);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("image", response.getBody().get(0).getField());
        assertEquals("must be a valid URL", response.getBody().get(0).getMessage());
    }

    private ResponseEntity<List<RestError>> createCake(CakeEntity cakeEntity) {
        return testRestTemplate.exchange(baseUrl,
                HttpMethod.POST, new HttpEntity(cakeEntity), new ParameterizedTypeReference<>() {
                });
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
