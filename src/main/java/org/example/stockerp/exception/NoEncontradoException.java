package org.example.stockerp.exception;

/** El recurso pedido no existe (o ya fue eliminado). Se responde con 404. */
public class NoEncontradoException extends RuntimeException {
    public NoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
