package app.groopy.userservice.presentation;

import app.groopy.protobuf.UserServiceProto;
import app.groopy.userservice.application.SignInService;
import app.groopy.userservice.application.SignUpService;
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
    private SignInService signInService;
    @Autowired
    private SignUpService signUpService;

    @Autowired
    private PresentationMapper presentationMapper;

    @PostMapping(value = "/signIn",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserServiceProto.SignInResponse> signIn(@RequestBody UserServiceProto.SignInRequest payload) {
        LOGGER.info("Processing message {}", payload);
        UserServiceProto.SignInResponse response = presentationMapper.map(
                signInService.login(presentationMapper.map(payload))
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/signUp",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserServiceProto.SignUpResponse> signUp(@RequestBody UserServiceProto.SignUpRequest payload) {
        LOGGER.info("Processing message {}", payload);
        UserServiceProto.SignUpResponse response = presentationMapper.map(
                signUpService.register(presentationMapper.map(payload))
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "dev/deleteAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteAllUsers() {
        signInService.deleteAllUsers();
        return ResponseEntity.ok().build();
    }}