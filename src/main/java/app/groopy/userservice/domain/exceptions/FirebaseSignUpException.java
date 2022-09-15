package app.groopy.userservice.domain.exceptions;

public class FirebaseSignUpException extends Throwable {

    public FirebaseSignUpException(String firebaseError) {
        super("Firebase call failed: " + firebaseError);
    }
}
