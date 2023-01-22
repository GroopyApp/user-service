package app.groopy.userservice.application

import app.groopy.providers.elasticsearch.ElasticsearchProvider
import app.groopy.providers.elasticsearch.exceptions.ElasticsearchOperationError
import app.groopy.providers.firebase.AuthenticationProvider
import app.groopy.providers.firebase.exceptions.FirebaseOperationError
import app.groopy.providers.firebase.exceptions.FirebaseProviderException
import app.groopy.userservice.application.validators.AuthenticationValidator
import app.groopy.userservice.domain.exceptions.SignUpException
import app.groopy.userservice.domain.exceptions.AuthenticationValidationException
import app.groopy.userservice.infrastructure.exceptions.ElasticsearchServiceException
import app.groopy.userservice.traits.SampleAuthData
import org.spockframework.spring.SpringBean
import spock.lang.Specification
import spock.lang.Subject

class SignUpServiceTest extends Specification implements SampleAuthData {

    @SpringBean
    private AuthenticationValidator validator = Mock AuthenticationValidator
    @SpringBean
    private AuthenticationProvider authServiceProvider = Mock AuthenticationProvider
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
        def registerResponse = testSubject.perform(providerSignUpRequest)

        then:
        registerResponse == providerSignUpResponse
    }

    def "when a register request is performed and the validation fails, an exception is thrown"() {

        given:
        def providerSignUpRequest = sampleSignUpRequest()

        and:
        validator.validate(providerSignUpRequest) >> { throw new AuthenticationValidationException("test error") }

        when:
        testSubject.perform(providerSignUpRequest)

        then:
        0 * authServiceProvider.signUp(_)
        0 * elasticsearchProvider.save(_)
        final AuthenticationValidationException _ = thrown()
    }

    def "when a register request is performed and the authProvider fails, an exception is thrown"() {

        given:
        def providerSignUpRequest = sampleSignUpRequest()

        and:
        authServiceProvider.signUp(providerSignUpRequest) >> { throw new FirebaseProviderException(FirebaseOperationError.UNKNOWN, "test error") }

        when:
        testSubject.perform(providerSignUpRequest)

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
        elasticsearchProvider.save(signUpResponse.user) >> { throw new ElasticsearchServiceException("test error", ElasticsearchOperationError.UNKNOWN)}

        when:
        testSubject.perform(providerSignUpRequest)

        then:
        final SignUpException _ = thrown()
        1 * authServiceProvider.deleteUser(signUpResponse.localId)
    }
}
