package com.brainsinjars.projectbackend.dto;

/* 
 * @author: Ayush Kumar Singh
 * @since: 08-03-2021
*/

public class CourseIdNameDTO {

	private long id;
	private String name;
	
	public CourseIdNameDTO(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	//no setters
	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		CourseIdNameDTO other = (CourseIdNameDTO) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
