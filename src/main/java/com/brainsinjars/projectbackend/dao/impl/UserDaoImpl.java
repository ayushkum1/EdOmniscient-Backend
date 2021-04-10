package com.brainsinjars.projectbackend.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.brainsinjars.projectbackend.pojo.Geography;
import com.brainsinjars.projectbackend.pojo.Location;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.brainsinjars.projectbackend.dao.LocationDao;
import com.brainsinjars.projectbackend.dao.UserDao;
import com.brainsinjars.projectbackend.dto.AddUserDTO;
import com.brainsinjars.projectbackend.dto.UpdateUserDTO;
import com.brainsinjars.projectbackend.dto.UserDetailsDTO;
import com.brainsinjars.projectbackend.dto.UserMemberDTO;
import com.brainsinjars.projectbackend.dto.UserPatchDTO;
import com.brainsinjars.projectbackend.exceptions.UserNotFoundException;
import com.brainsinjars.projectbackend.pojo.Member;
import com.brainsinjars.projectbackend.pojo.User;
import com.brainsinjars.projectbackend.repository.MemberRepository;
import com.brainsinjars.projectbackend.repository.UserRepository;
import com.brainsinjars.projectbackend.util.SecurityUtils;

/**
 * @author Kanchan Harjani
 * @since 08-03-2021
 */
@Component
public class UserDaoImpl implements UserDao {

	private UserRepository userRepository;
	private LocationDao locationDao;
	private MemberRepository memberRepository;

	@Autowired
	public UserDaoImpl(UserRepository userRepository, LocationDao locationDao, MemberRepository memberRepository) {
		this.userRepository = userRepository;
		this.locationDao = locationDao;
		this.memberRepository = memberRepository;
	}

	/* checks if a user exists with this email id */
	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsById(email);
	}

	/* finds a user based on email */
	@Override
	public User findByEmail(String email) throws UserNotFoundException {
		return userRepository.findById(email).orElseThrow(() -> new UserNotFoundException("Invalid User Email"));
	}

	/* finds a user based on role */
	@Override
	public Set<User> findByRole(String role) {
		return userRepository.findByRoleIgnoreCase(role);
	}

	/* fetches list of users from the database */
	@Override
	public List<UserDetailsDTO> findAllUsers() {
		return userRepository.findAllUsers();
	}

	/* persists user in the database */
	@Override
	public boolean createAccount(AddUserDTO addUser, String role) {
		User user = new User();
		BeanUtils.copyProperties(addUser, user);
		user.setPasswdHash(SecurityUtils.getPasswordEncoder().encode(addUser.getPasswdHash()));
		user.setLocation(locationDao.saveLocation(addUser.getStreetAddr(), addUser.getCity(), addUser.getState(),
				addUser.getPinCode(), addUser.getRegion()));
		user.setRole(role);
		if (userRepository.save(user) != null)
			return true;
		return false;

	}

	/* deletes a user record from the database and returns user object */
	@Override
	public boolean deleteUserById(String email) throws UserNotFoundException {
		userRepository.findById(email)
				.orElseThrow(() -> new UserNotFoundException("Deletion Failed! User with this email does not exist"));

		Member m = memberRepository.findMemberByEmail(email);
		if (m != null)
			memberRepository.deleteById(m.getId());

		userRepository.deleteById(email);
		return true;
	}

	/*
	 * updates a user record into the database and throws UserNotFoundException if
	 * record not found
	 */
	@Override
	public boolean updateUserAccount(String email, UpdateUserDTO updateUserDto) throws UserNotFoundException {
		User user = userRepository.findById(email)
				.orElseThrow(() -> new UserNotFoundException("Updation Failed!! User Account doesn't exist"));

		BeanUtils.copyProperties(updateUserDto, user, "email", "passwdHash");

		Location location = user.getLocation();
		Geography geography = location.getGeography();

		String streetAddr = Optional.ofNullable(updateUserDto.getStreetAddr()).orElse(location.getStreetAddr());
		String city = Optional.ofNullable(updateUserDto.getCity()).orElse(geography.getCity());
		String state = Optional.ofNullable(updateUserDto.getState()).orElse(geography.getState());
		String pinCode = Optional.ofNullable(updateUserDto.getPinCode()).orElse(geography.getPinCode());
		String region = Optional.ofNullable(updateUserDto.getRegion()).orElse(geography.getRegion());

		user.setLocation(locationDao.saveLocation(streetAddr, city, state, pinCode, region));
		if (updateUserDto.getPasswdHash() != null)
			user.setPasswdHash(SecurityUtils.getPasswordEncoder().encode(updateUserDto.getPasswdHash()));
		if (userRepository.save(user) != null)
			return true;
		return false;
	}

	/*
	 * returns UserMemberDTO with user details only if user is not a member and
	 * returns user + member details if user is a member
	 */
	@Override
	public UserMemberDTO fetchUserByEmailId(String email) throws UserNotFoundException {
		userRepository.findById(email).orElseThrow(() -> new UserNotFoundException("Invalid User Email"));
		UserMemberDTO dto = userRepository.fetchUserByEmailId(email);
		if (dto.getMemberType() != null)
			dto.setInstituteId(memberRepository.findMemberByEmail(email).getInstitute().getId());
		else
			dto.setInstituteId(null);
		return dto;
	}

	/* returns user for a given Role */
	@Override
	public List<User> findUserByRole(String role) {
		return userRepository.findByRole(role);
	}

	/* returns unaffiliated institute admin */
	@Override
	public List<String> findUnaffiliatedInstituteAdmin() {
		return userRepository.findUnaffiliatedInstituteAdmin();
	}

	/* changes user's profile pic */
	@Override
	public boolean changeUserProfilePic(String email, UserPatchDTO userPatchDTO) throws UserNotFoundException {
		User user = userRepository.findById(email).orElseThrow(() -> new UserNotFoundException("Invalid User Email"));
		user.setProfilePicLink(
				Optional.ofNullable(userPatchDTO.getProfilePicLink()).orElse(userPatchDTO.getProfilePicLink()));
		userRepository.save(user);
		return true;
	}

	@Override
	public UserMemberDTO findByMemberId(long id) throws UserNotFoundException {
		return userRepository.findUsingMemberId(id)
				.orElseThrow(() -> new UserNotFoundException("No user found with member id '" + id + "'"));
	}
}

///*
// * finds users using firstName and lastName, if not found throws
// * UserNotFoundException
// */
//@Override
//public List<User> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName)
//		throws UserNotFoundException {
//	return userRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName)
//			.orElseThrow(() -> new UserNotFoundException("No such user found!!"));
//}
///*
// * finds users using firstName, if not found throws UserNotFoundException
// */
//
//@Override
//public List<User> findByFirstNameIgnoreCase(String firstName) throws UserNotFoundException {
//	return userRepository.findByFirstNameIgnoreCase(firstName)
//			.orElseThrow(() -> new UserNotFoundException("No users found with this first name!!"));
//}
//
///*
// * finds users using lastName, if not found throws UserNotFoundException
// */
//@Override
//public List<User> findByLastNameIgnoreCase(String lastName) throws UserNotFoundException {
//	return userRepository.findByLastNameIgnoreCase(lastName)
//			.orElseThrow(() -> new UserNotFoundException("No users found with this last name!!"));
//}
//
///*
// * fetches user details using email and password, if not found throws
// * InvalidCredentialsException
// */
//@Override
//public User fetchUserByEmailIdAndPassword(String email, String pwd) throws InvalidCredentialsException {
//	return userRepository.fetchUserByEmailIdAndPassword(email, pwd)
//			.orElseThrow(() -> new InvalidCredentialsException("Invalid Credentials!!"));
//}
//
