package com.brainsinjars.projectbackend.repository;

/**
 * @author Kartik Singhal
 * @since 07-03-21
 */

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.brainsinjars.projectbackend.pojo.Media;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {

	// here all the media files are fetched by particular institute Id
	@Query("select m from Media m join m.institute i where i.id=:instituteId")
	List<Media> findAllMediaByIntituteId(@Param("instituteId") long instituteId);
}
