package com.flc.springthymeleaf.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.flc.springthymeleaf.domain.Cotacao;
import com.flc.springthymeleaf.repository.CotacaoRepository;
import com.flc.springthymeleaf.service.exceptions.ObjectNotFoundException;

@Service
@Transactional(readOnly = false)
public class CotacaoService {

	@Autowired
	private CotacaoRepository cotacaoRepo;
	
	
	
	

	public Cotacao insert(Cotacao obj) {
		return cotacaoRepo.save(obj);
	}

	@EntityGraph(attributePaths = "propriedade")
	public Cotacao findById(Integer id) {

		Optional<Cotacao> obj = cotacaoRepo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objecto não encontrado id: " + id + ", tipo: " + Cotacao.class.getName()));

	}

	@Transactional(readOnly = true)
	public List<Cotacao> findAll() {
		List<Cotacao> lista = cotacaoRepo.findAll();
		return lista;
	}

	public Cotacao update(Cotacao obj) {

		findById(obj.getId());
		return cotacaoRepo.save(obj);
	}

	public void delete(Integer id) {

		findById(id);

		cotacaoRepo.deleteById(id);

	}

	public boolean existeCotacaoComMesmaDataECategoria(Cotacao cotacao) {

		return cotacaoRepo.existsByPropriedadeAndDataCotacao(cotacao.getPropriedade().getId(),
				cotacao.getDataCotacao());
	}

	public List<Cotacao> getCotationsByDate(LocalDate selectedDate) {

		return cotacaoRepo.findByDataCotacao(selectedDate);

	}

		public Cotacao buscarCotacaoAnterior1(Integer propriedadeId, LocalDate dataCotacao) {
		Optional<Cotacao> cotacaoAnteriorOptional = cotacaoRepo
				.findTopByPropriedadeIdAndDataCotacaoLessThanEqualOrderByDataCotacaoDesc(propriedadeId, dataCotacao);

		return cotacaoAnteriorOptional.orElse(null);
	}

		  public List<Cotacao> findByPropriedadeIdAndDataCotacaoBetween(Integer propriedadeId, LocalDate dataInicio, LocalDate dataFim) {
		        return cotacaoRepo.findByPropriedadeIdAndDataCotacaoBetween(propriedadeId, dataInicio, dataFim);
		    }

		
}
