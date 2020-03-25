package com.waracle.cakemanager.web;

import com.waracle.cakemanager.db.CakeRepository;
import com.waracle.cakemanager.model.CakeEntity;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class CakeController {

    private CakeRepository cakeRepository;

    @GetMapping("/cakes")
    public List<CakeEntity> getCakes() {
        return cakeRepository.findAll();
    }

    @PostMapping("/cakes")
    public void createCake(@RequestBody CakeEntity cakeEntity) {
        cakeRepository.save(cakeEntity);
    }

}
