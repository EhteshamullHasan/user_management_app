package com.LoginRegister.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LoginRegister.example.dto.QuoteApiResponseDto;
import com.LoginRegister.example.service.DashboardService;

@RestController
public class DashboardController {
	
	private DashboardService dashboardService;
	
	public DashboardController(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}
	
	@GetMapping("/getQuote")
	public ResponseEntity<QuoteApiResponseDto> getQuoteApi(){
		
		QuoteApiResponseDto quote = dashboardService.getQuote();
		
		if(null!= quote) {
			return ResponseEntity.ok(quote);
		}
		return null;
		
		
	}
	

}
