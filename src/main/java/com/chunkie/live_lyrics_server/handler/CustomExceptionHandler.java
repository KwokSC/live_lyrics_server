package com.chunkie.live_lyrics_server.handler;

import com.chunkie.live_lyrics_server.common.ResponseObject;
import com.chunkie.live_lyrics_server.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseObject> handleUnauthorizedException(UnauthorizedException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject("Please login.", HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
    }
}
