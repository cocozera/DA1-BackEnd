package com.example.da1_backend.route;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    // Métodos personalizados, por ejemplo:
    // List<Route> findByStatus(RouteStatus status);
}