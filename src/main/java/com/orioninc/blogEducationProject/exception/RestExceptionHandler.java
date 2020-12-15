package com.orioninc.blogEducationProject.exception;

import com.orioninc.blogEducationProject.error.ApiError;
import com.orioninc.blogEducationProject.error.CmnError;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LogManager.getLogger(RestExceptionHandler.class);


    @ExceptionHandler({UserException.class})
    public ResponseEntity handleUserException(UserException ex){
        ApiError error = new ApiError(HttpStatus.CONFLICT, ex.getError(), ex.getMessage(), ex.getClass().getSimpleName());
        LOG.error(error);

        return new ResponseEntity(
                error, new HttpHeaders(), error.getStatus()
        );
    }

    @ExceptionHandler({PostException.class})
    public ResponseEntity handlePostException(PostException ex){
        ApiError error = new ApiError(HttpStatus.CONFLICT, ex.getError(), ex.getMessage(), ex.getClass().getSimpleName());
        LOG.error(error);

        return new ResponseEntity(
          error, new HttpHeaders(), error.getStatus()
        );
    }

    @ExceptionHandler({TagException.class})
    public ResponseEntity handleTagException(TagException ex){
        ApiError error = new ApiError(HttpStatus.CONFLICT, ex.getError(), ex.getMessage(), ex.getClass().getSimpleName());
        LOG.error(error);

        return new ResponseEntity(
                error, new HttpHeaders(), error.getStatus()
        );
    }


    @ExceptionHandler({CommentException.class})
    public ResponseEntity handleCommentException(CommentException ex){
        ApiError error = new ApiError(HttpStatus.CONFLICT, ex.getError(), ex.getMessage(), ex.getClass().getSimpleName());
        LOG.error(error);

        return new ResponseEntity(
                error, new HttpHeaders(), error.getStatus()
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, CmnError.METHOD_ARGUMENTS_NOT_VALID, ex.getLocalizedMessage(), errors);
        LOG.error(apiError + ", " + ex.getLocalizedMessage() + " " + errors);
        return handleExceptionInternal(
                ex, apiError, headers, apiError.getStatus(), request);
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }

        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        LOG.error(ex.getLocalizedMessage() + " " + errors);
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        LOG.error(ex.getLocalizedMessage() + " " + error);
        return new ResponseEntity(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";

        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        LOG.error(ex.getLocalizedMessage() + " " + error);
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(
                " method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

        ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, CmnError.HTTP_REQUEST_METHOD_NOT_SUPPORTED,
                ex.getLocalizedMessage(), builder.toString());
        LOG.error( apiError + ", " + ex.getLocalizedMessage() + " " + builder.toString());
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,CmnError.NOT_FOUND, ex.getLocalizedMessage(), error);
        LOG.error(ex.getLocalizedMessage() + " " + error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));

        ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                ex.getLocalizedMessage(), builder.substring(0, builder.length() - 2));
        LOG.error(ex.getLocalizedMessage() + " " + builder.toString());
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }


    @ExceptionHandler({ Exception.class })
    public ResponseEntity handleAll(Exception ex, WebRequest request) {
        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR, CmnError.UNEXPECTED_SERVER_ERROR, ex.getLocalizedMessage(), "Unexpected server error");
        String exception = ex.getLocalizedMessage() + "\n" + ExceptionUtils.getStackTrace(ex);
        LOG.error("Unexpected server error {}, Msg: {}", error, exception);
        return new ResponseEntity<Object>(
                error, new HttpHeaders(), error.getStatus());
    }
}
