package com.brainsinjars.projectbackend.service;

import com.brainsinjars.projectbackend.dao.UserDao;
import com.brainsinjars.projectbackend.dto.*;
import com.brainsinjars.projectbackend.exceptions.InvalidCredentialsException;
import com.brainsinjars.projectbackend.exceptions.UserNotFoundException;
import com.brainsinjars.projectbackend.pojo.Role;
import com.brainsinjars.projectbackend.pojo.User;
import com.brainsinjars.projectbackend.util.ResponseUtils;
import com.brainsinjars.projectbackend.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kanchan Harjani
 * @since 08-03-2021
 */
@Service
@Transactional
public class UserService {

	private UserDao userDao;

	@Autowired
	public UserService(UserDao userDao) {
		this.userDao = userDao;
	}

	/* This method returns a list of all the users */
	public ResponseEntity<?> findAllUsers() {
		List<UserDetailsDTO> list = new ArrayList<>();
		list = userDao.findAllUsers();
		if (list.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDto("No Users Found", MessageType.INFO));

		return ResponseEntity.ok(list);
	}

	public boolean existsUser(String email) {
		return userDao.existsByEmail(email);
	}

	/* This method fetches user details using email */
	public ResponseEntity<?> fetchUserByEmailId(String email) {
		try {
			UserMemberDTO user = userDao.fetchUserByEmailId(email);
			return ResponseEntity.ok(user);
		} catch (UserNotFoundException u) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new MessageDto(u.getMessage(), MessageType.ERROR));
		}
	}

	/* This method creates a user account */
	public ResponseEntity<?> createUserAccount(AddUserDTO addUser) {
		try {
			if (userDao.existsByEmail(addUser.getEmail()))
				return ResponseUtils.badRequestResponse("User already exists!");
			if (userDao.createAccount(addUser, Role.USER))
				return ResponseEntity.ok(new MessageDto("Account created successfully!", MessageType.SUCCESS));

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto("Account creation failed!", MessageType.ERROR));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
	}

	/* This method creates an institute's admin account */
	public ResponseEntity<?> createInstituteAdminAccount(AddUserDTO addUser) {
		try {
			if (userDao.existsByEmail(addUser.getEmail()))
				return ResponseUtils.badRequestResponse("User already exists!");
			if (userDao.createAccount(addUser, Role.INSTITUTE_ADMIN))
				return ResponseEntity.ok(new MessageDto("Account created successfully!", MessageType.SUCCESS));

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto("Account creation failed!", MessageType.ERROR));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
	}

	/* This method updates a user account */
	public ResponseEntity<?> updateUserAccount(String email, UpdateUserDTO updateUserDto) {
		try {
			if (userDao.updateUserAccount(email, updateUserDto))
				return ResponseEntity.ok(new MessageDto("Account updated successfully!", MessageType.SUCCESS));

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto("Account updation failed!", MessageType.ERROR));

		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDto(e.getMessage(), MessageType.ERROR));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
	}

	/* This method deletes a user account */
	public ResponseEntity<?> deleteUserAccount(String email) {
		try {
			if (userDao.deleteUserById(email))
				return ResponseEntity.ok(new MessageDto("Account deleted successfully!", MessageType.SUCCESS));

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto("Account deletion failed!", MessageType.ERROR));

		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDto(e.getMessage(), MessageType.ERROR));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto(e.getMessage(), MessageType.ERROR));
		}
	}

	/* This method is used to find user by Role */
	public ResponseEntity<?> findUserByRole(String role) {
		if (Arrays.asList(Role.getAllRoles()).contains(role))
			return ResponseEntity.ok(userDao.findUserByRole(role));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new MessageDto("Invalid User Role!", MessageType.ERROR));
	}

	/*
	 * This method is used to find institute admin not affiliated to any institute
	 */
	public ResponseEntity<?> findUnaffiliatedInstituteAdmin() {
		List<String> list;
		list = userDao.findUnaffiliatedInstituteAdmin();

		if (list.isEmpty())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDto("No Users Found", MessageType.INFO));

		return ResponseEntity.ok(list);
	}

	/* This method is used to change user profile picture */
	public ResponseEntity<?> changeUserProfilePic(String email, UserPatchDTO userPatchDTO) {
		try {
			if (userDao.changeUserProfilePic(email, userPatchDTO))
				return ResponseEntity.ok(new MessageDto("Profile Pic Changed Successfully!", MessageType.SUCCESS));

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageDto("Could not change profile pic", MessageType.ERROR));
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new MessageDto(e.getMessage(), MessageType.WARNING));
		}

	}

	public ResponseEntity<?> findUserByMemberId(String memberId) {
		try {
			long id = Long.parseLong(memberId);
			return ResponseEntity.ok(userDao.findByMemberId(id));
		} catch (UserNotFoundException e) {
			return ResponseUtils.notFoundResponse(e.getMessage());
		} catch (NumberFormatException e) {
			return ResponseUtils.badRequestResponse(ResponseUtils.INVALID_ID_MESSAGE);
		}
	}

	public Boolean changePassword(String email, PasswordChangeDto dto) throws UserNotFoundException, InvalidCredentialsException {
		String oldPassword = dto.getOldPassword();
		String newPassword = dto.getNewPassword();
		if (oldPassword != null && newPassword != null) {
			User user = userDao.findByEmail(email);
			boolean matches = SecurityUtils.getPasswordEncoder().matches(oldPassword, user.getPasswdHash());
			if (matches) {
				user.setPasswdHash(SecurityUtils.getPasswordEncoder().encode(newPassword));
				return true;
			} else {
				throw new InvalidCredentialsException("Wrong password!");
			}
		}
		return false;
	}
}

//	/* This method returns a list of users with same firstName and lastName */
//	public List<User> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName) {
//		List<User> users = new ArrayList<>();
//		try {
//			users = userDao.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);
//		} catch (UserNotFoundException u) {
//			u.printStackTrace();
//		}
//		return users;
//	}
//
//	/* This method returns a list of users with same firstName */
//	public List<User> findByFirstNameIgnoreCase(String firstName) {
//		List<User> user = new ArrayList<>();
//		try {
//			user = userDao.findByFirstNameIgnoreCase(firstName);
//		} catch (UserNotFoundException u) {
//			u.printStackTrace();
//		}
//		user.forEach(System.out::println);
//		return user;
//	}
//
//	/* This method returns a list of users with same lastName */
//	public List<User> findByLastNameIgnoreCase(String lastName) {
//		List<User> users = new ArrayList<>();
//		try {
//			users = userDao.findByLastNameIgnoreCase(lastName);
//		} catch (UserNotFoundException u) {
//			u.printStackTrace();
//		}
//		return users;
//	}
//
//	/* This method returns user details using emailId and password */
//	public User fetchUserByEmailIdAndPassword(String email, String pwd) {
//		User user = null;
//		try {
//			user = userDao.fetchUserByEmailIdAndPassword(email, pwd);
//
//		} catch (InvalidCredentialsException u) {
//			u.printStackTrace();
//		}
//		return user;
//	}
