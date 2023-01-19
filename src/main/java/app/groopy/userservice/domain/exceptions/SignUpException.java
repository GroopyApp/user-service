package app.groopy.userservice.domain.exceptions;

import app.groopy.userservice.domain.models.SignUpInternalRequest;

public class SignUpException extends Throwable {
    public SignUpException(SignUpInternalRequest request, String error) {
        super(String.format("An error occurred registering the user: {%s} \n exception: {%s}", request, error));
    }
}
