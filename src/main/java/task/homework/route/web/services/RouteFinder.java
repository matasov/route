package task.homework.route.web.services;

import task.homework.route.web.models.RouteDto;

public interface RouteFinder {
    RouteDto findRoute(String start, String end);
}
