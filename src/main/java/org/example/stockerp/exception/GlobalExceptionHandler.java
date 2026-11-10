package org.example.stockerp.exception;

import jakarta.persistence.EntityNotFoundException;
import org.example.stockerp.response.ResponseAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Traduce los errores a respuestas con un mensaje claro en español.
 * Los errores inesperados se registran en el log pero NO se muestran crudos al usuario.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<ResponseAPI<String>> respuesta(HttpStatus status, String mensaje) {
        return ResponseEntity.status(status).body(ResponseAPI.error(mensaje, null));
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ResponseAPI<String>> handleNegocio(NegocioException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler({NoEncontradoException.class, EntityNotFoundException.class})
    public ResponseEntity<ResponseAPI<String>> handleNoEncontrado(RuntimeException ex) {
        return respuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseAPI<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseAPI<String>> handleJsonInvalido(HttpMessageNotReadableException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, "Los datos enviados no son válidos. Revisá los campos e intentá de nuevo.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseAPI<String>> handleIntegridad(DataIntegrityViolationException ex) {
        log.warn("Violación de integridad: {}", ex.getMostSpecificCause().getMessage());
        return respuesta(HttpStatus.CONFLICT, "No se pudo completar la operación porque hay datos relacionados.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseAPI<String>> handleGenericException(Exception ex) {
        log.error("Error inesperado", ex);
        return respuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado. Intentá de nuevo en unos segundos.");
    }
}
