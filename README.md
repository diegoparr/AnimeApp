# 🩸 Crimson List - Gestión de Biblioteca de Anime & Manga

**Crimson List** es una solución móvil para la gestión y organización de catálogos personales de anime y manga. Desarrollada de forma nativa para Android, la aplicación combina una estética visual de alto contraste (basada en tonos negro profundo y carmesí) con una arquitectura moderna y escalable.

---

## 🛠️ Estado Técnico del Proyecto

### 🚀 Implementado (Fase de Desarrollo Activo)
- **Arquitectura de Software:** Implementación sólida del patrón **MVVM (Model-View-ViewModel)** y **Clean Architecture** mediante el uso de Mappers para desacoplar los modelos de la API de los modelos de dominio.
- **Framework de UI:** Desarrollo íntegro con **Jetpack Compose** bajo una arquitectura declarativa y reactiva.
- **Comunicación con API (GraphQL):** Integración de **Apollo Kotlin 4** para el consumo de la API de *AniList*. Incluye soporte para **Paginación Dinámica (Scroll Infinito)** optimizada mediante estados del ViewModel.
- **Navegación:** Uso de **Jetpack Navigation Compose** con rutas seguras basadas en *Sealed Classes* y paso de argumentos (ID) entre pantallas.
- **Inteligencia Artificial (IA Generativa):** Integración de **Gemini 2.5 Flash API** (Google AI SDK) para la traducción contextual y profesional de sinopsis del inglés al español, con sistema de caché en memoria para optimizar peticiones.
- **Gestión de Layout y Temas:** 
    - **Modal Navigation Drawer** personalizado con degradados vampíricos y bordes redondeados.
    - Sistema de temas dinámico inmersivo ("Eclipse Theme").
- **Gestión de Imágenes:** Uso de **Coil 3** para la carga asíncrona y eficiente de posters.
- **Tipografía y Estética:** Combinación de **Bebas Neue** (títulos) y **Montserrat** (cuerpo) para una identidad visual "Premium".

---

## 🛠️ Roadmap y Desarrollo Planificado

### 🗄️ Persistencia y Almacenamiento Local
- **Base de Datos (Room):** Migración del sistema de caché de traducciones a **SQLite** para persistencia permanente y soporte offline.
- **Gestión de Favoritos:** Permitir al usuario guardar animes/mangas en su biblioteca local.

### 📱 Funcionalidades de Usuario
- **Descubrir (Exploración):** Pantalla de búsqueda avanzada con filtros por géneros, temporada y año.
- **Sincronización:** Refinamiento de la comunicación entre la base de datos local y la API remota.

---

## 🏗️ Requisitos Técnicos
- **Android Studio:** Ladybug (2024.2.1) o superior.
- **Min SDK:** API 24 (Android 7.0).
- **Target SDK:** API 36.
- **Kotlin:** 2.1.0+
- **Bibliotecas Clave:** 
    - Apollo Kotlin 4.4.2 (GraphQL)
    - Navigation Compose 2.8.5
    - Google AI SDK (Gemini)
    - Coil 3 (Imágenes)

---
*Este proyecto se desarrolla como parte de un requerimiento académico para la materia Desarrollo Web y Movil*
