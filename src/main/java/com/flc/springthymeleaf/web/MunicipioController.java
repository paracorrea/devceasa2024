package com.flc.springthymeleaf.web;

import com.flc.springthymeleaf.domain.Municipio;
import com.flc.springthymeleaf.service.MunicipioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MunicipioController {

    private final MunicipioService municipioService;

    public MunicipioController(MunicipioService municipioService) {
        this.municipioService = municipioService;
    }

    @GetMapping("/searchMunicipio")
    public List<Municipio> searchMunicipio(
            @RequestParam(required = false, defaultValue = "") String nome,
            @RequestParam(required = false, defaultValue = "") String uf,
            @RequestParam(required = false, defaultValue = "") String codigo) {
        
        return municipioService.searchMunicipios(nome, uf, codigo);
  
}
}

