package com.example.da1_backend.route;

import com.example.da1_backend.route.dto.CompletedRouteDTO;
import com.example.da1_backend.route.dto.InProgressRouteDTO;
import com.example.da1_backend.route.dto.RouteDTO;
import com.example.da1_backend.route.dto.RouteDetailDTO;
import com.example.da1_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @Autowired
    private JwtUtil jwtUtil;

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

    @GetMapping("/completed-routes")
    public List<CompletedRouteDTO> getCompletedRoutes(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token); // Extraer el userId del token
        return routeService.getCompletedRoutesByUser(userId);
    }

    @GetMapping("/inprogress-routes")
    public List<InProgressRouteDTO> getInProgressRoutes(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token); // Extraer el userId del token
        return routeService.getInProgressRoutesByUser(userId);
    }

    @PutMapping("/{id}/zone")
    public ResponseEntity<RouteDTO> updateRouteZone(@PathVariable Long id, @RequestParam String zone) {
        RouteDTO updatedRoute = routeService.updateZone(id, zone);
        return ResponseEntity.ok(updatedRoute);
    }

}