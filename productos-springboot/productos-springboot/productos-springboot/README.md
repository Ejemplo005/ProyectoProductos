# Productos Spring Boot — Aguas Astrales

Este proyecto conecta dos cosas que hasta ahora estaban separadas:

1. **El proyecto JPA** (`productos-jpa`): las entidades `Categoria` y
   `Producto`, con su relación N a 1.
2. **La tienda estática** (`tienda-productos12`): el HTML, el CSS y las
   fotos de la tienda "Aguas Astrales".

Siguiendo la guía **"Conectar Spring Boot con un Frontend HTML y CSS"**,
armamos un único proyecto Spring Boot que sirve el HTML con **Thymeleaf**,
mostrando datos que vienen realmente de la base de datos (ya no de un
array de JavaScript en el propio HTML, como en la versión estática).

## Qué cambió respecto a cada proyecto original

| Proyecto original | Qué se llevó | Qué cambió |
|---|---|---|
| `productos-jpa` | Las entidades `Categoria` y `Producto`, la relación `@ManyToOne`/`@OneToMany` | Se agregó el atributo `imagenUrl` a `Producto` (para poder mostrar la foto), y `GenericRepository` se reemplazó por `JpaRepository` de Spring Data |
| `tienda-productos12` | Los 9 productos reales, sus categorías, las fotos y todo el CSS/diseño | El HTML pasó a ser una plantilla Thymeleaf; los productos ya no viven en un array de JavaScript ni en `localStorage`, sino en la base de datos; el panel de administración con login falso (JS) se reemplazó por un panel real (`/admin`) que guarda en la base |

## Cómo correrlo

1. Tener MySQL corriendo en `localhost:3306`.
2. `./gradlew bootRun`
3. Abrir `http://localhost:8080` → la tienda.
4. Abrir `http://localhost:8080/admin` → el panel de administración.

La primera vez que arranca, `DataInitializer` carga automáticamente las 6
categorías y los 9 productos originales de Aguas Astrales (ver más abajo).

## Estructura del proyecto

```
productos-springboot/
├── build.gradle                     Dependencias: Web + Data JPA + Thymeleaf + MySQL
├── settings.gradle
└── src/main/
    ├── java/com/productos/
    │   ├── ProductosSpringbootApplication.java   @SpringBootApplication
    │   ├── modelo/
    │   │   ├── Categoria.java
    │   │   └── Producto.java                     (+ imagenUrl)
    │   ├── repository/
    │   │   ├── CategoriaRepository.java           extends JpaRepository
    │   │   └── ProductoRepository.java             extends JpaRepository
    │   ├── servicio/
    │   │   └── ProductoService.java                @Service + @Transactional
    │   ├── controller/
    │   │   ├── ProductoController.java             @Controller — la vitrina pública
    │   │   └── AdminController.java                 @Controller — alta/edición/borrado
    │   └── config/
    │       └── DataInitializer.java                 carga los 9 productos al arrancar
    └── resources/
        ├── application.properties                   reemplaza persistence.xml
        ├── templates/
        │   ├── productos.html                        la vitrina (Thymeleaf)
        │   └── admin.html                             el panel (Thymeleaf)
        └── static/
            ├── css/estilos.css                        el diseño original de la tienda
            └── images/                                 las 9 fotos + el logo
```

## El recorrido de una request, explicado

### Mostrar la vitrina (GET)

```
Navegador → GET /productos
          → ProductoController.productos()
          → productoService.listarTodos()
          → productoRepository.findAllByOrderByNombreAsc()
          → SELECT * FROM producto ORDER BY nombre
          → model.addAttribute("productos", lista)
          → Thymeleaf procesa templates/productos.html
          → el navegador recibe HTML ya armado, con los productos reales
```

### Agregar un producto (POST)

```
Formulario en admin.html → POST /admin/guardar
                          → AdminController.guardar(@RequestParam...)
                          → productoService.guardar(producto)   (@Transactional)
                          → productoRepository.save(producto)
                          → INSERT INTO producto (...)
                          → return "redirect:/admin"
                          → el navegador hace GET /admin de nuevo
                          → se ve la lista ya actualizada
```

Es exactamente el flujo que explica la guía en la sección "¿Qué significa
redirect:?": guardar y volver a mostrar la lista actualizada.

## Decisiones de diseño (y por qué)

- **Se agregó `imagenUrl` a `Producto`.** El proyecto de JPA puro no lo
  tenía porque no hacía falta mostrar nada visualmente. Ahora que hay una
  vitrina real, cada producto necesita saber qué foto mostrar.

- **Las categorías dejaron de ser texto libre.** En la tienda estática,
  la categoría de un producto era un simple string (`category: "Hogar"`)
  escrito a mano en cada producto, sin ninguna relación real. Ahora es la
  entidad `Categoria` de JPA, con una relación de verdad: el `<select>`
  del formulario de admin se arma dinámicamente a partir de
  `categoriaRepository.findAll()`, así que nunca se puede escribir una
  categoría "inventada" que no exista.

- **`GenericRepository` (JPA puro) → `JpaRepository` (Spring Data JPA).**
  Todo el código manual de `EntityManager` que tenían en `productos-jpa`
  ya no hace falta: `ProductoRepository` y `CategoriaRepository` son
  interfaces vacías.

- **Un mismo formulario sirve para alta y edición**, tal como se explica
  en la guía de frontend: se decide el título y si se manda el `id` según
  si `producto.id` es `null` o no.

- **El panel `/admin` NO tiene login.** La tienda estática tenía un login
  falso hecho en JavaScript (usuario/contraseña hardcodeados, sin ninguna
  seguridad real — el propio código lo aclaraba). Como todavía no vimos
  Spring Security, se optó por ser honestos: dejamos el panel abierto y
  lo decimos explícitamente, en vez de fingir una seguridad que no existe.
  Es el paso lógico que sigue después de esta guía.

- **`DataInitializer` con `CommandLineRunner`.** Para no arrancar siempre
  con la base vacía, se agregó un componente que carga los datos
  originales de Aguas Astrales la primera vez (y no vuelve a insertarlos
  si ya hay categorías cargadas).

## Los 9 productos originales

| Producto | Categoría | Precio | Stock |
|---|---|---|---|
| Sahumerios Triple Combinado | Aromas | $3.000 | 20 |
| Perfume Textil | Textiles | $6.000 | 15 |
| Difusores de Ambiente | Hogar | $5.000 | 12 |
| Fragancias Saphirus | Aromaterapia | $4.200 | 10 |
| Bombas Aromáticas | Hogar | $4.800 | 18 |
| Pastillas de limpieza | Aromaterapia | $3.000 | 25 |
| Aceite para hornillo | Aceite | $4.500 | 14 |
| Sahumerios Canabis | Aromas | $3.500 | 22 |
| Textiles | Ropa | $5.500 | 16 |

## Un detalle corregido al migrar

En el proyecto estático, el archivo de la foto del aceite para hornillo se
llamaba `aceite para  hornillo04.jpeg` (con doble espacio), pero el código
JavaScript lo referenciaba como `aceite para hornilo04.jpeg` (un solo
espacio, y "hornilo" mal escrito): esa imagen nunca cargaba, y se veía el
logo de reemplazo (`onerror`) en su lugar. Al migrar, todos los nombres de
archivo se limpiaron (sin espacios, ej. `aceite-hornillo.jpeg`), así que
ahora todas las fotos cargan correctamente.

## Dónde está cada concepto de la guía de Thymeleaf

| Concepto | Dónde está |
|---|---|
| `spring-boot-starter-thymeleaf` | `build.gradle` |
| `@Controller` (vs `@RestController`) | `ProductoController`, `AdminController` |
| `return "productos";` → busca `templates/productos.html` | `ProductoController.productos()` |
| `model.addAttribute(...)` | en ambos controllers |
| `th:text`, `th:each`, `th:if` | `productos.html`, `admin.html` |
| Recursos estáticos en `/static` | `static/css/estilos.css`, `static/images/*` |
| `@RequestParam` en un formulario POST | `AdminController.guardar()` |
| `redirect:` después de guardar | `AdminController.guardar()` y `eliminar()` |
| Diferencia API REST vs Thymeleaf | este proyecto usa solo `@Controller`; si más adelante se agrega una API además de esto, convivirían sin problema |

## Próximos pasos posibles

- Proteger `/admin` con Spring Security (login real).
- Subir imágenes desde el propio formulario en vez de escribir una ruta a mano.
- Agregar `th:each` con paginación si el catálogo crece mucho.
- Sumar una API REST (`@RestController`) en paralelo, para un futuro frontend en React.
