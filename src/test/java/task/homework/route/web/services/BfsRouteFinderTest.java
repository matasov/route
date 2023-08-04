package task.homework.route.web.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.homework.route.web.controllers.exceptions.InsufficientArgsException;
import task.homework.route.web.controllers.exceptions.NotFoundAnyCountryForRoute;
import task.homework.route.web.models.CountryDto;
import task.homework.route.web.models.RouteDto;
import task.homework.route.web.repositories.WebCountryDao;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BfsRouteFinderTest {

    @Mock
    private WebCountryDao webCountryDao;

    @InjectMocks
    private BfsRouteFinder bfsRouteFinder;

    @BeforeEach
    public void setup() {
        CountryDto country1 = getTestingCountry("C1", List.of("C2", "C3"));
        CountryDto country2 = getTestingCountry("C2", List.of("C1"));
        CountryDto country3 = getTestingCountry("C3", List.of("C1"));
        when(webCountryDao.getAllCountries()).thenReturn(List.of(country1, country2, country3));
    }

    private CountryDto getTestingCountry(String key, List<String> borders) {
        CountryDto country = new CountryDto();
        country.setCca3(key);
        country.setBorders(borders);
        return country;
    }

    @Test
    public void findRouteTest() {
        RouteDto route = bfsRouteFinder.findRoute("C1", "C3");
        assertEquals(List.of("C1", "C3"), route.getRoute());
    }

    @Test
    public void findRouteWithInvalidOriginTest() {
        assertThrows(InsufficientArgsException.class, () -> bfsRouteFinder.findRoute("C4", "C3"));
    }

    @Test
    public void findRouteWithInvalidDestinationTest() {
        assertThrows(InsufficientArgsException.class, () -> bfsRouteFinder.findRoute("C1", "C4"));
    }

    @Test
    public void findRouteWithNoPathTest() {
        CountryDto country1 = getTestingCountry("C1", List.of());
        CountryDto country2 = getTestingCountry("C2", List.of());
        when(webCountryDao.getAllCountries()).thenReturn(List.of(country1, country2));
        assertThrows(NotFoundAnyCountryForRoute.class, () -> bfsRouteFinder.findRoute("C1", "C2"));
    }
}
