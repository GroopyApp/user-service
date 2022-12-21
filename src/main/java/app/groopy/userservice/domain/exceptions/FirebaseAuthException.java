package app.groopy.userservice.domain.exceptions;

public class FirebaseAuthException extends Throwable {

    public FirebaseAuthException(String firebaseError) {
        super("Firebase call failed: " + firebaseError);
    }
}
