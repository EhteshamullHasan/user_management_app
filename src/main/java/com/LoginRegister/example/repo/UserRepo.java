package com.LoginRegister.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LoginRegister.example.entities.UserEntity;

public interface UserRepo extends JpaRepository<UserEntity, Integer> {
	
	
	public UserEntity findByEmail(String email);
	
	public UserEntity findByEmailAndPwd(String email, String pwd);

}
