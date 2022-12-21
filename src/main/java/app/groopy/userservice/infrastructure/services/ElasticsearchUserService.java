package app.groopy.userservice.infrastructure.services;

import app.groopy.userservice.application.mapper.ApplicationMapper;
import app.groopy.userservice.domain.models.common.UserDetails;
import app.groopy.userservice.infrastructure.repository.ESUserRepository;
import app.groopy.userservice.infrastructure.repository.exceptions.ElasticsearchServiceException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;

@Repository
public class ElasticsearchUserService {

    @Autowired
    private ApplicationMapper mapper;

    @Autowired
    private ESUserRepository esUserRepository;

    @SneakyThrows
    public void save(UserDetails user) {
        try {
            esUserRepository.save(mapper.map(user));
        } catch (Exception ex) {
            throw new ElasticsearchServiceException("SAVE", ex.getLocalizedMessage());
        }
    }
}
