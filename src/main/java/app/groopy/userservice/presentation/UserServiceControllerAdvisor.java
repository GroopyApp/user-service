package app.groopy.userservice.presentation;

import app.groopy.userservice.domain.exceptions.SignUpValidationException;
import app.groopy.userservice.presentation.mapper.PresentationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import app.groopy.protobuf.UserServiceProto;

@ControllerAdvice
public class UserServiceControllerAdvisor extends ResponseEntityExceptionHandler {

    @Autowired
    private PresentationMapper mapper;

    @ExceptionHandler(SignUpValidationException.class)
    public ResponseEntity<UserServiceProto.ErrorResponse> handle(
            SignUpValidationException ex, WebRequest request) {

        UserServiceProto.ErrorResponse response = UserServiceProto.ErrorResponse.newBuilder()
                .setErrorMessage(ex.getLocalizedMessage()).build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}