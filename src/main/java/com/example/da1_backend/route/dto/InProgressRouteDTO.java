package com.example.da1_backend.route.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InProgressRouteDTO {
    private Long id;
    private String address;
    private String assignedUser;
    private String startedAt;
    private String status;
}
