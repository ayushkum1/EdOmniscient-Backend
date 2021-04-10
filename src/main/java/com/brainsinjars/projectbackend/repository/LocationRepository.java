package com.brainsinjars.projectbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brainsinjars.projectbackend.pojo.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {

}
