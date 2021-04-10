/* 
 * @Author Channappa Mirgale 
 * @Since 09-03-21
*/

package com.brainsinjars.projectbackend.dao;

import java.time.Year;
import java.util.List;
import com.brainsinjars.projectbackend.dto.MemberAddDTO;
import com.brainsinjars.projectbackend.dto.MemberUpdateDTO;
import com.brainsinjars.projectbackend.dto.MemberListDTO;
import com.brainsinjars.projectbackend.exceptions.MemberHandlingException;
import com.brainsinjars.projectbackend.pojo.Member;



public interface IMemberDao {
	
    Member findById(long memberId) throws MemberHandlingException;
    List<Member> findByPassOutYear(Year year) throws MemberHandlingException;
	List<MemberListDTO> findAllMembers(long instituteId);//is it filtered based on institute or all the members present in our website?
	//as this query will get all the members from table, and logic fails
	boolean addMember(MemberAddDTO member, long instituteId);
	boolean updateMember(long memberId, MemberUpdateDTO member,long instituteId) throws MemberHandlingException;
    boolean removeMember(long memberId,long instituteId) throws MemberHandlingException;
	List<MemberListDTO> findApprovedMembers(long id);

	// boolean addReview(Review review, long memberId) throws MemberHandlingException;
  // boolean updateReview(Review review, long memberId) throws MemberHandlingException;
	
 
	
	
  
}
