package app.groopy.userservice.infrastructure.providers;

import app.groopy.userservice.infrastructure.repository.exceptions.FirebaseAuthException;
import app.groopy.userservice.infrastructure.repository.exceptions.FirebaseUserProfileException;
import app.groopy.userservice.domain.models.SignInInternalRequest;
import app.groopy.userservice.domain.models.SignInInternalResponse;
import app.groopy.userservice.domain.models.SignUpInternalRequest;
import app.groopy.userservice.domain.models.SignUpInternalResponse;
import app.groopy.userservice.domain.models.common.UserDetails;
import app.groopy.userservice.infrastructure.repository.FirebaseRepository;
import app.groopy.userservice.infrastructure.repository.models.*;
import app.groopy.userservice.infrastructure.services.AuthServiceProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.InputStream;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class FirebaseProvider implements AuthServiceProvider {

    private final Logger LOGGER = LoggerFactory.getLogger(FirebaseProvider.class);

    FirebaseRepository firebaseRepository;
    FirebaseAuth firebaseInstance;

    @SneakyThrows
    @Autowired
    public FirebaseProvider(@Value("${firebase.authentication.host}") String firebaseHost) {
        InputStream in = new ClassPathResource("groopy-9356d-firebase-adminsdk-iszyf-91b95d0922.json").getInputStream();

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(in))
                .build();

        FirebaseApp.initializeApp(options);

        firebaseInstance = FirebaseAuth.getInstance();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(firebaseHost)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        firebaseRepository = retrofit.create(FirebaseRepository.class);
    }

    @SneakyThrows
    public SignInInternalResponse signIn(SignInInternalRequest request) {
        Response<FirebaseSignInResponse> signInResponse = firebaseRepository.signIn(FirebaseSignInRequest.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .returnSecureToken(true)
                .build()).execute();

        if (!signInResponse.isSuccessful()) {
            throw new FirebaseAuthException(signInResponse.errorBody() != null ? signInResponse.errorBody().toString() : "Unknown error");
        }

        FirebaseUserDetailsResponse entity = getUserDetails(signInResponse.body().getIdToken());

        return SignInInternalResponse.builder()
                .user(entity.getResponse())
                .token(entity.getToken())
                .build();
    }

    @SneakyThrows
    public SignUpInternalResponse signUp(SignUpInternalRequest request) {
        Response<FirebaseSignUpResponse> signUpResponse = firebaseRepository.signUp(FirebaseSignUpRequest.builder()
                        .email(request.getEmail())
                        .password(request.getPassword())
                        .returnSecureToken(true)
                .build()).execute();
        if (!signUpResponse.isSuccessful()) {
            throw new FirebaseAuthException(signUpResponse.errorBody() != null ? signUpResponse.errorBody().toString() : "Unknown error");
        }

        try {
            FirebaseUserDetailsResponse entity = updateUserDetails(signUpResponse.body().getIdToken(), request.getUsername(), request.getPhotoUrl());
            return SignUpInternalResponse.builder()
                    .user(entity.getResponse())
                    .localId(signUpResponse.body().getLocalId())
                    .token(entity.getToken())
                    .build();
        } catch (FirebaseUserProfileException ex) {
            deleteUser(signUpResponse.body().getLocalId());
            throw ex;
        }
    }

    public void deleteUser(String uid) {
        try {
            firebaseInstance.deleteUser(uid);
        } catch (Exception ex) {
            LOGGER.error(String.format("An error occurred trying to delete the user with UID: %s", uid));
            throw new RuntimeException(ex);
        }
    }

    @SneakyThrows
    public void deleteAllUsers() {
        ListUsersPage users = firebaseInstance.listUsers(null);
        firebaseInstance.deleteUsers(StreamSupport.stream(users.iterateAll().spliterator(), false).map(UserRecord::getUid).collect(Collectors.toList()));
    }

    @SneakyThrows
    private FirebaseUserDetailsResponse getUserDetails(String idToken) {
        Response<FirebaseUserProfileResponse> lookupProfileResponse = firebaseRepository.lookupProfile(FirebaseUserProfileRequest.builder()
                .idToken(idToken)
                .build()).execute();

        if (!lookupProfileResponse.isSuccessful()) {
            throw new FirebaseUserProfileException(lookupProfileResponse.errorBody() != null ? lookupProfileResponse.errorBody().toString() : "Unknown error");
        }

        FirebaseToken decodedToken =  firebaseInstance.verifyIdToken(idToken);

        if (lookupProfileResponse.body().getUsers().isEmpty()) {
            throw new FirebaseUserProfileException("User details not found");
        }

        FirebaseUserProfileResponse.User entity = lookupProfileResponse.body().getUsers().get(0);

        return FirebaseUserDetailsResponse.builder()
                .response(UserDetails.builder()
                                .userId(entity.getDisplayName())
                                .email(entity.getEmail())
                        //TODO add other fields
                                .build())
                .token(firebaseInstance.createCustomToken(decodedToken.getUid()))
                .build();
    }

    @SneakyThrows
    private FirebaseUserDetailsResponse updateUserDetails(String idToken, String username, String photoUrl) throws FirebaseUserProfileException {
        Response<FirebaseUpdateProfileResponse> updateProfileResponse = firebaseRepository.updateProfile(FirebaseUpdateProfileRequest.builder()
                .idToken(idToken)
                .returnSecureToken(true)
                .displayName(username)
                .photoUrl(photoUrl)
                .build()).execute();

        if (!updateProfileResponse.isSuccessful()) {
            throw new FirebaseUserProfileException(updateProfileResponse.errorBody() != null ? updateProfileResponse.errorBody().toString() : "Unknown error");
        }

        FirebaseToken decodedToken =  firebaseInstance.verifyIdToken(idToken);
        FirebaseUpdateProfileResponse entity = updateProfileResponse.body();

        return FirebaseUserDetailsResponse.builder()
                .response(UserDetails.builder()
                        .userId(entity.getDisplayName())
                        .email(entity.getEmail())
                        //TODO add other fields
                        .build())
                .token(firebaseInstance.createCustomToken(decodedToken.getUid()))
                .build();
    }

}
