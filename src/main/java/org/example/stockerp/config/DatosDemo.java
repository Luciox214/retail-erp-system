package org.example.stockerp.config;

import org.example.stockerp.clientes.model.Cliente;
import org.example.stockerp.clientes.repository.ClienteRepository;
import org.example.stockerp.productos.model.Producto;
import org.example.stockerp.productos.repository.ProductoRepository;
import org.example.stockerp.ventas.model.DetalleVentaDTO;
import org.example.stockerp.ventas.model.VentaRequestDTO;
import org.example.stockerp.ventas.service.VentasService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

/** Datos de ejemplo, SOLO en el perfil "local" (nunca se ejecuta en Render). */
@Configuration
@Profile("local")
public class DatosDemo {

    private static final String IMG = "https://res.cloudinary.com/dphfr7tcb/image/upload/";

    @Bean
    CommandLineRunner cargarDatosDemo(ProductoRepository productos, ClienteRepository clientes, VentasService ventas) {
        return args -> {
            Producto muneca = productos.save(new Producto(null, "Muñeca Madeleine", "Muñeca de trapo con corona de flores.", 9000.0, 18500.0, 4, IMG + "v1764554651/i2xhnwyifnztqseduqfk.jpg", true, false));
            Producto gato = productos.save(new Producto(null, "Gato Teo", null, 6000.0, 12900.0, 2, IMG + "v1764554847/agibvip2lyn68vbus4hl.jpg", true, false));
            Producto almohadon = productos.save(new Producto(null, "Almohadón arco iris realizado en tussor", null, 4500.0, 9800.0, 7, IMG + "v1764719712/kzpn12ahzvvsynpipzry.jpg", true, false));
            productos.save(new Producto(null, "Guirnalda ardillas de 1,20 m", null, 3000.0, 7500.0, 0, IMG + "v1764719923/pgblyz4f3q2dzzwhbdql.jpg", false, false));
            Producto conejo = productos.save(new Producto(null, "Conejo de apego Dante", null, 7000.0, 15000.0, 5, IMG + "v1774319503/ayikivttzwjd3wstdyio.jpg", true, false));
            productos.save(new Producto(null, "Moños bordados a mano", null, 1200.0, 3500.0, 12, IMG + "v1774317793/yjn855a1wqrqn7un6psf.jpg", true, false));
            productos.save(new Producto(null, "Set saco con capucha y sonajero", null, 11000.0, 24000.0, 1, IMG + "v1779810686/rjxzyc6tmuuvqw4kf4tp.jpg", true, false));
            productos.save(new Producto(null, "Muñeca Sophie con letra personalizada", null, 9500.0, 19500.0, 3, null, true, false));

            Cliente ana = clientes.save(new Cliente(null, "Ana Pérez", "1155550001"));
            Cliente laura = clientes.save(new Cliente(null, "Laura Gómez", "1155550002"));
            clientes.save(new Cliente(null, "Marta Díaz", null));

            ventas.registrarVenta(new VentaRequestDTO(ana.getId(), List.of(new DetalleVentaDTO(muneca.getId(), 1), new DetalleVentaDTO(almohadon.getId(), 2)), "TRANSFERENCIA", 1000.0, "Envío a Palermo"));
            ventas.registrarVenta(new VentaRequestDTO(laura.getId(), List.of(new DetalleVentaDTO(conejo.getId(), 1)), "MERCADO_PAGO", null, null));
            ventas.registrarVenta(new VentaRequestDTO(null, List.of(new DetalleVentaDTO(gato.getId(), 1)), "EFECTIVO", null, null));
        };
    }
}
