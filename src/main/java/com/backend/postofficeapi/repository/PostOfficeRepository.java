package com.backend.postofficeapi.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.postofficeapi.entities.PostOffice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface PostOfficeRepository extends JpaRepository<PostOffice, Long>, JpaSpecificationExecutor<PostOffice> {

    List<PostOffice> findByPincodeAndDeliveryStatus(Integer pincode, String deliveryStatus);

    default Page<PostOffice> findByCircleNameAndRegionNameAndDivisionName(String circleName, String regionName,
            String divisionName, Pageable pageable) {
        Specification<PostOffice> specification = Specification.where(null);

        if (circleName != null) {
            specification = specification
                    .and((root, query, builder) -> builder.equal(root.get("circleName"), circleName));
        }
        if (regionName != null) {
            specification = specification
                    .and((root, query, builder) -> builder.equal(root.get("regionName"), regionName));
        }
        if (divisionName != null) {
            specification = specification
                    .and((root, query, builder) -> builder.equal(root.get("divisionName"), divisionName));
        }

        return findAll(specification, pageable);
    }

    @Query("SELECT po.circleName, po.regionName, po.divisionName " +
            "FROM PostOffice po " +
            "WHERE (:circleName IS NULL OR po.circleName = :circleName) " +
            "GROUP BY po.circleName, po.regionName, po.divisionName")
    List<Object[]> findCircles(@Param("circleName") String circleName, Pageable pageable);

    @Query("SELECT po.regionName, po.divisionName " +
            "FROM PostOffice po " +
            "WHERE (:regionName IS NULL OR po.regionName = :regionName) " +
            "GROUP BY po.regionName, po.divisionName")
    List<Object[]> findRegions(@Param("regionName") String regionName, Pageable pageable);

}
