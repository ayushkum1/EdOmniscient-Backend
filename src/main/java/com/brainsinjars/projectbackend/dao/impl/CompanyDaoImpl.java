package com.brainsinjars.projectbackend.dao.impl;

import com.brainsinjars.projectbackend.dao.CompanyDao;
import com.brainsinjars.projectbackend.dto.CompanyDto;
import com.brainsinjars.projectbackend.exceptions.RecordNotFoundException;
import com.brainsinjars.projectbackend.pojo.Company;
import com.brainsinjars.projectbackend.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author Nilesh Pandit
 * @since 10-03-2021
 */

@Component
public class CompanyDaoImpl implements CompanyDao {

    private final CompanyRepository repository;

    @Autowired
    public CompanyDaoImpl(CompanyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Set<CompanyDto> findAll() {
        return repository.findAllCompanies();
    }

    @Override
    public Company findById(long companyId) throws RecordNotFoundException {
        return getCompany(companyId, false);
    }

    @Override
    public CompanyDto findCompanyUsingId(long companyId) throws RecordNotFoundException {
        return getCompany(companyId, true);
    }

    @Override
    public Set<Company> findCompaniesByIds(Set<Long> ids) {
        return repository.findCompaniesByIds(ids);
    }

    @Override
    public Set<CompanyDto> findAllByInstituteId(long instituteId) {
        return repository.findAllUsingInstituteId(instituteId);
    }

    @Override
    public boolean existsById(long companyId) {
        return repository.existsById(companyId);
    }

    @Override
    public long createNewCompany(Company company) {
        return repository.save(company).getId();
    }

    @Override
    public boolean updateCompany(long id, CompanyDto companyDto) throws RecordNotFoundException {
        Company company = getCompany(id, false);
        company.setName(companyDto.getName());
        company.setLogoPath(companyDto.getLogoUrl());
        company.setWebsiteLink(companyDto.getWebsiteUrl());
        repository.save(company);
        return true;
    }

    @Override
    public boolean deleteCompany(long companyId) {
        boolean exists = repository.existsById(companyId);
        if (exists) repository.deleteById(companyId);
        return exists;
    }

    @SuppressWarnings("unchecked")
    private <T> T getCompany(long companyId, boolean isProjection) throws RecordNotFoundException {
        if (isProjection)
            return (T) repository.findCompanyUsingId(companyId)
                    .orElseThrow(() -> new RecordNotFoundException("Company with id='" + companyId + "' not found!"));
        return (T) repository.findById(companyId)
                .orElseThrow(() -> new RecordNotFoundException("Company with id='" + companyId + "' not found!"));
    }
}
