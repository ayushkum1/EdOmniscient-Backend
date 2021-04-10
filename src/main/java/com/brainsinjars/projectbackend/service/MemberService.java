/* 
 * @Author Channappa Mirgale 
 * @Since 09-03-21
*/
package com.brainsinjars.projectbackend.service;

import com.brainsinjars.projectbackend.dao.IMemberDao;
import com.brainsinjars.projectbackend.dto.*;
import com.brainsinjars.projectbackend.exceptions.MemberHandlingException;
import com.brainsinjars.projectbackend.exceptions.ReviewHandlingException;
import com.brainsinjars.projectbackend.pojo.Member;
import com.brainsinjars.projectbackend.pojo.MembershipStatus;
import com.brainsinjars.projectbackend.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MemberService {


		private IMemberDao memberDao;

		@Autowired
		public MemberService(IMemberDao memberDao) {
			this.memberDao = memberDao;
		}
		
		
		//method returns a list of all the members
		public ResponseEntity<?> findAllMembers(long instituteId) {
			List<MemberListDTO> memberlist = new ArrayList<>();
				memberlist= memberDao.findAllMembers(instituteId);
					if (memberlist.isEmpty())
							return new ResponseEntity<>("No Users Found!", HttpStatus.NOT_FOUND);
			return ResponseEntity.ok(memberlist);
		}
		

		// method adds a member 
		public boolean addMember(MemberAddDTO member, long instituteId) {	
			try {
				return memberDao.addMember(member, instituteId);
			} catch (ReviewHandlingException e) {
				e.printStackTrace();
			}
			return false;
		}

		//method updates a member details
		public boolean updateMember(long memberId, MemberUpdateDTO member,long instituteId) {		
			try {
				return memberDao.updateMember(memberId, member,instituteId);
			} catch (ReviewHandlingException e) {
				e.printStackTrace();
			}
			return false;
		}

		public boolean patchStatus(long memberId, MemberPatchDto dto, long instituteId) {
			Member byId = memberDao.findById(memberId);
			if (instituteId == byId.getInstitute().getId()) {
				String op = dto.getOp();
				if (op.equalsIgnoreCase("approved")) {
					byId.setStatus(MembershipStatus.APPROVED);
				} else if (op.equalsIgnoreCase("pending")) {
					byId.setStatus(MembershipStatus.PENDING);
				} else if(op.equalsIgnoreCase("rejected")) {
					byId.setStatus(MembershipStatus.REJECTED);
				} else {
					return false;
				}
				return true;
			}
			return false;
		}

		// method delete a member
		public boolean removeMember(long memberId,long instituteId) {
			try {
			return memberDao.removeMember(memberId,instituteId);
			} catch (MemberHandlingException u) {
				u.printStackTrace();
			}
			return false;
		}
		

		/* this method finds the single member  */
		public ResponseEntity<?> findById(long memberId) {
			try {
				return ResponseEntity.ok(memberDao.findById(memberId));
				 // return ResponseEntity.ok(memberId);
			}catch (MemberHandlingException e) {
				 System.err.println(e.getMessage());
				 return ResponseEntity.status(HttpStatus.NOT_FOUND)
						 .body(new MessageDto(e.getMessage(), MessageType.ERROR));
			} catch (NumberFormatException e) {
	            String message = "Invalid Member ID";
	            System.err.println(message);
            return ResponseEntity.badRequest().body(new MessageDto(message, MessageType.ERROR));
			}
		}

		
		
		
//		 try {
//	            Institute institute = instituteDao.findById(Long.parseLong(instituteId));
//	          
//	        } catch (RecordNotFoundException e) {
//	            System.err.println(e.getMessage());
//	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//	                    .body(new MessageDto(e.getMessage(), MessageType.ERROR));
//	        } catch (NumberFormatException e) {
//	            String message = "Invalid institute ID";
//	            System.err.println(message);
//	            return ResponseEntity.badRequest().body(new MessageDto(message, MessageType.ERROR));
//	        }
//	    }
		
		
		
		
		
		
		
		/* this method finds the single member  */
		public List<Member> findByPassOutYear(Year year) {
			List<Member> m=new ArrayList<Member>();
			try {
				m = memberDao.findByPassOutYear(year);
			} catch (MemberHandlingException u) {
				u.printStackTrace();
			}
			return m;
		}

	public ResponseEntity<?> findApprovedMembers(String instituteId) {
		try {
			long id = Long.parseLong(instituteId);
			List<MemberListDTO> approvedMembers = memberDao.findApprovedMembers(id);
			return ResponseEntity.ok(approvedMembers);
		} catch (NumberFormatException e) {
			return ResponseUtils.badRequestResponse(ResponseUtils.INVALID_ID_MESSAGE);
		}
	}
}
