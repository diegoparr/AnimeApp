# 🩸 Crimson List - Gestión de Biblioteca de Anime & Manga

**Crimson List** es una solución móvil de nivel premium para la gestión y organización de catálogos personales de anime y manga. Desarrollada de forma nativa para Android, la aplicación combina una estética visual inmersiva ("Eclipse Theme") con una arquitectura moderna, escalable y el uso de Inteligencia Artificial Generativa.

---

## 🛠️ Estado Técnico del Proyecto

### 🚀 Implementado (Fase de Desarrollo Activo)
- **Arquitectura de Software:** Implementación sólida del patrón **MVVM (Model-View-ViewModel)** y **Clean Architecture** mediante el uso de Mappers para desacoplar modelos de API, dominio y persistencia.
- **Framework de UI:** Desarrollo íntegro con **Jetpack Compose** bajo una arquitectura declarativa y reactiva.
- **Persistencia de Datos (Room):**
    - Sistema de base de datos local **SQLite** con **Room**.
    - Modelado relacional avanzado que incluye tablas maestras (`Usuarios`, `AnimeManga`, `Generos`) y tablas de relación Muchos a Muchos (`CrossRefs`).
    - Uso de **TypeConverters** para el manejo de tipos complejos como Enums de estado y Listas de géneros.
- **Comunicación con API (GraphQL):** Integración de **Apollo Kotlin 4** para el consumo de la API de *AniList*. Soporte para **Paginación Dinámica (Scroll Infinito)** optimizada.
- **Navegación:** Uso de **Jetpack Navigation Compose** con rutas seguras basadas en *Sealed Classes* y paso de argumentos tipados.
- **Inteligencia Artificial (IA Generativa):** 
    - Integración de **Gemini 1.5 Flash API** para la traducción contextual de sinopsis.
    - **Caché Persistente:** Las traducciones se almacenan en la base de datos local para minimizar el consumo de cuota de API y permitir el acceso offline.
- **Diseño Visual "Premium":**
    - **Login Screen:** Fondo de mosaico inclinado (*Slanted Grid*) con posters de anime, capas de opacidad (*Scrim*) y animaciones de transición.
    - **Modal Navigation Drawer:** Personalizado con degradados carmesí.
    - **Detail Screen:** Vista inmersiva con gradientes, chips dinámicos y controles de traducción.
- **Tipografía:** Combinación de **Bebas Neue** y **Montserrat**.

---

## 🛠️ Roadmap y Desarrollo Planificado

### 🔐 Seguridad y Autenticación
- **Hashing de Contraseñas:** Implementación de **BCrypt** para el almacenamiento seguro de credenciales en la base de datos local.
- **Módulo de Registro:** Lógica completa para la creación y validación de nuevos usuarios.

### 📱 Funcionalidades de Usuario
- **Descubrir (Exploración):** Pantalla de búsqueda avanzada con filtros por géneros, temporada y año (Malla estilo Netflix/Crunchyroll).
- **Gestión de Biblioteca:** Funcionalidad para que el usuario guarde y actualice su progreso (episodios vistos, nota personal, estado) en su biblioteca local.

---

## 🏗️ Requisitos Técnicos
- **Android Studio:** Ladybug (2024.2.1) o superior.
- **Min SDK:** API 24 (Android 7.0).
- **Target SDK:** API 36.
- **Kotlin:** 2.1.0+
- **Bibliotecas Clave:** 
    - Room Persistence 2.6.1
    - Apollo Kotlin 4.4.2 (GraphQL)
    - Navigation Compose 2.8.5
    - Google AI SDK (Gemini)
    - Coil 3 (Imágenes)

---
*Este proyecto se desarrolla como proyecto final para la materia Desarrollo Web y Movil*
