package com.flc.springthymeleaf.service;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.flc.springthymeleaf.domain.Feira;
import com.flc.springthymeleaf.repository.FeiraRepository;

@Service
public class FeiraService {
    private final FeiraRepository feiraRepository;

    public FeiraService(FeiraRepository feiraRepository) {
        this.feiraRepository = feiraRepository;
    }

    public Page<Feira> findAll(Pageable pageable) {
        return feiraRepository.findAllByOrderByDataFeiraDesc(pageable);
    }

    public Page<Feira> findByDataFeiraBetween(LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        return feiraRepository.findByDataFeiraBetween(dataInicio, dataFim, pageable);
    }

    public Optional<Feira> findById(Long id) {
        return feiraRepository.findById(id);
    }

    public Feira findByDataFeira(LocalDate dataFeira) {
        return feiraRepository.findByDataFeira(dataFeira);
    }

    public void salvarFeira(Feira feira) {
        feiraRepository.save(feira);
    }

    public void excluirFeira(Long id) {
        feiraRepository.deleteById(id);
    }
}
