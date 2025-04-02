package com.example.da1_backend.packageUser;


import com.example.da1_backend.route.Route;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@AllArgsConstructor
public class PackageController {

    private final PackageService packageService;

    @PostMapping("/create")
    public ResponseEntity<Package> createPackage(@RequestBody Package pkg, @RequestParam Long routeId) {
        try {
            Package newPackage = packageService.createPackage(pkg, routeId);
            return ResponseEntity.ok(newPackage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Obtener un Package por su ID.
     */
    @GetMapping("/{packageId}")
    public ResponseEntity<Package> getPackageById(@PathVariable Long packageId) {
        return ResponseEntity.ok(packageService.getPackageById(packageId));
    }

    /**
     * Actualizar un Package por su ID.
     */
    @PutMapping("/{packageId}")
    public ResponseEntity<Package> updatePackage(@PathVariable Long packageId, @RequestBody Package packageDetails) {
        return ResponseEntity.ok(packageService.updatePackage(packageId, packageDetails));
    }

    /**
     * Eliminar un Package por su ID.
     */
    @DeleteMapping("/{packageId}")
    public ResponseEntity<Void> deletePackage(@PathVariable Long packageId) {
        packageService.deletePackage(packageId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Asignar una Ruta a un Package existente.
     */
    @PostMapping("/{packageId}/assign-route/{routeId}")
    public ResponseEntity<Package> assignRouteToPackage(@PathVariable Long packageId, @PathVariable Long routeId) {
        return ResponseEntity.ok(packageService.assignRouteToPackage(packageId, routeId));
    }
}
