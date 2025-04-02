package com.example.da1_backend.packageUser;

import com.example.da1_backend.route.Route;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "package")
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

    // Relación muchos a uno: un paquete pertenece a una ruta
    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;
}
