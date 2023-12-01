package com.flc.springthymeleaf.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flc.springthymeleaf.domain.Cotacao;
import com.flc.springthymeleaf.repository.CotacaoRepository;
import com.flc.springthymeleaf.service.exceptions.ObjectNotFoundException;

@Service
@Transactional(readOnly=false)
public class CotacaoService {

	
		@Autowired
		private CotacaoRepository cotacaoRepo;
		
		public Cotacao insert(Cotacao obj) {
			return cotacaoRepo.save(obj);
		}
		
		
		public Cotacao findById(Integer id) {
			
			Optional<Cotacao> obj = cotacaoRepo.findById(id);
			return obj.orElseThrow(() -> new ObjectNotFoundException("Objecto não encontrado id: "+id+", tipo: "+Cotacao.class.getName()));
			
		}
		@Transactional(readOnly=true)
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
		
}
