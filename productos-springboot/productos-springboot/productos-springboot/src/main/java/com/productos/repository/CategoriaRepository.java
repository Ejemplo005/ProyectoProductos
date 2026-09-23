package com.productos.repository;

import com.productos.modelo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Reemplaza por completo al GenericRepository + CategoriaRepository que
 * armábamos a mano en JPA puro: con solo extender JpaRepository, ya
 * tenemos save(), findById(), findAll(), deleteById()...
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
