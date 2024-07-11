package com.flc.springthymeleaf.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flc.springthymeleaf.domain.ItemNotaFiscal;
import com.flc.springthymeleaf.domain.NotaFiscal;
import com.flc.springthymeleaf.service.NotaFiscalService;

import jakarta.validation.Valid;

@Controller
@RestController
public class NotaFiscalController {

    private final NotaFiscalService notaFiscalService;

    public NotaFiscalController(NotaFiscalService notaFiscalService) {
        this.notaFiscalService = notaFiscalService;
    }



    @PostMapping("/notas-fiscais/salvar")
    public ResponseEntity<String> salvarNotaFiscal(@RequestBody NotaFiscal notaFiscal) {
        notaFiscalService.save(notaFiscal);
        return ResponseEntity.ok("Nota Fiscal salva com sucesso");
    }
    
    
}
    

   
