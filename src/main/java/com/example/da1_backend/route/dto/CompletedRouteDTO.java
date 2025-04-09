package com.example.da1_backend.route.dto;

import com.example.da1_backend.packageUser.dto.PackageDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompletedRouteDTO {
    private Long id;
    private String address;
    private String startedAt;
    private String finishedAt;
    private String status;
    private PackageDTO packageDTO;
    private String zone;
}
