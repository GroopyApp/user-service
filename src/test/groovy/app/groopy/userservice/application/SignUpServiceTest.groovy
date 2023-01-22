package app.groopy.userservice.application

import app.groopy.providers.elasticsearch.exceptions.ElasticsearchOperationError
import app.groopy.providers.firebase.exceptions.FirebaseOperationError
import app.groopy.userservice.application.mapper.ApplicationMapper
import app.groopy.userservice.application.mapper.ApplicationMapperImpl
import app.groopy.userservice.application.validators.AuthenticationValidator
import app.groopy.userservice.domain.exceptions.SignUpException
import app.groopy.userservice.domain.exceptions.AuthenticationValidationException
import app.groopy.userservice.infrastructure.AuthenticationInfrastructureService
import app.groopy.userservice.infrastructure.ElasticsearchInfrastructureService
import app.groopy.userservice.infrastructure.exceptions.AuthenticationServiceException
import app.groopy.userservice.infrastructure.exceptions.ElasticsearchServiceException
import app.groopy.userservice.traits.SampleAuthData
import app.groopy.userservice.traits.SampleDtoData
import org.spockframework.spring.SpringBean
import spock.lang.Specification
import spock.lang.Subject

class SignUpServiceTest extends Specification implements SampleAuthData, SampleDtoData {

    @SpringBean
    private AuthenticationValidator validator = Mock AuthenticationValidator
    @SpringBean
    private ApplicationMapper mapper = new ApplicationMapperImpl()
    @SpringBean
    private AuthenticationInfrastructureService authenticationInfrastructureService = Mock AuthenticationInfrastructureService
    @SpringBean
    private ElasticsearchInfrastructureService elasticsearchInfrastructureService = Mock ElasticsearchInfrastructureService

    @Subject
    def testSubject = new SignUpService(validator, mapper, authenticationInfrastructureService, elasticsearchInfrastructureService)

    def "when a register request is performed and the authenticationService and elasticsearch results are ok, a response is returned"() {

        given:
        def signUpRequest = sampleSignUpRequest()
        def signUpResponse = sampleSignUpResponse()
        def signUpDtoRequest = sampleSignUpDtoRequest()
        def signUpDtoResponse = sampleSignUpDtoResponse()

        and:
        authenticationInfrastructureService.signUp(signUpRequest) >> signUpResponse

        when:
        def registerResponse = testSubject.perform(signUpDtoRequest)

        then:
        registerResponse == signUpDtoResponse
    }

    def "when a register request is performed and the validation fails, an exception is thrown"() {

        given:
        def signUpDtoRequest = sampleSignUpDtoRequest()

        and:
        validator.validate(signUpDtoRequest) >> { throw new AuthenticationValidationException("test error") }

        when:
        testSubject.perform(signUpDtoRequest)

        then:
        0 * authenticationInfrastructureService.signUp(_)
        0 * elasticsearchInfrastructureService.save(_)
        final AuthenticationValidationException _ = thrown()
    }

    def "when a register request is performed and the authenticationService fails, an exception is thrown"() {

        given:
        def signUpDtoRequest = sampleSignUpDtoRequest()
        def signUpRequest = sampleSignUpRequest()

        and:
        authenticationInfrastructureService.signUp(signUpRequest) >> { throw new AuthenticationServiceException("test error", FirebaseOperationError.UNKNOWN) }

        when:
        testSubject.perform(signUpDtoRequest)

        then:
        0 * elasticsearchInfrastructureService.save(_)
        final SignUpException _ = thrown()
    }

    def "when a register request is performed and the authenticationService is ok but elasticsearch call fails an exception is thrown and a provider rollback is executed"() {

        given:
        def signUpResponse = sampleSignUpResponse()
        def signUpRequest = sampleSignUpRequest()
        def signUpDtoRequest = sampleSignUpDtoRequest()

        authenticationInfrastructureService.signUp(signUpRequest) >> signUpResponse

        and:
        elasticsearchInfrastructureService.save(mapper.map(signUpResponse.user)) >> { throw new ElasticsearchServiceException("test error", ElasticsearchOperationError.UNKNOWN)}

        when:
        testSubject.perform(signUpDtoRequest)

        then:
        final SignUpException _ = thrown()
        1 * authenticationInfrastructureService.deleteUser(signUpResponse.localId)
    }
}
