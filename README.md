# 🩸 Crimson List - Gestión de Biblioteca de Anime & Manga

**Crimson List** es una solución móvil para la gestión y organización de catálogos personales de anime y manga. Desarrollada de forma nativa para Android, la aplicación combina una estética visual de alto contraste (basada en tonos negro profundo y carmesí) con una arquitectura moderna y escalable.

---

## 🛠️ Estado Técnico del Proyecto

### 🚀 Implementado (Fase de Prototipado y UI)
- **Framework de UI:** Desarrollo íntegro con **Jetpack Compose** bajo una arquitectura declarativa.
- **Gestión de Imágenes:** Implementación de **Coil 3** con soporte para protocolos de red (OkHttp), permitiendo la carga asíncrona de portadas.
- **Diseño de Componentes:** 
    - `AnimeMangaCard`: Tarjetas personalizadas con relación de aspecto 2:3, gradientes de legibilidad y distintivos de categoría.
    - `HomeScreen`: Grilla dinámica de 3 columnas optimizada para la navegación de catálogos extensos.
- **Tipografía y Estilo:** Integración de **Google Fonts** (`Inter`, `Montserrat`) y un sistema de temas personalizado que garantiza un modo oscuro inmersivo.
- **Manejo de Dependencias:** Uso de **Version Catalogs** (`libs.versions.toml`) para una gestión centralizada y limpia de librerías.

---

## 🛠️ Roadmap y Desarrollo Planificado

### 📡 Comunicación y Datos (Requests)
- **Integración de API:** Se utilizará **Retrofit** como cliente HTTP principal para el consumo de servicios REST. Esto permitirá sincronizar la base de datos local con información actualizada de servicios externos como *AniList*.
- **Mapeo de Datos:** Implementación de conversores JSON a objetos Kotlin (POJOs) para una manipulación de datos segura y eficiente.

### 🗄️ Persistencia y Almacenamiento Local
- **Base de Datos:** Implementación de **SQLite** a través de la librería **Room**.
- **Modelado Relacional:** 
    - Tabla `Users`: Gestión de perfiles locales.
    - Tabla `Anime/Manga`: Caché local de metadatos.
    - **Relación N:N:** Tabla intermedia para gestionar favoritos, estados de lectura/visualización y puntuaciones personales.

### 📱 Funcionalidades de Usuario
- **Módulo de Autenticación:** Pantalla de Login inicial para el acceso a perfiles personalizados.
- **Búsqueda y Filtros:** Motor de búsqueda integrado en la vista principal para la localización rápida de títulos por nombre o género.
- **Pantalla de Detalle:** Vista extendida que incluirá sinopsis, sistema de calificación por estrellas y controles para la gestión de listas (Viendo, Pendiente, Completado).

---

## 🏗️ Arquitectura de Software
El proyecto sigue el patrón de diseño **MVVM (Model-View-ViewModel)** para separar la lógica de negocio de la interfaz de usuario, facilitando el mantenimiento y las pruebas unitarias.

---

## 🚀 Requisitos e Instalación
- **Android Studio:** Ladybug (2024.2.1) o superior.
- **Min SDK:** API 24 (Android 7.0).
- **Target SDK:** API 35.
- **Kotlin:** 2.1.0+

---
*Este proyecto se desarrolla como parte de un requerimiento académico para la materia Desarrollo Web y Movil*
