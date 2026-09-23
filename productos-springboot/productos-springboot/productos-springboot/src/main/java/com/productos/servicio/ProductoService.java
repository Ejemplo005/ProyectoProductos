package com.productos.servicio;

import com.productos.modelo.Producto;
import com.productos.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Acá vive la lógica de negocio. A diferencia de JPA puro (donde cada
 * método abría su propio EntityManager y manejaba la transacción a mano),
 * acá Spring ya nos da el ProductoRepository inyectado, y @Transactional
 * se encarga de la transacción sola.
 */
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAllByOrderByNombreAsc();
    }

    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el producto con id " + id));
    }

    @Transactional
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    @Transactional
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }
}
