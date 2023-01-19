package app.groopy.userservice.application.validator

import app.groopy.userservice.application.validators.AuthenticationValidator
import app.groopy.userservice.domain.exceptions.AuthenticationValidationException
import app.groopy.userservice.traits.SampleAuthData
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll;

class AuthenticationValidatorTest extends Specification implements SampleAuthData {

    @Subject
    def testSubject = new AuthenticationValidator()

    def "when a register request is performed and data are valid no exception is thrown"() {

        given:
        def providerSignUpRequest = sampleSignUpRequest()

        when:
        testSubject.validate(providerSignUpRequest)

        then:
        noExceptionThrown()
    }

    def "when a register request is performed and data contains invalid email an exception is thrown"() {

        given:
        def providerSignUpRequest = sampleSignUpRequest(["email": "invalidEmail.com"])

        when:
        testSubject.validate(providerSignUpRequest)

        then:
        final AuthenticationValidationException e = thrown()
        e.message == "email field is not matching the expected pattern: invalidEmail.com"
    }

    @Unroll
    def "when a register request is performed and #testLabel an exception is thrown"() {

        given:
        def providerSignUpRequest = sampleSignUpRequest(customUsername)

        when:
        testSubject.validate(providerSignUpRequest)

        then:
        AuthenticationValidationException e = thrown()
        e.message == "username cannot be null"

        where:
        customUsername         || testLabel
        ["username": ""]       || "username is empty"
        ["username": null]     || "username is null"
    }

}
