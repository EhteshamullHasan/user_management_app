package com.LoginRegister.example.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.LoginRegister.example.dto.LoginFormDto;
import com.LoginRegister.example.dto.RegistrationFormDto;
import com.LoginRegister.example.dto.ResetPwdFormDto;
import com.LoginRegister.example.dto.UserDto;
import com.LoginRegister.example.entities.CityEntity;
import com.LoginRegister.example.entities.CountryEntity;
import com.LoginRegister.example.entities.StateEntity;
import com.LoginRegister.example.entities.UserEntity;
import com.LoginRegister.example.repo.CityRepo;
import com.LoginRegister.example.repo.CountryRepo;
import com.LoginRegister.example.repo.StateRepo;
import com.LoginRegister.example.repo.UserRepo;

@Service
public class UserServiceImpl implements UserService{
	
	public UserRepo userRepo;
	public CountryRepo countryRepo;
	public StateRepo stateRepo;
	public CityRepo cityRepo;
	public EmailService emailService;
	
	public UserServiceImpl(UserRepo userRepo, CountryRepo countryRepo,StateRepo stateRepo, CityRepo cityRepo,EmailService emailService) {
		this.userRepo = userRepo;
		this.countryRepo = countryRepo;
		this.stateRepo = stateRepo;
		this.cityRepo = cityRepo;
		this.emailService = emailService;
	}

	@Override
	public Map<Integer, String> getCountries() {
		
		List<CountryEntity> countryList = countryRepo.findAll();
		
		Map<Integer, String> hashMap = new HashMap<>();
		
		countryList.forEach(c -> {
			hashMap.put(c.getCountryId(), c.getCountryName());
		});
		
		return hashMap;
	}

	@Override
	public Map<Integer, String> getStates(Integer countryId) {

		Map<Integer, String> hashMap = new HashMap<>();
		
		List<StateEntity> statesList = stateRepo.findByCountryId(countryId);
//		
		statesList.forEach(c ->{
			hashMap.put(c.getStateId(), c.getStateName());
		});
		
		return hashMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer stateId) {

		Map<Integer, String> hashMap = new HashMap<>();
		
		List<CityEntity> cityList = cityRepo.findByStateId(stateId);
		cityList.forEach(c ->{
			hashMap.put(c.getCityId(), c.getCityName());
		});
		
		return hashMap;
	}

	@Override
	public boolean duplicateEmailCheck(String email) {
		
		 UserEntity user = userRepo.findByEmail(email);
		if(null!= user) {
			return true;
		}
		return false;
	}

	@Override
	public UserEntity saveUser(RegistrationFormDto dto) {

		UserEntity user = new UserEntity();
		BeanUtils.copyProperties(dto, user);
		
		CountryEntity country = countryRepo.findById(dto.getCountryId()).orElse(null);
		user.setCountry(country);
		
		StateEntity state = stateRepo.findById(dto.getStateId()).orElse(null);
		user.setState(state);
		
		CityEntity city = cityRepo.findById(dto.getCityId()).orElse(null);
		user.setCity(city);
		
		String randomPwd = generateRandomPwd();
		
		user.setPwd(randomPwd);
		user.setPwdUpdate("no");
		
		
		UserEntity savedUser = userRepo.save(user);
		
		if(null!=savedUser.getUserId()) {
			String subject = "Your account created";
			String body = "Your password to login : "+randomPwd;
			String to = dto.getEmail();
			
			emailService.sendEmail(subject, body, to);
			
			return savedUser;
			
		}
		return null;
		
//		user.setUname(dto.getUname());
//		user.setEmail(dto.getEmail());
//		user.setPhno(dto.getPhno());
//		user.setPwd(dto.getPwd());
//		user.setPwdUpdate(dto.getPwdUpdate());
//		
//		if(dto.getCountryId() != null){
//			
//			CountryEntity country = countryRepo.getReferenceById(dto.getCountryId());
//			user.setCountry(country);
//		}
//		
//		if(dto.getStateId() != null) {
//			
//			StateEntity state = stateRepo.getReferenceById(dto.getStateId());
//			user.setState(state);
//			
//			
//		}
//		if(dto.getCityId() != null) {
//			CityEntity city = cityRepo.getReferenceById(dto.getCityId());
//			user.setCity(city);
//		}
//		
//		userRepo.save(user);
//		
//		return true;
	}

	@Override
	public UserDto login(LoginFormDto loginFormDto) {
		
		UserEntity userEntity = userRepo.findByEmailAndPwd(loginFormDto.getEmail(), loginFormDto.getPwd());
		
		if(null !=userEntity) {
			
			UserDto userDto = new UserDto();
			
			BeanUtils.copyProperties(userEntity, userDto);
			return userDto;	
		}
		return null;
	}

	@Override
	public boolean resetPwd(ResetPwdFormDto resetPwd) {
		
		UserEntity userEntity = userRepo.findByEmail(resetPwd.getEmail());
		
		userEntity.setPwd(resetPwd.getConfirmPwd());
		userEntity.setPwdUpdate("Yes");
			
		userRepo.save(userEntity); //Upsert
		return true;
	}

	private String generateRandomPwd() {
		String upperCaseLetter = "ABCDEFGHIJKLMNOPQRSTUVTWXYX";
		String lowerCaseLetter = "abcdefghijklmnopqrstuvwxyz";
		
		String alphabets = upperCaseLetter + lowerCaseLetter;
		Random random = new Random();
		
		StringBuffer generatedPwd = new StringBuffer();
		
		for(int i = 0;i<5;i++) {
			int randomIndex = random.nextInt(alphabets.length());
			generatedPwd.append(alphabets.charAt(randomIndex));
		}
		
		return generatedPwd.toString();
	}
	

}
