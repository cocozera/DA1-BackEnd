package com.example.da1_backend.packageUser;

import com.example.da1_backend.exception.ResourceNotFoundException;
import com.example.da1_backend.route.Route;
import com.example.da1_backend.route.RouteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PackageService {

    private final PackageRepository packageRepository;
    private final RouteRepository routeRepository;

    /**
     * Crea un nuevo Package y genera su QR
     */
    public Package createPackage(Package pkg, Long routeId) throws Exception {
        // Generar la URL para la ruta, por ejemplo
        String routeInfo = "http://localhost:8080/route-info?id=" + routeId;

        // Llamar al generador de QR y obtener la imagen en bytes
        byte[] qrCode = QRCodeGenerator.generateQRCodeImage(routeInfo);

        // Aquí puedes asignar el QR generado al paquete
        pkg.setQRcode(new String(qrCode));

        // Continuar con la creación del paquete
        return packageRepository.save(pkg);
    }


    /**
     * Obtiene un Package por su ID.
     */
    public Package getPackageById(Long packageId) {
        return packageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + packageId));
    }

    /**
     * Actualiza un Package existente.
     */
    public Package updatePackage(Long packageId, Package packageDetails) {
        Package existingPackage = packageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + packageId));

        // Actualiza los campos necesarios
        existingPackage.setQRcode(packageDetails.getQRcode());
        existingPackage.setDepositAddress(packageDetails.getDepositAddress());
        existingPackage.setWeight(packageDetails.getWeight());
        existingPackage.setHeight(packageDetails.getHeight());
        existingPackage.setLength(packageDetails.getLength());
        existingPackage.setWidth(packageDetails.getWidth());

        // Si deseas cambiar la ruta directamente aquí, podrías hacer:
        // existingPackage.setRoute(packageDetails.getRoute());

        return packageRepository.save(existingPackage);
    }

    /**
     * Elimina un Package de la base de datos.
     */
    public void deletePackage(Long packageId) {
        if (!packageRepository.existsById(packageId)) {
            throw new ResourceNotFoundException("Package not found with id: " + packageId);
        }
        packageRepository.deleteById(packageId);
    }

    /**
     * Asigna una ruta a un Package existente.
     */
    public Package assignRouteToPackage(Long packageId, Long routeId) {
        Package pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + packageId));

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + routeId));

        pkg.setRoute(route);
        return packageRepository.save(pkg);
    }
}