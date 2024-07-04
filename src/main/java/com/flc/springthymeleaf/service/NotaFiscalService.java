package com.flc.springthymeleaf.service;

import com.flc.springthymeleaf.domain.NotaFiscal;
import com.flc.springthymeleaf.repository.NotaFiscalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotaFiscalService {

    @Autowired
    private NotaFiscalRepository notaFiscalRepository;

    public void save(NotaFiscal notaFiscal) {
        notaFiscalRepository.save(notaFiscal);
    }

    public List<NotaFiscal> findAll() {
        return notaFiscalRepository.findAll();
    }

    public boolean existsByChaveAcesso(String chaveAcesso) {
        return notaFiscalRepository.existsByChaveAcesso(chaveAcesso);
    }
}
