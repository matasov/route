package task.homework.route.web.models;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RouteDto implements Serializable {
    private List<String> route;
}
