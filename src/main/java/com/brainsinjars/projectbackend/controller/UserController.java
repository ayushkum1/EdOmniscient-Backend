package com.brainsinjars.projectbackend.controller;

import com.brainsinjars.projectbackend.constant.HasRole;
import com.brainsinjars.projectbackend.dto.AddUserDTO;
import com.brainsinjars.projectbackend.dto.PasswordChangeDto;
import com.brainsinjars.projectbackend.dto.UpdateUserDTO;
import com.brainsinjars.projectbackend.dto.UserPatchDTO;
import com.brainsinjars.projectbackend.exceptions.InvalidCredentialsException;
import com.brainsinjars.projectbackend.exceptions.UserNotFoundException;
import com.brainsinjars.projectbackend.service.UserService;
import com.brainsinjars.projectbackend.util.ResponseUtils;
import com.brainsinjars.projectbackend.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.brainsinjars.projectbackend.util.ResponseUtils.forbiddenResponse;

/**
 * @author Kanchan Harjani
 * @since 09-03-2021
 *
 */
@RestController
@RequestMapping("/users")
public class UserController {

	UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	// @RolesAllowed(ROOT)
	@Secured(HasRole.ROOT)
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllUsers() {
		return userService.findAllUsers();
	}

	@Secured({HasRole.USER, HasRole.INSTITUTE_ADMIN, HasRole.MEMBER, HasRole.ROOT})
	@GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCurrentUser() {
		return userService.fetchUserByEmailId(SecurityUtils.getUsername());
	}

	@GetMapping(value = "/exists", produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean existsUser(@RequestParam("email") String email) {
		return userService.existsUser(email);
	}

	// @RolesNotAllowed(Anonymous)
	@Secured({ HasRole.ROOT, HasRole.INSTITUTE_ADMIN, HasRole.MEMBER, HasRole.USER })
	@GetMapping(value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUserDetails(@PathVariable String email) {
		if (SecurityUtils.hasAccess(email))
			return userService.fetchUserByEmailId(email);
		else
			return forbiddenResponse("You don't have access to this resource");
	}

	@Secured(HasRole.ROOT )
	@GetMapping(value = "/role", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findUserByRole(@RequestParam(value = "role", required = true) String role) {
		return userService.findUserByRole(role);
	}

	@Secured(HasRole.ROOT)
	@GetMapping(value = "/admins", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findUnaffiliatedInstituteAdmin() {
		return userService.findUnaffiliatedInstituteAdmin();
	}

	@Secured({HasRole.USER, HasRole.MEMBER, HasRole.INSTITUTE_ADMIN, HasRole.ROOT})
	@GetMapping(value = "/member/{memberId}")
	public ResponseEntity<?> getUserByMemberId(@PathVariable String memberId) {
		return userService.findUserByMemberId(memberId);
	}

	@PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createUserAccount(@RequestBody AddUserDTO addUser) {
		return userService.createUserAccount(addUser);
	}

	@PostMapping(value = "/register/institute", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createInstituteAdminAccount(@RequestBody AddUserDTO addUser) {
		return userService.createInstituteAdminAccount(addUser);
	}

	@Secured({HasRole.USER, HasRole.MEMBER, HasRole.INSTITUTE_ADMIN, HasRole.ROOT})
	@PutMapping(value = "/{email}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateUserAccount(@PathVariable String email, @RequestBody UpdateUserDTO updateUserDto) {
		if (SecurityUtils.hasAccess(email))
			return userService.updateUserAccount(email, updateUserDto);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have access to update this account");
	}

	@Secured({HasRole.USER, HasRole.MEMBER, HasRole.INSTITUTE_ADMIN, HasRole.ROOT})

	@PatchMapping(value = "/{email}")
	public ResponseEntity<?> patchUserAccount(@PathVariable String email, @RequestBody UserPatchDTO userPatchDTO){
		if (SecurityUtils.hasAccess(email))
			return userService.changeUserProfilePic(email, userPatchDTO);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have access to update this account");
	}

	@Secured({HasRole.USER, HasRole.MEMBER, HasRole.INSTITUTE_ADMIN, HasRole.ROOT})
	@PatchMapping(value = "/change_password")
	public ResponseEntity<?> patchPassword(@RequestBody PasswordChangeDto dto) {
		String email = SecurityUtils.getUsername();
		try {
			Boolean updated = userService.changePassword(email, dto);
			return updated ?
					ResponseUtils.successResponse("Password updated successfully")
					:
					ResponseUtils.badRequestResponse("Invalid request");
		} catch (UserNotFoundException e) {
			return ResponseUtils.notFoundResponse(e.getMessage());
		} catch	(InvalidCredentialsException e) {
			return ResponseUtils.badRequestResponse(e.getMessage());
		} catch (Exception e) {
			return ResponseUtils.internalServerErrorResponse("Unknown error occurred!");
		}
	}

	// @RolesNotAllowed(Anonymous, INSTITUTE_ADMIN)
	@Secured({HasRole.USER, HasRole.MEMBER, HasRole.INSTITUTE_ADMIN, HasRole.ROOT})
	@DeleteMapping(value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteUserAccount(@PathVariable String email) {
		if (SecurityUtils.hasAccess(email))
			return userService.deleteUserAccount(email);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have access to delete this account");
	}

}
