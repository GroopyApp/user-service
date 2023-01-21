package app.groopy.userservice.infrastructure.exceptions;


import app.groopy.commons.infrastructure.providers.exceptions.ElasticsearchOperationError;

public class ElasticsearchServiceException extends Throwable {

    ElasticsearchOperationError causeError;

    public ElasticsearchServiceException(String description, ElasticsearchOperationError cause) {
        super(String.format("Elasticsearch error -> %s. Root cause: %s", description, cause));
        this.causeError = cause;
    }
}
