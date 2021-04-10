package com.brainsinjars.projectbackend.dao;

import com.brainsinjars.projectbackend.dto.InstituteIdNameDto;
import com.brainsinjars.projectbackend.dto.InstituteProfileDto;
import com.brainsinjars.projectbackend.dto.InstitutesDto;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.exceptions.UserNotFoundException;
import com.brainsinjars.projectbackend.pojo.*;
import com.brainsinjars.projectbackend.repository.views.InstituteRegionView;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author Nilesh Pandit
 * @since 06-03-2021
 */

@Repository
public interface InstituteDao {
    boolean existsById(long instituteId);
    boolean existsByInstituteAdmin(String email);
    Long findByInstituteAdmin(String email) throws RecordNotFoundException;
    List<InstituteIdNameDto> findAllInstitutesIdName();
    List<Institute> findAll();
    Institute findById(long instituteId) throws RecordNotFoundException;
    InstituteProfileDto findInstituteById(long instituteId) throws RecordNotFoundException;
    String findInstituteAdminEmail(long id) throws UserNotFoundException;
    List<InstituteRegionView> findAllInstitutesByRegion();
    List<InstitutesDto> findInstitutes(List<Long> courseIds, String region, String city, String name);
    boolean createNewInstitute(Institute institute);
    boolean updateInstitute(long instituteId, InstituteProfileDto dto, User instituteAdmin) throws RecordNotFoundException;
    boolean deleteInstituteById(long instituteId);
    boolean addCourses(Set<Course> courses, long instituteId) throws RecordNotFoundException;
    boolean removeCourses(Set<Course> courses, long instituteId) throws RecordNotFoundException;
    boolean addPlacements(Placement placement, long instituteId) throws RecordNotFoundException;
    boolean removePlacements(Placement placement, long instituteId) throws RecordNotFoundException;
    boolean addCompanies(Set<Company> companies, long instituteId) throws RecordNotFoundException;
    boolean removeCompanies(Set<Company> companies, long instituteId) throws RecordNotFoundException;
    boolean addMembers(Member member, long instituteId) throws RecordNotFoundException;
    boolean removeMembers(Member member, long instituteId) throws RecordNotFoundException;
    boolean addReviews(Review review, long instituteId) throws RecordNotFoundException;
    boolean removeReviews(Review review, long instituteId) throws RecordNotFoundException;
    boolean addMedia(Media media, long instituteId) throws RecordNotFoundException;
    boolean removeMedia(Media media, long instituteId) throws RecordNotFoundException;
    boolean addAcademicCalendar(AcademicCalendar academicCalendar, long instituteId) throws RecordNotFoundException;
    boolean removeAcademicCalendar(AcademicCalendar academicCalendar, long instituteId) throws RecordNotFoundException;
}
