package com.brainsinjars.projectbackend.dao.impl;

import com.brainsinjars.projectbackend.dao.LocationDao;
import com.brainsinjars.projectbackend.pojo.Geography;
import com.brainsinjars.projectbackend.pojo.Location;
import com.brainsinjars.projectbackend.repository.GeographyRepository;
import com.brainsinjars.projectbackend.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LocationDaoImpl implements LocationDao {

	private final GeographyRepository geoRepo;
	private final LocationRepository locationRepo;

	@Autowired
	public LocationDaoImpl(GeographyRepository geoRepo, LocationRepository locationRepo) {
		this.geoRepo = geoRepo;
		this.locationRepo = locationRepo;
	}

	@Override
	public Location saveLocation(String streetAddress, String city, String state, String pinCode, String region) {
		Geography geo = geoRepo.findByCityIgnoreCaseAndStateIgnoreCaseAndPinCodeIgnoreCase(city, state, pinCode)
				.orElseGet(() -> geoRepo.save(new Geography(city, state, pinCode, region)));
		return locationRepo.save(new Location(streetAddress, geo));
	}

	@Override
	public Location updateLocation(Location location) {
		Geography geo = location.getGeography();
		// Effective final variable required inside lambda function
		Geography g = geo;
		geo = geoRepo.findByCityIgnoreCaseAndStateIgnoreCaseAndPinCodeIgnoreCase(geo.getCity(), geo.getState(), geo.getPinCode())
				.orElseGet(() -> geoRepo.save(new Geography(g.getCity(), g.getState(), g.getPinCode(), g.getRegion())));
		location.setStreetAddr(location.getStreetAddr());
		location.setGeography(geo);
		return locationRepo.save(location);
//		return location;
	}

	@Override
	public List<String> findAllStates() {
		return geoRepo.findAllStates();
	}

	@Override
	public List<String> findAllCities() {
		return geoRepo.findAllCities();
	}

	@Override
	public List<String> findAllRegions() {
		return geoRepo.findAllRegions();
	}
}
