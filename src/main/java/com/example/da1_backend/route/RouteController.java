package com.example.da1_backend.route;

import com.example.da1_backend.route.dto.CompletedRouteDTO;
import com.example.da1_backend.route.dto.PendingRouteDTO;
import com.example.da1_backend.route.dto.RouteDTO;
import com.example.da1_backend.route.dto.RouteDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    // Obtener todas las rutas
    @GetMapping("/")
    public List<RouteDTO> getAllRoutes() {
        return routeService.getAllRoutes();
    }

    // Obtener una ruta con todos sus detalles (solo un paquete asociado)
    @GetMapping("/{routeId}")
    public RouteDetailDTO getRouteDetails(@PathVariable Long routeId) {
        return routeService.getRouteDetails(routeId);
    }

    @PostMapping("/assign")
    public ResponseEntity<String> assignUserToRoute(@RequestParam Long routeId, @RequestParam Long userId) {
        routeService.assignUserToRoute(routeId, userId);
        return ResponseEntity.ok("User assigned to route successfully.");
    }

    @PostMapping("/complete")
    public ResponseEntity<String> completeRoute(@RequestParam Long routeId) {
        routeService.completeRoute(routeId);
        return ResponseEntity.ok("Route completed successfully.");
    }

    @GetMapping("/{userId}/completed-routes")
    public List<CompletedRouteDTO> getCompletedRoutes(@PathVariable Long userId) {
        return routeService.getCompletedRoutesByUser(userId);
    }

    @GetMapping("/{userId}/inprogress-routes")
    public List<PendingRouteDTO> getPendingRoutes(@PathVariable Long userId) {
        return routeService.getInProgressRoutesByUser(userId);
    }

}