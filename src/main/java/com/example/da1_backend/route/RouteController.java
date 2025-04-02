package com.example.da1_backend.route;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routes")
@AllArgsConstructor
public class RouteController {

    private final RouteService routeService;

    /**
     * Crear una nueva Route.
     */
    @PostMapping
    public ResponseEntity<Route> createRoute(@RequestBody Route route) {
        return ResponseEntity.ok(routeService.createRoute(route));
    }

    /**
     * Obtener una Route por su ID.
     */
    @GetMapping("/{routeId}")
    public ResponseEntity<Route> getRouteById(@PathVariable Long routeId) {
        return ResponseEntity.ok(routeService.getRouteById(routeId));
    }

    /**
     * Actualizar una Route por su ID.
     */
    @PutMapping("/{routeId}")
    public ResponseEntity<Route> updateRoute(@PathVariable Long routeId, @RequestBody Route routeDetails) {
        return ResponseEntity.ok(routeService.updateRoute(routeId, routeDetails));
    }

    /**
     * Eliminar una Route por su ID.
     */
    @DeleteMapping("/{routeId}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long routeId) {
        routeService.deleteRoute(routeId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Asignar un usuario (repartidor) a una Route.
     */
    @PostMapping("/{routeId}/assign-user/{userId}")
    public ResponseEntity<Route> assignUserToRoute(@PathVariable Long routeId, @PathVariable Long userId) {
        return ResponseEntity.ok(routeService.assignUserToRoute(routeId, userId));
    }
}