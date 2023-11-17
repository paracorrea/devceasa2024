package com.flc.springthymeleaf.web.validator;



import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.flc.springthymeleaf.domain.Produto;
import com.flc.springthymeleaf.service.ProdutoService;
import com.flc.springthymeleaf.service.SubgrupoService;

public class ProdutoValidator implements Validator {

	
	ProdutoService produtoService;
	SubgrupoService subService;
	
	
	
	public ProdutoValidator(ProdutoService produtoService) {
		this.produtoService=produtoService;
		
	}
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Produto.class.equals(clazz);
	}

	@Override
	public void validate(Object object, Errors errors) {
		
		Produto d = (Produto) object;
		
		
		String nome = d.getNome();
		String nomeS = d.getSubgrupo().getNome();
		String nomeS1 = "";
		
		List<Produto> produtos = produtoService.findByNome(nome);
		
	
		
		//List<Subgrupo> subgrupos = subService.findProdutoPorSubgrupo(id)
		//List<Subgru po> subgrupos = subService.findByNome(nomeS);


		
		
		if (produtos.size() >0 ) {
			
			for (Produto produto : produtos) {
				
				System.out.println(produto.getNome() + "   " + produto.getId() + "   "+ produto.getSubgrupo().getId() );
			
				nomeS1 = produto.getSubgrupo().getNome();
				
				if (nomeS1==null) {
					errors.rejectValue("nome", "nome.message.nome");
				} else {
				
				if (nomeS1 == nomeS) {
					errors.rejectValue("nome", "ProdutoJaExiste.produto.nome");
				System.out.println("passou aqui2:" + nome + "  " + nomeS);
				}
				
				}
			}
			
					
			
		}
		
	}
	
	

}