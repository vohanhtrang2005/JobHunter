package com.job.util.error;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.job.domain.RestResponse;

@RestControllerAdvice
public class GlobalException {
      @ExceptionHandler(value = {
     
      UsernameNotFoundException.class,
      BadCredentialsException.class,
      IdInvalidException.class
      }
      )
      
public ResponseEntity<RestResponse<Object>> handleIdException(Exception ex) {
  RestResponse<Object> res = new RestResponse<Object>();
    res.setError(ex.getMessage());
    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
    res.setMessage("Exception occurrs...");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
}  
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> ValidationError(MethodArgumentNotValidException ex) {
      BindingResult result = ex.getBindingResult();
      final List<FieldError> fieldErrors = result.getFieldErrors();
      RestResponse<Object> res = new RestResponse<Object>();
      res.setStatusCode(HttpStatus.BAD_REQUEST.value());
      res.setError(ex.getBody().getDetail());
      List<String> errors = fieldErrors.stream()
         .map(f -> f.getDefaultMessage()).collect(Collectors.toList());
      res.setMessage(errors.size()>1 ? errors : errors.get(0));
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);

    }

    @ExceptionHandler(value=NoResourceFoundException.class)
    public ResponseEntity<RestResponse<Object>> handleNoResourceFound(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setError(ex.getMessage());
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setMessage("404 not found. URL is invalid or resource does not exist");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
   
    



    @ExceptionHandler(InvalidFormatException.class)
public ResponseEntity<RestResponse<Object>> handleInvalidEnum(InvalidFormatException ex) {
    RestResponse<Object> res = new RestResponse<>();
    res.setStatusCode(HttpStatus.BAD_REQUEST.value());

    // Kiểm tra field gender
    boolean isGenderField = ex.getPath().stream()
            .anyMatch(field -> field.getFieldName().equals("gender"));

    if (isGenderField) {
        res.setError("Gender không hợp lệ");
        res.setMessage("Chỉ chấp nhận các giá trị: MALE, FEMALE, OTHER");
    } else {
        res.setError("Dữ liệu không hợp lệ");
        res.setMessage(ex.getOriginalMessage()); // Có thể lấy message chi tiết từ Jackson
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
}
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<RestResponse<Object>> handleNotFound(ResourceNotFoundException ex) {
    RestResponse<Object> res = new RestResponse<>();
    res.setError(ex.getMessage());
    res.setStatusCode(HttpStatus.NOT_FOUND.value());
    res.setMessage("Resource not found");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
}


}
