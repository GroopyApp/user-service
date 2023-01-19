package app.groopy.userservice.application

import app.groopy.userservice.application.validators.AuthenticationValidator
import app.groopy.userservice.domain.exceptions.SignInException
import app.groopy.userservice.domain.exceptions.SignUpException
import app.groopy.userservice.domain.exceptions.AuthenticationValidationException
import app.groopy.userservice.infrastructure.providers.ElasticsearchProvider
import app.groopy.userservice.infrastructure.repository.exceptions.ElasticsearchServiceException
import app.groopy.userservice.infrastructure.repository.exceptions.FirebaseAuthException
import app.groopy.userservice.infrastructure.services.AuthServiceProvider
import app.groopy.userservice.traits.SampleAuthData
import org.spockframework.spring.SpringBean
import spock.lang.Specification
import spock.lang.Subject

class SignUpServiceTest extends Specification implements SampleAuthData {

    @SpringBean
    private AuthenticationValidator validator = Mock AuthenticationValidator
    @SpringBean
    private AuthServiceProvider authServiceProvider = Mock AuthServiceProvider
    @SpringBean
    private ElasticsearchProvider elasticsearchProvider = Mock ElasticsearchProvider

    @Subject
    def testSubject = new SignUpService(validator, authServiceProvider, elasticsearchProvider)

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
        0 * elasticsearchProvider.save(_)
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
        0 * elasticsearchProvider.save(_)
        final SignUpException _ = thrown()
    }

    def "when a register request is performed and the authProvider is ok but elasticsearch call fails an exception is thrown and a provider rollback is executed"() {

        given:
        def signUpResponse = sampleSignUpResponse()
        def providerSignUpRequest = sampleSignUpRequest()

        authServiceProvider.signUp(providerSignUpRequest) >> signUpResponse

        and:
        elasticsearchProvider.save(signUpResponse.user) >> { throw new ElasticsearchServiceException("SAVE", "test error")}

        when:
        testSubject.register(providerSignUpRequest)

        then:
        final SignUpException _ = thrown()
        1 * authServiceProvider.deleteUser(signUpResponse.localId)
    }
}
