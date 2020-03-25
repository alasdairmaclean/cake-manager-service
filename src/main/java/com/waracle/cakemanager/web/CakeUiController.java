package com.waracle.cakemanager.web;

import com.waracle.cakemanager.db.CakeRepository;
import com.waracle.cakemanager.model.CakeEntity;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class CakeUiController {

    private CakeRepository cakeRepository;

    @GetMapping("/")
    public String getCakes(Model model) {
        model.addAttribute("newCake", new CakeEntity());
        model.addAttribute("cakes", cakeRepository.findAll());
        return "cakes-list";
    }

    @PostMapping("/")
    public String createCake(@ModelAttribute CakeEntity cakeEntity, Model model) {
        try {
            cakeRepository.save(cakeEntity);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", "Could not add cake: title must be unique.");
        }
        model.addAttribute("newCake", new CakeEntity());
        model.addAttribute("cakes", cakeRepository.findAll());
        return "cakes-list";
    }

}
