package app.groopy.userservice.application.exceptions;

import app.groopy.userservice.domain.models.ErrorMetadataDto;

public class ApplicationLoginFailedException extends ApplicationException {
    public ApplicationLoginFailedException(ErrorMetadataDto errorResponse) {
        super(errorResponse);
    }
}
