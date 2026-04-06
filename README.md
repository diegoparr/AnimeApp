# 🩸 Crimson List - Gestión de Biblioteca de Anime & Manga

**Crimson List** es una solución móvil para la gestión y organización de catálogos personales de anime y manga. Desarrollada de forma nativa para Android, la aplicación combina una estética visual de alto contraste (basada en tonos negro profundo y carmesí) con una arquitectura moderna y escalable.

---

## 🛠️ Estado Técnico del Proyecto

### 🚀 Implementado (Fase de Desarrollo Activo)
- **Framework de UI:** Desarrollo íntegro con **Jetpack Compose** bajo una arquitectura declarativa.
- **Comunicación con API (GraphQL):** Integración de **Apollo Kotlin 4** para el consumo de la API de *AniList*. Configuración de esquemas (`.graphqls`) y consultas tipadas para una obtención de datos eficiente.
- **Navegación y Layout:** 
    - Implementación de **Modal Navigation Drawer** con diseño personalizado (gradientes carmesí y tipografía temática).
    - Sistema de temas dinámico con soporte para **Modo Oscuro** inmersivo ("The Eclipse").
- **Gestión de Imágenes:** Uso de **Coil 3** con soporte para red (OkHttp) para la carga optimizada de portadas.
- **Diseño de Componentes:** 
    - `AnimeMangaCard`: Tarjetas personalizadas con relación de aspecto 2:3 y gradientes de legibilidad.
    - `HomeScreen`: Grilla dinámica optimizada.
- **Tipografía Personalizada:** Integración de **Bebas Neue** (para títulos impactantes) y **Montserrat** (para legibilidad), gestionadas a través de Google Fonts y recursos locales.
- **Traducción Inteligente:** Incorporación de **ML Kit Translate** para futuras funcionalidades de localización de contenido.

---

## 🛠️ Roadmap y Desarrollo Planificado

### 📡 Comunicación y Datos
- **Refinamiento de Consultas:** Expandir las queries de GraphQL para obtener detalles específicos (estudios, tráilers, recomendaciones).
- **Manejo de Errores:** Implementación de estados de carga y error robustos en la UI.

### 🗄️ Persistencia y Almacenamiento Local
- **Base de Datos:** Implementación de **Room (SQLite)** para el almacenamiento offline de la biblioteca del usuario.
- **Caché Inteligente:** Sincronización entre los datos de Apollo y la base de datos local para minimizar el consumo de datos.

### 📱 Funcionalidades de Usuario
- **Módulo de Autenticación:** Pantalla de Login para acceso a perfiles.
- **Búsqueda Avanzada:** Filtros por género, temporada, puntuación y estado de emisión.
- **Pantalla de Detalle:** Vista extendida con sinopsis, sistema de calificación y gestión de listas personales.

---

## 🏗️ Arquitectura de Software
El proyecto sigue el patrón **MVVM (Model-View-ViewModel)**, asegurando una separación clara entre la UI y la lógica de negocio, facilitando la escalabilidad y el testing.

---

## 🚀 Requisitos e Instalación
- **Android Studio:** Ladybug (2024.2.1) o superior.
- **Min SDK:** API 24 (Android 7.0).
- **Target SDK:** API 36.
- **Kotlin:** 2.1.0+
- **Apollo Kotlin:** 4.4.2

---
*Este proyecto se desarrolla como parte de un requerimiento académico para la materia Desarrollo Web y Movil*
