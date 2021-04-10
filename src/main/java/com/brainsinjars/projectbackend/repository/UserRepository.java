package com.brainsinjars.projectbackend.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.brainsinjars.projectbackend.dto.UserDetailsDTO;
import com.brainsinjars.projectbackend.dto.UserMemberDTO;
import com.brainsinjars.projectbackend.pojo.User;

/**
 * @author Kanchan Harjani
 * @since 08-03-2021
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

	Set<User> findByRoleIgnoreCase(String email);

	@Query("select new com.brainsinjars.projectbackend.dto.UserMemberDTO(u.email, u.firstName, u.lastName, m.id, m.memberType, m.status, m.prn, "
			+ "m.publicEmail, m.publicPhone,  m.year, l.streetAddr, g.city, g.state, g.pinCode, g.region,"
			+ "u.about, u.fbProfile, u.linkedinProfile, u.profilePicLink) "
			+ "from User u left outer join Member m on m.user = u inner join Location l on u.location = l inner join Geography g on l.geography = g "
			+ "where u.email = :email")
	UserMemberDTO fetchUserByEmailId(@Param("email") String email);

	@Query("select new com.brainsinjars.projectbackend.dto.UserDetailsDTO(u.email, u.firstName, u.lastName, u.fbProfile, "
			+ "u.linkedinProfile, u.profilePicLink, l.streetAddr, g.city, g.state, g.pinCode, g.region )"
			+ "from User u  inner join Location l on u.location = l inner join Geography g on l.geography = g")
	List<UserDetailsDTO> findAllUsers();
	
	@Query("select u from User u where u.role = :role")
	List<User> findByRole(@Param("role") String role);
	
		
	@Query("select u.email from User u where u.role = 'INSTITUTE_ADMIN' and u.email NOT IN (select i.instituteAdmin from Institute i)")
	List<String> findUnaffiliatedInstituteAdmin();


	@Query("select new com.brainsinjars.projectbackend.dto.UserMemberDTO(u.email, u.firstName, u.lastName, m.id, m.memberType, m.status, m.prn, "
			+ "m.publicEmail, m.publicPhone,  m.year, l.streetAddr, g.city, g.state, g.pinCode, g.region,"
			+ "u.about, u.fbProfile, u.linkedinProfile, u.profilePicLink) "
			+ "from User u left outer join Member m on m.user = u inner join Location l on u.location = l inner join Geography g on l.geography = g "
			+ "where m.id = :id")
	Optional<UserMemberDTO> findUsingMemberId(@Param("id") long id);
}

/*
 * Optional<List<User>> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String
 * firstName, String lastName);
 * 
 * Optional<List<User>> findByFirstNameIgnoreCase(String firstName);
 * 
 * Optional<List<User>> findByLastNameIgnoreCase(String lastName);
 * 
 * @Query("select u from User u where u.email = :email and u.passwdHash = :pwd")
 * Optional<User> fetchUserByEmailIdAndPassword(@Param("email") String
 * email, @Param("pwd") String pwd);
 */