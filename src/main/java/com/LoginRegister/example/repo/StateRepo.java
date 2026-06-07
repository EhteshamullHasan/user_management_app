package com.LoginRegister.example.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LoginRegister.example.entities.StateEntity;

import jakarta.persistence.JoinColumn;

public interface StateRepo extends JpaRepository<StateEntity, Integer> {
	
	public List<StateEntity> findByCountryId(Integer countryId);

}
