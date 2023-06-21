package app.groopy.userservice.application.exceptions;

import app.groopy.userservice.domain.models.ErrorMetadataDto;
import lombok.Getter;

@Getter
public class ApplicationAlreadyRegisteredException extends ApplicationException {
    public ApplicationAlreadyRegisteredException(ErrorMetadataDto errorResponse) {
        super(errorResponse);
    }
}
