package com.example.da1_backend.packageUser.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignUserToPackageDTO {
    private Long packageId;
    private Long userId;
}
