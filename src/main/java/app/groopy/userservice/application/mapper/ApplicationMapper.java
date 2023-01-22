package app.groopy.userservice.application.mapper;

import app.groopy.providers.firebase.models.AuthenticationSignInResponse;
import app.groopy.providers.firebase.models.AuthenticationSignUpResponse;
import app.groopy.providers.firebase.models.commons.UserDetails;
import app.groopy.userservice.domain.models.SignInResponseDto;
import app.groopy.userservice.domain.models.SignUpResponseDto;
import app.groopy.userservice.domain.models.common.UserDetailsDto;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface ApplicationMapper {

    @Mappings({
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "surname", ignore = true)
    })
    UserDetailsDto map(UserDetails input);

    SignInResponseDto map(AuthenticationSignInResponse input);
    SignUpResponseDto map(AuthenticationSignUpResponse signUp);
}
