package app.groopy.userservice.domain.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMetadataDto {
    String errorDescription;
    String userIdentifier;
    String userIdentifierKey;
}
