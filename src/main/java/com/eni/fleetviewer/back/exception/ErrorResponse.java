package com.eni.fleetviewer.back.exception;

import java.time.LocalDateTime;

/**
 * Représentation standard d'une réponse d'erreur de l'API.
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {}
