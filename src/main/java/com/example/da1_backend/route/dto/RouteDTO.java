package com.example.da1_backend.route.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDTO {
    private Long id;
    private String address;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

}
