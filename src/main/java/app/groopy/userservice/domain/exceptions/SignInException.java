package app.groopy.userservice.domain.exceptions;

import lombok.Getter;

@Getter
public class SignInException extends Exception {

    private final String identifier;

    public SignInException(String identifier) {
        super(String.format("Login failed for user with username/email: %s", identifier));
        this.identifier = identifier;
    }
}
