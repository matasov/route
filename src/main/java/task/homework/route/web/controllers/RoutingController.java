package task.homework.route.web.controllers;

import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import task.homework.route.web.models.RouteDto;
import task.homework.route.web.services.RouteFinder;

@RestController
@RequestMapping("/routing")
@RequiredArgsConstructor
@Validated
public class RoutingController {

    private final ApplicationContext context;

    @GetMapping("/{origin}/{destination}")
    public ResponseEntity<RouteDto> getRoute(
            @PathVariable @Size(min = 2, max = 3) String origin,
            @PathVariable @Size(min = 2, max = 3) String destination) {
        return ResponseEntity.ok(getRouteFinder().findRoute(
                origin.toUpperCase(), destination.toUpperCase()));
    }

    private RouteFinder getRouteFinder() {
        return context.getBean(RouteFinder.class);
    }

}
