package com.flc.springthymeleaf.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.flc.springthymeleaf.domain.Feira;
import com.flc.springthymeleaf.repository.FeiraRepository;

@Service
public class FeiraService {
    private final FeiraRepository feiraRepository;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public FeiraService(FeiraRepository feiraRepository) {
        this.feiraRepository = feiraRepository;
    }

    public Page<Feira> findAll(Pageable pageable) {
        return feiraRepository.findAll(pageable);
    }

    public Page<Feira> findByDataFeiraBetween(LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        return feiraRepository.findByDataFeiraBetween(dataInicio, dataFim, pageable);
    }

    public Optional<Feira> findById(Integer id) {
        return feiraRepository.findById(id);
    }

    public Feira findByDataFeira(LocalDate dataFeira) {
        return feiraRepository.findByDataFeira(dataFeira);
    }

    public void salvarFeira(Feira feira) {
        feiraRepository.save(feira);
    }

    public void excluirFeira(Integer id) {
        feiraRepository.deleteById(id);
        atualizarSequenciaIds();
    }

    private void atualizarSequenciaIds() {
        // Atualiza a sequência de IDs no banco de dados para evitar duplicidade de chave primária
        // Atualiza a sequência de IDs no banco de dados para evitar duplicidade de chave primária
        jdbcTemplate.execute("SELECT setval('feira_id_seq', (SELECT COALESCE(MAX(id), 1) FROM feira))");

    }
    public Long findMaxNumero() {
        Long maxNumero = feiraRepository.findMaxNumero();
        return maxNumero != null ? maxNumero : 0;
    }
}
