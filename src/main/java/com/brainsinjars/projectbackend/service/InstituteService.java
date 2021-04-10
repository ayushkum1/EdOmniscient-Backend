package com.brainsinjars.projectbackend.service;

import com.brainsinjars.projectbackend.dao.CompanyDao;
import com.brainsinjars.projectbackend.dao.InstituteDao;
import com.brainsinjars.projectbackend.dao.LocationDao;
import com.brainsinjars.projectbackend.dao.UserDao;
import com.brainsinjars.projectbackend.dto.*;
import com.brainsinjars.projectbackend.exceptions.RecordAlreadyExistsException;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.exceptions.UserNotFoundException;
import com.brainsinjars.projectbackend.pojo.*;
import com.brainsinjars.projectbackend.repository.CourseRepository;
import com.brainsinjars.projectbackend.repository.views.InstituteRegionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.brainsinjars.projectbackend.util.ResponseUtils.*;

/**
 * @author Nilesh Pandit
 * @since 06-03-2021
 */

@Service
@Transactional
public class InstituteService {
    private final InstituteDao instituteDao;
    private final LocationDao locationDao;
    private final CourseRepository courseRepository;
    private final CompanyDao companyDao;
    private final UserDao userDao;

    @Autowired
    public InstituteService(InstituteDao instituteDao,
                            CourseRepository courseRepository,
                            LocationDao locationDao, CompanyDao companyDao, UserDao userDao) {
        this.instituteDao = instituteDao;
        this.courseRepository = courseRepository;
        this.locationDao = locationDao;
        this.companyDao = companyDao;
        this.userDao = userDao;
    }

    public ResponseEntity<List<Institute>> findAll() {
        return ResponseEntity.ok(instituteDao.findAll());
    }

    public ResponseEntity<?> getAllInstitutesIdName() {
        return ResponseEntity.ok(instituteDao.findAllInstitutesIdName());
    }

    public ResponseEntity<?> findById(String instituteId) {
        try {
            Institute institute = instituteDao.findById(Long.parseLong(instituteId));
            return ResponseEntity.ok(institute);
        } catch (RecordNotFoundException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageDto(e.getMessage(), MessageType.ERROR));
        } catch (NumberFormatException e) {
            String message = "Invalid institute ID";
            System.err.println(message);
            return ResponseEntity.badRequest().body(new MessageDto(message, MessageType.ERROR));
        }
    }

    // Returns a projection containing only Institute Id, Name and Region
    public ResponseEntity<?> findAllInstitutesByRegion() {
        try {
            List<InstituteRegionView> insts = instituteDao.findAllInstitutesByRegion();

            System.out.println(insts);

            HashMap<String, List<InstituteRegionView>> map = new HashMap<>();
            insts.stream().distinct().forEach(irv -> map.put(irv.getRegion(), new ArrayList<>()));

            insts.forEach(instituteRegionView -> map.get(instituteRegionView.getRegion()).add(instituteRegionView));

            return ResponseEntity.ok(map);

        } catch (Exception e) {
            return internalServerErrorResponse(e.getMessage());
        }
    }

    public ResponseEntity<?> findInstitutes(List<String> courseIds, String region, String city, String name) {
        try {
            List<Long> ids = null;
            if (courseIds != null)
                ids = courseIds.stream().mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
            List<InstitutesDto> institutes = instituteDao.findInstitutes(
                    ids,
                    StringUtils.hasText(region) ? region : null,
                    StringUtils.hasText(city) ? city : null,
                    StringUtils.hasText(name) ? name : null
            );
            return ResponseEntity.ok(institutes);
        } catch (NumberFormatException e) {
            return badRequestResponse(INVALID_ID_MESSAGE);
        }
    }

    public ResponseEntity<?> findInstituteById(String id) {
        try {
            InstituteProfileDto institute = instituteDao.findInstituteById(Long.parseLong(id));
            return ResponseEntity.ok(institute);
        } catch (RecordNotFoundException e) {
            return badRequestResponse(e.getMessage());
        } catch (NumberFormatException e) {
            return badRequestResponse(INVALID_ID_MESSAGE);
        }
    }

    public ResponseEntity<?> getGalleryThumbs(String id) {
        try {
            long instId = Long.parseLong(id);

            List<String> thumbs = instituteDao.findById(instId).getMedia()
                    .stream()
                    .filter(media -> media.getMediaType().equals(MediaType.IMAGE))
                    .unordered().limit(4)
                    .map(Media::getUrl).collect(Collectors.toList());

            return ResponseEntity.ok(thumbs);
        } catch (RecordNotFoundException e) {
            return badRequestResponse(e.getMessage());
        } catch (NumberFormatException e) {
            return badRequestResponse(INVALID_ID_MESSAGE);
        } catch (Exception e) {
            return internalServerErrorResponse(e.getMessage());
        }
    }

    public ResponseEntity<?> createNewInstitute(InstituteProfileDto dto) {
        try {
            if (instituteDao.existsById(dto.getInstituteId()))
                throw new RecordAlreadyExistsException("Institute with ID: '" + dto.getInstituteId() + "' already exists");
            if (instituteDao.existsByInstituteAdmin(dto.getInstituteAdmin()))
                throw new RecordAlreadyExistsException("Admin '" + dto.getInstituteAdmin() + "' is already managing some other institute.");

            if (dto.getInstituteAdmin() == null)
                throw new UserNotFoundException("Institute admin email must not be null");
            User instituteAdmin = userDao.findByEmail(dto.getInstituteAdmin());
            if (!instituteAdmin.getRole().equals(Role.INSTITUTE_ADMIN))
                throw new SQLException("User must have role of " + Role.INSTITUTE_ADMIN + " to be set as Admin to an institute");

            Location location = dto.getLocation();
            Geography g = location.getGeography();
            location = locationDao.saveLocation(location.getStreetAddr(), g.getCity(),
                    g.getState(), g.getPinCode(), g.getRegion());

            Institute institute = new Institute(
                    dto.getInstituteId(),
                    dto.getName(),
                    dto.getNick(),
                    Optional.ofNullable(dto.getAbout()).orElse("Write something about this institute here that describes this institute"),
                    "Something about placements that describes the institute's placement agenda",
                    dto.getProfilePicUrl() == null || dto.getProfilePicUrl().isEmpty() ? "Profile picture url" : dto.getProfilePicUrl(),
                    dto.getCoverPicUrl() == null || dto.getCoverPicUrl().isEmpty() ? "Cover picture url" : dto.getCoverPicUrl(),
                    location,
                    instituteAdmin);

            if (instituteDao.createNewInstitute(institute))
                return successResponse("New institute created successfully!");
            else
                throw new Exception("Could not create new institute!");
        } catch (RecordAlreadyExistsException | UserNotFoundException | SQLException e) {
            return badRequestResponse(e.getMessage());
        } catch (Exception e) {
            return internalServerErrorResponse(e.getMessage());
        }
    }

    public ResponseEntity<?> updateInstitute(String instituteId, InstituteProfileDto dto) {
        try {
            // if institute admin email in dto is null then no need to update it or even check its role.
            String instAdmin = dto.getInstituteAdmin();
            User instituteAdmin = null;
            if (instAdmin != null) {
                instituteAdmin = userDao.findByEmail(instAdmin);
                if (!instituteAdmin.getRole().equals(Role.INSTITUTE_ADMIN))
                    throw new SQLException("User must have role of " + Role.INSTITUTE_ADMIN + " to be set a Admin to an institute");
            }
            boolean updated = instituteDao.updateInstitute(Long.parseLong(instituteId), dto, instituteAdmin);
            if (updated)
                return successResponse("Institute updated successfully");
            else
                return internalServerErrorResponse("Institute update failed");

        } catch (RecordNotFoundException | UserNotFoundException | SQLException e) {
            return badRequestResponse(e.getMessage());
        } catch (NumberFormatException e) {
            return badRequestResponse(INVALID_ID_MESSAGE);
        }
    }

    public ResponseEntity<?> deleteInstituteById(String instituteId) {
        try {
            if (instituteDao.deleteInstituteById(Long.parseLong(instituteId)))
                return successResponse("Institute deleted successfully!");
            else
                throw new RecordNotFoundException("Institute by ID '" + instituteId + "' does not exist or it is already deleted");
        } catch (NumberFormatException e) {
            return badRequestResponse(e.getMessage());
        } catch (RecordNotFoundException e) {
            return notFoundResponse(e.getMessage());
        } catch (Exception e) {
            return internalServerErrorResponse(e.getMessage());
        }
    }

    public ResponseEntity<?> addCoursesToInstitute(String instituteId, Set<String> courseIds) {
        try {
            Set<Course> courses = courseRepository.findCoursesByIds(convertSetType(courseIds, Long::parseLong));
            boolean allAdded = instituteDao.addCourses(courses, Long.parseLong(instituteId));
            if (allAdded)
                return successResponse("Courses added successfully!");
            else
                return infoResponse("All available courses were added successfully!");
        } catch (RecordNotFoundException e) {
            // Institute not found here
            return notFoundResponse(e.getMessage());
        } catch (NumberFormatException e) {
            // Invalid institute id is unacceptable
            return badRequestResponse(INVALID_ID_MESSAGE);
        } catch (Exception e) {
            // Catch any general exception
            return internalServerErrorResponse(e.getMessage());
        }
    }

    public ResponseEntity<?> removeCoursesFromInstitute(String instituteId, Set<String> courseIds) {
        try {
            Set<Course> courses = courseRepository.findCoursesByIds(convertSetType(courseIds, Long::parseLong));
            boolean allRemoved = instituteDao.removeCourses(courses, Long.parseLong(instituteId));
            if (allRemoved)
                return successResponse("Courses removed successfully!");
            else
                return warningResponse("Couldn't remove some courses!");
        } catch (RecordNotFoundException e) {
            // Institute not found here
            return notFoundResponse(e.getMessage());
        } catch (NumberFormatException e) {
            // Invalid institute id is unacceptable
            return badRequestResponse(INVALID_ID_MESSAGE);
        } catch (Exception e) {
            // Catch any general exception
            return internalServerErrorResponse(e.getMessage());
        }
    }

    public ResponseEntity<?> findCompaniesByInstituteId(String instituteId) {
        try {
            Set<CompanyDto> companies = companyDao.findAllByInstituteId(Long.parseLong(instituteId));
            return ResponseEntity.ok(companies);
        } catch (NumberFormatException e) {
            return badRequestResponse(INVALID_ID_MESSAGE);
        }
    }

    public ResponseEntity<?> addCompanies(String instituteId, Set<String> companyIds) {
        try {
            Set<Company> companies = companyDao.findCompaniesByIds(convertSetType(companyIds, Long::parseLong));
            boolean allAdded = instituteDao.addCompanies(companies, Long.parseLong(instituteId));
            if (allAdded)
                return successResponse("Companies added successfully!");
            else
                return infoResponse("All available companies added successfully!");
        } catch (RecordNotFoundException e) {
            return notFoundResponse(e.getMessage());
        } catch (NumberFormatException e) {
            return badRequestResponse(INVALID_ID_MESSAGE);
        }
    }

    public ResponseEntity<?> removeCompanies(String instituteId, Set<String> companyIds) {
        try {
            Set<Company> companies = companyDao.findCompaniesByIds(convertSetType(companyIds, Long::parseLong));
            boolean allRemoved = instituteDao.removeCompanies(companies, Long.parseLong(instituteId));
            if (allRemoved)
                return successResponse("Companies removed successfully!");
            else
                return warningResponse("Couldn't remove some companies!");
        } catch (RecordNotFoundException e) {
            return notFoundResponse(e.getMessage());
        } catch (NumberFormatException e) {
            return badRequestResponse(INVALID_ID_MESSAGE);
        }
    }

    public String getInstituteAdminEmail(String instituteId) {
        try {
            return instituteDao.findInstituteAdminEmail(Long.parseLong(instituteId));
        } catch (UserNotFoundException | NumberFormatException e) {
            return null;
        }
    }

    private <T, R> Set<R> convertSetType(Set<T> tSet, Function<T, R> converterFunction) {
        HashSet<R> rSet = new HashSet<>();
        tSet.forEach(s -> rSet.add(converterFunction.apply(s)));
        return rSet;
    }

    // move this to users controller, service etc
    public ResponseEntity<?> findAllAdmins() {
        Set<String> adminEmails = userDao.findByRole(Role.INSTITUTE_ADMIN).stream().map(User::getEmail).collect(Collectors.toSet());
        return ResponseEntity.ok(adminEmails);
    }

    public ResponseEntity<?> findByInstituteAdmin(String email) {
        try {
            return ResponseEntity.ok(instituteDao.findByInstituteAdmin(email));
        } catch (RecordNotFoundException e) {
            return notFoundResponse(e.getMessage());
        }
    }
}
