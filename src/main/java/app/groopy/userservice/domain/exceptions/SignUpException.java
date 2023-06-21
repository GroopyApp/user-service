package app.groopy.userservice.domain.exceptions;

import lombok.Getter;

@Getter
public class SignUpException extends Exception {

    private final String identifier;

    public SignUpException(String identifier) {
        super(String.format("SignUp failed for user with username/email: %s", identifier));
        this.identifier = identifier;
    }
}
