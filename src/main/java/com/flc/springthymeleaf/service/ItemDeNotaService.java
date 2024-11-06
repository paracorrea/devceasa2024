package com.flc.springthymeleaf.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flc.springthymeleaf.DTO.ProdutoPesoDTO;
import com.flc.springthymeleaf.DTO.SubgrupoPesoDTO;
import com.flc.springthymeleaf.repository.ItemDeNotaRepository;

@Service
public class ItemDeNotaService {
	
	

    @Autowired
    private ItemDeNotaRepository itemDeNotaRepository;

    public List<ProdutoPesoDTO> getTop5ProdutosByPesoX(LocalDate startDate, LocalDate endDate) {
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
    
    public List<SubgrupoPesoDTO> getAllSubgruposByPeso(LocalDate startDate, LocalDate endDate) {
    	List <Object[]> resultados = itemDeNotaRepository.findTop5SubgruposByPesoBetweenDates(startDate, endDate);
        List<SubgrupoPesoDTO> subgrupoDTO = new ArrayList<>();

        for (Object[] resultado : resultados) {
            String subgrupo = (String) resultado[0];
            Double totalPeso = (Double) resultado[1];
            SubgrupoPesoDTO dto = new SubgrupoPesoDTO(subgrupo, totalPeso);
            subgrupoDTO.add(dto);
        }
        
        return subgrupoDTO;
    }
    public List<ProdutoPesoDTO> getTop5ProdutosByPeso(LocalDate startDate, LocalDate endDate) {
        return itemDeNotaRepository.findTop5ProdutosByPesoBetweenDates(startDate, endDate)
                .stream()
                .map(result -> new ProdutoPesoDTO((String) result[0], (Double) result[1]))
                .collect(Collectors.toList());
    }

    // Método para obter os top 5 subgrupos por peso
    public List<SubgrupoPesoDTO> getTop5SubgruposByPeso(LocalDate startDate, LocalDate endDate) {
        return itemDeNotaRepository.findTop5SubgruposByPesoBetweenDates(startDate, endDate)
                .stream()
                .map(result -> new SubgrupoPesoDTO((String) result[0], (Double) result[1]))
                .collect(Collectors.toList());
    }

	public Double findVolumeTotalEntreDatas(LocalDate startDate, LocalDate endDate) {
		return itemDeNotaRepository.findVolumeTotalEntreDatas(startDate, endDate);
		
	}
}
