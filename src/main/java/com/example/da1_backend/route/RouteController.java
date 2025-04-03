package com.example.da1_backend.route;

import com.example.da1_backend.route.dto.RouteDTO;
import com.example.da1_backend.route.dto.RouteDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}