package com.brainsinjars.projectbackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.brainsinjars.projectbackend.pojo.Geography;

public interface GeographyRepository extends JpaRepository<Geography, Integer>{
	
	@Query("select distinct g.state from Geography g")
	List<String> findAllStates();
	
	@Query("select distinct g.city from Geography g")
	List<String> findAllCities();
	
	@Query("select distinct g.region from Geography g")
	List<String> findAllRegions();
	
	
	Optional<Geography> findByStateIgnoreCase(String state);
	
	
	Optional<Geography> findByCityIgnoreCase(String city);
	
	
	Optional<Geography> findByCityIgnoreCaseAndStateIgnoreCaseAndPinCodeIgnoreCase(String city,String state,String pinCode);
	
	
	
	
	
	
	
}
