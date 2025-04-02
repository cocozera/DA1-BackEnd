package com.example.da1_backend.packageUser;

import com.example.da1_backend.route.Route;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "routes")
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String QRcode;

    private String depositAddress;

    private Double weight;

    private Double height;

    private Double length;

    private Double width;

    @OneToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Package> packages = new ArrayList<>();

}
