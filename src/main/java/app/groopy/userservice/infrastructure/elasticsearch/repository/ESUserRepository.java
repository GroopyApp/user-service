package app.groopy.userservice.infrastructure.elasticsearch.repository;

import app.groopy.userservice.infrastructure.elasticsearch.repository.models.entities.ESUserEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface ESUserRepository extends ElasticsearchRepository<ESUserEntity, String> {

    Optional<ESUserEntity> findESUserEntityByUserId(String userId);
}