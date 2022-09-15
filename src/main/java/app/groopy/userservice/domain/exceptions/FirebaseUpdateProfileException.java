package app.groopy.userservice.domain.exceptions;

public class FirebaseUpdateProfileException extends Throwable {

    public FirebaseUpdateProfileException(String firebaseError) {
        super("Firebase call failed: " + firebaseError);
    }
}
