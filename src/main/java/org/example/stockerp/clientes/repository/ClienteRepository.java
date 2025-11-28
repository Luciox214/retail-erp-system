package org.example.stockerp.clientes.repository;

import org.example.stockerp.clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente,Long> {
    boolean existsByTelefono(String telefono);
}
