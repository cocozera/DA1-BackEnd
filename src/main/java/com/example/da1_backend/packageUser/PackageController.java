package com.example.da1_backend.packageUser;


import com.example.da1_backend.route.Route;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/packages")
@AllArgsConstructor
public class PackageController {

    private final PackageService packageService;

    private final PackageRepository packageRepository;

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

    /**
     * Endpoint para obtener el QR como imagen
     */
    @GetMapping("/{packageId}/qr")
    public ResponseEntity<byte[]> getQRCode(@PathVariable Long packageId) {
        Optional<Package> optionalPackage = packageRepository.getPackageById(packageId);

        if (optionalPackage.isPresent()) {
            Package pkg = optionalPackage.get();

            // Obtén el QR como byte array
            byte[] qrCode = pkg.getQRcode();

            // Verifica si el QR existe
            if (qrCode != null) {
                // Establece los encabezados para indicar que es una imagen PNG
                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type", "image/png");

                // Devuelve el byte array como una imagen en la respuesta
                return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
