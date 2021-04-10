package com.brainsinjars.projectbackend.dao;

import java.util.List;

import com.brainsinjars.projectbackend.pojo.Location;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationDao {
	Location saveLocation(String streetAddress,String city,String state,String pinCode,String region);
	Location updateLocation(Location location);
	List<String> findAllStates();
	List<String> findAllCities();
	List<String> findAllRegions();
}
