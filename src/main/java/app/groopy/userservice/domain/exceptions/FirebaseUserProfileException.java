package app.groopy.userservice.domain.exceptions;

public class FirebaseUserProfileException extends Throwable {

    public FirebaseUserProfileException(String firebaseError) {
        super("Firebase call failed: " + firebaseError);
    }
}
