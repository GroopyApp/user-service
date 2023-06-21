package app.groopy.userservice.domain.services;

import app.groopy.userservice.domain.exceptions.UserAlreadyExistsException;
import app.groopy.userservice.domain.models.SignInRequestDto;
import app.groopy.userservice.domain.models.SignUpRequestDto;
import app.groopy.userservice.infrastructure.models.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.query.Query;

import static org.apache.commons.validator.GenericValidator.isEmail;


@Component
public class Validator {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public Validator(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void validate(SignUpRequestDto request) throws UserAlreadyExistsException {
        //TODO add standard validations for fields.
        Query query = new Query();
        var identifierToValidate = isEmail(request.getEmail()) ? "email" : "userId";
        query.addCriteria(Criteria.where(identifierToValidate).is(request.getEmail()));
        var result = mongoTemplate.findOne(query, UserEntity.class);
        if (result != null) {
            throw new UserAlreadyExistsException(identifierToValidate, request.getEmail());
        }
    }
}
