package com.waracle.cakemanager.web;

import com.waracle.cakemanager.model.CakeEntity;
import com.waracle.cakemanager.service.CakeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class CakeController {

    private CakeService cakeService;

    @RequestMapping("/cakes")
    @ResponseBody
    public List<CakeEntity> getCakes() {
        return cakeService.getCakes();
    }

}
