package app.groopy.userservice.domain.models;

import app.groopy.userservice.domain.models.common.UserDetailsDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetailsResponseDto {
    private UserDetailsDto user;
}
