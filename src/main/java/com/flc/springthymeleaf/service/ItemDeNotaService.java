package com.flc.springthymeleaf.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flc.springthymeleaf.DTO.ProdutoPesoDTO;
import com.flc.springthymeleaf.repository.ItemDeNotaRepository;

@Service
public class ItemDeNotaService {
	
	

    @Autowired
    private ItemDeNotaRepository itemDeNotaRepository;

    public List<ProdutoPesoDTO> getTop5ProdutosByPeso(LocalDate startDate, LocalDate endDate) {
    	List <Object[]> resultados = itemDeNotaRepository.findTop5ProdutosByPesoBetweenDates(startDate, endDate);
        List<ProdutoPesoDTO> produtosDTO = new ArrayList<>();

        for (Object[] resultado : resultados) {
            String produto = (String) resultado[0];
            Double totalPeso = (Double) resultado[1];
            ProdutoPesoDTO dto = new ProdutoPesoDTO(produto, totalPeso);
            produtosDTO.add(dto);
        }
        
        return produtosDTO;
    }
}
