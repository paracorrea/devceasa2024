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
	
	public List<ProdutoPesoDTO> getTop5ProdutosComOutros(LocalDate startDate, LocalDate endDate) {
	    List<Object[]> produtos = itemDeNotaRepository.findTop5ProdutosByPesoBetweenDates(startDate, endDate);
	    
	    List<ProdutoPesoDTO> top5Produtos = new ArrayList<>();
	    double pesoOutros = 0.0;
	    
	    for (int i = 0; i < produtos.size(); i++) {
	        String nomeProduto = (String) produtos.get(i)[0];
	        double peso = ((Number) produtos.get(i)[1]).doubleValue();
	        
	        if (i < 6) {
	            top5Produtos.add(new ProdutoPesoDTO(nomeProduto, peso));
	        } else {
	            pesoOutros += peso;
	        }
	    }
	    
	    if (pesoOutros > 0) {
	        top5Produtos.add(new ProdutoPesoDTO("Outros", pesoOutros));
	    }
	    
	    return top5Produtos;
	}
}
