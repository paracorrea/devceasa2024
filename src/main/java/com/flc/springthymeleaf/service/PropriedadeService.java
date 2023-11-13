package com.flc.springthymeleaf.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flc.springthymeleaf.domain.Propriedade;
import com.flc.springthymeleaf.repository.PropriedadeRepository;
import com.flc.springthymeleaf.service.exceptions.DataIntegrityException;
import com.flc.springthymeleaf.service.exceptions.ObjectNotFoundException;

@Service
@Transactional(readOnly=false)
public class PropriedadeService {

	
		@Autowired
		private PropriedadeRepository propRepo;
		
		public Propriedade insert(Propriedade obj) {
			return propRepo.save(obj);
		}
		
		@Transactional(readOnly=true)
		public Propriedade findById(Integer id) {
			
			Optional<Propriedade> obj = propRepo.findById(id);
			return obj.orElseThrow(() -> new ObjectNotFoundException("Objecto não encontrado id: "+id+", tipo: "+Propriedade.class.getName()));
			
		}
		@Transactional(readOnly=true)
		public List<Propriedade> findAll() {
			List<Propriedade> lista = propRepo.findAll();
			return lista;
		}
		
		
		public Propriedade update(Propriedade obj) {
			
			findById(obj.getId());
			return propRepo.save(obj);
		}
		
		public void delete(Integer id) {
			
			findById(id);
			
			try {
				
				propRepo.deleteById(id);
			} catch (DataIntegrityViolationException e) {
				
				throw new DataIntegrityException
				("Não é possível excluir um Subgrupo "
						+ "já associação ou com grupo ou com produto");
			}
		}

	

		@Transactional(readOnly=true)
		public List<Propriedade> findPropriedadePorProduto(Integer id) {
			List<Propriedade> listPropriedades = propRepo.findPropriedadePorProduto(id);
			return listPropriedades;
		}


		//public List<Propriedade> findByDados(String pVariedade, String pSubvariedade, String pClassificacao, Integer produto, Integer sGrupo) {
			// TODO Auto-generated method stub
		//	return propRepo.findAllDados(pVariedade, pSubvariedade, pClassificacao, produto, sGrupo);
		//}
}
