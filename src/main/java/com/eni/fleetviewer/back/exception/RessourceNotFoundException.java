package com.eni.fleetviewer.back.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * Exception levée lorsqu'une ressource demandée n'est pas trouvée dans la base de données.
 * L'annotation @ResponseStatus indique à Spring de retourner un statut HTTP 404 par défaut.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RessourceNotFoundException extends RuntimeException {
    public RessourceNotFoundException(String message) {
        super(message);
    }
}