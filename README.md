# 🩸 Crimson List - Gestión de Biblioteca de Anime & Manga

**Crimson List** es una solución móvil de nivel premium para la gestión y organización de catálogos personales de anime y manga. Desarrollada de forma nativa para Android, la aplicación combina una estética visual inmersiva ("Eclipse Theme") con una arquitectura moderna, escalable y el uso de Inteligencia Artificial Generativa.

---

## 🛠️ Estado Técnico del Proyecto

### 🚀 Implementado (Fase de Desarrollo Activo)
- **Arquitectura de Software:** Implementación sólida del patrón **MVVM (Model-View-ViewModel)** y **Clean Architecture** mediante el uso de Mappers para desacoplar modelos de API, dominio y persistencia.
- **Seguridad de Datos:** Autenticación local robusta mediante el uso de **BCrypt** para el hasheo de contraseñas, garantizando que las credenciales nunca se almacenen en texto plano.
- **Módulo de Autenticación:** Lógica completa de Registro y Login funcional, con validaciones de campos, manejo de errores en tiempo real y gestión de estados de carga.
- **Framework de UI:** Desarrollo íntegro con **Jetpack Compose** bajo una arquitectura declarativa y reactiva.
- **Persistencia de Datos (Room):**
    - Sistema de base de datos local **SQLite** con **Room**.
    - Modelado relacional avanzado que incluye tablas maestras (`Usuarios`, `AnimeManga`, `Generos`) y tablas de relación Muchos a Muchos (`CrossRefs`).
    - Uso de **TypeConverters** para el manejo de tipos complejos.
- **Comunicación con API (GraphQL):** Integración de **Apollo Kotlin 4** para el consumo de la API de *AniList*. Soporte para **Paginación Dinámica (Scroll Infinito)** optimizada.
- **Navegación:** Uso de **Jetpack Navigation Compose** con rutas seguras basadas en *Sealed Classes* y paso de argumentos tipados.
- **Inteligencia Artificial (IA Generativa):** 
    - Integración de **Gemini API** para la traducción contextual de sinopsis.
    - **Caché Persistente:** Almacenamiento local de traducciones para optimizar el consumo de cuota de API y permitir acceso offline.
- **Diseño Visual "Premium":**
    - **Login & Register Screens:** Fondo de mosaico inclinado (*Slanted Grid*) con posters, capas de opacidad (*Scrim*) y animaciones de transición.
    - **Detail Screen:** Vista inmersiva con gradientes y controles de traducción.

---

## 🛠️ Roadmap y Desarrollo Planificado

### 📱 Funcionalidades de Usuario
- **Descubrir (Exploración):** Pantalla de búsqueda avanzada con filtros por géneros, temporada y año (Malla estilo Netflix/Crunchyroll).
- **Gestión de Biblioteca:** Funcionalidad para que el usuario guarde y actualice su progreso (episodios vistos, nota personal, estado) en su biblioteca local vinculada a su cuenta.

---

## 🏗️ Requisitos Técnicos
- **Android Studio:** Ladybug (2024.2.1) o superior.
- **Min SDK:** API 24 (Android 7.0).
- **Target SDK:** API 36.
- **Kotlin:** 2.2.10+
- **Bibliotecas Clave:** 
    - Room Persistence 2.8.4
    - Apollo Kotlin 4.4.2 (GraphQL)
    - Navigation Compose 2.8.5
    - JBcrypt 0.4 (Seguridad)
    - Google AI SDK / Firebase AI
    - Coil 3 (Imágenes)

---
*Este proyecto se desarrolla como proyecto final para la materia Desarrollo Web y Movil*
