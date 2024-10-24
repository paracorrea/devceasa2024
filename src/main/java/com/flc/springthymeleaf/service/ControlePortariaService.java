package com.flc.springthymeleaf.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.flc.springthymeleaf.domain.ControlePortaria;
import com.flc.springthymeleaf.domain.Nota;
import com.flc.springthymeleaf.enums.StatusSessao;
import com.flc.springthymeleaf.repository.ControlePortariaRepository;
import com.flc.springthymeleaf.repository.NotaRepository;


@Service
public class ControlePortariaService {

    @Autowired
    private ControlePortariaRepository controlePortariaRepository;
    
    private NotaRepository notaRepository;

    public List<ControlePortaria> findAll() {
        return controlePortariaRepository.findAll(Sort.by(Sort.Direction.DESC, "dataDaSessao"));
    }

    public ControlePortaria abrirSessaoDiaria() {
        LocalDate hoje = LocalDate.now();
        Optional<ControlePortaria> sessaoExistente = Optional.ofNullable(controlePortariaRepository.findByDataDaSessao(hoje));

        if (sessaoExistente.isPresent()) {
            return sessaoExistente.get();
        }

        ControlePortaria novaSessao = new ControlePortaria();
        novaSessao.setDataDaSessao(hoje);
        novaSessao.setStatusSessao(StatusSessao.ABERTA);
        novaSessao.setTotalNotas(0);
        novaSessao.setTotalPeso(0.0);
        return controlePortariaRepository.save(novaSessao);
    }

	public Optional<ControlePortaria> findById(Integer id) {
		// TODO Auto-generated method stub
		return controlePortariaRepository.findById(id);
	}

	public Optional<ControlePortaria> findByDataDaSessao(LocalDate dataDaSessao) {
		// TODO Auto-generated method stub
		return Optional.ofNullable(controlePortariaRepository.findByDataDaSessao(dataDaSessao));
	}

	public void save(ControlePortaria portaria) {
		// TODO Auto-generated method stub
		controlePortariaRepository.save(portaria);
	}

	public ControlePortaria mudarStatusParaEmDigitacao(Integer id) {
	    Optional<ControlePortaria> portariaOptional = controlePortariaRepository.findById(id);
	    if (portariaOptional.isPresent() && portariaOptional.get().getStatusSessao() == StatusSessao.ABERTA) {
	        ControlePortaria portaria = portariaOptional.get();
	        portaria.setStatusSessao(StatusSessao.EM_DIGITACAO);
	        return controlePortariaRepository.save(portaria);
	    }
	    return null;
	}

	public void delete(ControlePortaria portaria) {
		// TODO Auto-generated method stub
		controlePortariaRepository.delete(portaria);
	}

	public List<ControlePortaria> findByStatus(StatusSessao statusSessao) {
		
		return controlePortariaRepository.findByStatusSessao(statusSessao);
	}

    
    
    // Lógica de atualização de total de notas, peso e status pode ser adicionada aqui
}
