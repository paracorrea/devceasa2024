package com.flc.springthymeleaf.service;

import com.flc.springthymeleaf.domain.Mercado;
import com.flc.springthymeleaf.repository.MercadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MercadoService {

    @Autowired
    private MercadoRepository mercadoRepository;

    public Mercado findById(Integer id) {
        return mercadoRepository.findById(id).orElse(null);
    }

	public Object findAll() {
		// TODO Auto-generated method stub
		return mercadoRepository.findAll();
	}

    // Adicione outros métodos conforme necessário
}

