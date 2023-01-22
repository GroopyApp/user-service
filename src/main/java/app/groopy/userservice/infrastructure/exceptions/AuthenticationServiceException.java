package app.groopy.userservice.infrastructure.exceptions;

import app.groopy.providers.firebase.exceptions.FirebaseOperationError;

public class AuthenticationServiceException extends Throwable {

    FirebaseOperationError causeError;

    public AuthenticationServiceException(String description, FirebaseOperationError cause) {
        super(String.format("%s. Root cause: %s", description, cause));
        this.causeError = cause;
    }
}
