package com.example.da1_backend.packageUser;

import com.example.da1_backend.route.Route;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

    private String receptor;

    private byte[] QRcode;

    private String depositSector;

    private Double weight;

    private Double height;

    private Double length;

    private Double width;

    // Relación muchos a uno: un paquete pertenece a una ruta
    @JsonBackReference  // Indica que esta es la referencia "reversa"
    @OneToOne
    @JoinColumn(name = "route_id")
    private Route route;
}
