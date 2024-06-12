package com.flc.springthymeleaf.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.flc.springthymeleaf.domain.Feira;
import com.flc.springthymeleaf.service.FeiraService;

@Controller
public class FeiraController {

	@Autowired
	private FeiraService feiraService;
	
	@GetMapping("/feiras")
    public String listarFeiras(Model model) {
        List<Feira> feiras = feiraService.findAll();
        model.addAttribute("feiras", feiras);
        return "feira/lista_feiras"; // Nova página de listagem de feiras
    }
}
