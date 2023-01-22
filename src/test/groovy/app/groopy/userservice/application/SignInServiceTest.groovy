package app.groopy.userservice.application

import app.groopy.providers.firebase.exceptions.FirebaseOperationError
import app.groopy.userservice.application.mapper.ApplicationMapper
import app.groopy.userservice.application.mapper.ApplicationMapperImpl
import app.groopy.userservice.application.validators.AuthenticationValidator
import app.groopy.userservice.domain.exceptions.AuthenticationValidationException
import app.groopy.userservice.domain.exceptions.SignInException
import app.groopy.userservice.infrastructure.AuthenticationInfrastructureService
import app.groopy.userservice.infrastructure.ElasticsearchInfrastructureService
import app.groopy.userservice.infrastructure.exceptions.AuthenticationServiceException
import app.groopy.userservice.traits.SampleAuthData
import app.groopy.userservice.traits.SampleDtoData
import org.spockframework.spring.SpringBean
import spock.lang.Specification
import spock.lang.Subject

class SignInServiceTest extends Specification implements SampleAuthData, SampleDtoData {

    @SpringBean
    private AuthenticationValidator validator = Mock AuthenticationValidator
    @SpringBean
    private AuthenticationInfrastructureService authenticationInfrastructureService = Mock AuthenticationInfrastructureService
    @SpringBean
    private ElasticsearchInfrastructureService elasticsearchInfrastructureService = Mock ElasticsearchInfrastructureService
    @SpringBean
    private ApplicationMapper applicationMapper = new ApplicationMapperImpl()

    @Subject
    def testSubject = new SignInService(validator, applicationMapper, authenticationInfrastructureService, elasticsearchInfrastructureService)

    def "when a login request is performed and the authProvider and elasticsearch results are ok, a response is returned"() {

        given:
        def signInRequest = sampleSignInRequest()
        def signInResponse = sampleSignInResponse()
        def signInDtoRequest = sampleSignInDtoRequest()
        def signInDtoResponse = sampleSignInDtoResponse()

        and:
        authenticationInfrastructureService.signIn(signInRequest) >> signInResponse

        when:
        def loginResponse = testSubject.perform(signInDtoRequest)

        then:
        loginResponse == signInDtoResponse
    }

    def "when a login request is performed and the validation fails, an exception is thrown"() {

        given:
        def signInDtoRequest = sampleSignInDtoRequest()

        and:
        validator.validate(signInDtoRequest) >> { throw new AuthenticationValidationException("test error") }

        when:
        testSubject.perform(signInDtoRequest)

        then:
        0 * authenticationInfrastructureService.signIn(_)
        final AuthenticationValidationException _ = thrown()
    }

    def "when a login request is performed and the authProvider fails, an exception is thrown"() {

        given:
        def signInRequest = sampleSignInRequest()
        def signInDtoRequest = sampleSignInDtoRequest()

        and:
        authenticationInfrastructureService.signIn(signInRequest) >> { throw new AuthenticationServiceException("test error", FirebaseOperationError.UNKNOWN) }

        when:
        testSubject.perform(signInDtoRequest)

        then:
        final SignInException _ = thrown()
    }
}
