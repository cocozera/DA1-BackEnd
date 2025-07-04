package com.example.da1_backend.route;

import com.example.da1_backend.exception.RouteException;
import com.example.da1_backend.notification.NotificationService;
import com.example.da1_backend.packageUser.Package;
import com.example.da1_backend.packageUser.PackageRepository;
import com.example.da1_backend.packageUser.dto.PackageDTO;
import com.example.da1_backend.route.dto.CompletedRouteDTO;
import com.example.da1_backend.route.dto.InProgressRouteDTO;
import com.example.da1_backend.route.dto.RouteDTO;
import com.example.da1_backend.route.dto.RouteDetailDTO;
import com.example.da1_backend.user.User;
import com.example.da1_backend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;


    public List<RouteDTO> getAllRoutes() {
        List<Route> routes = routeRepository.findByStatus(Status.PENDING);
        return routes.stream().map(route -> {
            RouteDTO routeDTO = new RouteDTO();
            routeDTO.setId(route.getId());
            routeDTO.setAddress(route.getAddress());
            routeDTO.setStatus(route.getStatus().name());
            routeDTO.setZone(route.getZone());
            routeDTO.setStartedAt(route.getStartedAt() != null ? route.getStartedAt().toString() : null);
            routeDTO.setFinishedAt(route.getFinishedAt() != null ? route.getFinishedAt().toString() : null);
            return routeDTO;
        }).collect(Collectors.toList());
    }

    public void assignUserToRoute(Long routeId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RouteException(RouteException.USER_NOT_FOUND));

        if (routeRepository.existsByAssignedToIdAndStatus(user.getId(), Status.IN_PROGRESS)) {
            throw new RouteException(RouteException.USER_HAS_ROUTE_IN_PROGRESS);
        }

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RouteException(RouteException.ROUTE_NOT_FOUND));

        route.setAssignedTo(user);
        route.setStartedAt(LocalDateTime.now());
        route.setStatus(Status.IN_PROGRESS);

        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        route.setCompletionCode(code);

        notificationService.createNotification(
                user.getId(),
                "Nueva ruta asignada",
                "Zona: " + route.getZone()
        );

        routeRepository.save(route);
    }

    public List<CompletedRouteDTO> getCompletedRoutesByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RouteException(RouteException.USER_NOT_FOUND));
        return getCompletedRoutesByUser(user.getId());
    }

    public List<InProgressRouteDTO> getInProgressRoutesByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RouteException(RouteException.USER_NOT_FOUND));
        return getInProgressRoutesByUser(user.getId());
    }




    public RouteDetailDTO getRouteDetails(Long routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RouteException(RouteException.ROUTE_NOT_FOUND));

        RouteDetailDTO dto = new RouteDetailDTO();
        dto.setId(route.getId());
        dto.setAddress(route.getAddress());
        dto.setStatus(route.getStatus().name());
        dto.setZone(route.getZone());
        dto.setStartedAt(route.getStartedAt() != null ? route.getStartedAt().toString() : null);
        dto.setFinishedAt(route.getFinishedAt() != null ? route.getFinishedAt().toString() : null);

        if (route.getAssignedTo() != null) {
            dto.setAssignedUserId(route.getAssignedTo().getId());
        }

        if (route.getPackageItem() != null) {
            Package pkg = route.getPackageItem();
            PackageDTO pkgDTO = new PackageDTO();
            pkgDTO.setId(pkg.getId());
            pkgDTO.setReceptor(pkg.getReceptor());
            pkgDTO.setDepositSector(pkg.getDepositSector());
            pkgDTO.setWeight(pkg.getWeight());
            pkgDTO.setHeight(pkg.getHeight());
            pkgDTO.setLength(pkg.getLength());
            pkgDTO.setWidth(pkg.getWidth());
            dto.setPackageDTO(pkgDTO);
        }

        return dto;
    }


    public void completeRoute(Long routeId, String code) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RouteException(RouteException.ROUTE_NOT_FOUND));

        if (!code.equalsIgnoreCase(route.getCompletionCode())) {
            throw new RouteException("Código de finalización incorrecto.");
        }

        route.setFinishedAt(LocalDateTime.now());
        route.setStatus(Status.COMPLETED);
        routeRepository.save(route);

        if (route.getAssignedTo() != null) {
            notificationService.createNotification(
                    route.getAssignedTo().getId(),
                    "Ruta completada",
                    "Has finalizado la ruta en la zona " + route.getZone() + ". ¡Te esperan nuevas rutas pendientes!"
            );
        }
    }

    public RouteDTO updateZone(Long routeId, String newZone) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RouteException(RouteException.ROUTE_NOT_FOUND_WITH_ID + routeId));

        route.setZone(newZone);
        routeRepository.save(route);

        RouteDTO dto = new RouteDTO();
        dto.setId(route.getId());
        dto.setAddress(route.getAddress());
        dto.setZone(route.getZone());
        dto.setStatus(route.getStatus().name());
        dto.setStartedAt(route.getStartedAt() != null ? route.getStartedAt().toString() : null);
        dto.setFinishedAt(route.getFinishedAt() != null ? route.getFinishedAt().toString() : null);

        return dto;
    }

    private List<CompletedRouteDTO> getCompletedRoutesByUser(Long userId) {
        List<Route> completedRoutes = routeRepository.findByAssignedTo_IdAndStatus(userId, Status.COMPLETED);
        return completedRoutes.stream().map(route -> {
            CompletedRouteDTO dto = new CompletedRouteDTO();
            dto.setId(route.getId());
            dto.setAddress(route.getAddress());
            dto.setStartedAt(route.getStartedAt() != null ? route.getStartedAt().toString() : null);
            dto.setFinishedAt(route.getFinishedAt() != null ? route.getFinishedAt().toString() : null);
            dto.setStatus(route.getStatus().name());
            dto.setZone(route.getZone());

            if (route.getPackageItem() != null) {
                Package pkg = route.getPackageItem();
                PackageDTO pkgDTO = new PackageDTO();
                pkgDTO.setId(pkg.getId());
                pkgDTO.setReceptor(pkg.getReceptor());
                pkgDTO.setDepositSector(pkg.getDepositSector());
                pkgDTO.setWeight(pkg.getWeight());
                pkgDTO.setHeight(pkg.getHeight());
                pkgDTO.setLength(pkg.getLength());
                pkgDTO.setWidth(pkg.getWidth());
                dto.setPackageDTO(pkgDTO);
            }

            return dto;
        }).collect(Collectors.toList());
    }

    private List<InProgressRouteDTO> getInProgressRoutesByUser(Long userId) {
        List<Route> routes = routeRepository.findByAssignedTo_IdAndStatus(userId, Status.IN_PROGRESS);
        return routes.stream().map(route -> {
            InProgressRouteDTO dto = new InProgressRouteDTO();
            dto.setId(route.getId());
            dto.setAddress(route.getAddress());
            dto.setZone(route.getZone());
            dto.setAssignedUser(route.getAssignedTo() != null ? route.getAssignedTo().getName() : null);
            dto.setStartedAt(route.getStartedAt() != null ? route.getStartedAt().toString() : null);
            dto.setStatus(route.getStatus().name());
            return dto;
        }).collect(Collectors.toList());
    }
}
