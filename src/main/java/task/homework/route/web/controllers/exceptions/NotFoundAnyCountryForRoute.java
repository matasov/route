package task.homework.route.web.controllers.exceptions;

public class NotFoundAnyCountryForRoute extends AbstractTaskException {
    private static final String MESSAGE = "Not found any countries between %s and %s.";

    public NotFoundAnyCountryForRoute(String origin, String destination) {
        super(String.format(MESSAGE, origin, destination));
    }
}
