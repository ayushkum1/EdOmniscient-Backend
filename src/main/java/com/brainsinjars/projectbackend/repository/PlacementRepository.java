package com.brainsinjars.projectbackend.repository;

import java.time.Year;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.brainsinjars.projectbackend.dto.PlacementChartDTO;
import com.brainsinjars.projectbackend.dto.PlacementDTO;
import com.brainsinjars.projectbackend.pojo.Batch;
import com.brainsinjars.projectbackend.pojo.Placement;

/**
 * @author Kanchan Harjani
 * @since 08-03-2021
 */
@Repository
public interface PlacementRepository extends JpaRepository<Placement, Long> {

	@Query("select new com.brainsinjars.projectbackend.dto.PlacementDTO(p.id, c.name, p.noPlacedStudents,"
			+ " p.totalStudents, p.batch, p.year, p.maxLPAOffered, p.avgLPAOffered) from Placement p inner join Institute i on"
			+ " p.institute = i  inner join Course c on p.course = c where i.id = :instituteId")
	List<PlacementDTO> fetchPlacementDetails(@Param("instituteId") long instituteId);

	@Query("select new com.brainsinjars.projectbackend.dto.PlacementDTO(p.id, c.name, p.noPlacedStudents, p.totalStudents, p.batch,"
			+ "			p.year, p.maxLPAOffered, p.avgLPAOffered) "
			+ "from Placement p inner join Institute i on p.institute = i  inner join Course c"
			+ " on p.course = c where i.id = :instituteId and p.batch = :batch and p.year = :year and c.name = :courseName")
	PlacementDTO fetchPlacementDetailsByBatchAndYearAndCourse(@Param("instituteId") long instituteId,
			@Param("batch") Batch batch, @Param("year") Year year, @Param("courseName") String courseName);


	@Query("select new com.brainsinjars.projectbackend.dto.PlacementDTO(p.id, c.name, p.noPlacedStudents, p.totalStudents,"
			+ "p.batch, p.year, p.maxLPAOffered, p.avgLPAOffered) from Placement p inner join Institute i on p.institute = i"
			+ " inner join Course c on p.course = c where i.id = :instituteId")
	List<PlacementDTO> fetchLastNPlacementDetails(Pageable pageable, @Param("instituteId") long instituteId);

	@Query("select new com.brainsinjars.projectbackend.dto.PlacementChartDTO(c.name, p.noPlacedStudents, p.totalStudents,p.batch, p.year) "
			+ "from Placement p inner join Institute i on p.institute = i inner join Course c on p.course = c " +
			"where i.id = :instituteId and c.name = :courseName order by p.year desc, p.batch desc ")
	List<PlacementChartDTO> getCourseSortByYear(@Param("instituteId") Long instituteId, @Param("courseName") String courseName);
}

/*
 * @Query("select p.maxLPAOffered from Placement p inner join Institute i on " +
 * "p.institute = i inner join Course c on p.course = c " +
 * "where p.batch = :batch and p.year = :year and c.name = :cname and i.name = :iname"
 * ) double findMaxLPAOffered(@Param("batch") Batch batch, @Param("year") Year
 * year, @Param("cname") String cname,
 * 
 * @Param("iname") String iname);
 * 
 * @Query("select p.avgLPAOffered from Placement p inner join Institute i " +
 * "on p.institute = i inner join Course c on p.course = c" +
 * " where p.batch = :batch and p.year = :year and c.name = :cname and i.name = :iname"
 * ) double findAvgLPAOffered(@Param("batch") Batch batch, @Param("year") Year
 * year, @Param("cname") String cname,
 * 
 * @Param("iname") String iname);
 * 
 * @Query("select p.noPlacedStudents from Placement p inner join Institute i on p.institute = i "
 * +
 * "inner join Course c on p.course = c where p.batch = :batch and p.year = :year and c.name = :cname and i.name = :iname"
 * ) int findNoPlacedStudents(@Param("batch") Batch batch, @Param("year") Year
 * year, @Param("cname") String cname,
 * 
 * @Param("iname") String iname);
 * 
 * @Query("select p.totalStudents from Placement p inner join Institute i on p.institute = i inner join Course c on p.course = c where"
 * +
 * " p.batch = :batch and p.year = :year and c.name = :cname and i.name = :iname"
 * ) int findTotalStudents(@Param("batch") Batch batch, @Param("year") Year
 * year, @Param("cname") String cname,
 * 
 * @Param("iname") String iname);
 */