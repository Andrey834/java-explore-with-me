package ru.practicum.ewmmain.controller.error;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewmmain.controller.admin.CategoryAdminController;
import ru.practicum.ewmmain.controller.publics.CategoryPublicController;
import ru.practicum.ewmmain.model.ApiError;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice(assignableTypes = {CategoryAdminController.class, CategoryPublicController.class})
public class CategoryErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleCompilationConstraintException(final ConstraintViolationException e) {
        return ApiError.builder()
                .errors("ConstraintViolationException")
                .message(e.getMessage())
                .reason("Duplicate name")
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCompilationNotFoundException(final NoSuchElementException e) {
        return ApiError.builder()
                .errors("NoSuchElementException")
                .message(e.getMessage())
                .reason("The required object was not found.")
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleUserConstraintException(final MethodArgumentNotValidException e) {
        return ApiError.builder()
                .errors("MethodArgumentNotValidException")
                .message(e.getMessage())
                .reason("Bad data for Category")
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleUserConstraintException(final DataIntegrityViolationException e) {
        return ApiError.builder()
                .errors("DataIntegrityViolationException")
                .message(e.getMessage())
                .reason("Bad data for Category")
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
