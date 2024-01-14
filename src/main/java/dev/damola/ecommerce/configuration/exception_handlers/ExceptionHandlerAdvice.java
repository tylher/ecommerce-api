package dev.damola.ecommerce.configuration.exception_handlers;


import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.damola.ecommerce.exceptions.CustomException;
import dev.damola.ecommerce.exceptions.NotfoundException;
import dev.damola.ecommerce.exceptions.UnAuthorizedException;
import dev.damola.ecommerce.system.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler({UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return new Result(false, ex.getMessage());
    }

    @ExceptionHandler({NotfoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleNotFoundException(NotfoundException ex) {
        return new Result(false, ex.getMessage());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new Result(false, ex.getMessage().substring(0,32));
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return new Result(false, ex.getMessage());
    }

    @ExceptionHandler({CustomException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleCustomException(CustomException ex) {
        return new Result(false, ex.getMessage());
    }

    @ExceptionHandler({UnAuthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleUnAuthorizedException(UnAuthorizedException ex) {
        return new Result(false, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleValidationRException(MethodArgumentNotValidException ex) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        Map<String, String> map = new HashMap<>(errors.size());
        errors.forEach(error -> {
            String key = ((FieldError) error).getField();
            String val = error.getDefaultMessage();
            map.put(key, val);
        });
        return new Result(false, "input neccessary details", map);
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request){
        return  new Result(false, "the url "+ request.getRequestURL()+" does not exist");
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleMissingServletRequestException(MissingServletRequestParameterException ex, HttpServletRequest request){
        return  new Result(false, ex.getMessage()
        );
    }


    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleAuthenticationException(AuthenticationException ex){
        return  new Result(false, "Not authorized to perform this operation, please input bearer token");
    }


    @ExceptionHandler(JWTVerificationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleJWTVerificationException(JWTVerificationException ex){
        return  new Result(false, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    Result handleAccessDeniedException(AccessDeniedException ex){
        return  new Result(false, "No permission");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleIllegalArgumentException(IllegalArgumentException ex){
        return  new Result(false, ex.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    Result handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return new Result(false, exc.getMessage());
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    Result handleMultipartException(MultipartException exc) {
        return new Result(false, exc.getMessage());
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleMissingServletRequestPartException(MissingServletRequestPartException ex){
        return  new Result(false, ex.getMessage());
    }


}
