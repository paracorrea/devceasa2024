package com.flc.springthymeleaf.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flc.springthymeleaf.domain.Propriedade;
import com.flc.springthymeleaf.repository.PropriedadeRepository;
import com.flc.springthymeleaf.service.exceptions.DataIntegrityException;

@Service
@Transactional(readOnly=false)
public class PropriedadeService {

	
		@Autowired
		private PropriedadeRepository propRepo;
		
		public Propriedade insert(Propriedade obj) {
			return propRepo.save(obj);
		}
		
		@Transactional(readOnly=true)
		public Optional<Propriedade> findById(Integer id) {
			
			Optional<Propriedade> obj = propRepo.findById(id);
			return obj;
			
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

		
		@Transactional(readOnly=true)
		public List<Propriedade> findPropriedadePorCotacao() {
			
			List<Propriedade> listPropriedades = propRepo.findPropriedadePorCotacao();
			return listPropriedades;
		}

		public boolean existsByCodigo(String codigo) {
			boolean resp = propRepo.existsByCodigo(codigo);
			return resp;
		}

		
		public List<Propriedade> findPropriedadesNotNull(){
			return propRepo.findAllWithNonNullCodigo();
			
		}

		public List<Propriedade> searchByQuery(String query) {
			// TODO Auto-generated method stub
			return propRepo.findByCodigoContainingOrVariedadeContainingOrProdutoNomeContaining(query, query, query);
}

		 public Propriedade findByCodigo(String codigo) {
		        return propRepo.findByCodigoIgnoreCase(codigo);
		    }

		    public List<Propriedade> findByProdutoNome(String nome) {
		        return propRepo.findByProdutoNomeContainingIgnoreCase(nome);
		    }

		    public List<Propriedade> findByProdutoNome1(String nome){
		    	return propRepo.findByProdutoNome(nome);
		    }
		    public List<Propriedade> findPropriedadesByEmbalagemId(Integer id) {
		        return propRepo.findPropriedadesByEmbalagemId(id);
		    }
		    
		    public List<Propriedade> findPropriedadesComCotacaoNoPeriodo(LocalDate startDate, LocalDate endDate) {
		        return propRepo.findPropriedadesComCotacaoNoPeriodo(startDate, endDate);
		    }
}
