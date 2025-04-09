package com.example.da1_backend.packageUser;


import com.example.da1_backend.packageUser.dto.AssignUserToPackageDTO;
import com.example.da1_backend.packageUser.dto.PackageCreateDTO;
import com.example.da1_backend.route.Route;
import com.google.zxing.WriterException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/packages")
@AllArgsConstructor
public class PackageController {

    private final PackageService packageService;

    // Endpoint para crear un paquete con ruta y generar QR
    @PostMapping("/create")
    public Package createPackage(@RequestBody PackageCreateDTO packageCreateDTO) throws WriterException, IOException {
        return packageService.createPackage(packageCreateDTO);
    }

    // Endpoint para asignar un usuario a un paquete cuando se escanea el QR
    @PostMapping("/assignUser")
    public void assignUserToPackage(@RequestBody AssignUserToPackageDTO assignUserToPackageDTO) {
        packageService.assignUserToPackage(assignUserToPackageDTO);
    }

    // Endpoint para finalizar la ruta
    @PostMapping("/finishRoute/{packageId}")
    public void finishRoute(@PathVariable Long packageId) {
        packageService.finishRoute(packageId);
    }

    @GetMapping("/{id}/qrcode")
    public ResponseEntity<byte[]> getQRCode(@PathVariable Long id) {
        byte[] qrCode = packageService.getQRCodeByPackageId(id);

        if (qrCode == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);
    }

}
