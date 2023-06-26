package app.groopy.userservice.domain.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class SignUpRequestDto {
    String username;
    String name;
    String surname;
    String email;
    String password;
    String photoUrl;
    LocalDate birthDate;
    String phone;
    String gender;
    String preferredLanguage;
}
