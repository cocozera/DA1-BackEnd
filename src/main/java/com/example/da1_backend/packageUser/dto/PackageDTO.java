package com.example.da1_backend.packageUser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PackageDTO {
    private Long id;
    private String QRcode;
    private String depositAddress;
    private Double weight;
    private Double length;
    private Double width;
    private Double height;
}
