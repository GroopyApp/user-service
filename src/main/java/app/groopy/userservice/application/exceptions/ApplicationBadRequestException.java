package app.groopy.userservice.application.exceptions;

import app.groopy.userservice.domain.models.ErrorMetadataDto;
import lombok.Getter;

@Getter
public class ApplicationBadRequestException extends ApplicationException {

    public ApplicationBadRequestException(ErrorMetadataDto errorResponse) {
        super(errorResponse);
    }
}
