package com.example.da1_backend.route;

import com.example.da1_backend.route.dto.CompletedRouteDTO;
import com.example.da1_backend.route.dto.InProgressRouteDTO;
import com.example.da1_backend.route.dto.RouteDTO;
import com.example.da1_backend.route.dto.RouteDetailDTO;
import com.example.da1_backend.util.QrCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import java.security.Principal;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping("/")
    public List<RouteDTO> getAllRoutes() {
        return routeService.getAllRoutes();
    }

    @GetMapping("/{routeId}")
    public RouteDetailDTO getRouteDetails(@PathVariable Long routeId) {
        return routeService.getRouteDetails(routeId);
    }


    @PostMapping("/assign")
    public ResponseEntity<String> assignUserToRoute(@RequestParam Long routeId, Principal principal) {
        routeService.assignUserToRoute(routeId, principal.getName()); // email del usuario
        return ResponseEntity.ok("User assigned to route successfully.");
    }

    @PostMapping("/complete")
    public ResponseEntity<String> completeRoute(
            @RequestParam Long routeId,
            @RequestParam String code
    ) {
        routeService.completeRoute(routeId, code);
        return ResponseEntity.ok("Route completed successfully.");
    }



    @GetMapping("/completed-routes")
    public List<CompletedRouteDTO> getCompletedRoutes(Principal principal) {
        return routeService.getCompletedRoutesByUserEmail(principal.getName());
    }


    @GetMapping("/inprogress-routes")
    public List<InProgressRouteDTO> getInProgressRoutes(Principal principal) {
        return routeService.getInProgressRoutesByUserEmail(principal.getName());
    }

    @PutMapping("/{id}/zone")
    public ResponseEntity<RouteDTO> updateRouteZone(@PathVariable Long id, @RequestParam String zone) {
        RouteDTO updatedRoute = routeService.updateZone(id, zone);
        return ResponseEntity.ok(updatedRoute);
    }

    @GetMapping("/{routeId}/qrcode")
    public ResponseEntity<byte[]> getRouteQr(@PathVariable Long routeId) throws IOException {
        // Aquí decidís qué codificar: solo el ID, o bien una URL/deep-link
        String qrContent = String.valueOf(routeId);
        byte[] png = QrCodeGenerator.generatePng(qrContent);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(png);
    }

}
