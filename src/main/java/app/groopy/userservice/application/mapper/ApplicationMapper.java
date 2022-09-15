package app.groopy.userservice.application.mapper;

import app.groopy.userservice.domain.models.common.UserDetails;
import app.groopy.userservice.infrastructure.repository.models.ESUserEntity;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface ApplicationMapper {

    @Mappings({})
    UserDetails map(ESUserEntity input);

    //TODO remove these mappings or adjust them
    @Mappings({
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "surname", ignore = true),
            @Mapping(target = "subscribedRooms", ignore = true)
    })
    ESUserEntity map(UserDetails input);
}
