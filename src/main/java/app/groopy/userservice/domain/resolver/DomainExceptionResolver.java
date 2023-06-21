package app.groopy.userservice.domain.resolver;

import app.groopy.userservice.application.exceptions.*;
import app.groopy.userservice.domain.exceptions.SignInException;
import app.groopy.userservice.domain.exceptions.SignUpException;
import app.groopy.userservice.domain.exceptions.UserAlreadyExistsException;
import app.groopy.userservice.domain.exceptions.UserNotFoundException;
import app.groopy.userservice.domain.models.ErrorMetadataDto;

public class DomainExceptionResolver {


    public static ApplicationException resolve(Exception e) {
        if (e instanceof UserNotFoundException) {
            var ex = (UserNotFoundException) e;
            return new ApplicationUserNotFoundException(ErrorMetadataDto.builder()
                    .userIdentifier(ex.getIdentifier())
                    .errorDescription(ex.getLocalizedMessage())
                    .build());
        } else if (e instanceof SignInException) {
            var ex = (SignInException) e;
            return new ApplicationLoginFailedException(ErrorMetadataDto.builder()
                    .userIdentifier(ex.getIdentifier())
                    .errorDescription(ex.getLocalizedMessage())
                    .build());
        } else if (e instanceof SignUpException) {
            var ex = (SignUpException) e;
            return new ApplicationSignUpFailedException(ErrorMetadataDto.builder()
                    .userIdentifier(ex.getIdentifier())
                    .errorDescription(ex.getLocalizedMessage())
                    .build());
        } else if (e instanceof UserAlreadyExistsException) {
            var ex = (UserAlreadyExistsException) e;
            return new ApplicationAlreadyRegisteredException(ErrorMetadataDto.builder()
                    .userIdentifier(ex.getIdentifier())
                    .userIdentifierKey(ex.getIdentifierKey())
                    .errorDescription(ex.getLocalizedMessage())
                    .build());
        }

        return new ApplicationException(ErrorMetadataDto.builder()
                .errorDescription(String.format("Unknown error: %s", e.getLocalizedMessage()))
                .build());
    }
}
