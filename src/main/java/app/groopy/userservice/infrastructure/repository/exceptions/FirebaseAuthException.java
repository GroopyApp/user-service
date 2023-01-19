package app.groopy.userservice.infrastructure.repository.exceptions;

public class FirebaseAuthException extends Throwable {

    public FirebaseAuthException(String firebaseError) {
        super("Firebase call failed: " + firebaseError);
    }
}
