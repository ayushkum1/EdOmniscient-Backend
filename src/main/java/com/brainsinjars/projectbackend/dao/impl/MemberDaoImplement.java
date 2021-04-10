/*
 * @Author Channappa Mirgale
 * @Since 09-03-21
 */
package com.brainsinjars.projectbackend.dao.impl;

import com.brainsinjars.projectbackend.dao.IMemberDao;
import com.brainsinjars.projectbackend.dto.MemberAddDTO;
import com.brainsinjars.projectbackend.dto.MemberListDTO;
import com.brainsinjars.projectbackend.dto.MemberUpdateDTO;
import com.brainsinjars.projectbackend.exceptions.MemberHandlingException;
import com.brainsinjars.projectbackend.pojo.*;
import com.brainsinjars.projectbackend.repository.CourseRepository;
import com.brainsinjars.projectbackend.repository.InstituteRepository;
import com.brainsinjars.projectbackend.repository.MemberRepository;
import com.brainsinjars.projectbackend.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PreRemove;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Component
public class MemberDaoImplement implements IMemberDao {

    private MemberRepository memberRepository;
    private CourseRepository courseRepository;
    private UserRepository userRepository;
    private InstituteRepository instituteRepository;

    @Autowired
    public MemberDaoImplement(MemberRepository memberRepository, UserRepository userRepository,
                              CourseRepository courseRepository, InstituteRepository instituteRepository) {
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.instituteRepository = instituteRepository;
    }

    // Dao for all list of members
    @Override
    public List<MemberListDTO> findAllMembers(long instituteId) {
        List<MemberListDTO> dto = new ArrayList<>();
        Institute institute = instituteRepository.findById(instituteId)
                .orElseThrow(() -> new MemberHandlingException("Institute Record Not found!!!!"));

        //iterate throught the list of members from institute
        institute.getMembers().forEach(m -> {
            MemberListDTO memberDTO = new MemberListDTO();
            User u = m.getUser();
            Course course = m.getCourse();
            BeanUtils.copyProperties(m, memberDTO);
            memberDTO.setId(m.getId());
            memberDTO.setName(u.getFirstName() + " " + u.getLastName());
            memberDTO.setUserEmail(m.getUser().getEmail());
            memberDTO.setAbout(u.getAbout());
            memberDTO.setProfilePic(u.getProfilePicLink());
            memberDTO.setCourseId(course.getId());
            dto.add(memberDTO);
        });
        return dto;
    }

    // Dao implement to Add member in the members
    @Override
    public boolean addMember(MemberAddDTO member, long instituteId) {
        Member newMember = new Member();
        Course course = courseRepository.findById(member.getCourseId())
                .orElseThrow(() -> new MemberHandlingException("Course Not found"));

        Institute institute = instituteRepository.findById(instituteId)
                .orElseThrow(() -> new MemberHandlingException("Institute Not found"));

        User user = userRepository.findById(member.getUserEmail())
                .orElseThrow(() -> new MemberHandlingException("User Not found"));
        user.setRole(Role.MEMBER);
        System.out.println("member : " + member.getYear());
        BeanUtils.copyProperties(member, newMember);
        //new request for members recieves we give pending status to the Member
        newMember.setStatus(MembershipStatus.PENDING);
        newMember.setCourse(course);
        newMember.setInstitute(institute);
        newMember.setUser(user);
        System.out.println("new member : " + newMember.getYear());
        if (memberRepository.save(newMember) != null)
            return true;

        return false;
    }

    // Dao implement to remove member
    @Override
    public boolean removeMember(long memberId, long instituteId) throws MemberHandlingException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandlingException("Member Not Found"));
        User user = member.getUser();
        Institute institute = member.getInstitute();
        if (institute.getId() == instituteId) {
            user.setRole(Role.USER);
            memberRepository.deleteById(memberId);
            return true;
        }
        return false;
    }

    @Override
    public List<MemberListDTO> findApprovedMembers(long id) {
        return memberRepository.findApprovedMembers(id);
    }

    // Dao implement to update the details of member
    @Override
    public boolean updateMember(long memberId, MemberUpdateDTO member, long instituteId)
            throws MemberHandlingException {
        Member memberOP = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandlingException("Member Not Found"));
        if (memberOP.getInstitute().getId() == instituteId) {
            BeanUtils.copyProperties(member, memberOP);
            return true;
        }
        return false;
    }

    @Override
    public List<Member> findByPassOutYear(Year year) throws MemberHandlingException {
        return memberRepository.findByPassOutYear(year)
                .orElseThrow(() -> new MemberHandlingException("Details of Member not found"));
    }

    @Override
    public Member findById(long memberId) throws MemberHandlingException {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandlingException("Member Not Found!!!"));


    }
}
