package com.flc.springthymeleaf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.flc.springthymeleaf.domain.Municipio;
import com.flc.springthymeleaf.domain.Mercado;
import com.flc.springthymeleaf.service.NotaFiscalService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Controller
public class ImportacaoController {
    @Autowired
    private NotaFiscalService notaFiscalService;

    @PostMapping("/importar/municipios")
    @ResponseBody
    public String importarMunicipios(@RequestParam("file") MultipartFile file) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\t"); // Assumindo que o arquivo está separado por tabulações
                Municipio municipio = new Municipio();
                municipio.setIbge(Integer.parseInt(data[0]));
                municipio.setCodigo(data[1]);
                municipio.setUf(data[2]);
                municipio.setNome(data[3]);
                notaFiscalService.saveMunicipio(municipio);
            }
            return "Importação de municípios concluída com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao importar municípios: " + e.getMessage();
        }
    }

    @PostMapping("/importar/mercados")
    @ResponseBody
    public String importarMercados(@RequestParam("file") MultipartFile file) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\t"); // Assumindo que o arquivo está separado por tabulações
                Mercado mercado = new Mercado();
                mercado.setUf(data[0]);
                mercado.setCodigo(Integer.parseInt(data[1]));
                mercado.setNome(data[2]);
                notaFiscalService.saveMercado(mercado);
            }
            return "Importação de mercados concluída com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao importar mercados: " + e.getMessage();
        }
    }
}
