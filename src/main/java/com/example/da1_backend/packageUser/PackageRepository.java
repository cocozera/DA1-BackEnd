package com.example.da1_backend.packageUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    // Métodos personalizados, por ejemplo:
    Optional<Package> getPackageById(Long packageId);
}