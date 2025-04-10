package com.example.da1_backend.route;

import com.example.da1_backend.packageUser.Package;
import com.example.da1_backend.route.dto.CompletedRouteDTO;
import com.example.da1_backend.route.dto.InProgressRouteDTO;
import com.example.da1_backend.route.dto.RouteDTO;
import com.example.da1_backend.route.dto.RouteDetailDTO;
import com.example.da1_backend.packageUser.dto.PackageDTO;
import com.example.da1_backend.user.User;
import com.example.da1_backend.packageUser.PackageRepository;
import com.example.da1_backend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;


    @Autowired
    private UserRepository userRepository;

    // Obtener todas las rutas
    public List<RouteDTO> getAllRoutes() {
        List<Route> routes = routeRepository.findByStatus(Status.PENDING);
        return routes.stream().map(route -> {
            RouteDTO routeDTO = new RouteDTO();
            routeDTO.setId(route.getId());
            routeDTO.setAddress(route.getAddress());
            routeDTO.setStatus(route.getStatus().name());
            routeDTO.setZone(route.getZone());

            // Verifica si startedAt es null antes de llamar a toString()
            routeDTO.setStartedAt(route.getStartedAt() != null ? route.getStartedAt().toString() : null);

            // Verifica si finishedAt es null antes de llamar a toString()
            routeDTO.setFinishedAt(route.getFinishedAt() != null ? route.getFinishedAt().toString() : null);

            return routeDTO;
        }).collect(Collectors.toList());
    }
    public List<CompletedRouteDTO> getCompletedRoutesByUser(Long userId) {
        List<Route> completedRoutes = routeRepository.findByAssignedTo_IdAndStatus(userId, Status.COMPLETED);
        return completedRoutes.stream().map(route -> {
            CompletedRouteDTO completedRouteDTO = new CompletedRouteDTO();
            completedRouteDTO.setId(route.getId());
            completedRouteDTO.setAddress(route.getAddress());
            completedRouteDTO.setStartedAt(route.getStartedAt() != null ? route.getStartedAt().toString() : null);
            completedRouteDTO.setFinishedAt(route.getFinishedAt() != null ? route.getFinishedAt().toString() : null);
            completedRouteDTO.setStatus(route.getStatus().name());
            completedRouteDTO.setZone(route.getZone());

            // Agregar el paquete en el DTO
            if (route.getPackageItem() != null) {
                Package packageItem = route.getPackageItem();
                PackageDTO packageDTO = new PackageDTO();
                packageDTO.setId(packageItem.getId());
                packageDTO.setReceptor(packageItem.getReceptor());
                packageDTO.setDepositSector(packageItem.getDepositSector());
                packageDTO.setWeight(packageItem.getWeight());
                packageDTO.setHeight(packageItem.getHeight());
                packageDTO.setLength(packageItem.getLength());
                packageDTO.setWidth(packageItem.getWidth());
                completedRouteDTO.setPackageDTO(packageDTO);
            }

            return completedRouteDTO;
        }).collect(Collectors.toList());
    }


    // Obtener una ruta con todos sus detalles (solo un paquete asociado)
    public RouteDetailDTO getRouteDetails(Long routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        // Crear el DTO para la ruta
        RouteDetailDTO routeDetailDTO = new RouteDetailDTO();
        routeDetailDTO.setId(route.getId());
        routeDetailDTO.setAddress(route.getAddress());
        routeDetailDTO.setStatus(route.getStatus().name());
        routeDetailDTO.setZone(route.getZone());

        // Verifica si startedAt es null antes de llamar a toString()
        routeDetailDTO.setStartedAt(route.getStartedAt() != null ? route.getStartedAt().toString() : null);

        // Verifica si finishedAt es null antes de llamar a toString()
        routeDetailDTO.setFinishedAt(route.getFinishedAt() != null ? route.getFinishedAt().toString() : null);

        // Asignar usuario
        if (route.getAssignedTo() != null) {
            User assignedUser = route.getAssignedTo();
            routeDetailDTO.setAssignedUserId(assignedUser.getId());
        }

        // Asignar paquete (solo un paquete asociado a la ruta)
        PackageDTO packageDTO = null;
        if (route.getPackageItem() != null) {
            Package packageItem = route.getPackageItem();  // Solo obtenemos el paquete asociado
            packageDTO = new PackageDTO();
            packageDTO.setId(packageItem.getId());
            packageDTO.setReceptor(packageItem.getReceptor());
            packageDTO.setDepositSector(packageItem.getDepositSector());
            packageDTO.setWeight(packageItem.getWeight());
            packageDTO.setHeight(packageItem.getHeight());
            packageDTO.setLength(packageItem.getLength());
            packageDTO.setWidth(packageItem.getWidth());
        }

        routeDetailDTO.setPackageDTO(packageDTO);  // Establecer el paquete en el DTO

        return routeDetailDTO;
    }
    public void assignUserToRoute(Long routeId, Long userId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        route.setAssignedTo(user);
        route.setStartedAt(LocalDateTime.now());
        route.setStatus(Status.IN_PROGRESS); // ✅ Cambia el estado

        routeRepository.save(route);
    }
    public void completeRoute(Long routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        route.setFinishedAt(LocalDateTime.now());
        route.setStatus(Status.COMPLETED);

        routeRepository.save(route);
    }
    public List<InProgressRouteDTO> getInProgressRoutesByUser(Long userId) {
        List<Route> pendingRoutes = routeRepository.findByAssignedTo_IdAndStatus(userId, Status.IN_PROGRESS);

        return pendingRoutes.stream().map(route -> {
            InProgressRouteDTO inProgressRouteDTO = new InProgressRouteDTO();
            inProgressRouteDTO.setId(route.getId());
            inProgressRouteDTO.setAddress(route.getAddress());
            inProgressRouteDTO.setZone(route.getZone());

            // Verifica si assignedTo es null antes de llamar a toString()
            inProgressRouteDTO.setAssignedUser(route.getAssignedTo() != null ? route.getAssignedTo().getName() : null);

            inProgressRouteDTO.setStartedAt(route.getStartedAt() != null ? route.getStartedAt().toString() : null);
            inProgressRouteDTO.setStatus(route.getStatus().name());

            return inProgressRouteDTO;
        }).collect(Collectors.toList());
    }

    public RouteDTO updateZone(Long routeId, String newZone) {
        Optional<Route> optionalRoute = routeRepository.findById(routeId);

        if (optionalRoute.isEmpty()) {
            throw new RuntimeException("Ruta no encontrada con ID: " + routeId);
        }

        Route route = optionalRoute.get();
        route.setZone(newZone);
        routeRepository.save(route);

        // Mapear a DTO
        RouteDTO dto = new RouteDTO();
        dto.setId(route.getId());
        dto.setAddress(route.getAddress());
        dto.setZone(route.getZone());
        dto.setStatus(route.getStatus().name());
        dto.setStartedAt(route.getStartedAt() != null ? route.getStartedAt().toString() : null);
        dto.setFinishedAt(route.getFinishedAt() != null ? route.getFinishedAt().toString() : null);

        return dto;
    }

}
