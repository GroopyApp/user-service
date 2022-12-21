package app.groopy.userservice.domain.exceptions;

public class AuthenticationValidationException extends Throwable {
    public AuthenticationValidationException(String field, String value) {
        super(String.format("%s field is not matching the expected pattern: %s", field, value));
    }

    public AuthenticationValidationException(String description) {
        super(String.format(description));
    }
}
