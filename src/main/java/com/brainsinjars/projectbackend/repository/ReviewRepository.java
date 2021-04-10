/* 
 * @Author Channappa Mirgale 
 * @Since 09-03-21
*/

package com.brainsinjars.projectbackend.repository;


import java.util.List;
import java.util.Optional;

import com.brainsinjars.projectbackend.dto.ReviewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.brainsinjars.projectbackend.exceptions.ReviewHandlingException;
import com.brainsinjars.projectbackend.pojo.Review;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
	@Query("select r from Review r join fetch r.institute i where i.id =:id")
	Optional<List<Review>> getByInstitute(@Param("id") long instituteId) throws ReviewHandlingException;

	@Query("select new com.brainsinjars.projectbackend.dto.ReviewDTO(r.id, concat(m.user.firstName, ' ', m.user.lastName), " +
			"r.rating, r.content, c.name, r.createdDateTime, r.modifiedDateTime) " +
			"from Review r join Member m on r.member = m join Course c on m.course = c where m.user.email = :email")
	Optional<ReviewDTO> findUsingUserEmail(@Param("email") String email);

	@Query("select r.rating from Review r where r.institute.id = :instituteId")
	List<Integer> findAllRatingsByInstitute(@Param("instituteId") long instituteId);

	@Query("select new com.brainsinjars.projectbackend.dto.ReviewDTO(r.id, concat(m.user.firstName, ' ', m.user.lastName), " +
			"r.rating, r.content, c.name, r.createdDateTime, r.modifiedDateTime) " +
			"from Review r join Member m on r.member = m join Course c on m.course = c")
	List<ReviewDTO> findAllReviews();

	@Transactional
	@Modifying
	@Query("delete from Review r where r.id = :id")
	void deleteUsingId(@Param("id") Long id);
}
