package com.waracle.cakemanager.web;

import com.waracle.cakemanager.db.CakeRepository;
import com.waracle.cakemanager.model.CakeEntity;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class CakeUiController {

    private CakeRepository cakeRepository;

    @GetMapping("/")
    public String getCakes(Model model) {
        return buildResponse(model);
    }

    @PostMapping("/")
    public String createCake(@Valid @ModelAttribute CakeEntity cakeEntity, BindingResult bindingResult, Model model) {
        if(bindingResult.hasFieldErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            model.addAttribute("errorMessage", String.format("Could not add cake: %s %s", fieldError.getField(), fieldError.getDefaultMessage()));
            return buildResponse(model);
        }
        try {
            cakeRepository.save(cakeEntity);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", "Could not add cake: title must be unique.");
        }
        return buildResponse(model);
    }

    private String buildResponse(Model model) {
        model.addAttribute("newCake", new CakeEntity());
        model.addAttribute("cakes", cakeRepository.findAll());
        return "cakes-list";
    }

}
