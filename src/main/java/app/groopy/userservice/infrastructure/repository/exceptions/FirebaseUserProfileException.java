package app.groopy.userservice.infrastructure.repository.exceptions;

public class FirebaseUserProfileException extends Throwable {

    public FirebaseUserProfileException(String firebaseError) {
        super("Firebase call failed: " + firebaseError);
    }
}
