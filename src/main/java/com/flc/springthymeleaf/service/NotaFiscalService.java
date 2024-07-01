package com.flc.springthymeleaf.service;

import org.springframework.stereotype.Service;
import com.flc.springthymeleaf.domain.Municipio;
import com.flc.springthymeleaf.domain.Mercado;
import com.flc.springthymeleaf.repository.MunicipioRepository;
import com.flc.springthymeleaf.repository.MercadoRepository;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class NotaFiscalService {
    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private MercadoRepository mercadoRepository;

    public void saveMunicipio(Municipio municipio) {
        municipioRepository.save(municipio);
    }

    public void saveMercado(Mercado mercado) {
        mercadoRepository.save(mercado);
    }
}