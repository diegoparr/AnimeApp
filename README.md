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

## 🏗️ Estructura Completa del Proyecto

A continuación se detalla la jerarquía de archivos y carpetas del repositorio:

```text
.
├── app/
│   ├── google-services.json    # Configuración de Firebase para Gemini
│   ├── build.gradle.kts        # Dependencias y configuración del módulo app
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           ├── graphql/        # Schema y queries de AniList API
│           ├── java/com/example/animeapp2/
│           │   ├── data/
│           │   │   ├── local/
│           │   │   │   ├── dao/        # UserDao.kt, AnimeMangaDao.kt
│           │   │   │   ├── entities/   # UserEntity, AnimeMangaEntity, etc.
│           │   │   │   ├── AppDatabase.kt
│           │   │   │   └── Converters.kt
│           │   │   ├── mapper/         # AnimeMangaMapper.kt
│           │   │   ├── model/          # AnimeManga, Title, CoverImage, AnimeStatus
│           │   │   └── network/        # ApolloClient.kt
│           │   ├── ui/
│           │   │   ├── navigation/     # NavDestinations.kt
│           │   │   ├── screens/        # LoginScreen, RegisterScreen, HomeScreen, DetailScreen
│           │   │   └── theme/          # Color, Theme, Type
│           │   ├── util/               # Extensions, SecurityHelper, TranslatorManager
│           │   ├── viewmodel/          # AuthUsersViewModel, AnimeMangaViewModel
│           │   └── MainActivity.kt     # Entry point & NavHost
│           └── res/
│               ├── drawable/           # Iconos y recursos gráficos
│               ├── font/               # Tipografías personalizadas
│               ├── values/             # strings.xml, colors.xml, themes.xml
│               └── xml/                # Configuraciones de red y backup
├── gradle/                     # Wrapper y catálogos de versiones (libs.versions.toml)
├── build.gradle.kts            # Configuración de proyecto raíz
├── settings.gradle.kts         # Definición de módulos
└── README.md                   # Documentación principal
```

---

## 🗄️ Modelo de Datos (Esquema de Base de Datos Room)

La persistencia local se gestiona mediante un esquema relacional optimizado para rendimiento y seguridad:

### 1. Tabla: `users`
| Campo | Tipo | Restricción | Descripción |
| :--- | :--- | :--- | :--- |
| `id_usuario` | `Int` | PK (Auto) | Identificador único del usuario. |
| `email_usuario` | `String` | Único | Correo electrónico para autenticación. |
| `nombre_usuario` | `String` | - | Nombre público del perfil. |
| `contraseña_hash` | `String` | - | Password cifrado con BCrypt. |

### 2. Tabla: `anime_manga`
| Campo | Tipo | Restricción | Descripción |
| :--- | :--- | :--- | :--- |
| `id_animemanga` | `Int` | PK | ID persistente desde la API AniList. |
| `titulo_romaji` | `String` | - | Título principal (formato Romaji). |
| `titulo_english` | `String?` | - | Título internacional (opcional). |
| `titulo_native` | `String?` | - | Título original (Japonés). |
| `descripcion` | `String` | - | Sinopsis (soporta HTML). |
| `tipo` | `String` | - | Clasificación (ANIME / MANGA). |
| `portada_url` | `String` | - | Enlace a la imagen principal. |

### 3. Tabla: `anime_translations` (Caché IA)
| Campo | Tipo | Restricción | Descripción |
| :--- | :--- | :--- | :--- |
| `id_animemanga` | `Int` | PK / FK | Referencia a la obra. |
| `lenguaje` | `String` | PK | Código ISO de idioma (ej: "es"). |
| `descripcion_traducida` | `String` | - | Texto generado por IA. |
| `last_updated` | `Long` | - | Timestamp de sincronización. |

### 4. Tabla: `genre_animemanga` (Maestra)
| Campo | Tipo | Restricción | Descripción |
| :--- | :--- | :--- | :--- |
| `nombre_genero` | `String` | PK | Identificador único del género. |

### 5. Tablas de Relación (Many-to-Many)

**`anime_genre_cross_ref` (Relación Obra-Género)**

| Campo | Tipo | Restricción | Descripción |
| :--- | :--- | :--- | :--- |
| `id_animemanga` | `Int` | PK / FK | Referencia al Anime/Manga. |
| `nombre_genero` | `String` | PK / FK | Referencia al Género. |

**`user_anime_library` (Biblioteca Personal)**

| Campo  |  Tipo | Restricción | Descripción    |
|---|---|-------------|----------------|
|  `id_usuario` |  `Int` | PK / FK     | Usuario dueño de la colección. |
| `id_animemanga`  |  `Int` | PK / FK     | Obra vinculada.|
|  `estado`  |  `Enum` | -           | Estado (PENDIENTE, VIENDO, etc.). |
|  `episodios_vistos` | `Int`  | -           | Progreso actual de lectura/visionado. |
|  `nota_personal` |  `Int?` | -           |      Puntuación personal (1-10).          |
| `es_favorito`  | `Boolean`  | -           |      Marcado como destacado.          |
|  `fecha_agregado` |  `Long` | -           |         Timestamp de creación del registro.        |






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
