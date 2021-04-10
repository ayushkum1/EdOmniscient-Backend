package com.brainsinjars.projectbackend.controller;

import com.brainsinjars.projectbackend.constant.HasRole;
import com.brainsinjars.projectbackend.dto.CompanyDto;
import com.brainsinjars.projectbackend.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

/**
 * @author Nilesh Pandit
 * @since 10-03-2021
 */

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyService service;

    @Autowired
    public CompanyController(CompanyService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllCompanies() {
        return service.findAllCompanies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCompany(@PathVariable String id) {
        return service.findCompanyUsingId(id);
    }

    @Secured({HasRole.INSTITUTE_ADMIN, HasRole.ROOT})
    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createNewCompany(@RequestBody CompanyDto company) {
        return service.addNewCompany(company);
    }

    @Secured(HasRole.ROOT)
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateCompany(@PathVariable String id, @RequestBody CompanyDto company) {
        return service.updateCompany(id, company);
    }

    @Secured(HasRole.ROOT)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable String id) {
        return service.deleteCompany(id);
    }
}
