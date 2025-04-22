package com.example.da1_backend.packageUser;

import com.example.da1_backend.packageUser.dto.AssignUserToPackageDTO;
import com.example.da1_backend.packageUser.dto.PackageCreateDTO;
import com.example.da1_backend.route.Route;
import com.example.da1_backend.route.Status;
import com.example.da1_backend.route.RouteRepository;
import com.example.da1_backend.user.User;
import com.example.da1_backend.user.UserRepository;
import com.example.da1_backend.exception.PackageException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private UserRepository userRepository;

    public Package createPackage(PackageCreateDTO packageCreateDTO) {
        try {
            Route route = new Route();
            route.setAddress(packageCreateDTO.getRouteAddress());
            route.setStatus(Status.PENDING);

            Package newPackage = new Package();
            newPackage.setReceptor(packageCreateDTO.getReceptor());
            newPackage.setDepositSector(packageCreateDTO.getDepositSector());
            newPackage.setWeight(packageCreateDTO.getWeight());
            newPackage.setHeight(packageCreateDTO.getHeight());
            newPackage.setLength(packageCreateDTO.getLength());
            newPackage.setWidth(packageCreateDTO.getWidth());

            newPackage.setRoute(route);
            route.setPackageItem(newPackage);

            route = routeRepository.save(route);
            newPackage = packageRepository.save(newPackage);

            byte[] qrCode = generateQRCode(route.getId().toString());
            newPackage.setQRcode(qrCode);

            return packageRepository.save(newPackage);
        } catch (Exception e) {
            throw new PackageException(PackageException.QR_GENERATION_FAILED);
        }
    }

    private byte[] generateQRCode(String text) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            var bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new PackageException(PackageException.QR_GENERATION_FAILED);
        }
    }

    public void assignUserToPackage(AssignUserToPackageDTO dto) {
        Package packageToAssign = packageRepository.findById(dto.getPackageId())
                .orElseThrow(() -> new PackageException(PackageException.PACKAGE_NOT_FOUND));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new PackageException(PackageException.USER_NOT_FOUND));

        Route route = packageToAssign.getRoute();
        route.setAssignedTo(user);
        route.setStartedAt(LocalDateTime.now());

        routeRepository.save(route);
        packageRepository.save(packageToAssign);
    }

    public void finishRoute(Long packageId) {
        Package packageToFinish = packageRepository.findById(packageId)
                .orElseThrow(() -> new PackageException(PackageException.PACKAGE_NOT_FOUND));

        Route route = packageToFinish.getRoute();
        route.setFinishedAt(LocalDateTime.now());
        route.setStatus(Status.COMPLETED);

        routeRepository.save(route);
    }

    public byte[] getQRCodeByPackageId(Long id) {
        return packageRepository.findById(id)
                .map(Package::getQRcode)
                .orElseThrow(() -> new PackageException(PackageException.PACKAGE_NOT_FOUND));
    }
}
