package app.groopy.userservice.infrastructure.providers.retrofit;

import app.groopy.userservice.infrastructure.providers.models.*;
import org.springframework.stereotype.Repository;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

@Repository
public interface FirebaseRepository {
    String SIGN_IN_ENDPOINT = "./accounts:signInWithPassword";
    String SIGN_UP_ENDPOINT = "./accounts:signUp";
    String OAUTH_SIGN_IN_ENDPOINT = "./accounts:signInWithIdp";
    String LOOKUP_PROFILE_ENDPOINT = "./accounts:lookup";
    String UPDATE_PROFILE_ENDPOINT = "./accounts:update";

    @POST(SIGN_IN_ENDPOINT)
    Call<FirebaseSignInResponse> signIn(@Query("key") String apiKey,
                                        @Body FirebaseSignInRequest request);

    @POST(OAUTH_SIGN_IN_ENDPOINT)
    Call<FirebaseOAuthResponse> oauth(@Query("key") String apiKey,
                                      @Body FirebaseOAuthRequest request);

    @POST(SIGN_UP_ENDPOINT)
    Call<FirebaseSignUpResponse> signUp(@Query("key") String apiKey,
                                        @Body FirebaseSignUpRequest request);

    @POST(LOOKUP_PROFILE_ENDPOINT)
    Call<FirebaseUserProfileResponse> lookupProfile(@Query("key") String apiKey,
                                                    @Body FirebaseUserProfileRequest request);

    @POST(UPDATE_PROFILE_ENDPOINT)
    Call<FirebaseUpdateProfileResponse> updateProfile(@Query("key") String apiKey,
                                                      @Body FirebaseUpdateProfileRequest request);
}
