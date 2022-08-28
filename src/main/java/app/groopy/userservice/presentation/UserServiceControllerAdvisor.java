package app.groopy.userservice.presentation;

import app.groopy.userservice.presentation.mapper.PresentationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class UserServiceControllerAdvisor extends ResponseEntityExceptionHandler {

    @Autowired
    private PresentationMapper mapper;

//    @ExceptionHandler(CreateRoomValuesValidationException.class)
//    public ResponseEntity<RoomServiceProto.ErrorResponse> handle(
//            CreateRoomValuesValidationException ex, WebRequest request) {
//
//        RoomServiceProto.ErrorResponse response = RoomServiceProto.ErrorResponse.newBuilder()
//                .setDescription(ex.getLocalizedMessage()).build();
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
}