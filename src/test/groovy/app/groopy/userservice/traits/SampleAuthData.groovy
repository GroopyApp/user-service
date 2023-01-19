package app.groopy.userservice.traits


import app.groopy.userservice.domain.models.SignInResponseDto
import app.groopy.userservice.domain.models.SignInRequestDto
import app.groopy.userservice.domain.models.SignUpRequestDto
import app.groopy.userservice.domain.models.SignUpResponseDto
import app.groopy.userservice.domain.models.common.UserDetailsDto

trait SampleAuthData {

    SignUpRequestDto sampleSignUpRequest(Map params = [:]) {
        def defaultParams = [
                username: "test_username",
                email: "test@test.com",
                password: "testPassword",
                photoUrl: "http://www.example.com",
        ]

        new SignUpRequestDto.SignUpRequestDtoBuilder(defaultParams + params).build()
    }

    SignUpResponseDto sampleSignUpResponse(Map params = [:]) {
        def defaultParams = [
                user: sampleUserDetails(),
                token: "1234567890abcdefg",
        ]

        new SignUpResponseDto.SignUpResponseDtoBuilder(defaultParams + params).build()
    }

    SignInRequestDto sampleSignInRequest(Map params = [:]) {
        def defaultParams = [
                email: "test@test.com",
                password: "testPassword",
        ]

        new SignInRequestDto.SignInRequestDtoBuilder(defaultParams + params).build()
    }

    SignInResponseDto sampleSignInResponse(Map params = [:]) {
        def defaultParams = [
                user: sampleUserDetails(),
                token: "1234567890abcdefg",
        ]

        new SignInResponseDto.SignInResponseDtoBuilder(defaultParams + params).build()
    }

    UserDetailsDto sampleUserDetails(Map params = [:]) {
        def defaultParams = [
                userId: "test_username",
                email: "test@test.com",
        ]

        new UserDetailsDto.UserDetailsDtoBuilder(defaultParams + params).build()
    }

}