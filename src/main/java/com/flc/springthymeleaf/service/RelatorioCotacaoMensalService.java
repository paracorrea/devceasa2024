package com.flc.springthymeleaf.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flc.springthymeleaf.DTO.RelatorioMensalDto;
import com.flc.springthymeleaf.domain.Cotacao;
import com.flc.springthymeleaf.repository.CotacaoRepository;
import com.flc.springthymeleaf.repository.PropriedadeRepository;

@Service
@Transactional(readOnly = false)
public class RelatorioCotacaoMensalService {

	@Autowired
	private CotacaoRepository cotacaoRepo;

	@Autowired
	private PropriedadeRepository propriedadeRepository;

	public List<RelatorioMensalDto> gerarRelatorioMensal(List<Integer> propriedadeIds) {
		List<RelatorioMensalDto> relatorio = new ArrayList<>();

		for (Integer propriedadeId : propriedadeIds) {
			RelatorioMensalDto dto = new RelatorioMensalDto();
			List<Cotacao> cotacoes = cotacaoRepo.findByPropriedadeId(propriedadeId);

			Map<String, BigDecimal> mediasPorMes = calcularMediasPorMes(cotacoes);
			dto.setPropriedade(propriedadeRepository.findById(propriedadeId).orElse(null));
			dto.setMediasPorMes(mediasPorMes);

			relatorio.add(dto);
		}

		return relatorio;
	}

	private Map<String, BigDecimal> calcularMediasPorMes(List<Cotacao> cotacoes) {
		return cotacoes.stream()
				.collect(Collectors.groupingBy(
						c -> c.getDataCotacao().getYear() + "-" + c.getDataCotacao().getMonthValue(),
						Collectors.collectingAndThen(Collectors.toList(), this::calcularMediaDeCotacoes)));
	}

	private BigDecimal calcularMediaDeCotacoes(List<Cotacao> cotacoes) {
		BigDecimal soma = BigDecimal.ZERO;
		for (Cotacao cotacao : cotacoes) {
			soma = soma.add(cotacao.getValorComum());
		}
		return soma.divide(new BigDecimal(cotacoes.size()), RoundingMode.HALF_UP);
	}
}
