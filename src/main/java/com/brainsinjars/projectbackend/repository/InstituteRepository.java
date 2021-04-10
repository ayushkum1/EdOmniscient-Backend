package com.brainsinjars.projectbackend.repository;

import com.brainsinjars.projectbackend.dto.InstituteIdNameDto;
import com.brainsinjars.projectbackend.dto.InstituteProfileDto;
import com.brainsinjars.projectbackend.dto.InstitutesDto;
import com.brainsinjars.projectbackend.pojo.Institute;
import com.brainsinjars.projectbackend.repository.views.InstituteRegionView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author nilesh
 * @since 06-03-2021
 */

@Repository
public interface InstituteRepository extends JpaRepository<Institute, Long> {

    boolean existsById(long id);

    @Query("select case when count(i.id) > 0 then true else false end " +
            "from Institute i where i.instituteAdmin.email = :email")
    boolean existsByInstituteAdmin(@Param("email") String email);

    @Query("select i.id from Institute i where i.instituteAdmin.email = :email")
    Optional<Long> findByInstituteAdmin(String email);

    // Use the result set of this query to filter by region using java streams
    @Query("select new com.brainsinjars.projectbackend.repository.views.InstituteRegionView(i.id, i.name, g.region) " +
            "from Institute i inner join Location l on i.location = l inner join Geography g on l.geography = g")
    List<InstituteRegionView> findAllInstitutesWithRegion();

    @Query("select new com.brainsinjars.projectbackend.dto.InstituteProfileDto(i.id, i.name, i.nick, i.about, i.aboutPlacements, i.profilePicUrl, i.coverPicUrl, i.location, a.email) " +
            "from Institute i join i.instituteAdmin a where i.id = :id")
    Optional<InstituteProfileDto> findInstituteUsingId(@Param("id") long id);

    // We need distinct as this queries the join table
    // without distinct: inst1 -- course1
    //                   inst1 -- course2
    //                   inst1 -- course3
    //                   inst2 -- course1
    //                   inst2 -- course2
    //                   inst2 -- course3
    @Query("select distinct " +
            "new com.brainsinjars.projectbackend.dto.InstitutesDto(i.id, i.name, i.nick, size(i.reviews), i.location) " +
            "from Institute i " +
            "inner join Location l on i.location = l " +
            "inner join Geography g on l.geography = g " +
            "join i.courses c " +
            "where (( coalesce(:courseIds, :courseIds) is null or c.id in :courseIds ) " +
            "and (:region is null or lower(g.region) = lower(:region)) " +
            "and (:city is null or lower(g.city) = lower(:city)) " +
            "and (:name is null or lower(i.name) like lower(concat('%', :name, '%'))))")
    List<InstitutesDto> findInstitutes(@Param("courseIds") List<Long> courseIds, @Param("region") String region,
                                       @Param("city") String city, @Param("name") String name);

    @Query("select i.instituteAdmin.email from Institute i where i.id = :id")
    Optional<String> findInstituteAdminEmail(@Param("id") long id);

    @Query("select new com.brainsinjars.projectbackend.dto.InstituteIdNameDto(i.id, i.nick, g.region) from Institute i inner join Location l on i.location = l inner join Geography g on l.geography = g")
    List<InstituteIdNameDto> findAllInstitutesIdName();

    @Transactional
    @Modifying
    @Query("delete from Institute i where i.id = :id")
    void deleteById(@Param("id") Long id);
}
