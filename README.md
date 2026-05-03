# 🩸 Crimson List - Gestión de Biblioteca de Anime & Manga

**Crimson List** es una solución móvil de nivel premium para la gestión y organización de catálogos personales de anime y manga. Desarrollada de forma nativa para Android, la aplicación combina una estética visual inmersiva con una arquitectura moderna, escalable y el uso de Inteligencia Artificial Generativa.

---

## 🛠️ Estado Técnico del Proyecto

### 🚀 Implementado (Fase de Desarrollo Final)
- **Arquitectura de Software:** Implementación sólida del patrón **MVVM (Model-View-ViewModel)** y **Clean Architecture** mediante el uso de Mappers para desacoplar modelos de API, dominio y persistencia.
- **Diseño Inmersivo & Reactivo:**
    - **Dynamic Color Extraction:** La interfaz adapta sus colores (botones, sombras, acentos) automáticamente basándose en la paleta predominante de la portada de cada anime.
    - **Estética Cinematic:** Uso de degradados profundos, tipografía de alto impacto (*Bebas Neue* & *Montserrat*) y efectos de iluminación neón en botones de acción.
- **Comunicación con API (GraphQL):** Integración de **Apollo Kotlin 4** para el consumo de la API de *AniList*. 
    - **Paginación Lateral Infinita:** Sistema de carga dinámica en filas horizontales (Discover) que detecta la proximidad del final del scroll para cargar contenido nuevo sin interrupciones.
- **Inteligencia Artificial (IA Generativa):** 
    - Integración de **Gemini AI** para la traducción contextual de sinopsis del inglés al español.
    - **Caché Inteligente:** Las traducciones se persisten en Room para evitar llamadas innecesarias a la API y permitir lectura offline.
- **Gestión de Biblioteca (Persistence):**
    - Sistema de **Upsert** (Update/Insert) para gestionar el progreso del usuario en tiempo real.
    - Seguimiento detallado: Estado (Viendo, Completado, etc.), contador de episodios, nota personal (0-100) y favoritos.
- **Seguridad & Autenticación:**
    - **Firebase Authentication:** Manejo de identidades y verificación de email.
    - **BCrypt:** Hasheo de contraseñas para la capa de seguridad local en Room.
- **Navegación:** Uso de **Jetpack Navigation Compose** con rutas seguras y paso de argumentos para pantallas de detalle.

---

## 🏗️ Estructura Completa del Proyecto

```text
.
├── app/
│   ├── graphql/        # Schema y queries (.graphql)
│   ├── java/com/example/animeapp2/
│   │   ├── data/
│   │   │   ├── local/  # Room (DAO, Entities, DB)
│   │   │   ├── mapper/ # Conversores API -> Domain
│   │   │   └── model/  # Modelos de datos limpios
│   │   ├── ui/
│   │   │   ├── components/  # AddToLibrarySheet, DiscoverRow, SearchBar, etc.
│   │   │   ├── screens/     # HomeScreen, DetailScreen, DiscoverScreen, Auth
│   │   │   └── theme/       # Colores reactivos y tipografías
│   │   └── viewmodel/       # Lógica de negocio y estados
└── README.md
```

---

## 🗄️ Modelo de Datos Destacado

### Tabla: `user_anime_library` (Relación Muchos a Muchos)
La pieza central de la personalización, gestionada mediante la anotación `@Upsert` para una sincronización fluida:

| Campo  |  Tipo | Descripción    |
|---|---|----------------|
|  `id_usuario` |  `Int` | Usuario dueño de la colección (FK). |
| `id_animemanga`  |  `Int` | Obra vinculada (FK).|
|  `estado`  |  `Enum` | VIENDO, COMPLETADO, PAUSADO, etc. |
|  `episodios_vistos` | `Int`  | Progreso actual. |
|  `nota_personal` |  `Int?` | Puntuación personal (0-100). |
| `es_favorito`  | `Boolean`  | Marcado como destacado. |

---

## 🛠️ Requisitos Técnicos
- **Android Studio:** Ladybug (2024.2.1) o superior.
- **Bibliotecas Clave:** 
    - **Apollo Kotlin 4.4.2** (GraphQL)
    - **Room Persistence 2.8.4**
    - **Gemini AI SDK** (Traducción)
    - **Jetpack Compose 1.7.x**
    - **Coil 3** (Carga de imágenes asíncrona)

---
*Este proyecto se desarrolla como proyecto final para la materia Desarrollo Web y Movil. La aplicación busca demostrar la capacidad de integrar servicios cloud (Firebase), APIs complejas (GraphQL) e IA Generativa en un entorno móvil profesional.*
