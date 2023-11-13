package com.flc.springthymeleaf.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flc.springthymeleaf.domain.Subgrupo;
import com.flc.springthymeleaf.repository.SubgrupoRepository;
import com.flc.springthymeleaf.service.exceptions.DataIntegrityException;
import com.flc.springthymeleaf.service.exceptions.ObjectNotFoundException;

@Service
@Transactional(readOnly=false)
public class SubgrupoService {
	
	@Autowired
	private SubgrupoRepository subRepo;
	
	
	public Subgrupo insert(Subgrupo obj) {
		return subRepo.save(obj);
	}
		
	@Transactional(readOnly=true)
	public Subgrupo findById(Integer id) {
		
		
		Optional<Subgrupo> obj = subRepo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objecto não encontrado id: "+id+", tipo: "+Subgrupo.class.getName()));
	}
	
	@Transactional(readOnly=true)
	public List<Subgrupo> findAll() {
		
		List<Subgrupo> lista = subRepo.findAll();
		return lista;
	}
	
	
	public Subgrupo update(Subgrupo obj) {
		
		findById(obj.getId());
		return subRepo.save(obj);
	}
	
	
	public void delete(Integer id) {
		
		findById(id);
		
		try {
			
			subRepo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			
			throw new DataIntegrityException
			("Não é possível excluir um Subgrupo "
					+ "já associação ou com grupo ou com produto");
		}
	}

	@Transactional(readOnly=true)
	public List<Subgrupo> findByNome(String nome) {
		
		List<Subgrupo> theQuery = subRepo.findSubgrupoByNome(nome);
		return theQuery;
	}
	
	@Transactional(readOnly=true)
	public List<Subgrupo> findProdutoPorSubgrupo(Integer id) {
		List<Subgrupo> listGroups = subRepo.findProdutoEmSubgrupo(id);
		return listGroups;
	}

	@Transactional(readOnly=true)
	public List<Subgrupo> findGrupoSubgrupo(Integer id) {
		List<Subgrupo> listGrupos = subRepo.findGrupoPorSubgrupo(id);
		return listGrupos;
		
	}
	
	@Transactional(readOnly=true)
	public boolean subgrupoTemProdutos(Integer id) {
		if (findById(id).getProduto().isEmpty()) {
			return false;
		}
		return true;
	}
	
}