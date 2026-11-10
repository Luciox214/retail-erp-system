package org.example.stockerp;

import org.springframework.boot.SpringApplication;
import java.util.TimeZone;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockerpApplication {

    public static void main(String[] args) {
        // El servidor de Render corre en UTC: sin esto, las ventas hechas despues de las 21 h
        // quedaban registradas en el dia siguiente y el resumen de "hoy" salia mal.
        TimeZone.setDefault(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));
        SpringApplication.run(StockerpApplication.class, args);
    }

}
