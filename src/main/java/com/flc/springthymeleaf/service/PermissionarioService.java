package com.flc.springthymeleaf.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.flc.springthymeleaf.domain.Permissionario;
import com.flc.springthymeleaf.repository.PermissionarioRepository;

@Service
public class PermissionarioService {

	PermissionarioRepository permissionarioRepository;
	
	
	
	private PermissionarioService(PermissionarioRepository permissionarioRepository) {
		super();
		this.permissionarioRepository = permissionarioRepository;
	}



	public List<Permissionario> findAll() {
		// TODO Auto-generated method stub
		return permissionarioRepository.findAll();
	}

	 public Permissionario findByCnpj(String cnpj) {
	        return permissionarioRepository.findById(cnpj).orElse(null);
	    }

	
}
