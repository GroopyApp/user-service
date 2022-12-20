package app.groopy.userservice.domain.exceptions;

public class SignUpValidationException extends Throwable {
    public SignUpValidationException(String field, String value) {
        super(String.format("%s field is not matching the expected pattern: %s", field, value));
    }
}
