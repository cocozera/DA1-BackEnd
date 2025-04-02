package com.example.da1_backend.packageUser;


import com.example.da1_backend.route.Route;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@AllArgsConstructor
public class PackageController {

    private final PackageService packageService;

    /**
     * Crear un nuevo Package.
     */
    @PostMapping
    public ResponseEntity<Package> createPackage(@RequestBody Package pkg) {
        return ResponseEntity.ok(packageService.createPackage(pkg));
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
