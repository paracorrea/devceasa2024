package com.flc.springthymeleaf.service;

import com.flc.springthymeleaf.domain.NotaFiscal;
import com.flc.springthymeleaf.repository.NotaFiscalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.List;

@Service
public class NotaFiscalService {

    @Autowired
    private NotaFiscalRepository notaFiscalRepository;

    public NotaFiscal save(NotaFiscal notaFiscal) {
        //notaFiscal.setDataEntrada(LocalDate.now());
        return notaFiscalRepository.save(notaFiscal);
    }

   // public List<NotaFiscal> findByDataEntrada(LocalDate dataEntrada) {
    //    return notaFiscalRepository.findByDataEntrada(dataEntrada);
   // }
}
