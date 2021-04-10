package com.brainsinjars.projectbackend.dto;

/* 
 * @author: Ayush Kumar Singh
 * @since: 08-03-2021
*/

public class CourseDTO {
	
	private long id;
	private String name;
	private String description;
	private String photoUrl;
	private double fees;
	private short duration;
	
	public CourseDTO(long id, String name, String description, String photoUrl, double fees, short duration) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.photoUrl = photoUrl;
		this.fees = fees;
		this.duration = duration;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public double getFees() {
		return fees;
	}

	public short getDuration() {
		return duration;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + duration;
		long temp;
		temp = Double.doubleToLongBits(fees);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((photoUrl == null) ? 0 : photoUrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CourseDTO other = (CourseDTO) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (duration != other.duration)
			return false;
		if (Double.doubleToLongBits(fees) != Double.doubleToLongBits(other.fees))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (photoUrl == null) {
			if (other.photoUrl != null)
				return false;
		} else if (!photoUrl.equals(other.photoUrl))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CourseDTO [id=" + id + ", name=" + name + ", description=" + description + ", photoUrl=" + photoUrl
				+ ", fees=" + fees + ", duration=" + duration + "]";
	}
	
}
