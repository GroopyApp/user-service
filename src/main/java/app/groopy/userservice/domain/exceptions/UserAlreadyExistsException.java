package app.groopy.userservice.domain.exceptions;

import app.groopy.userservice.infrastructure.models.UserEntity;
import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends Exception {

    private final String identifierKey;
    private final String identifier;
    private final String entityName = UserEntity.class.getSimpleName();

    public UserAlreadyExistsException(String identifierKey, String identifier) {
        super(String.format("User already exists with %s: %s", identifierKey, identifier));
        this.identifierKey = identifierKey;
        this.identifier = identifier;
    }
}
