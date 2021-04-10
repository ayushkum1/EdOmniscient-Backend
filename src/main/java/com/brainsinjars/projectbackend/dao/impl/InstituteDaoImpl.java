package com.brainsinjars.projectbackend.dao.impl;

import com.brainsinjars.projectbackend.dao.InstituteDao;
import com.brainsinjars.projectbackend.dao.LocationDao;
import com.brainsinjars.projectbackend.dto.CourseIdNameDTO;
import com.brainsinjars.projectbackend.dto.InstituteIdNameDto;
import com.brainsinjars.projectbackend.dto.InstituteProfileDto;
import com.brainsinjars.projectbackend.dto.InstitutesDto;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.exceptions.UserNotFoundException;
import com.brainsinjars.projectbackend.pojo.*;
import com.brainsinjars.projectbackend.repository.CourseRepository;
import com.brainsinjars.projectbackend.repository.InstituteRepository;
import com.brainsinjars.projectbackend.repository.views.InstituteRegionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author Nilesh Pandit
 * @since 06-03-2021
 */

@Component
public class InstituteDaoImpl implements InstituteDao {
    private final InstituteRepository repository;
    private final LocationDao locationDao;
    private final CourseRepository courseRepository;

    @Autowired
    public InstituteDaoImpl(InstituteRepository repository,
                            LocationDao locationDao,
                            CourseRepository courseRepository) {
        this.repository = repository;
        this.locationDao = locationDao;
        this.courseRepository = courseRepository;
    }

    @Override
    public boolean existsById(long instituteId) {
        return repository.existsById(instituteId);
    }

    @Override
    public boolean existsByInstituteAdmin(String email) {
        return repository.existsByInstituteAdmin(email);
    }

    @Override
    public Long findByInstituteAdmin(String email) throws RecordNotFoundException {
        return repository.findByInstituteAdmin(email)
                .orElseThrow(() -> new RecordNotFoundException("No institute found for you to manage"));
    }

    @Override
    public List<InstituteIdNameDto> findAllInstitutesIdName() {
        return repository.findAllInstitutesIdName();
    }

    @Override
    public List<Institute> findAll() {
        return repository.findAll();
    }

    @Override
    public Institute findById(long instituteId) throws RecordNotFoundException {
        return getCurrentInstitute(instituteId);
    }

    @Override
    public InstituteProfileDto findInstituteById(long instituteId) throws RecordNotFoundException {
        return getCurrentInstitute(instituteId, true);
    }

    @Override
    public List<InstituteRegionView> findAllInstitutesByRegion() {
        return repository.findAllInstitutesWithRegion();
    }

    @Override
    public List<InstitutesDto> findInstitutes(List<Long> courseIds, String region, String city, String name) {
        List<InstitutesDto> institutes = repository.findInstitutes(courseIds, region, city, name);
        return institutes.stream().peek(institute -> {
            List<CourseIdNameDTO> courses = courseRepository.getAllCoursesByInstituteId(institute.getInstituteId());
            institute.setCourses(new HashSet<>(courses));
        }).collect(Collectors.toList());
    }

    @Override
    public boolean createNewInstitute(Institute institute) {
        repository.save(institute);
        return true;
    }

    @Override
    public boolean updateInstitute(long instituteId, InstituteProfileDto dto, User instituteAdmin) throws RecordNotFoundException {
        Institute institute = getCurrentInstitute(instituteId);

        institute.setName(Optional.ofNullable(dto.getName()).orElse(institute.getName()));
        institute.setNick(Optional.ofNullable(dto.getNick()).orElse(institute.getNick()));
        institute.setAbout(Optional.ofNullable(dto.getAbout()).orElse(institute.getAbout()));
        institute.setAboutPlacements(Optional.ofNullable(dto.getAboutPlacements()).orElse(institute.getAboutPlacements()));
        institute.setProfilePicUrl(Optional.ofNullable(dto.getProfilePicUrl()).orElse(institute.getProfilePicUrl()));
        institute.setCoverPicUrl(Optional.ofNullable(dto.getCoverPicUrl()).orElse(institute.getCoverPicUrl()));

        Location oldLocation = dto.getLocation();
        if (oldLocation != null)
            oldLocation.setId(institute.getLocation().getId());

        Location newLocation = locationDao.updateLocation(Optional.ofNullable(oldLocation).orElse(institute.getLocation()));
        institute.setLocation(newLocation);

        institute.setInstituteAdmin(Optional.ofNullable(instituteAdmin).orElse(institute.getInstituteAdmin()));

        return true;
    }

    @Override
    public boolean deleteInstituteById(long instituteId) {
        boolean exists = repository.existsById(instituteId);
        if (exists) repository.deleteById(instituteId);
        return exists;
    }

    @Override
    public boolean addCourses(Set<Course> courses, long instituteId) throws RecordNotFoundException {
        Institute currentInstitute = getCurrentInstitute(instituteId);
        AtomicBoolean allAdded = new AtomicBoolean(true);
        // allRemoved latches to false if one of the remove operation fails.
        courses.forEach(course -> allAdded.set(allAdded.get() && currentInstitute.addCourse(course)));
        return allAdded.get();
    }

    @Override
    public boolean removeCourses(Set<Course> courses, long instituteId) throws RecordNotFoundException {
        Institute currentInstitute = getCurrentInstitute(instituteId);
        AtomicBoolean allRemoved = new AtomicBoolean(true);
        // allRemoved latches to false if one of the remove operation fails.
        courses.forEach(course -> allRemoved.set(allRemoved.get() && currentInstitute.removeCourse(course)));
        return allRemoved.get();
    }

    @Override
    public boolean addPlacements(Placement placement, long instituteId) throws RecordNotFoundException {
        return getCurrentInstitute(instituteId).addPlacement(placement);
    }

    @Override
    public boolean removePlacements(Placement placement, long instituteId) throws RecordNotFoundException {
        return getCurrentInstitute(instituteId).removePlacement(placement);
    }

    @Override
    public boolean addCompanies(Set<Company> companies, long instituteId) throws RecordNotFoundException {
        Institute currentInstitute = getCurrentInstitute(instituteId);
        AtomicBoolean allAdded = new AtomicBoolean(true);
        companies.forEach(company -> allAdded.set(allAdded.get() && currentInstitute.addCompany(company)));
        return allAdded.get();
    }

    @Override
    public boolean removeCompanies(Set<Company> companies, long instituteId) throws RecordNotFoundException {
        Institute currentInstitute = getCurrentInstitute(instituteId);
        AtomicBoolean allRemoved = new AtomicBoolean(true);
        companies.forEach(company -> allRemoved.set(allRemoved.get() && currentInstitute.removeCompany(company)));
        return allRemoved.get();
    }

    @Override
    public boolean addMembers(Member member, long instituteId) throws RecordNotFoundException {
        return getCurrentInstitute(instituteId).addMember(member);
    }

    @Override
    public boolean removeMembers(Member member, long instituteId) throws RecordNotFoundException {
        return getCurrentInstitute(instituteId).removeMember(member);
    }

    @Override
    public boolean addReviews(Review review, long instituteId) throws RecordNotFoundException {
        return getCurrentInstitute(instituteId).addReview(review);
    }

    @Override
    public boolean removeReviews(Review review, long instituteId) throws RecordNotFoundException {
        return getCurrentInstitute(instituteId).removeReview(review);
    }

    @Override
    public boolean addMedia(Media media, long instituteId) throws RecordNotFoundException {
        return getCurrentInstitute(instituteId).addMedia(media);
    }

    @Override
    public boolean removeMedia(Media media, long instituteId) throws RecordNotFoundException {
        return getCurrentInstitute(instituteId).removeMedia(media);
    }

    @Override
    public boolean addAcademicCalendar(AcademicCalendar academicCalendar, long instituteId) throws RecordNotFoundException {
        return getCurrentInstitute(instituteId).addAcademicCalendar(academicCalendar);
    }

    @Override
    public boolean removeAcademicCalendar(AcademicCalendar academicCalendar, long instituteId) throws RecordNotFoundException {
        return getCurrentInstitute(instituteId).removeAcademicCalendar(academicCalendar);
    }

    @Override
    public String findInstituteAdminEmail(long id) throws UserNotFoundException {
        return repository.findInstituteAdminEmail(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    private Institute getCurrentInstitute(long instituteId) throws RecordNotFoundException {
        return getCurrentInstitute(instituteId, false);
    }

    @SuppressWarnings("unchecked")
    private <T> T getCurrentInstitute(long instituteId, boolean isProjection) throws RecordNotFoundException {
        if (isProjection)
            return (T) repository.findInstituteUsingId(instituteId)
            .orElseThrow(() -> new RecordNotFoundException("Institute with id='" + instituteId + "' not found!"));
        return (T) repository.findById(instituteId)
                .orElseThrow(() -> new RecordNotFoundException("Institute with id='" + instituteId + "' not found!"));
    }
}
