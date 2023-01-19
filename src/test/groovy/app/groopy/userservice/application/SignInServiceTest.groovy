package app.groopy.userservice.application

import app.groopy.userservice.application.validators.AuthenticationValidator
import app.groopy.userservice.domain.exceptions.AuthenticationValidationException
import app.groopy.userservice.domain.exceptions.SignInException
import app.groopy.userservice.domain.exceptions.SignUpException
import app.groopy.userservice.infrastructure.providers.ElasticsearchProvider
import app.groopy.userservice.infrastructure.repository.exceptions.ElasticsearchServiceException
import app.groopy.userservice.infrastructure.repository.exceptions.FirebaseAuthException
import app.groopy.userservice.infrastructure.services.AuthServiceProvider
import app.groopy.userservice.traits.SampleAuthData
import org.spockframework.spring.SpringBean
import spock.lang.Specification
import spock.lang.Subject

class SignInServiceTest extends Specification implements SampleAuthData {

    @SpringBean
    private AuthenticationValidator validator = Mock AuthenticationValidator
    @SpringBean
    private AuthServiceProvider authServiceProvider = Mock AuthServiceProvider
    @SpringBean
    private ElasticsearchProvider elasticsearchProvider = Mock ElasticsearchProvider

    @Subject
    def testSubject = new SignInService(validator, authServiceProvider, elasticsearchProvider)

    def "when a login request is performed and the authProvider and elasticsearch results are ok, a response is returned"() {

        given:
        def providerSignInRequest = sampleSignInRequest()
        def providerSignInResponse = sampleSignInResponse()

        and:
        authServiceProvider.signIn(providerSignInRequest) >> providerSignInResponse

        when:
        def loginResponse = testSubject.perform(providerSignInRequest)

        then:
        loginResponse == providerSignInResponse
    }

    def "when a login request is performed and the validation fails, an exception is thrown"() {

        given:
        def providerSignInRequest = sampleSignInRequest()

        and:
        validator.validate(providerSignInRequest) >> { throw new AuthenticationValidationException("test error") }

        when:
        testSubject.perform(providerSignInRequest)

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
        testSubject.perform(providerSignInRequest)

        then:
        final SignInException _ = thrown()
    }
}
