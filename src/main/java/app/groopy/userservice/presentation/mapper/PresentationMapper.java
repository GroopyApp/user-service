package app.groopy.userservice.presentation.mapper;

import app.groopy.protobuf.UserServiceProto;
import app.groopy.userservice.domain.models.*;
import app.groopy.userservice.domain.models.common.UserDetails;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface PresentationMapper {


    @Mappings({})
    SignInInternalRequest map(UserServiceProto.SignInRequest input);

    @Mappings({@Mapping(target = "data", source = "user"),})
    UserServiceProto.SignInResponse map(SignInInternalResponse input);

    @Mappings({})
    SignUpInternalRequest map(UserServiceProto.SignUpRequest input);

    @Mappings({@Mapping(target = "data", source = "user"),})
    UserServiceProto.SignUpResponse map(SignUpInternalResponse input);

    @Mappings({@Mapping(target = "data", source = "user"),})
    UserServiceProto.UserDetailsResponse map(UserDetailsInternalResponse input);

    @Mappings({
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "surname", ignore = true),
            @Mapping(target = "emailAddress", ignore = true),
            @Mapping(target = "preferredLanguages", ignore = true),
            @Mapping(target = "dateOfBirth", ignore = true)
    })
    UserServiceProto.UserDetails map(UserDetails input);
}
