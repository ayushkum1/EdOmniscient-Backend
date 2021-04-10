package com.brainsinjars.projectbackend.controller;

import com.brainsinjars.projectbackend.constant.HasRole;
import com.brainsinjars.projectbackend.dao.UserDao;
import com.brainsinjars.projectbackend.dto.InstituteCompanyPatchDto;
import com.brainsinjars.projectbackend.dto.InstituteCoursePatchDto;
import com.brainsinjars.projectbackend.dto.InstituteProfileDto;
import com.brainsinjars.projectbackend.service.InstituteService;
import com.brainsinjars.projectbackend.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.brainsinjars.projectbackend.util.ResponseUtils.*;

/**
 * @author Nilesh Pandit
 * @since 06-03-2021
 */

//@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/institutes")
public class InstituteController {

    private final InstituteService service;

    @Autowired
    public InstituteController(InstituteService service) {
        this.service = service;
    }

    @GetMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getInstitutes(@RequestParam(value = "courses", required = false) List<String> courses,
                                           @RequestParam(value = "region", required = false) String region,
                                           @RequestParam(value = "city", required = false) String city,
                                           @RequestParam(value = "name", required = false) String name) {
        return service.findInstitutes(courses, region, city, name);
    }

    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getInstituteById(@PathVariable String id) {
        return service.findInstituteById(id);
    }

    @GetMapping(
            value = "/{id}/gallery-thumbs",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getGalleryThumbs(@PathVariable String id) {
        return service.getGalleryThumbs(id);
    }

    @Secured({HasRole.INSTITUTE_ADMIN, HasRole.ROOT})
    @GetMapping(
            value = "/admins/{email}",
            produces=MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getInstituteByAdmin(@PathVariable String email) {
        return service.findByInstituteAdmin(email);
    }

    @Secured(HasRole.ROOT)
    @GetMapping(
            value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getAllInstitutes() {
        return service.findAll();
    }

    @GetMapping(
            value = "/all/short",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getAllInstitutesIdName() {
        return service.getAllInstitutesIdName();
    }

    @Secured(HasRole.ROOT)
    @GetMapping(
            value = "/admins",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getAllAdmins() {
        return service.findAllAdmins();
    }

    @Secured(HasRole.ROOT)
    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createNewInstitute(@RequestBody InstituteProfileDto dto) {
        return service.createNewInstitute(dto);
    }

    @Secured({HasRole.INSTITUTE_ADMIN, HasRole.ROOT})
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateInstitute(@PathVariable String id, @RequestBody InstituteProfileDto dto) {
        if (adminHasAccess(id)) return service.updateInstitute(id, dto);
        else return forbiddenResponse(FORBIDDEN_MESSAGE);
    }

    @Secured(HasRole.ROOT)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteInstitute(@PathVariable String id) {
        return service.deleteInstituteById(id);
    }

    @Secured({HasRole.INSTITUTE_ADMIN, HasRole.ROOT})
    @PatchMapping(value = "/{id}/courses")
    public ResponseEntity<?> patchCourses(@PathVariable String id, @RequestBody InstituteCoursePatchDto dto) {
        if (adminHasAccess(id)) {
            if (dto.getOp().equalsIgnoreCase("add"))
                return service.addCoursesToInstitute(id, dto.getCourses());
            else if (dto.getOp().equalsIgnoreCase("remove"))
                return service.removeCoursesFromInstitute(id, dto.getCourses());
            return badRequestResponse("Invalid operation");
        } else {
            // forbidden
            // You do not have access to this resource
            return forbiddenResponse(FORBIDDEN_MESSAGE);
        }
    }

    @Secured({HasRole.USER, HasRole.MEMBER, HasRole.INSTITUTE_ADMIN, HasRole.ROOT})
    @GetMapping(value = "/{id}/companies")
    public ResponseEntity<?> getCompanies(@PathVariable String id) {
        return service.findCompaniesByInstituteId(id);
    }

    @Secured({HasRole.INSTITUTE_ADMIN, HasRole.ROOT})
    @PatchMapping(value = "/{id}/companies")
    public ResponseEntity<?> patchCompanies(@PathVariable String id, @RequestBody InstituteCompanyPatchDto dto) {
        if (adminHasAccess(id)) {
            if (dto.getOp().equalsIgnoreCase("add"))
                return service.addCompanies(id, dto.getCompanies());
            else if (dto.getOp().equalsIgnoreCase("remove"))
                return service.removeCompanies(id, dto.getCompanies());
            return badRequestResponse("Invalid operation");
        } else {
            return forbiddenResponse(FORBIDDEN_MESSAGE);
        }
    }

    private boolean adminHasAccess(String instituteId) {
        String adminEmail = service.getInstituteAdminEmail(instituteId);
        return SecurityUtils.hasAccess(adminEmail);
    }
}

