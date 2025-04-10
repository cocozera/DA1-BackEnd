package com.example.da1_backend.packageUser.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackageDTO {
    private Long id;
    private String receptor;
    private String depositSector;
    private Double weight;
    private Double height;
    private Double length;
    private Double width;
}