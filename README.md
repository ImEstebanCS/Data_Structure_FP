# 🎵 SyncUp - Plataforma de Música Social

**SyncUp** es una aplicación de streaming musical desarrollada en Java con JavaFX que implementa estructuras de datos avanzadas y algoritmos para proporcionar recomendaciones inteligentes y funcionalidades sociales.

### Autores : Nicol Marin - Maicol Paez - Esteban Carmona 

## 📋 Características Principales

### 🎯 Funcionalidades del Usuario
- **Registro y Autenticación**: Sistema de usuarios con autenticación segura
- **Gestión de Favoritos**: Agregar y remover canciones favoritas
- **Búsqueda Avanzada**:
    - Autocompletado con Trie (búsqueda O(m) donde m es la longitud del prefijo)
    - Búsqueda por título, artista, género y año
- **Recomendaciones Inteligentes**:
    - Descubrimiento Semanal basado en favoritos
    - Radio personalizada basada en similitud de canciones
- **Red Social**:
    - Seguir/dejar de seguir usuarios
    - Sugerencias de amigos usando BFS
    - Grado de separación entre usuarios

### 👨‍💼 Funcionalidades del Administrador
- Gestión completa del catálogo de canciones (CRUD)
- Administración de usuarios
- Panel de control administrativo

## 🏗️ Arquitectura del Proyecto

```
SyncUp/
├── Controller/          # Controladores de lógica de negocio
├── Model/
│   ├── Entidades/      # Entidades del dominio (Usuario, Cancion, etc.)
│   ├── Grafos/         # Implementación de grafos (Similitud y Social)
│   └── Trie/           # Estructura Trie para autocompletado
├── Service/            # Servicios principales (SyncUpService, MusicDataService)
├── view/
│   └── controllers/    # Controladores de vista (JavaFX)
└── utils/              # Utilidades (NavigationManager, SessionManager)
```

## 🔧 Estructuras de Datos Implementadas

### 1. **Trie (Árbol de Prefijos)**
- **Ubicación**: `Model/Trie/TrieAutocompletado.java`
- **Propósito**: Búsqueda eficiente con autocompletado
- **Complejidad**: O(m) para búsqueda por prefijo, donde m es la longitud del prefijo
- **Uso**: Autocompletado de búsqueda de canciones

### 2. **Grafo de Similitud (Grafo Ponderado No Dirigido)**
- **Ubicación**: `Model/Grafos/GrafoDeSimilitud.java`
- **Propósito**: Conectar canciones por similitud para recomendaciones
- **Algoritmo**: Dijkstra para encontrar rutas de menor costo (mayor similitud)
- **Uso**: Generación de recomendaciones y radio personalizada

### 3. **Grafo Social (Grafo No Dirigido)**
- **Ubicación**: `Model/Grafos/GrafoSocial.java`
- **Propósito**: Modelar conexiones entre usuarios
- **Algoritmo**: BFS (Breadth-First Search) para encontrar amigos de amigos
- **Uso**: Sugerencias de usuarios y cálculo de grado de separación

### 4. **HashMap**
- **Propósito**: Acceso O(1) a usuarios y canciones por ID/username
- **Uso**: Almacenamiento eficiente de usuarios y administradores

### 5. **LinkedList**
- **Propósito**: Lista de favoritos del usuario
- **Uso**: Gestión de canciones favoritas

## 🚀 Requisitos del Sistema

- **Java**: JDK 17 o superior
- **Maven**: 3.6+ (incluido en el proyecto)
- **JavaFX**: 17.0.11 (gestionado por Maven)

## 📦 Instalación y Ejecución

### 1. Clonar el repositorio
```bash
git clone <repository-url>
cd Data_Structure_FP/SyncUp
```

### 2. Compilar el proyecto
```bash
# Windows
mvnw.cmd clean compile

# Linux/Mac
./mvnw clean compile
```

### 3. Ejecutar la aplicación
```bash
# Windows
mvnw.cmd javafx:run

# Linux/Mac
./mvnw javafx:run
```

O desde tu IDE favorito, ejecuta la clase `SyncUpApp.java`

## 👤 Credenciales por Defecto

### Administrador
- **Usuario**: `admin`
- **Contraseña**: `admin123`

## 📊 Algoritmos Implementados

### Dijkstra (Grafo de Similitud)
- Encuentra las canciones más similares a una canción dada
- Utiliza cola de prioridad para eficiencia
- Complejidad: O((V + E) log V) donde V son canciones y E son aristas

### BFS (Grafo Social)
- Encuentra amigos de amigos para sugerencias
- Calcula grado de separación entre usuarios
- Complejidad: O(V + E) donde V son usuarios y E son conexiones

## 🎨 Interfaz de Usuario

La aplicación utiliza JavaFX con las siguientes vistas:
- **Login**: Autenticación de usuarios y administradores
- **Home**: Recomendaciones y contenido personalizado
- **Search**: Búsqueda con autocompletado
- **Library**: Biblioteca de canciones
- **Favorites**: Canciones favoritas
- **Social**: Red social y conexiones
- **Profile**: Perfil del usuario
- **Admin Panel**: Panel de administración

## 📝 Requisitos Funcionales (RF)

El proyecto implementa los siguientes requisitos funcionales:
- **RF-001**: Registro y autenticación de usuarios
- **RF-002**: Gestión de favoritos
- **RF-003**: Búsqueda con autocompletado (Trie)
- **RF-004**: Búsqueda avanzada por atributos
- **RF-005**: Descubrimiento Semanal
- **RF-006**: Radio personalizada
- **RF-007**: Sistema de seguimiento social
- **RF-008**: Sugerencias de usuarios (BFS)
- **RF-010**: Gestión de catálogo (Admin)
- **RF-011**: Administración de usuarios (Admin)
- **RF-013**: Lista de favoritos (LinkedList)
- **RF-014**: Acceso O(1) a usuarios (HashMap)
- **RF-015**: Identificación única de usuarios
- **RF-016**: Entidad Cancion
- **RF-017**: Características de audio
- **RF-018**: Identificación única de canciones
- **RF-019**: Grafo de similitud
- **RF-020**: Algoritmo de Dijkstra
- **RF-021**: Grafo social
- **RF-022**: BFS para sugerencias
- **RF-023**: Estructura Trie
- **RF-024**: Búsqueda por prefijo

## 🔄 Mejoras Implementadas

- ✅ Optimización del cálculo de similitudes (solo compara canciones del mismo género/artista)
- ✅ Corrección de umbral de similitud para mejores recomendaciones
- ✅ Limpieza de código (eliminación de campos no utilizados)
- ✅ Documentación mejorada

## 🛠️ Tecnologías Utilizadas

- **Java 17**: Lenguaje de programación
- **JavaFX 17.0.11**: Framework de interfaz gráfica
- **Maven**: Gestión de dependencias y construcción
- **Gson**: Serialización/deserialización JSON
- **JUnit 5**: Framework de testing (preparado)

## 📚 Estructura de Datos

### Catálogo de Canciones
- Almacenamiento: `List<Cancion>` + `HashMap<Integer, Cancion>`
- Búsqueda por ID: O(1)
- Búsqueda por atributos: O(n) con filtros

### Usuarios
- Almacenamiento: `HashMap<String, Usuario>` (key: username)
- Acceso: O(1)
- Validación de unicidad: O(1)

## 🤝 Contribución

Este es un proyecto académico desarrollado para demostrar el uso de estructuras de datos avanzadas en una aplicación real.

## 📄 Licencia

Este proyecto es de uso académico.

---

**Desarrollado con ❤️ usando Java y JavaFX**
