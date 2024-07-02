package com.flc.springthymeleaf;

import com.flc.springthymeleaf.domain.*;
import com.flc.springthymeleaf.repository.*;
import com.flc.springthymeleaf.service.MunicipioService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(SpringRunner.class)
@SpringBootTest
//@ActiveProfiles("test")
@Transactional
public class NotaFiscalIntegrationTest {

    @Autowired
    private NotaFiscalRepository notaFiscalRepository;

    @Autowired
    private MunicipioService municipioService;

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private MercadoRepository mercadoRepository;

    @Autowired
    private PermissionarioRepository permissionarioRepository;

    @Autowired
    private ItemNotaFiscalRepository itemNotaFiscalRepository;

    @Test
    public void testCreateNotaFiscal() {
        Municipio municipio = new Municipio();
        municipio.setNome("Teste");
        municipio.setCodigo("01Teste");
        municipio.setIbge("9999");
        municipio.setUf("SP");
        
        municipioService.save(municipio);
        
        Municipio found = municipioRepository.findById("1234567").orElse(null);
        assert found != null;
        assert "São Paulo".equals(found.getNome());

        Mercado mercado = new Mercado();
        mercado.setNome("Mercado Municipal");
        mercadoRepository.save(mercado);

        Permissionario permissionario = new Permissionario();
        permissionario.setCnpj("12345678901234");
        permissionario.setNome("Empresa XYZ");
        permissionarioRepository.save(permissionario);

        NotaFiscal notaFiscal = new NotaFiscal();
        notaFiscal.setChaveAcesso("00000000000000000000000000000000000000000000");
        notaFiscal.setNumero("12345");
        notaFiscal.setSerie("1");
        notaFiscal.setTipo("Entrada");
        notaFiscal.setMunicipio(municipio);
        notaFiscal.setMercado(mercado);
        notaFiscal.setPermissionario(permissionario);
        notaFiscal.setDataEmissao(LocalDate.now());

        ItemNotaFiscal item1 = new ItemNotaFiscal();
        item1.getPropriedade().setId(3);
        item1.setQuantidade(new BigDecimal("10.00"));
        item1.setValorUnitario(new BigDecimal("5.00"));
        item1.setNotaFiscal(notaFiscal);

        ItemNotaFiscal item2 = new ItemNotaFiscal();
        item2.getPropriedade().setId(5);
        item2.setQuantidade(new BigDecimal(5));
        item2.setValorUnitario(new BigDecimal("3.00"));
        item2.setNotaFiscal(notaFiscal);

        notaFiscal.setItens(Arrays.asList(item1, item2));
        notaFiscalRepository.save(notaFiscal);

        assertThat(notaFiscalRepository.findAll()).hasSize(1);
        assertThat(itemNotaFiscalRepository.findAll()).hasSize(2);
    }
}
