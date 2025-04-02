package com.example.da1_backend.route;

import com.example.da1_backend.exception.ResourceNotFoundException;
import com.example.da1_backend.user.User;
import com.example.da1_backend.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final UserRepository userRepository; // si necesitas asignar un usuario
    // u otros repositorios que necesites

    /**
     * Crea una nueva Route en la base de datos.
     */
    public Route createRoute(Route route) {
        return routeRepository.save(route);
    }

    /**
     * Obtiene una Route por su ID.
     */
    public Route getRouteById(Long routeId) {
        return routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + routeId));
    }

    /**
     * Actualiza una Route existente.
     */
    public Route updateRoute(Long routeId, Route routeDetails) {
        Route existingRoute = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + routeId));

        // Actualiza los campos necesarios
        existingRoute.setClient(routeDetails.getClient());
        existingRoute.setAddress(routeDetails.getAddress());
        existingRoute.setStatus(routeDetails.getStatus());
        existingRoute.setStartedAt(routeDetails.getStartedAt());
        existingRoute.setFinishedAt(routeDetails.getFinishedAt());

        return routeRepository.save(existingRoute);
    }

    /**
     * Elimina una Route de la base de datos.
     */
    public void deleteRoute(Long routeId) {
        if (!routeRepository.existsById(routeId)) {
            throw new ResourceNotFoundException("Route not found with id: " + routeId);
        }
        routeRepository.deleteById(routeId);
    }

    /**
     * Asigna un usuario (repartidor, por ejemplo) a una Route.
     */
    public Route assignUserToRoute(Long routeId, Long userId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + routeId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        route.setAssignedTo(user);
        return routeRepository.save(route);
    }

}