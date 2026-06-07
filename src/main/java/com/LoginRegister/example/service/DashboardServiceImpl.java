package com.LoginRegister.example.service;

import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.LoginRegister.example.dto.QuoteApiResponseDto;
@Service
public class DashboardServiceImpl implements DashboardService {

	String quoteUrl = "https://dummyjson.com/quote/random";
	@Override
	public QuoteApiResponseDto getQuote() {
		
		RestTemplate rt = new RestTemplate();
		
		ResponseEntity<QuoteApiResponseDto> forEntity =
				rt.getForEntity(quoteUrl, QuoteApiResponseDto.class);
		
		@Nullable
		QuoteApiResponseDto body = forEntity.getBody();
		
		return body;
	}

}
