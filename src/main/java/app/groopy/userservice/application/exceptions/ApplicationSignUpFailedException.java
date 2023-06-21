package app.groopy.userservice.application.exceptions;

import app.groopy.userservice.domain.models.ErrorMetadataDto;

public class ApplicationSignUpFailedException extends ApplicationException {
    public ApplicationSignUpFailedException(ErrorMetadataDto errorResponse) {
        super(errorResponse);
    }
}
