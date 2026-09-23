package com.productos.repository;

import com.productos.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Método derivado: Spring Data JPA arma la consulta solo, a partir del
    // nombre del método (SELECT ... ORDER BY nombre ASC).
    List<Producto> findAllByOrderByNombreAsc();
}
