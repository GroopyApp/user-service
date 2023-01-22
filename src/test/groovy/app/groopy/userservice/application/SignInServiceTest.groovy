package app.groopy.userservice.application

import app.groopy.providers.firebase.exceptions.FirebaseOperationError
import app.groopy.userservice.application.mapper.ApplicationMapper
import app.groopy.userservice.application.validators.AuthenticationValidator
import app.groopy.userservice.domain.exceptions.AuthenticationValidationException
import app.groopy.userservice.domain.exceptions.SignInException
import app.groopy.userservice.infrastructure.AuthenticationInfrastructureService
import app.groopy.userservice.infrastructure.ElasticsearchInfrastructureService
import app.groopy.userservice.infrastructure.exceptions.AuthenticationServiceException
import app.groopy.userservice.infrastructure.providers.ElasticsearchProvider
import app.groopy.userservice.infrastructure.services.AuthServiceProvider
import app.groopy.userservice.traits.SampleAuthData
import org.spockframework.spring.SpringBean
import spock.lang.Specification
import spock.lang.Subject

class SignInServiceTest extends Specification implements SampleAuthData {

    @SpringBean
    private AuthenticationValidator validator = Mock AuthenticationValidator
    @SpringBean
    private AuthenticationInfrastructureService authenticationInfrastructureService = Mock AuthServiceProvider
    @SpringBean
    private ElasticsearchInfrastructureService elasticsearchInfrastructureService = Mock ElasticsearchProvider
    @SpringBean
    private ApplicationMapper applicationMapper = Mock ApplicationMapper

    @Subject
    def testSubject = new SignInService(validator, applicationMapper, authenticationInfrastructureService, elasticsearchInfrastructureService)

    def "when a login request is performed and the authProvider and elasticsearch results are ok, a response is returned"() {

        given:
        def providerSignInRequest = sampleSignInRequest()
        def providerSignInResponse = sampleSignInResponse()

        and:
        authenticationInfrastructureService.signIn(providerSignInRequest) >> providerSignInResponse

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
        0 * authenticationInfrastructureService.signIn(_)
        final AuthenticationValidationException _ = thrown()
    }

    def "when a login request is performed and the authProvider fails, an exception is thrown"() {

        given:
        def providerSignInRequest = sampleSignInRequest()

        and:
        authenticationInfrastructureService.signIn(providerSignInRequest) >> { throw new AuthenticationServiceException("test error", FirebaseOperationError.UNKNOWN) }

        when:
        testSubject.perform(providerSignInRequest)

        then:
        final SignInException _ = thrown()
    }
}
