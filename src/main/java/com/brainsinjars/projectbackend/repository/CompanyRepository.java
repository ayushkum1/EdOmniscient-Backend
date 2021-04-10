package com.brainsinjars.projectbackend.repository;

import com.brainsinjars.projectbackend.dto.CompanyDto;
import com.brainsinjars.projectbackend.pojo.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * @author Nilesh Pandit
 * @since 06-03-2021
 */

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("select new com.brainsinjars.projectbackend.dto.CompanyDto(c.id, c.name, c.logoPath, c.websiteLink) " +
            "from Company c")
    Set<CompanyDto> findAllCompanies();

    @Query("select new com.brainsinjars.projectbackend.dto.CompanyDto(c.id, c.name, c.logoPath, c.websiteLink) " +
            "from Company c where c.id = :id")
    Optional<CompanyDto> findCompanyUsingId(@Param("id") long id);

    @Query("select new com.brainsinjars.projectbackend.dto.CompanyDto(c.id, c.name, c.logoPath, c.websiteLink) " +
            "from Company c join c.institutes i where i.id = :instituteId")
    Set<CompanyDto> findAllUsingInstituteId(@Param("instituteId") long instituteId);

    @Query("select c from Company c where c.id in :ids")
    Set<Company> findCompaniesByIds(@Param("ids") Set<Long> ids);
}
