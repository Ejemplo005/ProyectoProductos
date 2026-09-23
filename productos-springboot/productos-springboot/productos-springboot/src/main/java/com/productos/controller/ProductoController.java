package com.productos.controller;

import com.productos.servicio.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller "normal" (no @RestController): en vez de devolver JSON,
 * devuelve el NOMBRE de una plantilla Thymeleaf. Spring Boot busca ese
 * nombre dentro de src/main/resources/templates/ y le agrega ".html" solo.
 */
@Controller
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Tanto "/" como "/productos" muestran la misma vitrina de productos.
    @GetMapping({"/", "/productos"})
    public String productos(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        return "productos";
    }
}
