package app.groopy.userservice.domain.exceptions;

import app.groopy.userservice.domain.models.SignUpRequestDto;

public class SignUpException extends Throwable {
    public SignUpException(SignUpRequestDto request, String error) {
        super(String.format("An error occurred registering the user: {%s} \n exception: {%s}", request, error));
    }
}
