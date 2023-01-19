package app.groopy.userservice.domain.models;

import app.groopy.userservice.domain.models.common.UserDetailsDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpResponseDto {
    private UserDetailsDto user;
    private String localId;
    private String token;
}
