package ru.job4j.passport.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.job4j.passport.exception.EntityNotFoundException;
import ru.job4j.passport.exception.PassportReservedException;
import ru.job4j.passport.exception.ServiceValidateException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final Logger logger
            = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    private static final String CONTENT_HEADER = "application/json";
    private final ObjectMapper objectMapper;

    public GlobalControllerExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(value = {ServiceValidateException.class})
    public void handleValidateException(Exception e, HttpServletResponse response) throws IOException {
        setResponseArgs(HttpStatus.BAD_REQUEST, response, Map.of(
                "message", "Some of fields are not valid",
                "details", e.getMessage()));
        logger.error(e.getMessage());
    }

    @ExceptionHandler(value = {PassportReservedException.class})
    public void handleReservedException(Exception e, HttpServletResponse response) throws IOException {
        setResponseArgs(HttpStatus.CONFLICT, response, Map.of(
                "message", e.getMessage(),
                "type", e.getClass()));
        logger.warn(e.getMessage());
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public void handleEntityNotFoundException(Exception e, HttpServletResponse response) throws IOException {
        setResponseArgs(HttpStatus.NOT_FOUND, response, Map.of("details", e.getMessage()));
        logger.warn(e.getMessage());
    }

    @ExceptionHandler(value = {ServiceException.class})
    public void handleServiceException(Exception e, HttpServletResponse response) throws IOException {
        setResponseArgs(HttpStatus.BAD_REQUEST, response, Map.of("Service exception", e.getMessage()));
        logger.error("Service Exception: {}", e.getMessage());
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintException(ConstraintViolationException e) {
        logger.error("the data is not valid: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                e.getConstraintViolations().stream().map(x -> Map.of(
                        x.getPropertyPath(),
                        String.format("%s. Actual value: %s", x.getMessage(), x.getInvalidValue())
                )).collect(Collectors.toList())
        );
    }

    private void setResponseArgs(HttpStatus status, HttpServletResponse response,
                                 Map<String, ?> exceptionCaptionsToDescriptions) throws IOException {
        response.setStatus(status.value());
        response.setContentType(CONTENT_HEADER);
        response.getWriter().write(objectMapper.writeValueAsString(exceptionCaptionsToDescriptions));
    }
}
