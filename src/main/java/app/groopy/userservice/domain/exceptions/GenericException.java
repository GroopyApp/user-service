package app.groopy.userservice.domain.exceptions;

import app.groopy.userservice.domain.models.SignInRequestDto;

public class GenericException extends Throwable {
    public GenericException(String error) {
        super(String.format("A generic error occurred. \n exception: {%s}", error));
    }
}
