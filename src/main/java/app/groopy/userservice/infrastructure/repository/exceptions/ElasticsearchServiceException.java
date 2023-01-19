package app.groopy.userservice.infrastructure.repository.exceptions;

public class ElasticsearchServiceException extends Throwable {

    public ElasticsearchServiceException(String operation, String error) {
        super(String.format("Elasticsearch failed performing %s operation: %s", operation, error));
    }
}
