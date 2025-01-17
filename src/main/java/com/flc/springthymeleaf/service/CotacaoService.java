package com.flc.springthymeleaf.service;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
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
	private CotacaoRepository cotacaoRepository;
	
	
	public Cotacao insert(Cotacao obj) {
		return cotacaoRepository.save(obj);
	}

	@EntityGraph(attributePaths = "propriedade")
	public Cotacao findById(Integer id) {

		Optional<Cotacao> obj = cotacaoRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objecto não encontrado id: " + id + ", tipo: " + Cotacao.class.getName()));

	}

	@Transactional(readOnly = true)
	public List<Cotacao> findAll() {
		List<Cotacao> lista = cotacaoRepository.findAll();
		return lista;
	}

	public Cotacao update(Cotacao obj) {

		findById(obj.getId());
		return cotacaoRepository.save(obj);
	}

	public void delete(Integer id) {

		findById(id);

		cotacaoRepository.deleteById(id);

	}

	public boolean existeCotacaoComMesmaDataECategoria(Cotacao cotacao) {

		return cotacaoRepository.existsByPropriedadeAndDataCotacao(cotacao.getPropriedade().getId(),
				cotacao.getDataCotacao());
	}

	public List<Cotacao> getCotationsByDate(LocalDate selectedDate) {

		return cotacaoRepository.findByDataCotacao(selectedDate);

	}

		public Cotacao buscarCotacaoAnterior1(Integer propriedadeId, LocalDate dataCotacao) {
		Optional<Cotacao> cotacaoAnteriorOptional = cotacaoRepository
				.findTopByPropriedadeIdAndDataCotacaoLessThanEqualOrderByDataCotacaoDesc(propriedadeId, dataCotacao);

		return cotacaoAnteriorOptional.orElse(null);
	}

		
		public List<Cotacao> findByPropriedadeIdAndDataCotacaoBetween(Integer propriedadeId, LocalDate dataInicio, LocalDate dataFim) {
		        return cotacaoRepository.findByPropriedadeIdAndDataCotacaoBetween(propriedadeId, dataInicio, dataFim);
		}

		
		// Chart of produto and modal price in cotacao. Called from GraficoController in "/graficos/produtos_preco"
		public Map<String, Object> getMediaSemanalPorProduto(
		        Integer propriedadeId,
		        LocalDate startDate,
		        LocalDate endDate) {

		    List<Cotacao> cotacoes = cotacaoRepository.findByCodigoPropriedadeAndDateRange(propriedadeId, startDate, endDate);

		    // Agrupamento por semanas, ordenado por data
		    Map<LocalDate, List<Cotacao>> cotacoesPorSemana = cotacoes.stream()
		            .collect(Collectors.groupingBy(
		                    c -> c.getDataCotacao().with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1),
		                    TreeMap::new, // TreeMap para ordenar as semanas automaticamente
		                    Collectors.toList()
		            ));

		    // Calcular a média semanal
		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		    List<String> datas = new ArrayList<>();
		    List<Double> mediasSemanais = new ArrayList<>();

		    for (Entry<LocalDate, List<Cotacao>> entry : cotacoesPorSemana.entrySet()) {
		        double media = entry.getValue().stream()
		                .mapToDouble(cotacao -> cotacao.getValorComum().doubleValue())
		                .average()
		                .orElse(0.0);
		        mediasSemanais.add(media);

		        // Adicionar a data representativa da semana para o eixo X
		        String formattedDate = entry.getKey().format(formatter);
		        datas.add(formattedDate);
		    }

		    // Obter o nome do produto
		    String nomeProduto = cotacoes.isEmpty() ? "" : cotacoes.get(0).getPropriedade().getProduto().getNome();
		    String variedade = cotacoes.isEmpty() ? "" : cotacoes.get(0).getPropriedade().getVariedade();
		    String subVariedade = cotacoes.isEmpty() ? "" : cotacoes.get(0).getPropriedade().getSubvariedade();
		    String classificacao = cotacoes.isEmpty() ? "" : cotacoes.get(0).getPropriedade().getClassificacao();

		    // Calcular tendência
		    List<Double> tendencia = calcularTendencia(datas, mediasSemanais);

		    // Montar o resultado
		    Map<String, Object> result = new HashMap<>();
		    result.put("variedade", variedade);
		    result.put("subVariedade", subVariedade);
		    result.put("classificacao", classificacao);
		    result.put("nomeProduto", nomeProduto);
		    result.put("datas", datas);
		    result.put("mediasSemanais", mediasSemanais);
		    result.put("tendencia", tendencia); // Adiciona a série da linha de tendência

		    return result;
		}


private List<Double> calcularTendencia(List<String> datas, List<Double> mediasSemanais) {
    int n = mediasSemanais.size();
    double sumX = 0;
    double sumY = 0;
    double sumXY = 0;
    double sumX2 = 0;

    for (int i = 0; i < n; i++) {
        double x = i;
        double y = mediasSemanais.get(i);
        sumX += x;
        sumY += y;
        sumXY += x * y;
        sumX2 += x * x;
    }

    double b = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
    double a = (sumY - b * sumX) / n;

    List<Double> tendencia = new ArrayList<>();
    for (int i = 0; i < n; i++) {
        tendencia.add(a + b * i);
    }

    return tendencia;
}

public Map<String, Map<LocalDate, Double>> getMediaSemanalPorProduto(LocalDate startDate, LocalDate endDate) {
    List<Object[]> resultados = cotacaoRepository.findMediaSemanalPorProduto(startDate, endDate);
    Map<String, Map<LocalDate, Double>> mediaSemanalPorProduto = new LinkedHashMap<>();

    for (Object[] row : resultados) {
        String codigoPropriedade = (String) row[0];
        String produtoNome = (String) row[1];
        String variedade = (String) row[2];
        String subvariedade = (String) row[3];
        String classificacao = (String) row[4];
        LocalDate semana = (LocalDate) row[5]; // Agora `semana` é diretamente um LocalDate
        Double media = (Double) row[6];

        // Constrói a chave com todos os detalhes
        String descricaoProduto = String.format("%s - %s %s %s %s", 
            codigoPropriedade, produtoNome, variedade != null ? variedade : "", 
            subvariedade != null ? subvariedade : "", classificacao != null ? classificacao : "");

        mediaSemanalPorProduto
            .computeIfAbsent(descricaoProduto, k -> new LinkedHashMap<>())
            .put(semana, media);
    }


    return mediaSemanalPorProduto;
}
}

