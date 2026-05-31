package rarlog.me.MusicPlay.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import rarlog.me.dto.ErrorResponseDto;
import rarlog.me.MusicPlay.exception.AccountAlreadyExistsException;
import rarlog.me.MusicPlay.exception.ArtistNotFoundException;
import rarlog.me.MusicPlay.exception.PlaylistNotFoundException;
import rarlog.me.MusicPlay.exception.SongNotFoundException;
import rarlog.me.MusicPlay.exception.UserNotFoundException;

@RestControllerAdvice()
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            ArtistNotFoundException.class,
            PlaylistNotFoundException.class,
            SongNotFoundException.class,
            UserNotFoundException.class,
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleNotFoundException(RuntimeException e) {
        return new ErrorResponseDto(LocalDateTime.now(), e.getMessage());
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleBadRequestException(RuntimeException e) {
        return new ErrorResponseDto(LocalDateTime.now(), e.getMessage());
    }

}
