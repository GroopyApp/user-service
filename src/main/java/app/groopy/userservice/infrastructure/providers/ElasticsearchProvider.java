package app.groopy.userservice.infrastructure.providers;

import app.groopy.userservice.domain.models.common.UserDetailsDto;
import app.groopy.userservice.infrastructure.mapper.InfrastructureMapper;
import app.groopy.userservice.infrastructure.repository.ESUserRepository;
import app.groopy.userservice.infrastructure.repository.exceptions.ElasticsearchServiceException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository //This must be marked as a repository for elasticsearch limitations
public class ElasticsearchProvider {

    @Autowired
    private InfrastructureMapper mapper;

    @Autowired
    private ESUserRepository esUserRepository;

    @SneakyThrows
    public void save(UserDetailsDto user) {
        try {
            esUserRepository.save(mapper.map(user));
        } catch (Exception ex) {
            throw new ElasticsearchServiceException("SAVE", ex.getLocalizedMessage());
        }
    }
}
