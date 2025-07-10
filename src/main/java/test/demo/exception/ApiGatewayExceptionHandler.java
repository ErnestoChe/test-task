package test.demo.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;


@RestController
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ApiGatewayExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<Map<String, String>> handle(MethodArgumentNotValidException ex) {
        log.error("Caught MethodArgumentNotValidException", ex);
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (msg1, msg2) -> msg1
                ));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler({LogicException.class, BalanceException.class})
    public final ResponseEntity<String> handle(BalanceException ex) {
        log.error("Caught MethodArgumentNotValidException", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getLocalizedMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<String> handle(NotFoundException ex) {
        log.error("Caught MethodArgumentNotValidException", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getLocalizedMessage());
    }

    @ExceptionHandler(NotAuthException.class)
    public final ResponseEntity<String> handle(NotAuthException ex) {
        log.error("Caught MethodArgumentNotValidException", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getLocalizedMessage());
    }


}
