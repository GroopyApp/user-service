package app.groopy.userservice.infrastructure.providers;

import app.groopy.userservice.domain.models.common.UserDetails;
import app.groopy.userservice.infrastructure.mapper.InfrastructureMapper;
import app.groopy.userservice.infrastructure.repository.ESUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository //This must be marked as a repository for elasticsearch limitations
public class ElasticsearchProvider {

    @Autowired
    private InfrastructureMapper mapper;

    @Autowired
    private ESUserRepository esUserRepository;

    public void save(UserDetails user) {
        esUserRepository.save(mapper.map(user));
    }
}
