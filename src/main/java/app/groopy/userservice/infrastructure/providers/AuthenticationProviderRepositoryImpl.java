package app.groopy.userservice.infrastructure.providers;


import app.groopy.userservice.infrastructure.models.AuthenticationSignInRequest;
import app.groopy.userservice.infrastructure.models.AuthenticationSignInResponse;
import app.groopy.userservice.infrastructure.models.AuthenticationSignUpRequest;
import app.groopy.userservice.infrastructure.models.AuthenticationSignUpResponse;
import app.groopy.userservice.infrastructure.models.AuthenticationUserResponse;
import app.groopy.userservice.infrastructure.providers.exception.AuthException;
import app.groopy.userservice.infrastructure.providers.exception.FirebaseUserProfileException;
import app.groopy.userservice.infrastructure.providers.models.*;
import app.groopy.userservice.infrastructure.providers.retrofit.FirebaseRepository;
import app.groopy.userservice.infrastructure.repository.AuthenticationProviderRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthenticationProviderRepositoryImpl implements AuthenticationProviderRepository {

    private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationProviderRepositoryImpl.class);

    FirebaseAuth firebaseInstance;
    FirebaseRepository firebaseRepository;

    @SneakyThrows
    @Autowired
    public AuthenticationProviderRepositoryImpl(@Value("${firebase.host}") String firebaseHost) {

        FirebaseApp.initializeApp();

        firebaseInstance = FirebaseAuth.getInstance();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(firebaseHost)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.firebaseRepository = retrofit.create(FirebaseRepository.class);
    }

    @SneakyThrows
    public AuthenticationSignInResponse signIn(AuthenticationSignInRequest request) {
        Response<FirebaseSignInResponse> signInResponse = firebaseRepository.signIn(FirebaseSignInRequest.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .returnSecureToken(true)
                .build()).execute();

        if (!signInResponse.isSuccessful()) {
            throw new AuthException(signInResponse.errorBody() != null ? signInResponse.errorBody().toString() : "Unknown error");
        }

        FirebaseUserDetailsResponse entity = getUserDetails(signInResponse.body().getIdToken());

        return AuthenticationSignInResponse.builder()
                .authenticationUserResponse(AuthenticationUserResponse.builder()
                        .name(entity.getResponse().getDisplayName())
                        .email(entity.getResponse().getEmail())
                        .photoUrl(entity.getResponse().getPhotoUrl())
                        .build())
                .token(entity.getToken())
                .build();
    }

    @SneakyThrows
    public AuthenticationSignUpResponse signUp(AuthenticationSignUpRequest request) {
        Response<FirebaseSignUpResponse> signUpResponse = firebaseRepository.signUp(FirebaseSignUpRequest.builder()
                        .email(request.getEmail())
                        .password(request.getPassword())
                        .returnSecureToken(true)
                .build()).execute();
        if (!signUpResponse.isSuccessful()) {
            throw new AuthException(signUpResponse.errorBody() != null ? signUpResponse.errorBody().toString() : "Unknown error");
        }

        try {
            FirebaseUserDetailsResponse entity = updateUserDetails(signUpResponse.body().getIdToken(), request.getUsername(), request.getPhotoUrl());
            return AuthenticationSignUpResponse.builder()
                    .authenticationUserResponse(AuthenticationUserResponse.builder()
                            .email(entity.getResponse().getEmail())
                            .photoUrl(entity.getResponse().getPhotoUrl())
                            .build())
                    .localId(signUpResponse.body().getLocalId())
                    .token(entity.getToken())
                    .build();
        } catch (FirebaseUserProfileException ex) {
            deleteUser(signUpResponse.body().getLocalId());
            throw ex;
        }
    }

    @SneakyThrows
    public AuthenticationSignInResponse oauth(String token, String provider) {
        String postBody = String.format("id_token=[%s]&providerId=[%s]", token, provider);
        Response<FirebaseOAuthResponse> signUpResponse = firebaseRepository.oauth(FirebaseOAuthRequest.builder()
                        .requestUri("")
                        .postBody(postBody)
                        .returnIdpCredential(true)
                        .returnSecureToken(true)
                .build()).execute();
        if (!signUpResponse.isSuccessful()) {
            throw new AuthException(signUpResponse.errorBody() != null ? signUpResponse.errorBody().toString() : "Unknown error");
        }
        return AuthenticationSignInResponse.builder()
                .authenticationUserResponse(AuthenticationUserResponse.builder()
                        .email(signUpResponse.body().getEmail())
                        .name(signUpResponse.body().getDisplayName())
                        .photoUrl(signUpResponse.body().getPhotoUrl())
                        .build())
                .token(signUpResponse.body().getIdToken())
                .build();
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

        FirebaseUserProfileResponse.ProviderUser entity = lookupProfileResponse.body().getUsers().get(0);

        return FirebaseUserDetailsResponse.builder()
                .response(FirebaseUserResponse.builder()
                        .displayName(entity.getDisplayName())
                        .userId(entity.getLocalId())
                        .disabled(entity.getDisabled())
                        .photoUrl(entity.getPhotoUrl())
                        .email(entity.getEmail())
                        .emailVerified(entity.getEmailVerified())
                        .validSince(entity.getValidSince())
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
                .response(FirebaseUserResponse.builder()
                        .userId(entity.getDisplayName())
                        .email(entity.getEmail())
                        .photoUrl(entity.getPhotoUrl())
                        .build())
                .token(firebaseInstance.createCustomToken(decodedToken.getUid()))
                .build();
    }

}
