package app.groopy.userservice.infrastructure.services;

import app.groopy.userservice.domain.exceptions.FirebaseSignUpException;
import app.groopy.userservice.domain.exceptions.FirebaseUpdateProfileException;
import app.groopy.userservice.domain.models.SignUpInternalRequest;
import app.groopy.userservice.domain.models.SignUpInternalResponse;
import app.groopy.userservice.domain.models.common.UserDetails;
import app.groopy.userservice.infrastructure.repository.FirebaseRepository;
import app.groopy.userservice.infrastructure.repository.models.FirebaseSignUpRequest;
import app.groopy.userservice.infrastructure.repository.models.FirebaseSignUpResponse;
import app.groopy.userservice.infrastructure.repository.models.FirebaseUpdateProfileRequest;
import app.groopy.userservice.infrastructure.repository.models.FirebaseUpdateProfileResponse;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import lombok.SneakyThrows;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FirebaseService {


    FirebaseRepository firebaseRepository;
    FirebaseAuth firebaseInstance;

    @SneakyThrows
    @Autowired
    public FirebaseService(@Value("${firebase.authentication.host}") String firebaseHost) {
        File file = ResourceUtils.getFile("classpath:groopy-9356d-firebase-adminsdk-iszyf-91b95d0922.json");
        InputStream in = new FileInputStream(file);

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
    public SignUpInternalResponse signUp(SignUpInternalRequest request) {

        Response<FirebaseSignUpResponse> signUpResponse = firebaseRepository.signUp(FirebaseSignUpRequest.builder()
                        .email(request.getEmail().isEmpty() ? null : request.getEmail())
                        .password(request.getPassword().isEmpty() ? null : request.getPassword())
                        .returnSecureToken(true)
                .build()).execute();
        if (!signUpResponse.isSuccessful()) {
            throw new FirebaseSignUpException(signUpResponse.errorBody() != null ? signUpResponse.errorBody().toString() : "Unknown error");
        }

        Response<FirebaseUpdateProfileResponse> updateProfileResponse = firebaseRepository.updateProfile(FirebaseUpdateProfileRequest.builder()
                        .idToken(signUpResponse.body() != null ? signUpResponse.body().getIdToken() : null)
                        .returnSecureToken(true)
                        .displayName(getRandomName())
                .build()).execute();

        if (!updateProfileResponse.isSuccessful()) {
            throw new FirebaseUpdateProfileException(updateProfileResponse.errorBody() != null ? updateProfileResponse.errorBody().toString() : "Unknown error");
        }

        FirebaseUpdateProfileResponse entity = updateProfileResponse.body();

        FirebaseToken decodedToken =  firebaseInstance.verifyIdToken(signUpResponse.body().getIdToken());

        return SignUpInternalResponse.builder()
                .user(UserDetails.builder()
                        .userId(entity.getDisplayName())
//                        .email(entity.getEmail())
                        .email("test@test.com")
                        .build())
                .token(firebaseInstance.createCustomToken(decodedToken.getUid()))
                .build();
    }

    private String getRandomName() {
        return UUID.randomUUID()
                .toString()
                .substring(0, 6);
    }

    @SneakyThrows
    public void deleteAllUsers() {
        ListUsersPage users = firebaseInstance.listUsers(null);
        firebaseInstance.deleteUsers(StreamSupport.stream(users.iterateAll().spliterator(), false).map(UserRecord::getUid).collect(Collectors.toList()));
    }
}
