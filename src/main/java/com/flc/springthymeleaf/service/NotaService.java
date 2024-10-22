package com.flc.springthymeleaf.service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.flc.springthymeleaf.domain.ItemDeNota;
import com.flc.springthymeleaf.domain.Nota;
import com.flc.springthymeleaf.repository.ItemDeNotaRepository;
import com.flc.springthymeleaf.repository.NotaRepository;

import jakarta.transaction.Transactional;

@Service
public class NotaService {

	private final NotaRepository notaRepository;
	
	private final ItemDeNotaRepository itemDeNotaRepository;
	
	
	 public NotaService(NotaRepository notaRepository, ItemDeNotaRepository itemDeNotaRepository) {
	        this.notaRepository = notaRepository;
			this.itemDeNotaRepository = itemDeNotaRepository;
    }
	
		public Page<Nota> findAll(Pageable pageable) {
	        return notaRepository.findAll(pageable);
    }

	public Page<Nota> findByDataNotaBetween(LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
		// TODO Auto-generated method stub
		return notaRepository.findByDataNotaBetween(dataInicio, dataFim, pageable);
	}

	public void save(Nota nota) {
		notaRepository.save(nota);		
	}

	public Optional<Nota> findById(Integer id) {
		// TODO Auto-generated method stub
		return notaRepository.findById(id);
	}

	@Transactional
    public void deleteById(Integer id) {
        Nota nota = notaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nota não encontrada"));

        for (ItemDeNota item : nota.getItens()) {
            itemDeNotaRepository.delete(item);
        }

        notaRepository.delete(nota);
    }

	public Page<Nota> findNotasByData(LocalDate dataDaSessao, Pageable pageable) {
		// TODO Auto-generated method stub
		return notaRepository.findAllByData(dataDaSessao, pageable);
	}

	public List<Nota> findNotasByDataAndNoControlePortaria(LocalDate dataDaSessao) {
		// TODO Auto-generated method stub
		return notaRepository.findNotasByDataAndNoControlePortaria(dataDaSessao);
	}
	
}
