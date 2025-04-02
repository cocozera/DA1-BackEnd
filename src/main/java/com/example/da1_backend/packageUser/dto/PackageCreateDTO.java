package com.example.da1_backend.packageUser.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackageCreateDTO {
    private String receptor;
    private String depositSector;
    private Double weight;
    private Double height;
    private Double length;
    private Double width;

    private String routeAddress;
}