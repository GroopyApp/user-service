package app.groopy.userservice.infrastructure.repository;

import app.groopy.userservice.infrastructure.repository.models.FirebaseSignUpRequest;
import app.groopy.userservice.infrastructure.repository.models.FirebaseSignUpResponse;
import app.groopy.userservice.infrastructure.repository.models.FirebaseUpdateProfileRequest;
import app.groopy.userservice.infrastructure.repository.models.FirebaseUpdateProfileResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FirebaseRepository {

    //FIXME find a way to use @Value to fetch from properties file
    String FIREBASE_KEY = "AIzaSyDWPGT6TyVUsc4v_3dsa8UrkPc725-ZXgc";
    String KEY_APPENDER = "?key=" + FIREBASE_KEY;

    String SIGN_UP_ENDPOINT = "./accounts:signUp" + KEY_APPENDER;
    String UPDATE_PROFILE_ENDPOINT = "./accounts:update" + KEY_APPENDER;

    @POST(SIGN_UP_ENDPOINT)
    Call<FirebaseSignUpResponse> signUp(@Body FirebaseSignUpRequest request);

    @POST(UPDATE_PROFILE_ENDPOINT)
    Call<FirebaseUpdateProfileResponse> updateProfile(@Body FirebaseUpdateProfileRequest request);
}
