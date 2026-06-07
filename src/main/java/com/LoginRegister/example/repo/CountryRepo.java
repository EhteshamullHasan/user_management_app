package com.LoginRegister.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LoginRegister.example.entities.CountryEntity;

public interface CountryRepo extends JpaRepository<CountryEntity, Integer> {

}
