package org.example.stockerp.exception;

/**
 * Error de regla de negocio con un mensaje pensado para mostrarse tal cual al usuario
 * (por ejemplo: "No hay stock suficiente de Muñeca Sophie: quedan 2").
 */
public class NegocioException extends RuntimeException {
    public NegocioException(String mensaje) {
        super(mensaje);
    }
}
