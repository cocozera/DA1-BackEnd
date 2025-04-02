package com.example.da1_backend.route;

import com.example.da1_backend.user.User;
import com.example.da1_backend.packageUser.Package;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonManagedReference  // Indica que esta es la referencia "administrada"
    @OneToOne(mappedBy = "route", cascade = CascadeType.ALL)
    private Package packageItem;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User assignedTo;
}
