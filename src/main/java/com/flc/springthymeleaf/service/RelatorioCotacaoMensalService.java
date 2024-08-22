	package com.flc.springthymeleaf.service;
	
	import java.math.BigDecimal;
	import java.math.RoundingMode;
	import java.time.LocalDate;
	import java.util.ArrayList;
	import java.util.LinkedHashMap;
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
	
		public List<RelatorioMensalDto> gerarRelatorioMensal(List<Integer> propriedadeIds, LocalDate mesAnoInicial, int quantidadeMeses) {
		    List<RelatorioMensalDto> relatorio = new ArrayList<>();
	
		    for (Integer propriedadeId : propriedadeIds) {
		        RelatorioMensalDto dto = new RelatorioMensalDto();
		        List<Cotacao> cotacoes = cotacaoRepo.findByPropriedadeId(propriedadeId);
	
		        // Filtrar e calcular médias por mês no intervalo especificado
		        Map<String, BigDecimal> mediasPorMes = calcularMediasPorMes(cotacoes, mesAnoInicial, quantidadeMeses);
		        dto.setPropriedade(propriedadeRepository.findById(propriedadeId).orElse(null));
		        dto.setMediasPorMes(mediasPorMes);
	
		        relatorio.add(dto);
		    }
	
		    return relatorio;
		}
	
		private Map<String, BigDecimal> calcularMediasPorMes(List<Cotacao> cotacoes, LocalDate mesAnoInicial, int quantidadeMeses) {
		    Map<String, BigDecimal> mediasPorMes = new LinkedHashMap<>();
	
		    for (int i = 0; i < quantidadeMeses; i++) {
		        LocalDate mesAtual = mesAnoInicial.plusMonths(i);
		        String chaveMes = mesAtual.getYear() + "-" + mesAtual.getMonthValue();
	
		        List<Cotacao> cotacoesDoMes = cotacoes.stream()
		                .filter(c -> c.getDataCotacao().getYear() == mesAtual.getYear() &&
		                             c.getDataCotacao().getMonthValue() == mesAtual.getMonthValue())
		                .collect(Collectors.toList());
	
		        if (!cotacoesDoMes.isEmpty()) {
		            BigDecimal media = calcularMediaDeCotacoes(cotacoesDoMes);
		            mediasPorMes.put(chaveMes, media);
		        } else {
		            mediasPorMes.put(chaveMes, BigDecimal.ZERO);
		        }
		    }
	
		    return mediasPorMes;
		}
		private BigDecimal calcularMediaDeCotacoes(List<Cotacao> cotacoes) {
			BigDecimal soma = BigDecimal.ZERO;
			for (Cotacao cotacao : cotacoes) {
				soma = soma.add(cotacao.getValorComum());
			}
			return soma.divide(new BigDecimal(cotacoes.size()), RoundingMode.HALF_UP);
		}
	}
