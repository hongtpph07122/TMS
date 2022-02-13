package com.tms.api.exception;

import com.tms.api.response.TMSResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class TMSExceptionHandler extends ResponseEntityExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(TMSExceptionHandler.class);

    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     *
     * @param ex      MissingServletRequestParameterException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
    	logger.error("handleMissingServletRequestParameter: {}", ex.getMessage(), ex);
    	return buildResponseEntity(new TMSException(ErrorMessage.INVALID_PARAM));
    }


    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param ex      HttpMediaTypeNotSupportedException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
    	logger.error(ex.getMessage(), ex);
    	return buildResponseEntity(new TMSException(ErrorMessage.UNSUPPORTED_MEDIA_TYPE));
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
    	logger.error("handleMethodArgumentNotValid: {}", ex.getMessage(), ex);
        return buildResponseEntity(new TMSException(ErrorMessage.BAD_REQUEST));
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex the ConstraintViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
            javax.validation.ConstraintViolationException ex) {
        logger.error("handleConstraintViolation: {}", ex.getMessage(), ex);
    	return buildResponseEntity(new TMSException(ErrorMessage.CONTRAINS_EXCEPTION));
    }


    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     *
     * @param ex      HttpMessageNotReadableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("handleHttpMessageNotReadable: {}", ex.getMessage(), ex);
    	return buildResponseEntity(new TMSException(ErrorMessage.BAD_REQUEST));
    }

    /**
     * Handle HttpMessageNotWritableException.
     *
     * @param ex      HttpMessageNotWritableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    	logger.error("handleHttpMessageNotWritable: {}", ex.getMessage(), ex);
        return buildResponseEntity(new TMSException(ErrorMessage.BAD_REQUEST));
    }

    /**
     * Handle NoHandlerFoundException.
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    	logger.error("handleNoHandlerFoundException: {}", ex.getMessage(), ex);
        return buildResponseEntity(new TMSException(ErrorMessage.NOT_FOUND));
    }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     *
     * @param ex the DataIntegrityViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                  WebRequest request) {
    	logger.error("handleConstraintViolation: {}", ex.getMessage(), ex);
        return buildResponseEntity(new TMSException(ErrorMessage.CONTRAINS_EXCEPTION));
    }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     *
     * @param ex the DataIntegrityViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(TMSInvalidInputException.class)
    protected ResponseEntity<Object> handleInvalidInputRequest(TMSInvalidInputException ex,
                                                                  WebRequest request) {
        logger.error("handleInvalidInputRequest TMSInvalidInputException: {}", ex.getMessage(), ex);
        return buildResponseEntity(new TMSException(ErrorMessage.INVALID_PARAM));
    }

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<Object> handleInvalidInputRequest(IOException ex,
                                                               WebRequest request) {
        logger.error("handleInvalidInputRequest IOException: {}", ex.getMessage(), ex);
        return buildResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle Exception, handle generic Exception.class
     *
     * @param ex the Exception
     * @return the ApiError object
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {
        logger.error("handleMethodArgumentTypeMismatch: {}", ex.getMessage(), ex);
    	return buildResponseEntity(new TMSException(ErrorMessage.BAD_REQUEST));
    }

    /**
     * Handles EntityNotFoundException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     *
     * @param ex the EntityNotFoundException
     * @return the ApiError object
     */
    @ExceptionHandler(TMSException.class)
    protected ResponseEntity<Object> handleEntityNotFound(TMSException ex) {
        logger.error("handleEntityNotFound: {}", ex.getMessage(), ex);
        return buildResponseEntity(ex);
    }

    private ResponseEntity<Object> buildResponseEntity(String message, HttpStatus status) {
        return new ResponseEntity<>(TMSResponse.buildApplicationException(message, status.value()), status);
    }

    private ResponseEntity<Object> buildResponseEntity(TMSException tmsEx) {
        return new ResponseEntity<>(TMSResponse.buildApplicationException(tmsEx), HttpStatus.OK);
    }

}