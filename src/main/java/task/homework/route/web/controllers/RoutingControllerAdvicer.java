package task.homework.route.web.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import task.homework.route.web.controllers.exceptions.AbstractTaskException;
import task.homework.route.web.controllers.exceptions.NotFoundAnyCountryForRoute;

@RestControllerAdvice
public class RoutingControllerAdvicer {
    private static final String ERROR = "error";

    @ExceptionHandler(NotFoundAnyCountryForRoute.class)
    public ResponseEntity<Map<String, String>> notFoundCountries(NotFoundAnyCountryForRoute exception) {
        return getExceptionBody(exception);
    }

    @ExceptionHandler(AbstractTaskException.class)
    public ResponseEntity<Map<String, String>> handleCustomException(AbstractTaskException exception) {
        return getExceptionBody(exception);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ConstraintViolationException exception) {
        return getExceptionBody(exception);
    }

    private ResponseEntity<Map<String, String>> getExceptionBody(Throwable throwable) {
        Map<String, String> body = new HashMap<>();
        body.put(ERROR, throwable.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
