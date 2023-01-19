package app.groopy.userservice.infrastructure.mapper;

import app.groopy.userservice.domain.models.common.UserDetailsDto;
import app.groopy.userservice.infrastructure.repository.models.ESUserEntity;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface InfrastructureMapper {

    //TODO remove these mappings or adjust them
    @Mappings({
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "surname", ignore = true),
            @Mapping(target = "subscribedRooms", ignore = true)
    })
    ESUserEntity map(UserDetailsDto input);
}
