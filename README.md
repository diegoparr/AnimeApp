# 🩸 Crimson List - Gestión de Biblioteca de Anime & Manga

**Crimson List** es una aplicación Android de alto rendimiento diseñada para la organización y seguimiento de catálogos personales de anime y manga. El proyecto destaca por integrar tecnologías de vanguardia como **Inteligencia Artificial Generativa**, **GraphQL**, y una arquitectura **MVVM Reactiva**, ofreciendo una experiencia de usuario fluida y visualmente inmersiva.

---

## 🚀 Características Principales

### 🎨 Diseño Inmersivo ("Eclipse Theme")
- **Dynamic Color Extraction:** La interfaz adapta sus acentos cromáticos automáticamente basándose en la paleta predominante de la portada de cada obra.
- **Cinematic UI:** Uso de degradados dinámicos, tipografía de alto impacto (*Bebas Neue* & *Montserrat*) y efectos neón en componentes de acción.

### 🤖 Inteligencia Artificial (IA)
- **Traducción con Gemini AI:** Integración del modelo de lenguaje de Google para traducir sinopsis del inglés al español con precisión contextual.
- **Caché de Traducciones:** Implementación de un sistema de persistencia local para traducciones, reduciendo latencia y consumo de tokens de API (Offline-first para sinopsis).

### 📡 Comunicación y Datos (GraphQL)
- **Apollo Kotlin 4:** Consumo eficiente de la API de *AniList* mediante queries optimizadas.
- **Paginación Lateral Infinita:** Sistema de carga bajo demanda en filas horizontales que garantiza un scroll fluido sin fin en la pantalla de descubrimiento.

### 💾 Persistencia y Relaciones (Room)
- **Modelado Relacional Avanzado:** Uso de relaciones Muchos-a-Muchos (`CrossRef`) para vincular usuarios con sus bibliotecas personales.
- **Sincronización Reactiva:** Implementación de `Flow` y `StateFlow` para que cualquier cambio en la base de datos se refleje instantáneamente en la interfaz sin refrescos manuales.
- **Operaciones Atómicas:** Uso de `@Upsert` y `@Transaction` para garantizar la integridad de los datos.

### 🛡️ Seguridad Híbrida
- **Firebase Authentication:** Gestión robusta de identidades y verificación de cuentas por email.
- **Cifrado Local (BCrypt):** Hasheo de contraseñas en la base de datos local para proteger la privacidad del usuario incluso en el almacenamiento físico del dispositivo.

---

## 🏗️ Arquitectura de Software

La aplicación sigue los principios de **Clean Architecture** y **MVVM**:

- **UI Layer (Jetpack Compose):** Pantallas declarativas y componentes modulares.
- **Domain Layer:** Modelos de datos limpios desacoplados de fuentes externas.
- **Data Layer:** Implementación de Repositorios, Mappers (Transformadores de datos) y DAOs.

### Estructura de Directorios
```text
.
├── app/
│   ├── src/main/
│   │   ├── graphql/          # Esquemas y Queries de AniList (.graphql)
│   │   ├── java/com/example/animeapp2/
│   │   │   ├── data/
│   │   │   │   ├── local/    # Room (AppDatabase, DAOs, Entities)
│   │   │   │   ├── mapper/   # Transformadores API -> Domain -> Entity
│   │   │   │   ├── model/    # Modelos de dominio (AnimeManga, Status, etc.)
│   │   │   │   └── network/  # Configuración de Apollo Client
│   │   │   ├── ui/
│   │   │   │   ├── components/ # Componentes reutilizables (Cards, Sheets, Bars)
│   │   │   │   ├── navigation/ # Definición de rutas y NavHost
│   │   │   │   ├── screens/    # Pantallas principales (Home, Library, Detail, Auth)
│   │   │   │   └── theme/      # Definiciones de color reactivo y tipografía
│   │   │   ├── util/         # Helpers (IA, Seguridad, Extensiones)
│   │   │   └── viewmodel/    # Lógica de negocio y gestión de estados
│   │   └── res/              # Recursos visuales y fuentes
└── README.md
```

---

## 🗄️ Modelo de Datos (Esquema Room)

### Tablas Principales
| Tabla | Descripción |
| :--- | :--- |
| `users` | Credenciales y estado de verificación de usuarios. |
| `anime_manga` | Información técnica de las obras (Títulos, portadas, sinopsis). |
| `anime_translations` | Almacenamiento local de sinopsis procesadas por la IA. |
| `user_anime_library` | **Tabla Intermedia (CrossRef):** Almacena el progreso del usuario (Estado, episodios, nota, favorito). |

### Detalle de `user_anime_library`
| Campo | Tipo | Función |
| :--- | :--- | :--- |
| `id_usuario` | `Int` (FK) | Vinculación con el perfil del usuario. |
| `id_animemanga` | `Int` (FK) | Vinculación con la obra maestra. |
| `estado` | `Enum` | VIENDO, COMPLETADO, PAUSADO, ABANDONADO, PENDIENTE. |
| `episodios_vistos` | `Int` | Contador de progreso. |
| `nota_personal` | `Int?` | Valoración del usuario (0-100). |
| `es_favorito` | `Boolean` | Flag de destaque personal. |

---

## ⚙️ Especificaciones Técnicas
- **Lenguaje:** Kotlin 2.1.0 (Corrutinas, Flow, Serialization)
- **UI:** Jetpack Compose (Material 3)
- **Red:** Apollo GraphQL 4.4.2
- **Persistencia:** Room 2.8.4 (SQLite)
- **IA:** Google Generative AI SDK (Gemini)
- **Imágenes:** Coil 3 (Carga asíncrona con caché de disco)
- **Seguridad:** Firebase Auth & JBcrypt

---
*Desarrollado como Proyecto Final para la materia de Desarrollo Web y Móvil. 2025.*
