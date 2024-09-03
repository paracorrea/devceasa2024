package com.flc.springthymeleaf.service;




import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


		public Page<Embalagem> findAll(Pageable pageable) {
			return embalagemRepository.findAll(pageable);
			
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
		
		
}
