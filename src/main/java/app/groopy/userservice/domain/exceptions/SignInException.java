package app.groopy.userservice.domain.exceptions;

import app.groopy.userservice.domain.models.SignInInternalRequest;

public class SignInException extends Throwable {
    public SignInException(SignInInternalRequest request, String error) {
        super(String.format("An error occurred trying to login: {%s}. \n exception: {%s}", request, error));
    }
}
