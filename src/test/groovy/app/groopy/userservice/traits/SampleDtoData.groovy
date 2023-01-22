package app.groopy.userservice.traits

import app.groopy.userservice.domain.models.SignInRequestDto
import app.groopy.userservice.domain.models.SignInResponseDto
import app.groopy.userservice.domain.models.SignUpRequestDto
import app.groopy.userservice.domain.models.SignUpResponseDto
import app.groopy.userservice.domain.models.common.UserDetailsDto

trait SampleDtoData {

    SignUpRequestDto sampleSignUpDtoRequest(Map params = [:]) {
        def defaultParams = [
                username: "test_username",
                email: "test@test.com",
                password: "testPassword",
                photoUrl: "http://www.example.com",
        ]
        new SignUpRequestDto.SignUpRequestDtoBuilder(defaultParams + params).build()
    }

    SignUpResponseDto sampleSignUpDtoResponse(Map params = [:]) {
        def defaultParams = [
                user: sampleUserDetailsDto(),
                token: "1234567890abcdefg",
        ]

        new SignUpResponseDto.SignUpResponseDtoBuilder(defaultParams + params).build()
    }

    SignInRequestDto sampleSignInDtoRequest(Map params = [:]) {
        def defaultParams = [
                email: "test@test.com",
                password: "testPassword",
        ]

        new SignInRequestDto.SignInRequestDtoBuilder(defaultParams + params).build()
    }

    SignInResponseDto sampleSignInDtoResponse(Map params = [:]) {
        def defaultParams = [
                user: sampleUserDetailsDto(),
                token: "1234567890abcdefg",
        ]

        new SignInResponseDto.SignInResponseDtoBuilder(defaultParams + params).build()
    }

    UserDetailsDto sampleUserDetailsDto(Map params = [:]) {
        def defaultParams = [
                userId: "test_username",
                email: "test@test.com",
        ]

        new UserDetailsDto.UserDetailsDtoBuilder(defaultParams + params).build()
    }

}