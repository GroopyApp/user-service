package app.groopy.userservice.traits

import app.groopy.userservice.domain.models.SignInInternalRequest
import app.groopy.userservice.domain.models.SignInInternalResponse
import app.groopy.userservice.domain.models.SignUpInternalRequest
import app.groopy.userservice.domain.models.SignUpInternalResponse
import app.groopy.userservice.domain.models.common.UserDetails

trait SampleAuthData {

    SignUpInternalRequest sampleSignUpRequest(Map params = [:]) {
        def defaultParams = [
                username: "test_username",
                email: "test@test.com",
                password: "testPassword",
                photoUrl: "http://www.example.com",
        ]

        new SignUpInternalRequest.SignUpInternalRequestBuilder(defaultParams + params).build()
    }

    SignUpInternalResponse sampleSignUpResponse(Map params = [:]) {
        def defaultParams = [
                user: sampleUserDetails(),
                token: "1234567890abcdefg",
        ]

        new SignUpInternalResponse.SignUpInternalResponseBuilder(defaultParams + params).build()
    }

    SignInInternalRequest sampleSignInRequest(Map params = [:]) {
        def defaultParams = [
                email: "test@test.com",
                password: "testPassword",
        ]

        new SignInInternalRequest.SignInInternalRequestBuilder(defaultParams + params).build()
    }

    SignInInternalResponse sampleSignInResponse(Map params = [:]) {
        def defaultParams = [
                user: sampleUserDetails(),
                token: "1234567890abcdefg",
        ]

        new SignInInternalResponse.SignInInternalResponseBuilder(defaultParams + params).build()
    }

    UserDetails sampleUserDetails(Map params = [:]) {
        def defaultParams = [
                userId: "test_username",
                email: "test@test.com",
        ]

        new UserDetails.UserDetailsBuilder(defaultParams + params).build()
    }

}