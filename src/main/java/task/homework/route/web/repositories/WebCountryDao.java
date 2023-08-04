package task.homework.route.web.repositories;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import task.homework.route.web.controllers.exceptions.RepositoryException;
import task.homework.route.web.models.CountryDto;

@Component
@AllArgsConstructor
public class WebCountryDao implements CountryDao {
    private static final String EXCEPTION_TEXT = "Cannot parse repository result.";
    private static final String COUNTRIES_URL = "https://raw.githubusercontent.com/mledoze/countries/master/countries.json";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public List<CountryDto> getAllCountries() {
        String responseString = restTemplate.getForObject(COUNTRIES_URL, String.class);
        return parseCountries(responseString);
    }

    private List<CountryDto> parseCountries(String responseString) {
        try {
            return objectMapper.readValue(responseString, new TypeReference<List<CountryDto>>() {
            });
        } catch (JsonProcessingException exceptionParse) {
            throw new RepositoryException(EXCEPTION_TEXT, exceptionParse);
        }
    }

}
