package app.groopy.userservice.application.mapper;

import app.groopy.commons.infrastructure.repository.models.firebase.AuthenticationSignInResponse;
import app.groopy.commons.infrastructure.repository.models.firebase.AuthenticationSignUpResponse;
import app.groopy.commons.infrastructure.repository.models.firebase.commons.UserDetails;
import app.groopy.userservice.domain.models.SignInResponseDto;
import app.groopy.userservice.domain.models.SignUpResponseDto;
import app.groopy.userservice.domain.models.common.UserDetailsDto;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface ApplicationMapper {

    UserDetailsDto map(UserDetails input);
    SignInResponseDto map(AuthenticationSignInResponse input);
    SignUpResponseDto map(AuthenticationSignUpResponse signUp);
}
