package com.waracle.cakemanager;

import com.waracle.cakemanager.model.CakeEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getCakes_returnsCakes() {
        ResponseEntity<List<CakeEntity>> cakesResponse =
                restTemplate.exchange(String.format("http://localhost:%s/cakes", port),
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        assertEquals(HttpStatus.OK, cakesResponse.getStatusCode());
        List<CakeEntity> cakes = cakesResponse.getBody();
        assertEquals(20, cakes.size());

        Optional<CakeEntity> lemonCheesecake = cakes.stream()
                .filter(c -> "Lemon cheesecake".equals(c.getTitle()))
                .findFirst();
        assertTrue(lemonCheesecake.isPresent());
        assertEquals(lemonCheesecake(), lemonCheesecake.get());
    }

    private CakeEntity lemonCheesecake() {
        CakeEntity expected = new CakeEntity();
        expected.setTitle("Lemon cheesecake");
        expected.setDescription("A cheesecake made of lemon");
        expected.setImage("https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg");
        return expected;
    }

}
