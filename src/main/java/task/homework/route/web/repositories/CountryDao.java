package task.homework.route.web.repositories;

import java.util.List;
import task.homework.route.web.models.CountryDto;

public interface CountryDao {
    List<CountryDto> getAllCountries();
}
