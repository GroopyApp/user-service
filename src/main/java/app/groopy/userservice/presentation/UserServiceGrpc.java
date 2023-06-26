package app.groopy.userservice.presentation;

import app.groopy.protobuf.UserServiceProto;
import app.groopy.userservice.application.ApplicationService;
import app.groopy.userservice.application.exceptions.ApplicationException;
import app.groopy.userservice.presentation.mapper.PresentationMapper;
import app.groopy.userservice.presentation.resolver.ApplicationExceptionResolver;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


@GrpcService
public class UserServiceGrpc extends app.groopy.protobuf.UserServiceGrpc.UserServiceImplBase {

    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceGrpc.class);

    private final ApplicationService applicationService;
    private final PresentationMapper presentationMapper;

    @Autowired
    public UserServiceGrpc(ApplicationService applicationService, PresentationMapper presentationMapper) {
        this.applicationService = applicationService;
        this.presentationMapper = presentationMapper;
    }

    @Override
    public void signIn(UserServiceProto.SignInRequest request, StreamObserver<UserServiceProto.SignInResponse> responseObserver) {
        LOGGER.info("Processing signIn message");
        try {
            UserServiceProto.SignInResponse response = presentationMapper.map(
                    applicationService.signIn(presentationMapper.map(request))
            );

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (ApplicationException e) {
            responseObserver.onError(ApplicationExceptionResolver.resolve(e));
        }
    }

    @Override
    public void signUp(UserServiceProto.SignUpRequest request, StreamObserver<UserServiceProto.SignUpResponse> responseObserver) {
        LOGGER.info("Processing signUp message");
        try {
            UserServiceProto.SignUpResponse response = presentationMapper.map(
                    applicationService.signUp(presentationMapper.map(request))
            );
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (ApplicationException e) {
            responseObserver.onError(ApplicationExceptionResolver.resolve(e));
        }
    }

    @Override
    public void oAuth(UserServiceProto.OAuthRequest request, StreamObserver<UserServiceProto.SignInResponse> responseObserver) {
        LOGGER.info("Processing oAuth message");
        try {
            UserServiceProto.SignInResponse response = presentationMapper.map(
                    applicationService.oAuth(presentationMapper.map(request))
            );

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (ApplicationException e) {
            responseObserver.onError(ApplicationExceptionResolver.resolve(e));
        }
    }
}