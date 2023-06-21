package app.groopy.userservice.application.exceptions;

import app.groopy.userservice.domain.models.ErrorMetadataDto;
import lombok.Getter;

@Getter
public class ApplicationUserNotFoundException extends ApplicationException {
    public ApplicationUserNotFoundException(ErrorMetadataDto errorResponse) {
        super(errorResponse);
    }
}
