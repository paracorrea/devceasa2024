/*
 * package com.flc.springthymeleaf.web;
 * 
 * import org.springframework.http.ResponseEntity; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestBody; import
 * org.springframework.web.bind.annotation.RestController;
 * 
 * import com.flc.springthymeleaf.domain.NotaFiscal; import
 * com.flc.springthymeleaf.service.NotaFiscalService;
 * 
 * @Controller
 * 
 * @RestController public class NotaFiscalController {
 * 
 * private final NotaFiscalService notaFiscalService;
 * 
 * public NotaFiscalController(NotaFiscalService notaFiscalService) {
 * this.notaFiscalService = notaFiscalService; }
 * 
 * 
 * 
 * @PostMapping("/notas-fiscais/salvar") public ResponseEntity<String>
 * salvarNotaFiscal(@RequestBody NotaFiscal notaFiscal) {
 * notaFiscalService.save(notaFiscal); return
 * ResponseEntity.ok("Nota Fiscal salva com sucesso"); }
 * 
 * 
 * }
 * 
 * 
 * 
 */