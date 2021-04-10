package com.brainsinjars.projectbackend.dao;

import com.brainsinjars.projectbackend.dto.CompanyDto;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.pojo.Company;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author Nilesh Pandit
 * @since 10-03-2021
 */

@Repository
public interface CompanyDao {
    Set<CompanyDto> findAll();
    Company findById(long companyId) throws RecordNotFoundException;
    CompanyDto findCompanyUsingId(long companyId) throws RecordNotFoundException;
    Set<Company> findCompaniesByIds(Set<Long> ids);
    Set<CompanyDto> findAllByInstituteId(long instituteId);
    boolean existsById(long companyId);
    long createNewCompany(Company company);
    boolean updateCompany(long id, CompanyDto companyDto) throws RecordNotFoundException;
    boolean deleteCompany(long companyId);
}
