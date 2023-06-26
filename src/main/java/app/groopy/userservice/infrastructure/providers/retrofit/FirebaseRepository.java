package app.groopy.userservice.infrastructure.providers.retrofit;

import app.groopy.userservice.infrastructure.providers.models.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FirebaseRepository {

    //FIXME find a way to use @Value to fetch from properties file
    String FIREBASE_KEY = "AIzaSyDWPGT6TyVUsc4v_3dsa8UrkPc725-ZXgc";
    String KEY_APPENDER = "?key=" + FIREBASE_KEY;

    String SIGN_IN_ENDPOINT = "./accounts:signInWithPassword" + KEY_APPENDER;
    String SIGN_UP_ENDPOINT = "./accounts:signUp" + KEY_APPENDER;
    String OAUTH_SIGN_IN_ENDPOINT = "./accounts:signInWithIdp" + KEY_APPENDER;

    String LOOKUP_PROFILE_ENDPOINT = "./accounts:lookup" + KEY_APPENDER;
    String UPDATE_PROFILE_ENDPOINT = "./accounts:update" + KEY_APPENDER;

    @POST(SIGN_IN_ENDPOINT)
    Call<FirebaseSignInResponse> signIn(@Body FirebaseSignInRequest request);

    @POST(OAUTH_SIGN_IN_ENDPOINT)
    Call<FirebaseOAuthResponse> oauth(@Body FirebaseOAuthRequest request);

    @POST(SIGN_UP_ENDPOINT)
    Call<FirebaseSignUpResponse> signUp(@Body FirebaseSignUpRequest request);

    @POST(LOOKUP_PROFILE_ENDPOINT)
    Call<FirebaseUserProfileResponse> lookupProfile(@Body FirebaseUserProfileRequest request);

    @POST(UPDATE_PROFILE_ENDPOINT)
    Call<FirebaseUpdateProfileResponse> updateProfile(@Body FirebaseUpdateProfileRequest request);
}