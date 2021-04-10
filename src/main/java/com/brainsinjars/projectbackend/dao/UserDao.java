package com.brainsinjars.projectbackend.dao;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.brainsinjars.projectbackend.dto.AddUserDTO;
import com.brainsinjars.projectbackend.dto.UpdateUserDTO;
import com.brainsinjars.projectbackend.dto.UserDetailsDTO;
import com.brainsinjars.projectbackend.dto.UserMemberDTO;
import com.brainsinjars.projectbackend.dto.UserPatchDTO;
import com.brainsinjars.projectbackend.exceptions.UserNotFoundException;
import com.brainsinjars.projectbackend.pojo.User;

/**
 * @author Kanchan Harjani
 * @since 08-03-2021
 */
@Repository
public interface UserDao {

	boolean existsByEmail(String email);

	User findByEmail(String email) throws UserNotFoundException;

	Set<User> findByRole(String role);

	List<UserDetailsDTO> findAllUsers();

	boolean createAccount(AddUserDTO addUser, String role);

	boolean deleteUserById(String email) throws UserNotFoundException;

	boolean updateUserAccount(String email, UpdateUserDTO updateUserDto) throws UserNotFoundException;

	UserMemberDTO fetchUserByEmailId(String email) throws UserNotFoundException;

	List<User> findUserByRole(String role);

	List<String> findUnaffiliatedInstituteAdmin();

	boolean changeUserProfilePic(String email, UserPatchDTO userPatchDTO) throws UserNotFoundException;

	UserMemberDTO findByMemberId(long id) throws UserNotFoundException;
}

//List<User> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName)
//		throws UserNotFoundException;
//
//List<User> findByFirstNameIgnoreCase(String firstName) throws UserNotFoundException;
//
//List<User> findByLastNameIgnoreCase(String lastName) throws UserNotFoundException;
//
//User fetchUserByEmailIdAndPassword(String email, String pwd) throws InvalidCredentialsException;
//
