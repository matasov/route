package task.homework.route.web.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import task.homework.route.web.controllers.exceptions.InsufficientArgsException;
import task.homework.route.web.controllers.exceptions.NotFoundAnyCountryForRoute;
import task.homework.route.web.controllers.exceptions.RepositoryException;
import task.homework.route.web.models.CountryDto;
import task.homework.route.web.models.RouteDto;
import task.homework.route.web.repositories.WebCountryDao;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class BfsRouteFinder implements RouteFinder {

    private Map<String, Set<String>> adjacency;

    private Map<String, Boolean> visited;

    private Map<String, Integer> distances;

    private Map<String, String> parents;

    private final WebCountryDao dao;

    @Override
    public RouteDto findRoute(String origin, String destination) {
        init(origin, destination);
        bfs(origin);
        validateResult(origin, destination);
        return new RouteDto(getRoute(origin, destination));
    }

    private void init(String origin, String destination) {
        initializeGraph();
        validateArgs(origin, destination);
        initVisited();
        initDistances(origin);
        initParents();
    }

    private void initializeGraph() {
        List<CountryDto> countries = dao.getAllCountries();
        adjacency = new HashMap<>(countries.size());
        for (CountryDto country : countries) {
            adjacency.put(country.getCca3(), getBorders(country));
        }
    }

    private Set<String> getBorders(CountryDto country) {
        return country.getBorders() != null ? new HashSet<>(country.getBorders()) : Set.of();
    }

    private void validateArgs(String origin, String destination) {
        if (adjacency == null || origin == null || destination == null) {
            throw new RepositoryException("Not found countries.");
        }
        if (origin == null || !adjacency.containsKey(origin)) {
            throw new InsufficientArgsException("Not found origin country " + origin);
        }
        if (destination == null || !adjacency.containsKey(destination)) {
            throw new InsufficientArgsException("Not found destination country " + destination);
        }
    }

    private void initVisited() {
        visited = adjacency.entrySet().stream().collect(Collectors.toMap(Entry::getKey, entry -> false));
    }

    private void initDistances(String origin) {
        distances = new HashMap<>(adjacency.size());
        for (String key : adjacency.keySet()) {
            distances.put(key, null);
        }
        distances.put(origin, 0);
    }

    private void initParents() {
        parents = adjacency.entrySet().stream().collect(Collectors.toMap(Entry::getKey, Entry::getKey));
    }

    private void bfs(String origin) {
        LinkedList<String> roadMap = new LinkedList<>();
        roadMap.addLast(origin);
        while (!roadMap.isEmpty()) {
            String currentVertex = roadMap.removeFirst();
            if (!visited.get(currentVertex)) {
                visited.put(currentVertex, true);
                recalculateNeighboursDistances(currentVertex, roadMap);
            }
        }
    }

    private void recalculateNeighboursDistances(String currentVertex, LinkedList<String> roadMap) {
        for (String neighbour : adjacency.get(currentVertex)) {
            if (!visited.get(neighbour)) {
                recalculateNeighbourDistance(currentVertex, neighbour, roadMap);
            }
        }
    }

    private void recalculateNeighbourDistance(String currentVertex, String neighbour, LinkedList<String> roadMap) {
        if (!visited.get(neighbour)) {
            roadMap.addLast(neighbour);
            Integer parentCurrentDistance = distances.get(currentVertex);
            Integer calculatedCurrentDistance = distances.get(neighbour);
            if (calculatedCurrentDistance == null
                    || parentCurrentDistance + 1 < calculatedCurrentDistance) {
                distances.put(neighbour, parentCurrentDistance + 1);
                parents.put(neighbour, currentVertex);
            }
        }
    }

    private void validateResult(String origin, String destination) {
        if (distances.get(destination) == null) {
            throw new NotFoundAnyCountryForRoute(origin, destination);
        }
    }

    private List<String> getRoute(String origin, String destination) {
        String parent = destination;
        List<String> route = new LinkedList<>();
        while (!parent.equals(origin)) {
            route.add(0, parent);
            parent = parents.get(parent);
        }
        route.add(0, origin);
        return route;
    }

}
