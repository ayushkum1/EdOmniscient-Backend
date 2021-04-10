/* 
 * @Author Channappa Mirgale 
 * @Since 09-03-21
*/ 


package com.brainsinjars.projectbackend.repository;


import com.brainsinjars.projectbackend.dto.MemberListDTO;
import com.brainsinjars.projectbackend.exceptions.MemberHandlingException;
import com.brainsinjars.projectbackend.pojo.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

//	@Query("select m FROM Member m")
//	List<Member> displayAllMembers(); //display all Member

	//Optional<Member> findbyId();  //find user by particular userID

	//	@Query("select r from Review r join fetch r. i where i.id =:id")
	@Query("select m from Member m where m.year =:yr")
	Optional<List<Member>> findByPassOutYear(@Param(value = "yr") Year year) throws MemberHandlingException;      //find the members from particular year

	@Query("select m from Member m inner join User u on m.user = u where u.email = :email")
	Member findMemberByEmail(@Param("email") String email);

	@Query("select new com.brainsinjars.projectbackend.dto.MemberListDTO(m.id, concat(m.user.firstName, ' ', m.user.lastName), " +
			"u.profilePicLink, u.email, m.course.id, u.about, m.prn, m.memberType, m.publicPhone, m.status, m.publicEmail, m.year ) " +
			"from Member m inner join User u on m.user = u where m.status = 'APPROVED' and m.institute.id = :id")
	List<MemberListDTO> findApprovedMembers(long id);
}
