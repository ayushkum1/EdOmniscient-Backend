package com.brainsinjars.projectbackend.service;

import com.brainsinjars.projectbackend.dao.CompanyDao;
import com.brainsinjars.projectbackend.dto.CompanyDto;
import com.brainsinjars.projectbackend.dto.MessageType;
import com.brainsinjars.projectbackend.exceptions.RecordAlreadyExistsException;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.pojo.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Set;

import static com.brainsinjars.projectbackend.util.ResponseUtils.*;

@Service
@Transactional
public class CompanyService {
    private final CompanyDao companyDao;

    @Autowired
    public CompanyService(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    public ResponseEntity<?> findAllCompanies() {
        try {
            Set<CompanyDto> all = companyDao.findAll();
            if (all.size() > 0)
                return ResponseEntity.ok(all);
            else
                return notFoundResponse("No companies found!");
        } catch (Exception e) {
            return internalServerErrorResponse(e.getMessage());
        }
    }

    public ResponseEntity<?> findCompanyUsingId(String companyId) {
        try {
            return ResponseEntity.ok(companyDao.findCompanyUsingId(Long.parseLong(companyId)));
        } catch (RecordNotFoundException e) {
            return badRequestResponse(e.getMessage());
        } catch (NumberFormatException e) {
            return badRequestResponse(INVALID_ID_MESSAGE);
        }
    }

    public ResponseEntity<?> addNewCompany(CompanyDto dto) {
        try {
            if (companyDao.existsById(dto.getId()))
                throw new RecordAlreadyExistsException("Company with ID: '" + dto.getId() + "' already exists");
            Company company = new Company(dto.getId(), dto.getName(), dto.getWebsiteUrl(), dto.getLogoUrl());

            HashMap<String, String> resp = new HashMap<>();
            resp.put("companyId", String.valueOf(companyDao.createNewCompany(company)));
            resp.put("messageType", MessageType.SUCCESS.name());
            return ResponseEntity.ok(resp);
        } catch (RecordAlreadyExistsException e) {
            return badRequestResponse(e.getMessage());
        } catch (Exception e) {
            return internalServerErrorResponse(e.getMessage());
        }
    }

    public ResponseEntity<?> updateCompany(String id, CompanyDto dto) {
        try {
            if (companyDao.updateCompany(Long.parseLong(id), dto))
                return successResponse("Company updated successfully");
            else
                throw new Exception("Could not update company");
        } catch (RecordNotFoundException e) {
            return badRequestResponse(e.getMessage());
        } catch (Exception e) {
            return internalServerErrorResponse(e.getMessage());
        }
    }

    public ResponseEntity<?> deleteCompany(String companyId) {
        try {
            if (companyDao.deleteCompany(Long.parseLong(companyId)))
                return successResponse("Company deleted successfully");
            else
                return notFoundResponse("Company with ID '" + companyId + "' does not exist or it is already deleted");
        } catch (NumberFormatException e) {
            return badRequestResponse(INVALID_ID_MESSAGE);
        } catch (Exception e) {
            return internalServerErrorResponse(e.getMessage());
        }
    }
}
