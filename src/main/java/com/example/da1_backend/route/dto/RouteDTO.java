package com.example.da1_backend.route.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteDTO {
    private Long id;
    private String address;
    private String status;
    private String startedAt;
    private String finishedAt;
    private String zone;
}

