package app.groopy.userservice.presentation.mapper;

import app.groopy.protobuf.UserServiceProto;
import app.groopy.userservice.domain.models.*;
import app.groopy.userservice.domain.models.common.UserDetails;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface PresentationMapper {

    SignUpInternalRequest map(UserServiceProto.SignUpRequest input);

    SignInInternalRequest map(UserServiceProto.SignInRequest input);

    @Mappings({@Mapping(target = "data", source = "user")})
    UserServiceProto.SignUpResponse map(SignUpInternalResponse input);

    @Mappings({@Mapping(target = "data", source = "user")})
    UserServiceProto.SignInResponse map(SignInInternalResponse input);

    @Mappings({@Mapping(target = "data", source = "user"),})
    UserServiceProto.UserDetailsResponse map(UserDetailsInternalResponse input);

    UserServiceProto.UserDetails map(UserDetails input);
}
