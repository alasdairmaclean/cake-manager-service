package com.waracle.cakemanager.web;

import com.waracle.cakemanager.db.CakeRepository;
import com.waracle.cakemanager.exception.CakeRequestValidationException;
import com.waracle.cakemanager.model.CakeEntity;
import lombok.AllArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
    public void createCake(@Valid @RequestBody CakeEntity cakeEntity,
                           BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new CakeRequestValidationException(bindingResult.getFieldErrors());
        }
        cakeRepository.save(cakeEntity);
    }

}
