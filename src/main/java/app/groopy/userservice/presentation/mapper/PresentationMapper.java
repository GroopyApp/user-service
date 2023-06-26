package app.groopy.userservice.presentation.mapper;

import app.groopy.protobuf.UserServiceProto;
import app.groopy.userservice.domain.models.*;
import app.groopy.userservice.domain.models.common.UserDetailsDto;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface PresentationMapper {

    SignUpRequestDto map(UserServiceProto.SignUpRequest input);

    SignInRequestDto map(UserServiceProto.SignInRequest input);

    OAuthRequestDto map(UserServiceProto.OAuthRequest input);

    @Mappings({@Mapping(target = "data", source = "user")})
    UserServiceProto.SignUpResponse map(SignUpResponseDto input);

    @Mappings({@Mapping(target = "data", source = "user")})
    UserServiceProto.SignInResponse map(SignInResponseDto input);

    @Mappings({@Mapping(target = "data", source = "user"),})
    UserServiceProto.UserDetailsResponse map(UserDetailsResponseDto input);

    UserServiceProto.UserDetails map(UserDetailsDto input);

    default LocalDateTime toLocalDate(String input) {
        return LocalDateTime.parse(input);
    }

    default String toDateString(LocalDateTime input) {
        return input.toString();
    }
}
