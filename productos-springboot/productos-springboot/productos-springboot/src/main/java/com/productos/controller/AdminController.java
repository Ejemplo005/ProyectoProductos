package com.productos.controller;

import com.productos.modelo.Categoria;
import com.productos.modelo.Producto;
import com.productos.repository.CategoriaRepository;
import com.productos.servicio.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Panel de administración: alta, edición y borrado de productos mediante
 * formularios HTML (no JSON). Es el mismo patrón que la guía de "Spring
 * Boot con Frontend" explica: @RequestParam para leer cada campo del
 * formulario, y "redirect:" para volver a mostrar la lista actualizada
 * después de guardar.
 *
 * IMPORTANTE: esta ruta no tiene login ni protección (no vimos todavía
 * Spring Security), así que cualquiera que entre a /admin puede editar el
 * catálogo. Para una tienda real, este panel debería estar protegido.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductoService productoService;
    private final CategoriaRepository categoriaRepository;

    public AdminController(ProductoService productoService, CategoriaRepository categoriaRepository) {
        this.productoService = productoService;
        this.categoriaRepository = categoriaRepository;
    }

    // Panel principal: lista de productos + formulario vacío (modo "alta").
    @GetMapping
    public String panel(Model model) {
        cargarListasComunes(model);
        model.addAttribute("producto", new Producto());
        return "admin";
    }

    // Reutiliza la misma página, pero con el formulario precargado
    // (modo "edición").
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        cargarListasComunes(model);
        model.addAttribute("producto", productoService.buscarPorId(id));
        return "admin";
    }

    // Recibe el formulario (tanto de alta como de edición) y guarda.
    @PostMapping("/guardar")
    public String guardar(@RequestParam(required = false) Long id,
                           @RequestParam String nombre,
                           @RequestParam double precio,
                           @RequestParam String descripcion,
                           @RequestParam int stock,
                           @RequestParam Long categoriaId,
                           @RequestParam(required = false) String imagenUrl) {

        // Si vino un id, editamos ese producto existente; si no, es uno nuevo.
        Producto producto = (id != null) ? productoService.buscarPorId(id) : new Producto();

        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setDescripcion(descripcion);
        producto.setStock(stock);
        producto.setImagenUrl(imagenUrl);

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("La categoría elegida no existe"));
        producto.setCategoria(categoria);

        productoService.guardar(producto);

        // Volvemos a pedirle al navegador que haga GET /admin, para mostrar
        // la lista ya actualizada (evita reenviar el formulario si se
        // recarga la página).
        return "redirect:/admin";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return "redirect:/admin";
    }

    private void cargarListasComunes(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        model.addAttribute("categorias", categoriaRepository.findAll());
    }
}
