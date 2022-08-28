package app.groopy.userservice.infrastructure.elasticsearch.repository;

import app.groopy.userservice.application.mapper.ApplicationMapper;
import app.groopy.userservice.domain.models.common.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;

@Repository
public class ElasticsearchUserRepository {

    private static final Integer DEFAULT_SEARCH_RANGE_IN_METERS = 10000;

    @Autowired
    private ApplicationMapper mapper;

    @Autowired
    private ESUserRepository esUserRepository;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;


    public UserDetails findByUserId(String userId) {
        return mapper.map(esUserRepository.findESUserEntityByUserId(userId).orElse(null));
    }

    public void save(UserDetails user) {
        esUserRepository.save(mapper.map(user));
    }
}
