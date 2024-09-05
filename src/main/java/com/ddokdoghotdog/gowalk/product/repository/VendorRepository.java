package com.ddokdoghotdog.gowalk.product.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ddokdoghotdog.gowalk.entity.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long>{

	Optional<Vendor> findByName(@Param("name") String name);
}
