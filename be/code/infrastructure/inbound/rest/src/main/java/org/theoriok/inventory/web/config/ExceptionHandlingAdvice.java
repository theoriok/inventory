package org.theoriok.inventory.web.config;

import static java.util.stream.Collectors.toMap;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
public class ExceptionHandlingAdvice {
    public static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlingAdvice.class);

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(code = INTERNAL_SERVER_ERROR)
    ResponseEntity<String> onException(Throwable exception) {
        LOGGER.error(exception.getMessage());
        return ResponseEntity.internalServerError()
            .body("Something went wrong");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException ex) {
        var problemDetail = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), "Validation failed");
        var errors = ex.getBindingResult().getFieldErrors().stream()
            .collect(toMap(org.springframework.validation.FieldError::getField, org.springframework.validation.FieldError::getDefaultMessage));
        problemDetail.setProperty("errors", errors);
        return ResponseEntity.of(problemDetail).build();
    }
}