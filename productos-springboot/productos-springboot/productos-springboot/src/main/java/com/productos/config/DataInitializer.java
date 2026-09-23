package com.productos.config;

import com.productos.modelo.Categoria;
import com.productos.modelo.Producto;
import com.productos.repository.CategoriaRepository;
import com.productos.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * CommandLineRunner: Spring Boot ejecuta el método run() una sola vez,
 * automáticamente, justo después de terminar de arrancar la aplicación.
 * Lo usamos para cargar datos de ejemplo (los mismos productos que tenía
 * la tienda "Aguas Astrales" en su versión estática), para no arrancar
 * siempre con la base vacía.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public DataInitializer(CategoriaRepository categoriaRepository, ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public void run(String... args) {
        if (categoriaRepository.count() > 0) {
            return; // ya hay datos cargados: no los duplicamos en cada arranque
        }

        Categoria aromas = categoriaRepository.save(new Categoria("Aromas"));
        Categoria textiles = categoriaRepository.save(new Categoria("Textiles"));
        Categoria hogar = categoriaRepository.save(new Categoria("Hogar"));
        Categoria aromaterapia = categoriaRepository.save(new Categoria("Aromaterapia"));
        Categoria aceite = categoriaRepository.save(new Categoria("Aceite"));
        Categoria ropa = categoriaRepository.save(new Categoria("Ropa"));

        productoRepository.save(new Producto(
                "Sahumerios Triple Combinado", 3000, "Sahumerio artesanal.",
                20, aromas, "/images/sahumerios.jpeg"));

        productoRepository.save(new Producto(
                "Perfume Textil", 6000, "Aromatizante textil, aroma cálido para crear tu momento.",
                15, textiles, "/images/perfume-textil.jpeg"));

        productoRepository.save(new Producto(
                "Difusores de Ambiente", 5000, "Difusores en varilla para acompañar tu día a día.",
                12, hogar, "/images/difusores.jpeg"));

        productoRepository.save(new Producto(
                "Fragancias Saphirus", 4200, "Home spray y esencias para renovar el ambiente.",
                10, aromaterapia, "/images/fragancias.jpeg"));

        productoRepository.save(new Producto(
                "Bombas Aromáticas", 4800, "Bombitas de sahumerio para tus momentos especiales.",
                18, hogar, "/images/bombas-aromaticas.jpeg"));

        productoRepository.save(new Producto(
                "Pastillas de limpieza", 3000, "Pastillas aromáticas de limpieza para el hogar.",
                25, aromaterapia, "/images/pastillas-limpieza.jpeg"));

        productoRepository.save(new Producto(
                "Aceite para hornillo", 4500, "Aceite esencial para hornillo aromático.",
                14, aceite, "/images/aceite-hornillo.jpeg"));

        productoRepository.save(new Producto(
                "Sahumerios Canabis", 3500, "Sahumerio artesanal.",
                22, aromas, "/images/sahumerios-canabis.jpeg"));

        productoRepository.save(new Producto(
                "Textiles", 5500, "Aromatizante para ropa.",
                16, ropa, "/images/textiles.jpeg"));
    }
}
