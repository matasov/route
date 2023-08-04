package task.homework.route.web.controllers.exceptions;

public class RepositoryException extends AbstractTaskException {
    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
