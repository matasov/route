package task.homework.route.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintViolationException;
import task.homework.route.web.controllers.exceptions.AbstractTaskException;
import task.homework.route.web.controllers.exceptions.NotFoundAnyCountryForRoute;
import task.homework.route.web.models.RouteDto;
import task.homework.route.web.services.RouteFinder;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class RoutingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RouteFinder routeFinder;

    @Test
    public void getRouteTest() throws Exception {
        RouteDto expectedRouteDto = new RouteDto(List.of("CZE", "AUT", "ITA"));
        given(routeFinder.findRoute("CZE", "ITA")).willReturn(expectedRouteDto);

        mockMvc.perform(get("/routing/CZE/ITA")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedRouteDto)));

        verify(routeFinder).findRoute("CZE", "ITA");
    }

    @Test
    public void testNotFoundCountriesException() throws Exception {
        given(routeFinder.findRoute(anyString(), anyString()))
                .willThrow(new NotFoundAnyCountryForRoute("AGA", "DAD"));

        mockMvc.perform(get("/routing/AGA/DAD")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"Not found any countries between AGA and DAD.\"}"));
    }

    @Test
    public void testAbstractTaskException() throws Exception {
        given(routeFinder.findRoute(anyString(), anyString()))
                .willThrow(new AbstractTaskException("Not found origin country OOO"));

        mockMvc.perform(get("/routing/OOO/ITA")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"Not found origin country OOO\"}"));
    }

    @Test
    public void testConstraintViolationException() throws Exception {
        given(routeFinder.findRoute(anyString(), anyString()))
                .willThrow(new ConstraintViolationException("getRoute.destination: size must be between 2 and 3", new HashSet<>()));

        mockMvc.perform(get("/routing/CZE/I")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"getRoute.destination: size must be between 2 and 3\"}"));
    }

}
