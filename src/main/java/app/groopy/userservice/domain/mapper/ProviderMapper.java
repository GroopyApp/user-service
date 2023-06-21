package app.groopy.userservice.domain.mapper;

import app.groopy.userservice.domain.models.SignInRequestDto;
import app.groopy.userservice.domain.models.SignUpRequestDto;
import app.groopy.userservice.infrastructure.models.AuthenticationSignInRequest;
import app.groopy.userservice.infrastructure.models.AuthenticationSignUpRequest;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface ProviderMapper {

    AuthenticationSignInRequest map(SignInRequestDto request);
    AuthenticationSignUpRequest map(SignUpRequestDto request);
}
