package com.flc.springthymeleaf.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flc.springthymeleaf.domain.Grupo;
import com.flc.springthymeleaf.domain.Produto;
import com.flc.springthymeleaf.repository.ProdutoRepository;
import com.flc.springthymeleaf.service.exceptions.DataIntegrityException;
import com.flc.springthymeleaf.service.exceptions.ObjectNotFoundException;

@Service
@Transactional(readOnly=false)
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepo;
	
	@Transactional(readOnly=true)
	public Produto findById(Integer id) {
		
		Optional<Produto> obj = produtoRepo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado id: " + id + ", tipo: " + Grupo.class.getName()));
	}

	@Transactional(readOnly=true)
	public List<Produto> findAll() {
		List<Produto> listGroups = produtoRepo.findAll();
		return listGroups;
	}
	
	public Page<Produto> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
			Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
			Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
			return this.produtoRepo.findAll(pageable);
	}

	
	
	@Transactional(readOnly=true)
	public List<Produto> paginatedProdutoPorSubgrupo(Integer id, int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		List<Produto> listGroups = produtoRepo.findSubgrupoEmProduto(id, pageable);
		return listGroups;
	}
		
	
	public Produto insert(Produto obj) {
		obj.setId(null);
		return produtoRepo.save(obj);
	}
	
	public Produto update(Produto obj) {
		findById(obj.getId());
		return produtoRepo.save(obj);
		
	}
	
	public void delete(Integer id)  {
		
		findById(id);
		
		try {
			
			produtoRepo.deleteById(id);
			
		} catch (DataIntegrityViolationException e ) {
			
			throw new DataIntegrityException
			("Não é possível excluir um Grupo "
					+ "já associado a um subgrupo");
		}
	}
	
	
	
	@Transactional(readOnly=true)
	public List<Produto> findProdutoPertencenteAoGrupo(Integer id) {
		List<Produto> listGroups = produtoRepo.findProdutoPertencenteAoGrupo(id);
		return listGroups;
	}
	@Transactional(readOnly=true)
	public List<Produto> findByNome(String nome) {
		List<Produto> theQuery = produtoRepo.findByNome(nome);
		return theQuery;
		
	}
	@Transactional(readOnly=true)
	public boolean produtoTemPropriedades(Integer id) {
		if (findById(id).getPropriedades().isEmpty()) {
			return false;
		}
		return true;
	}

	public List<Produto> findProdutoBySubgrupo(Integer id) {
		List<Produto> listGrupos = produtoRepo.findGrupoPorSubgrupo(id);
		return listGrupos;
		
	}
	}
