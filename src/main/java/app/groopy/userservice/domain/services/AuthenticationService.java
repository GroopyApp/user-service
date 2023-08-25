package app.groopy.userservice.domain.services;

import app.groopy.userservice.domain.exceptions.SignInException;
import app.groopy.userservice.domain.exceptions.SignUpException;
import app.groopy.userservice.domain.exceptions.UserNotFoundException;
import app.groopy.userservice.domain.mapper.ProviderMapper;
import app.groopy.userservice.domain.models.*;
import app.groopy.userservice.domain.models.common.UserDetailsDto;
import app.groopy.userservice.infrastructure.models.AuthenticationSignInResponse;
import app.groopy.userservice.infrastructure.models.AuthenticationSignUpResponse;
import app.groopy.userservice.infrastructure.models.UserEntity;
import app.groopy.userservice.infrastructure.repository.AuthenticationProviderRepository;
import app.groopy.userservice.infrastructure.repository.UserRepository;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {

    private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final AuthenticationProviderRepository authenticationProvider;

    private final Validator validator;
    private final ProviderMapper providerMapper;

    @Autowired
    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationProviderRepository authenticationProvider,
            Validator validator,
            ProviderMapper providerMapper) {
        this.userRepository = userRepository;
        this.authenticationProvider = authenticationProvider;
        this.validator = validator;
        this.providerMapper = providerMapper;
    }

    @SneakyThrows
    public SignInResponseDto signIn(SignInRequestDto request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail()));
        try {
            AuthenticationSignInResponse response = authenticationProvider.signIn(providerMapper.map(request));
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            return SignInResponseDto.builder()
                    .token(response.getToken())
                    .user(UserDetailsDto.builder()
                            .userId(user.getUserId())
                            .name(user.getName())
                            .surname(user.getSurname())
                            .email(user.getEmail())
                            .photoUrl(response.getAuthenticationUserResponse().getPhotoUrl())
                            .birthDate(user.getBirthDate())
                            .subscribedTopics(user.getSubscribedTopics())
                            .subscribedEvents(user.getSubscribedEvents())
                            .build())
                    .build();
        } catch (Exception e) {
            LOGGER.error("An error occurred trying to login user: {}", request.getEmail(), e);
            throw new SignInException(request.getEmail());
        }
    }

    @SneakyThrows
    public SignInResponseDto signInWithToken(OAuthRequestDto request) {
        AuthenticationSignInResponse response = authenticationProvider.oauth(request.getToken(), request.getProvider());
        UserEntity user = userRepository.findByEmail(response.getAuthenticationUserResponse().getEmail())
                .orElseThrow(() -> new UserNotFoundException(response.getAuthenticationUserResponse().getEmail()));
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        return SignInResponseDto.builder()
                .token(response.getToken())
                .user(UserDetailsDto.builder()
                        .userId(user.getUserId())
                        .name(user.getName())
                        .surname(user.getSurname())
                        .email(user.getEmail())
                        .photoUrl(response.getAuthenticationUserResponse().getPhotoUrl())
                        .birthDate(user.getBirthDate())
                        .build())
                .build();
    }

    @SneakyThrows
    public SignUpResponseDto signUp(SignUpRequestDto request) {
        validator.validate(request);
        try {
            AuthenticationSignUpResponse response;
            UserEntity user = userRepository.save(UserEntity.builder()
                            .email(request.getEmail())
                            .birthDate(request.getBirthDate())
                            .gender(request.getGender())
                            .language(request.getPreferredLanguage())
                            .userId(request.getUsername())
                            .name(request.getName())
                            .surname(request.getSurname())
                            .phone(request.getPhone())
                    .build());

            try {
                response = authenticationProvider.signUp(providerMapper.map(request));
            } catch (Exception e) {
                LOGGER.error("An error occurred trying to register user on firebase: {}", request.getEmail(), e);
                userRepository.delete(user);
                throw e;
            }

            return SignUpResponseDto.builder()
                    .user(UserDetailsDto.builder()
                            .userId(user.getUserId())
                            .name(user.getName())
                            .surname(user.getSurname())
                            .email(user.getEmail())
                            .photoUrl(response.getAuthenticationUserResponse().getPhotoUrl())
                            .birthDate(user.getBirthDate())
                            .build())
                    .localId(response.getLocalId())
                    .token(response.getToken())
                    .build();
        } catch (Exception e) {
            LOGGER.error("An error occurred trying register user: {}", request.getEmail(), e);
            throw new SignUpException(request.getEmail());
        }
    }
}
