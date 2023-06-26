package app.groopy.userservice.domain.exceptions;

import app.groopy.userservice.infrastructure.models.UserEntity;
import lombok.Getter;

@Getter
public class UserNotFoundException extends Exception {

    private final String identifier;
    private final String entityName = UserEntity.class.getSimpleName();

    public UserNotFoundException(String identifier) {
        super(String.format("User not found with username/email %s", identifier));
        this.identifier = identifier;
    }
}
