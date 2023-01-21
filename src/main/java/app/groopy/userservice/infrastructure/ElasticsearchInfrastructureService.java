package app.groopy.userservice.infrastructure;

import app.groopy.commons.infrastructure.providers.ElasticsearchProvider;
import app.groopy.commons.infrastructure.providers.exceptions.ElasticsearchProviderException;
import app.groopy.commons.infrastructure.repository.models.elasticsearch.entities.UserEntity;
import app.groopy.userservice.infrastructure.exceptions.ElasticsearchServiceException;
import app.groopy.userservice.domain.models.common.UserDetailsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ElasticsearchInfrastructureService {

    private final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchInfrastructureService.class);

    private final ElasticsearchProvider provider;

    public ElasticsearchInfrastructureService(ElasticsearchProvider provider) {
        this.provider = provider;
    }

    public void save(UserDetailsDto user) throws ElasticsearchServiceException {
        try {
            this.provider.save(UserEntity.builder()
                            .name(user.getName())
                            .userId(user.getUserId())
                            .subscribedRooms(new ArrayList<>())
                            .surname(user.getSurname())
                    .build());
        } catch (ElasticsearchProviderException e) {
            LOGGER.error("An error occurred trying to call elasticsearch", e);
            throw new ElasticsearchServiceException(String.format("Unable to save user with id %s", user.getUserId()), e.getError());
        }
    }
}
