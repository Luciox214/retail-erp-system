package org.example.stockerp.response;

public record ResponseAPI<T>(
        String status,
        String mensaje,
        T data) {
    public static <T> ResponseAPI<T> success(String mensaje, T data) {
        return new ResponseAPI<>("success", mensaje, data);
    }
    public static <T> ResponseAPI<T> error(String mensaje, T data) {
        return new ResponseAPI<>("error", mensaje, data);
    }
}

