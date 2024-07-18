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

    @GetMapping("/api/municipios")
    public List<Municipio> searchMunicipios(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String ibge,
            @RequestParam(required = false) String uf) {
        return municipioService.searchMunicipios(nome, ibge, uf);
    }
  
}


