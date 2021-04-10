package com.brainsinjars.projectbackend.dto;

/**
 * @author Kanchan Harjani
 * @since 09-03-2021
 */
public class AddUserDTO {
	private String email;
	private String passwdHash;
	private String firstName;
	private String lastName;
	private String streetAddr;
	private String city;
	private String state;
	private String pinCode;
	private String region;

	public AddUserDTO() {
		super();
	}

	public AddUserDTO(String email, String passwdHash, String firstName, String lastName, String streetAddr,
			String city, String state, String pinCode, String region) {
		super();
		this.email = email;
		this.passwdHash = passwdHash;
		this.firstName = firstName;
		this.lastName = lastName;
		this.streetAddr = streetAddr;
		this.city = city;
		this.state = state;
		this.pinCode = pinCode;
		this.region = region;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswdHash() {
		return passwdHash;
	}

	public void setPasswdHash(String passwdHash) {
		this.passwdHash = passwdHash;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getStreetAddr() {
		return streetAddr;
	}

	public void setStreetAddr(String streetAddr) {
		this.streetAddr = streetAddr;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Override
	public String toString() {
		return "AddUserDTO [email=" + email + ", passwdHash=" + passwdHash + ", firstName=" + firstName + ", lastName="
				+ lastName + ", streetAddr=" + streetAddr + ", city=" + city + ", state=" + state + ", pincode="
				+ pinCode + ", region=" + region + "]";
	}

}
