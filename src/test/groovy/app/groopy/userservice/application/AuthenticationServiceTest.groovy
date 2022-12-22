package app.groopy.userservice.application

import app.groopy.userservice.application.validators.AuthenticationValidator
import app.groopy.userservice.domain.exceptions.SignInException
import app.groopy.userservice.domain.exceptions.SignUpException
import app.groopy.userservice.domain.exceptions.AuthenticationValidationException
import app.groopy.userservice.infrastructure.repository.exceptions.ElasticsearchServiceException
import app.groopy.userservice.infrastructure.repository.exceptions.FirebaseAuthException
import app.groopy.userservice.infrastructure.services.AuthServiceProvider
import app.groopy.userservice.infrastructure.services.ElasticsearchUserService
import app.groopy.userservice.traits.SampleAuthData
import org.spockframework.spring.SpringBean
import spock.lang.Specification
import spock.lang.Subject

class AuthenticationServiceTest extends Specification implements SampleAuthData {

    @SpringBean
    private AuthenticationValidator validator = Mock AuthenticationValidator
    @SpringBean
    private AuthServiceProvider authServiceProvider = Mock AuthServiceProvider
    @SpringBean
    private ElasticsearchUserService elasticsearchUserService = Mock ElasticsearchUserService

    @Subject
    def testSubject = new AuthenticationService(validator, authServiceProvider, elasticsearchUserService)

    def "when a register request is performed and the authProvider and elasticsearch results are ok, a response is returned"() {

        given:
        def providerSignUpRequest = sampleSignUpRequest()
        def providerSignUpResponse = sampleSignUpResponse()

        and:
        authServiceProvider.signUp(providerSignUpRequest) >> providerSignUpResponse

        when:
        def registerResponse = testSubject.register(providerSignUpRequest)

        then:
        registerResponse == providerSignUpResponse
    }

    def "when a register request is performed and the validation fails, an exception is thrown"() {

        given:
        def providerSignUpRequest = sampleSignUpRequest()

        and:
        validator.validate(providerSignUpRequest) >> { throw new AuthenticationValidationException("test error") }

        when:
        testSubject.register(providerSignUpRequest)

        then:
        0 * authServiceProvider.signUp(_)
        0 * elasticsearchUserService.save(_)
        final AuthenticationValidationException _ = thrown()
    }

    def "when a register request is performed and the authProvider fails, an exception is thrown"() {

        given:
        def providerSignUpRequest = sampleSignUpRequest()

        and:
        authServiceProvider.signUp(providerSignUpRequest) >> { throw new FirebaseAuthException("test error") }

        when:
        testSubject.register(providerSignUpRequest)

        then:
        0 * elasticsearchUserService.save(_)
        final SignUpException _ = thrown()
    }

    def "when a register request is performed and the authProvider is ok but elasticsearch call fails an exception is thrown and a provider rollback is executed"() {

        given:
        def signUpResponse = sampleSignUpResponse()
        def providerSignUpRequest = sampleSignUpRequest()

        authServiceProvider.signUp(providerSignUpRequest) >> signUpResponse

        and:
        elasticsearchUserService.save(signUpResponse.user) >> { throw new ElasticsearchServiceException("SAVE", "test error")}

        when:
        testSubject.register(providerSignUpRequest)

        then:
        final SignUpException _ = thrown()
        1 * authServiceProvider.deleteUser(providerSignUpRequest.email)
    }

    def "when a login request is performed and the authProvider and elasticsearch results are ok, a response is returned"() {

        given:
        def providerSignInRequest = sampleSignInRequest()
        def providerSignInResponse = sampleSignInResponse()

        and:
        authServiceProvider.signIn(providerSignInRequest) >> providerSignInResponse

        when:
        def loginResponse = testSubject.login(providerSignInRequest)

        then:
        loginResponse == providerSignInResponse
    }

    def "when a login request is performed and the validation fails, an exception is thrown"() {

        given:
        def providerSignInRequest = sampleSignInRequest()

        and:
        validator.validate(providerSignInRequest) >> { throw new AuthenticationValidationException("test error") }

        when:
        testSubject.login(providerSignInRequest)

        then:
        0 * authServiceProvider.signIn(_)
        final AuthenticationValidationException _ = thrown()
    }

    def "when a login request is performed and the authProvider fails, an exception is thrown"() {

        given:
        def providerSignInRequest = sampleSignInRequest()

        and:
        authServiceProvider.signIn(providerSignInRequest) >> { throw new FirebaseAuthException("test error") }

        when:
        testSubject.login(providerSignInRequest)

        then:
        final SignInException _ = thrown()
    }
}
