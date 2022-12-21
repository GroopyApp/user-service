package app.groopy.userservice.presentation;

import app.groopy.protobuf.UserServiceProto;
import app.groopy.userservice.application.AuthenticationService;
import app.groopy.userservice.application.UserDetailsService;
import app.groopy.userservice.presentation.mapper.PresentationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/")
public class UserServiceController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceController.class);

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PresentationMapper presentationMapper;

    @PostMapping(value = "/signIn",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserServiceProto.SignInResponse> signIn(@RequestBody UserServiceProto.SignInRequest payload) {
        LOGGER.info("Processing message {}", payload);
        UserServiceProto.SignInResponse response = presentationMapper.map(
                authenticationService.login(presentationMapper.map(payload))
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/signUp",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserServiceProto.SignUpResponse> signUp(@RequestBody UserServiceProto.SignUpRequest payload) {
        LOGGER.info("Processing message {}", payload);
        UserServiceProto.SignUpResponse response = presentationMapper.map(
                authenticationService.register(presentationMapper.map(payload))
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserServiceProto.UserDetailsResponse> getUser(@PathVariable("userId") String userId) {
        LOGGER.info("Processing user details request for userId: {}", userId);
        UserServiceProto.UserDetailsResponse response = presentationMapper.map(userDetailsService.getUser(userId));
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "dev/deleteAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteAllUsers() {
        authenticationService.deleteAllUsers();
        return ResponseEntity.ok().build();
    }}