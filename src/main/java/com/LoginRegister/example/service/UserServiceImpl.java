package com.LoginRegister.example.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class UserServiceImpl implements UserService {

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	public UserRepo userRepo;
	public CountryRepo countryRepo;
	public StateRepo stateRepo;
	public CityRepo cityRepo;
	public EmailService emailService;

	public UserServiceImpl(UserRepo userRepo, CountryRepo countryRepo, StateRepo stateRepo, CityRepo cityRepo,
			EmailService emailService) {
		this.userRepo = userRepo;
		this.countryRepo = countryRepo;
		this.stateRepo = stateRepo;
		this.cityRepo = cityRepo;
		this.emailService = emailService;
	}

	@Override
	public Map<Integer, String> getCountries() {

		log.info("Fetching all countries");

		List<CountryEntity> countryList = countryRepo.findAll();

		log.info("Found {} countries", countryList.size());

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
		statesList.forEach(c -> {
			hashMap.put(c.getStateId(), c.getStateName());
		});

		return hashMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer stateId) {

		Map<Integer, String> hashMap = new HashMap<>();

		List<CityEntity> cityList = cityRepo.findByStateId(stateId);
		cityList.forEach(c -> {
			hashMap.put(c.getCityId(), c.getCityName());
		});

		return hashMap;
	}

	@Override
	public boolean duplicateEmailCheck(String email) {

		log.info("Checking duplicate email: {}", email);

		UserEntity user = userRepo.findByEmail(email);

		if (user != null) {
			log.warn("Email already exists: {}", email);
			return true;
		}

		log.info("Email is available: {}", email);
		return false;
	}

	@Override
	public UserEntity saveUser(RegistrationFormDto dto) {

		log.info("User registration started for email: {}", dto.getEmail());

		UserEntity user = new UserEntity();

		BeanUtils.copyProperties(dto, user);

		CountryEntity country = countryRepo.findById(dto.getCountryId()).orElse(null);

		StateEntity state = stateRepo.findById(dto.getStateId()).orElse(null);

		CityEntity city = cityRepo.findById(dto.getCityId()).orElse(null);

		user.setCountry(country);
		user.setState(state);
		user.setCity(city);

		String randomPwd = generateRandomPwd();

		user.setPwd(randomPwd);
		user.setPwdUpdate("no");

		log.info("Saving user into database");

		UserEntity savedUser = userRepo.save(user);

		if (savedUser.getUserId() != null) {

			log.info("User saved successfully with id: {}", savedUser.getUserId());

			String subject = "Your account created";
			String body = "Your password to login : " + randomPwd;
			String to = dto.getEmail();

			log.info("Sending registration email to {}", to);

			emailService.sendEmail(subject, body, to);

			log.info("Registration email sent successfully");

			return savedUser;
		}

		log.error("User registration failed for email: {}", dto.getEmail());

		return null;
	}

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
//	}

	@Override
	public UserDto login(LoginFormDto loginFormDto) {

		log.info("Login attempt for email: {}", loginFormDto.getEmail());

		UserEntity userEntity = userRepo.findByEmailAndPwd(loginFormDto.getEmail(), loginFormDto.getPwd());

		if (userEntity != null) {

			log.info("Login successful for email: {}", loginFormDto.getEmail());

			UserDto userDto = new UserDto();

			BeanUtils.copyProperties(userEntity, userDto);

			return userDto;
		}

		log.warn("Login failed for email: {}", loginFormDto.getEmail());

		return null;
	}

	@Override
	public boolean resetPwd(ResetPwdFormDto resetPwd) {

	    log.info("Password reset started for email: {}",
	            resetPwd.getEmail());

	    UserEntity userEntity =
	            userRepo.findByEmail(resetPwd.getEmail());

	    if (userEntity == null) {

	        log.error("User not found for email: {}",
	                resetPwd.getEmail());

	        return false;
	    }

	    userEntity.setPwd(resetPwd.getConfirmPwd());
	    userEntity.setPwdUpdate("Yes");

	    userRepo.save(userEntity);

	    log.info("Password reset successful for email: {}",
	            resetPwd.getEmail());

	    return true;
	}

	private String generateRandomPwd() {
		String upperCaseLetter = "ABCDEFGHIJKLMNOPQRSTUVTWXYX";
		String lowerCaseLetter = "abcdefghijklmnopqrstuvwxyz";

		String alphabets = upperCaseLetter + lowerCaseLetter;
		Random random = new Random();

		StringBuffer generatedPwd = new StringBuffer();

		for (int i = 0; i < 5; i++) {
			int randomIndex = random.nextInt(alphabets.length());
			generatedPwd.append(alphabets.charAt(randomIndex));
		}

		return generatedPwd.toString();
	}

}
