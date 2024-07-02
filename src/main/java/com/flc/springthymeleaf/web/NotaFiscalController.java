package com.flc.springthymeleaf.web;

import com.flc.springthymeleaf.domain.NotaFiscal;
import com.flc.springthymeleaf.service.NotaFiscalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/notas-fiscais")
public class NotaFiscalController {

    @Autowired
    private NotaFiscalService notaFiscalService;

    @PostMapping("/save")
    public NotaFiscal saveNotaFiscal(@RequestBody NotaFiscal notaFiscal) {
        return notaFiscalService.save(notaFiscal);
    }

    @GetMapping("/por-data-entrada")
    public List<NotaFiscal> findByDataEntrada(@RequestParam("dataEntrada") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataEntrada) {
        return notaFiscalService.findByDataEntrada(dataEntrada);
    }
}
