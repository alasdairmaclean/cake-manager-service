package com.waracle.cakemanager.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waracle.cakemanager.exception.CakeManagerException;
import com.waracle.cakemanager.model.CakeEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Service
@AllArgsConstructor
public class CakeService {

    public static final String CAKES_URL = "https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json";

    private ObjectMapper mapper;

    public List<CakeEntity> getCakes() {
        try {
            return mapper.readValue(new URL(CAKES_URL), new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new CakeManagerException(e);
        }
    }

}
