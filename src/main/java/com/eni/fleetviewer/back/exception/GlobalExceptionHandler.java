package com.eni.fleetviewer.back.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire global des exceptions pour l'application.
 * Permet de centraliser la gestion des erreurs et de renvoyer des réponses cohérentes.
 * Intercepte les exceptions lancées par n'importe quel contrôleur de votre application et les transforme en réponses JSON propres.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gère les exceptions de type RessourceNotFoundException (HTTP 404).
     * Se déclenche lorsque le service lève notre exception personnalisée.
     */
    @ExceptionHandler(RessourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRessourceNotFoundException(RessourceNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Gère les erreurs de validation de @Valid (HTTP 400).
     * Se déclenche lorsque les données envoyées dans un @RequestBody ne sont pas valides.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", "Validation Failed");
        errorResponse.put("details", errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
