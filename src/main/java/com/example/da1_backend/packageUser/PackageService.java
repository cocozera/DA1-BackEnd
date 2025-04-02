package com.example.da1_backend.packageUser;

import com.example.da1_backend.packageUser.dto.AssignUserToPackageDTO;
import com.example.da1_backend.packageUser.dto.PackageCreateDTO;
import com.example.da1_backend.route.Route;
import com.example.da1_backend.route.Status;
import com.example.da1_backend.route.RouteRepository;
import com.example.da1_backend.user.User;
import com.example.da1_backend.user.UserRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private UserRepository userRepository;

    // Método para crear un paquete sin asignar el usuario
    public Package createPackage(PackageCreateDTO packageCreateDTO) throws WriterException, IOException {
        // Paso 1: Crear la ruta
        Route route = new Route();
        route.setAddress(packageCreateDTO.getRouteAddress());
        route.setStatus(Status.PENDING);

        // Paso 2: Crear el paquete
        Package newPackage = new Package();
        newPackage.setReceptor(packageCreateDTO.getReceptor());
        newPackage.setDepositSector(packageCreateDTO.getDepositSector());
        newPackage.setWeight(packageCreateDTO.getWeight());
        newPackage.setHeight(packageCreateDTO.getHeight());
        newPackage.setLength(packageCreateDTO.getLength());
        newPackage.setWidth(packageCreateDTO.getWidth());

        // Establecer la relación entre el paquete y la ruta
        newPackage.setRoute(route);
        route.setPackageItem(newPackage);  // Enlazar el paquete con la ruta

        // Guardar la ruta y el paquete
        route = routeRepository.save(route);  // Guardar la ruta
        newPackage = packageRepository.save(newPackage);  // Guardar el paquete

        // Paso 3: Generar el QR
        byte[] qrCode = generateQRCode(route.getId().toString());  // Usamos la ID de la ruta en el QR
        newPackage.setQRcode(qrCode);

        // Guardar el paquete
        return packageRepository.save(newPackage);
    }

    // Método para generar un QR a partir de un texto
    private byte[] generateQRCode(String text) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            com.google.zxing.common.BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
        } catch (WriterException e) {
            throw new WriterException();
        }

        return baos.toByteArray();
    }

    public void assignUserToPackage(AssignUserToPackageDTO assignUserToPackageDTO) {
        Package packageToAssign = packageRepository.findById(assignUserToPackageDTO.getPackageId())
                .orElseThrow(() -> new RuntimeException("Package not found"));

        User user = userRepository.findById(assignUserToPackageDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Asignamos el usuario a la ruta y establecemos startedAt
        Route route = packageToAssign.getRoute();
        route.setAssignedTo(user);
        route.setStartedAt(LocalDateTime.now());  // Establecemos el startedAt en el momento de la asignación

        routeRepository.save(route);  // Guardamos la ruta con la asignación y el startedAt actualizado

        packageRepository.save(packageToAssign);  // Guardamos el paquete con la nueva asignación
    }

    // Método para finalizar la ruta y marcar finishedAt
    public void finishRoute(Long packageId) {
        Package packageToFinish = packageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        Route route = packageToFinish.getRoute();
        route.setFinishedAt(LocalDateTime.now());  // Establecemos el finishedAt cuando la ruta se finaliza

        route.setStatus(Status.COMPLETED);  // Asumimos que la ruta pasa a estado 'COMPLETED' al finalizar

        routeRepository.save(route);  // Guardamos la ruta con el finishedAt actualizado
    }
}
