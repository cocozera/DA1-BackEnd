package com.example.da1_backend.route;

import com.example.da1_backend.user.User;
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

    private String client;

    private String address;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User assignedTo;
}
