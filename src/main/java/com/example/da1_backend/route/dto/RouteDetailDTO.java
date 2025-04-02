package com.example.da1_backend.route.dto;

import com.example.da1_backend.packageUser.dto.PackageDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteDetailDTO {
    private Long id;
    private String address;
    private String status;
    private String startedAt;
    private String finishedAt;
    private Long assignedUserId;
    private PackageDTO packageDTO;  // Solo un único paquete asociado a la ruta
}
