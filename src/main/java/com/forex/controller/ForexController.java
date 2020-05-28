package com.forex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.forex.model.Rate;
import com.forex.service.ForexService;

@Controller
public class ForexController {

	@Autowired
	private ForexService forexService;

	@GetMapping("/")
	public String index(Model model) {
		List<Rate> rates = this.forexService.getForexData();
		String error = null;
		if(rates.isEmpty()) {			
			rates = this.forexService.getDefaultData();
			error = "You are either offline or connection to API is limited. We will display the offline data instead";
		}
		
		model.addAttribute("rates", rates);
		model.addAttribute("error", error);
		
		return "index";
	}
}
