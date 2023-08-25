package app.groopy.userservice.domain.models.common;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class UserDetailsDto {
    String userId;
    String name;
    String surname;
    String email;
    String photoUrl;
    LocalDate birthDate;
    List<String> subscribedTopics;
    List<String> subscribedEvents;
}
