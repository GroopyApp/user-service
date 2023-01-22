package app.groopy.userservice.application.validator

import app.groopy.userservice.application.validators.AuthenticationValidator
import app.groopy.userservice.domain.exceptions.AuthenticationValidationException
import app.groopy.userservice.traits.SampleDtoData
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll;

class AuthenticationValidatorTest extends Specification implements SampleDtoData {

    @Subject
    def testSubject = new AuthenticationValidator()

    def "when a register request is performed and data are valid no exception is thrown"() {

        given:
        def signUpDtoRequest = sampleSignUpDtoRequest()

        when:
        testSubject.validate(signUpDtoRequest)

        then:
        noExceptionThrown()
    }

    def "when a register request is performed and data contains invalid email an exception is thrown"() {

        given:
        def signUpDtoRequest = sampleSignUpDtoRequest(["email": "invalidEmail.com"])

        when:
        testSubject.validate(signUpDtoRequest)

        then:
        final AuthenticationValidationException e = thrown()
        e.message == "email field is not matching the expected pattern: invalidEmail.com"
    }

    @Unroll
    def "when a register request is performed and #testLabel an exception is thrown"() {

        given:
        def signUpDtoRequest = sampleSignUpDtoRequest(customUsername)

        when:
        testSubject.validate(signUpDtoRequest)

        then:
        AuthenticationValidationException e = thrown()
        e.message == "username cannot be null"

        where:
        customUsername         || testLabel
        ["username": ""]       || "username is empty"
        ["username": null]     || "username is null"
    }

}
