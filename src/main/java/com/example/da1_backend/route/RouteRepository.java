package com.example.da1_backend.route;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    // Métodos personalizados, por ejemplo:
    List<Route> findByStatus(Status status);
    List<Route> findByAssignedTo_IdAndStatus(Long userId, Status status);
    boolean existsByAssignedToIdAndStatus(Long userId, Status status);
}