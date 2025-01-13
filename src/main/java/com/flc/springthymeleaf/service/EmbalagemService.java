package com.flc.springthymeleaf.service;


import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.flc.springthymeleaf.domain.Embalagem;
import com.flc.springthymeleaf.repository.EmbalagemRepository;

@Service
public class EmbalagemService {

		private final EmbalagemRepository embalagemRepository;
		
				
		private EmbalagemService(EmbalagemRepository embalagemRepository) {
			super();
			this.embalagemRepository = embalagemRepository;
		}


		public List<Embalagem> findAll() {
			return embalagemRepository.findAll();
			
		}
		
		public Optional<Embalagem> findById(Integer id) {
			
			Optional<Embalagem> obj = embalagemRepository.findById(id);
			return obj;
		}
		
		public void salvarEmbalagem(Embalagem embalagem) {
			embalagemRepository.save(embalagem);
		}
		
		
		public Embalagem update(Embalagem obj) {
			 findById(obj.getId());
			 return embalagemRepository.save(obj);
		}
		
		public void excluirEmbalagem(Integer id) {
			embalagemRepository.deleteById(id);
		}
		
		public void atualizarAtributos(Embalagem embalagemAtualizada) {
		    // Busca a embalagem existente pelo código (ou ID)
		    Embalagem embalagemExistente = embalagemRepository.findByCodigo(embalagemAtualizada.getCodigo())
		        .orElseThrow(() -> new IllegalArgumentException("Embalagem não encontrada"));

		    if (!embalagemExistente.getCodigo().equals(embalagemAtualizada.getCodigo())) {
		        throw new DataIntegrityViolationException("Erro: Alteração do código não é permitida.");
		    }
		    // Atualiza apenas os atributos desejados (sem alterar o código)
		    embalagemExistente.setPeso(embalagemAtualizada.getPeso());
		    embalagemExistente.setTipoEmbalagem(embalagemAtualizada.getTipoEmbalagem());
		    embalagemExistente.setUnidadeMedida(embalagemAtualizada.getUnidadeMedida());

		    // Salva a embalagem atualizada
		    embalagemRepository.save(embalagemExistente);
		}


		public List<Embalagem> findAllById(List<Integer> embalagemIds) {
			// TODO Auto-generated method stub
			return embalagemRepository.findAllById(embalagemIds);
		}


		public List<Embalagem> findByPropriedadeId(Integer id) {
			// TODO Auto-generated method stub
			return embalagemRepository.findEmbalagemByPropriedadeId(id);
		}


		public boolean isEmbalagemAssociada(Integer id) {
			// TODO Auto-generated method stub
			 // Verificar se a embalagem está associada a alguma propriedade
		    Embalagem embalagem = embalagemRepository.findById(id).orElseThrow(() -> new RuntimeException("Embalagem não encontrada"));
		    return !embalagem.getPropriedades().isEmpty();  // Verifica se a lista de propriedades está vazia
		}

	
		
}
