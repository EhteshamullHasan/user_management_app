package com.LoginRegister.example.service;

import java.util.Map;
import com.LoginRegister.example.dto.LoginFormDto;
import com.LoginRegister.example.dto.RegistrationFormDto;
import com.LoginRegister.example.dto.ResetPwdFormDto;
import com.LoginRegister.example.dto.UserDto;
import com.LoginRegister.example.entities.UserEntity;

public interface UserService {
	
	public Map<Integer, String> getCountries();
	
	public Map<Integer, String> getStates(Integer countryId);
	
	public Map<Integer, String> getCities(Integer stateId);
	
	public boolean duplicateEmailCheck(String email);
	
	public UserEntity saveUser(RegistrationFormDto regirstrationForm);
	
	public UserDto login(LoginFormDto loginFormDto);
	
	public boolean resetPwd(ResetPwdFormDto resetPwd);
	
	
	

	

}
