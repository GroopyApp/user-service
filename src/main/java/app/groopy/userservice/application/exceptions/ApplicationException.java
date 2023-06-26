package app.groopy.userservice.application.exceptions;

import app.groopy.userservice.domain.models.ErrorMetadataDto;
import lombok.Getter;

@Getter
public class ApplicationException extends Exception {
    private final ErrorMetadataDto errorResponse;

    public ApplicationException(ErrorMetadataDto errorResponse) {
        this.errorResponse = errorResponse;
    }
}
