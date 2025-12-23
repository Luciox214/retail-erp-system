# Descripción del Proyecto
Este repositorio contiene el código fuente de un sistema de gestión de recursos (ERP). El sistema implementa lógica de negocio para el control de stock, facturación y seguridad.

## Funcionalidad Destacada: Integración Web
El sistema permite la conexión con plataformas externas (como una tienda online).
* **Sincronización en Tiempo Real:** Los cambios de precio o stock impactan inmediatamente en cualquier frontend conectado.
* **Control de Visibilidad:** Permite gestionar qué productos se publican en la web y cuáles quedan reservados solo para venta interna.
```mermaid
sequenceDiagram
    participant Client
    participant CorsFilter
    participant JwtRequestFilter
    participant CatalogoController
    participant ProductoService
    participant ProductoRepository
    participant MySQL

    Client->>CorsFilter: "GET /api/v1/catalogo Authorization: Bearer {token}"
    CorsFilter->>JwtRequestFilter: "Forward request"
    JwtRequestFilter->>JwtRequestFilter: "validateToken(token) extractUsername(token)"
    JwtRequestFilter->>CatalogoController: "Authenticated request"
    CatalogoController->>ProductoService: "obtenerProductosCatalogo()"
    ProductoService->>ProductoRepository: "findAll() / custom query"
    ProductoRepository->>MySQL: "SELECT * FROM productos WHERE..."
    MySQL-->>ProductoRepository: "Result Set"
    ProductoRepository-->>ProductoService: "List<Producto>"
    ProductoService-->>CatalogoController: "List<ProductoCatalogoDTO>"
    CatalogoController-->>Client: "ResponseAPI<List<ProductoCatalogoDTO>>"
```



## Qué puede hacer el sistema

### Gestión de Inventario
* Control de altas, bajas y modificaciones de productos.
* Monitoreo de niveles de stock para prevenir faltantes.
* Cálculo de costos y precios de venta.

### Ventas y Facturación
* Registro transaccional de ventas con múltiples productos.
* **Generación de PDFs:** El sistema crea automáticamente comprobantes de venta utilizando **JasperReports**, listos para entregar al cliente.
* Cálculo automático de márgenes de ganancia por operación.

### Métricas
* Panel de control para visualizar el estado del negocio.
* Reportes de ingresos diarios y mensuales.
* Identificación rápida de productos con mayor rotación.

## Stack Tecnológico
* **Lenguaje:** Java 17
* **Framework:** Spring Boot 3 (Web, Data JPA, Security)
* **Base de Datos:** MySQL
* **Seguridad:** Implementación de JWT 
* **Reportes:** JasperReports
* **Despliegue:** Soporte para Docker
```mermaid
graph TD
    subgraph External [Cliente Externo]
        Client
    end

    subgraph Security [Capa de Seguridad]
        Cors[Cors Filter]
        Jwt[JWT Filter]
        Util[JWT Util]
    end

    subgraph API [Controladores API]
        AuthC[Auth]
        CatC[Catálogo]
        ProdC[Productos]
        VentaC[Ventas]
        ClientC[Clientes]
        DashC[Dashboard]
    end

    subgraph Service [Lógica de Negocio]
        ProdS[Service Productos]
        VentaS[Service Ventas]
        ClientS[Service Clientes]
    end

    subgraph Data [Acceso a Datos]
        ProdR[Repo Productos]
        VentaR[Repo Ventas]
        ClientR[Repo Clientes]
    end

    DB[( MySQL)]

    %% Flujo Principal
    Client --> Cors
    Cors --> Jwt
    Jwt --> AuthC & CatC & ProdC & VentaC & ClientC & DashC
    
    %% Dependencias de Seguridad
    Jwt -.-> Util
    AuthC -.-> Util

    %% Flujo Estándar (Controller -> Service -> Repo)
    CatC --> ProdS
    ProdC --> ProdS
    VentaC --> VentaS
    ClientC --> ClientS

    ProdS --> ProdR
    VentaS --> VentaR
    ClientS --> ClientR

    %% Optimizaciones (Dashboard accede directo a Repos)
    DashC -. "Acceso Directo" .-> ProdR
    DashC -. "Acceso Directo" .-> VentaR
    DashC -. "Acceso Directo" .-> ClientR

    %% Base de Datos
    ProdR & VentaR & ClientR --> DB
```
