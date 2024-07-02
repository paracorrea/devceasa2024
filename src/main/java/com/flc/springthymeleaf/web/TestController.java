package com.flc.springthymeleaf.web;

import com.flc.springthymeleaf.domain.Mercado;
import com.flc.springthymeleaf.domain.Municipio;
import com.flc.springthymeleaf.domain.NotaFiscal;
import com.flc.springthymeleaf.domain.Permissionario;
import com.flc.springthymeleaf.repository.MercadoRepository;
import com.flc.springthymeleaf.repository.MunicipioRepository;
import com.flc.springthymeleaf.repository.NotaFiscalRepository;
import com.flc.springthymeleaf.repository.PermissionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class TestController {

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private MercadoRepository mercadoRepository;

    @Autowired
    private NotaFiscalRepository notaFiscalRepository;

    @Autowired
    private PermissionarioRepository permissionarioRepository;

    @GetMapping("/test/relationships")
    public String testRelationships() {
        // Cria e salva um Município
        Municipio municipio = new Municipio();
        municipio.setIbge("12345");
        municipio.setCodigo("98765");
        municipio.setUf("SP");
        municipio.setNome("Campinas");
        municipioRepository.save(municipio);

        // Cria e salva um Mercado
        Mercado mercado = new Mercado();
        
        mercado.setCodigo(999);
        mercado.setNome("Mercado Municipal de Teste");
        mercado.setUf("SP"); 
        mercadoRepository.save(mercado);

        // Cria e salva um Permissionário
        Permissionario permissionario = new Permissionario();
        permissionario.setCnpj("12345678000190");
        permissionario.setNome("Permissionario Teste");
        permissionarioRepository.save(permissionario);

        // Cria e salva uma Nota Fiscal
        NotaFiscal notaFiscal = new NotaFiscal();
        notaFiscal.setChaveAcesso("12345678901234567890123456789012345678901234");
        notaFiscal.setNumero("1234");
        notaFiscal.setSerie("1");
        notaFiscal.setTipo("Tipo Teste");
        notaFiscal.setMunicipio(municipio);
        notaFiscal.setMercado(mercado);
        notaFiscal.setPermissionario(permissionario);
        notaFiscal.setDataEmissao(LocalDate.now());
        notaFiscalRepository.save(notaFiscal);

        return "Test executed successfully!";
    }
}
