package app.groopy.userservice.infrastructure.repository;

import app.groopy.userservice.infrastructure.models.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

    Optional<UserEntity> findByUserId(String userId);

    Optional<UserEntity> findByEmail(String email);
}
