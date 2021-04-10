/* 
 * @Author Channappa Mirgale 
 * @Since 09-03-21
*/

package com.brainsinjars.projectbackend.controller;


import com.brainsinjars.projectbackend.constant.HasRole;
import com.brainsinjars.projectbackend.dto.MemberAddDTO;
import com.brainsinjars.projectbackend.dto.MemberPatchDto;
import com.brainsinjars.projectbackend.dto.MemberUpdateDTO;
import com.brainsinjars.projectbackend.service.MemberService;
import com.brainsinjars.projectbackend.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/institutes/{instituteId}")
public class MemberController {

	@Autowired
	private MemberService memberService;

	public MemberController() {
		System.out.print("In Member COntoller");
	}
	
//    @Autowired
//    public MemberController(MemberService memberService) {
//        this.memberService = memberService;
//    }
//    
	@Secured({ HasRole.INSTITUTE_ADMIN, HasRole.MEMBER, HasRole.ROOT, HasRole.USER })
    @GetMapping("/members")
	public ResponseEntity<?> getAllMembers(@PathVariable long instituteId) {
    			System.out.println("in all list members");
		return memberService.findAllMembers(instituteId);
	}

	@Secured({HasRole.USER, HasRole.INSTITUTE_ADMIN,HasRole.MEMBER, HasRole.ROOT})
	@GetMapping("/members/approved")
	public ResponseEntity<?> getApprovedMembers(@PathVariable String instituteId) {
		return memberService.findApprovedMembers(instituteId);
	}
    
    //need course_id for adding a member
	@Secured({ HasRole.INSTITUTE_ADMIN, HasRole.MEMBER, HasRole.ROOT, HasRole.USER })
	@PostMapping("/members")
	public ResponseEntity<?> addMemberDetails(@RequestBody MemberAddDTO member, @PathVariable long instituteId) {
		if(memberService.addMember(member, instituteId))
			 return ResponseEntity.ok("Record has been successfully added");
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
    
    //need course_id for updating a member

      /*method to update the review*/
		@Secured({ HasRole.INSTITUTE_ADMIN, HasRole.MEMBER, HasRole.ROOT})
	    @PutMapping("/members/{id}")
		public ResponseEntity<?> updateMemberDetails(@PathVariable(value = "id") long memberId, @RequestBody
				MemberUpdateDTO  member,@PathVariable long instituteId)  {
			if(memberService.updateMember(memberId, member,instituteId))
				return ResponseEntity.ok("Record has been successfully updated");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		
		@PatchMapping(value = "/members/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		@Secured({ HasRole.INSTITUTE_ADMIN})
		public ResponseEntity<?> approveMembership(@PathVariable String id, @RequestBody MemberPatchDto dto, @PathVariable String instituteId) {
	    	try {
				if (memberService.patchStatus(Long.parseLong(id), dto, Long.parseLong(instituteId))) {
					return ResponseUtils.successResponse("Member status changed to '" + dto.getOp() + "'");
				} else {
					return ResponseUtils.badRequestResponse("Invalid operation requested!");
				}
			} catch (NumberFormatException e) {
				return ResponseUtils.badRequestResponse(ResponseUtils.INVALID_ID_MESSAGE);
			} catch (Exception e) {
	    		return ResponseUtils.internalServerErrorResponse(e.getMessage());
			}
		}
    
	//method to delete the member using member id
	@Secured({HasRole.INSTITUTE_ADMIN,HasRole.ROOT})
	@DeleteMapping("/members/{id}")
	public ResponseEntity<?> deleteMemberDetails(@PathVariable String id, @PathVariable String instituteId) {
		try {
			if (memberService.removeMember(Long.parseLong(id), Long.parseLong(instituteId)))
				return ResponseUtils.successResponse("Record successfully deleted");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (NumberFormatException e) {
			return ResponseUtils.badRequestResponse(ResponseUtils.INVALID_ID_MESSAGE);
		}
	}
}
