package com.waracle.cakemanager.web;

import com.waracle.cakemanager.db.CakeRepository;
import com.waracle.cakemanager.exception.CakeRequestValidationException;
import com.waracle.cakemanager.model.CakeEntity;
import lombok.AllArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CakeRestController {

    private CakeRepository cakeRepository;

    @GetMapping("/cakes")
    public List<CakeEntity> getCakes() {
        return cakeRepository.findAll();
    }

    @PostMapping("/cakes")
    public CakeEntity createCake(@Valid @RequestBody CakeEntity cakeEntity,
                           BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new CakeRequestValidationException(bindingResult.getFieldErrors());
        }
        return cakeRepository.save(cakeEntity);
    }

}
