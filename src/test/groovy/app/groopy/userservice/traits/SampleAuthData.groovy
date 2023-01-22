package app.groopy.userservice.traits

import app.groopy.providers.firebase.models.AuthenticationSignInRequest
import app.groopy.providers.firebase.models.AuthenticationSignInResponse
import app.groopy.providers.firebase.models.AuthenticationSignUpRequest
import app.groopy.providers.firebase.models.AuthenticationSignUpResponse
import app.groopy.providers.firebase.models.commons.UserDetails
import app.groopy.userservice.domain.models.SignInResponseDto
import app.groopy.userservice.domain.models.SignInRequestDto
import app.groopy.userservice.domain.models.SignUpRequestDto
import app.groopy.userservice.domain.models.SignUpResponseDto
import app.groopy.userservice.domain.models.common.UserDetailsDto

trait SampleAuthData {

    AuthenticationSignUpRequest sampleSignUpRequest(Map params = [:]) {
        def defaultParams = [
                username: "test_username",
                email: "test@test.com",
                password: "testPassword",
                photoUrl: "http://www.example.com",
        ]
        new AuthenticationSignUpRequest.AuthenticationSignUpRequestBuilder(defaultParams + params).build()
    }

    AuthenticationSignUpResponse sampleSignUpResponse(Map params = [:]) {
        def defaultParams = [
                user: sampleUserDetails(),
                token: "1234567890abcdefg",
        ]

        new AuthenticationSignUpResponse.AuthenticationSignUpResponseBuilder(defaultParams + params).build()
    }

    AuthenticationSignInRequest sampleSignInRequest(Map params = [:]) {
        def defaultParams = [
                email: "test@test.com",
                password: "testPassword",
        ]

        new AuthenticationSignInRequest.AuthenticationSignInRequestBuilder(defaultParams + params).build()
    }

    AuthenticationSignInResponse sampleSignInResponse(Map params = [:]) {
        def defaultParams = [
                user: sampleUserDetails(),
                token: "1234567890abcdefg",
        ]

        new AuthenticationSignInResponse.AuthenticationSignInResponseBuilder(defaultParams + params).build()
    }

    UserDetails sampleUserDetails(Map params = [:]) {
        def defaultParams = [
                userId: "test_username",
                email: "test@test.com",
        ]

        new UserDetails.UserDetailsBuilder(defaultParams + params).build()
    }

}