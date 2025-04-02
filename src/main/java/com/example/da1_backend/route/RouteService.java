package com.example.da1_backend.route;

import com.example.da1_backend.packageUser.Package;
import com.example.da1_backend.route.dto.RouteDTO;
import com.example.da1_backend.route.dto.RouteDetailDTO;
import com.example.da1_backend.packageUser.dto.PackageDTO;
import com.example.da1_backend.user.User;
import com.example.da1_backend.packageUser.PackageRepository;
import com.example.da1_backend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private UserRepository userRepository;

    // Obtener todas las rutas
    public List<RouteDTO> getAllRoutes() {
        List<Route> routes = routeRepository.findAll();
        return routes.stream().map(route -> {
            RouteDTO routeDTO = new RouteDTO();
            routeDTO.setId(route.getId());
            routeDTO.setAddress(route.getAddress());
            routeDTO.setStatus(route.getStatus().name());
            routeDTO.setStartedAt(route.getStartedAt().toString());
            routeDTO.setFinishedAt(route.getFinishedAt().toString());
            return routeDTO;
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
        routeDetailDTO.setStartedAt(route.getStartedAt().toString());
        routeDetailDTO.setFinishedAt(route.getFinishedAt().toString());

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
}
