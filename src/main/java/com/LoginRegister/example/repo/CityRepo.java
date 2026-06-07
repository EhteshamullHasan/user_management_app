package com.LoginRegister.example.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LoginRegister.example.entities.CityEntity;

public interface CityRepo extends JpaRepository<CityEntity, Integer> {

	public List<CityEntity> findByStateId(Integer stateId);
}
