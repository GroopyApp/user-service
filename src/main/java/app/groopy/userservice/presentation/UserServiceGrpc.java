package app.groopy.userservice.presentation;

import app.groopy.protobuf.UserServiceProto;
import app.groopy.userservice.application.SignInService;
import app.groopy.userservice.application.SignUpService;
import app.groopy.userservice.presentation.mapper.PresentationMapper;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


@GrpcService
public class UserServiceGrpc extends app.groopy.protobuf.UserServiceGrpc.UserServiceImplBase {

    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceGrpc.class);

    @Autowired
    private SignInService signInService;
    @Autowired
    private SignUpService signUpService;

    @Autowired
    private PresentationMapper presentationMapper;

    @Override
    public void signIn(UserServiceProto.SignInRequest request, StreamObserver<UserServiceProto.SignInResponse> responseObserver) {
        LOGGER.info("Processing message {}", request);
        UserServiceProto.SignInResponse response = presentationMapper.map(
                signInService.perform(presentationMapper.map(request))
        );

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void signUp(UserServiceProto.SignUpRequest request, StreamObserver<UserServiceProto.SignUpResponse> responseObserver) {
        LOGGER.info("Processing message {}", request);
        UserServiceProto.SignUpResponse response = presentationMapper.map(
                signUpService.perform(presentationMapper.map(request))
        );
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}