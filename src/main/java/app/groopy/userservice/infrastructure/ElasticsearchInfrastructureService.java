package app.groopy.userservice.infrastructure;

import app.groopy.commons.infrastructure.providers.ElasticsearchProvider;
import app.groopy.commons.infrastructure.repository.models.elasticsearch.entities.UserEntity;
import app.groopy.userservice.domain.models.common.UserDetailsDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ElasticsearchInfrastructureService {

    private final ElasticsearchProvider provider;

    public ElasticsearchInfrastructureService(ElasticsearchProvider provider) {
        this.provider = provider;
    }

    public void save(UserDetailsDto user) {
        try {
            this.provider.save(UserEntity.builder()
                            .name(user.getName())
                            .userId(user.getUserId())
                            .subscribedRooms(new ArrayList<>())
                            .surname(user.getSurname())
                    .build());
        } catch (Exception e) {
            //TODO add infrastructure exception
            throw new RuntimeException(e);
        }
    }
}
