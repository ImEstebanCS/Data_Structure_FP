# 📊 Análisis del Proyecto SyncUp - Puntos Clave para Sustentación

## 🎯 1. CONCURRENCIA (Threading y Paralelismo)

### Ubicación Principal
**Archivo**: `SyncUp/src/main/java/co/edu/uniquindio/syncup/Model/Entidades/CatalogoCanciones.java`

### Implementación: Método `busquedaAvanzada()`
**Líneas**: 69-121

#### Detalles Técnicos:
```java
// Línea 76: Lista sincronizada para acceso thread-safe
List<Cancion> resultados = Collections.synchronizedList(new ArrayList<>());

// Línea 78-79: Cálculo de hilos (máximo 4 hilos)
int numHilos = Math.min(4, todasLasCanciones.size());
int cancionesPorHilo = (int) Math.ceil((double) todasLasCanciones.size() / numHilos);

// Línea 81: ExecutorService con pool de hilos fijo
ExecutorService executor = Executors.newFixedThreadPool(numHilos);

// Líneas 84-103: División del trabajo en tareas paralelas
for (int i = 0; i < numHilos; i++) {
    // Cada hilo procesa una sublista de canciones
    Future<?> future = executor.submit(() -> {
        for (Cancion cancion : sublista) {
            if (evaluarCriterios(cancion, artista, genero, año, usarOR)) {
                resultados.add(cancion); // Thread-safe gracias a synchronizedList
            }
        }
    });
    futures.add(future);
}

// Líneas 105-111: Espera a que todos los hilos terminen
for (Future<?> future : futures) {
    future.get(); // Bloquea hasta que el hilo termine
}

// Líneas 113-118: Cierre ordenado del ExecutorService
executor.shutdown();
executor.awaitTermination(5, TimeUnit.SECONDS);
```

### ¿Por qué se usa?
- **RF-027**: Búsqueda avanzada con hilos de ejecución
- **Optimización**: Divide el catálogo en partes y busca en paralelo
- **Rendimiento**: Reduce el tiempo de búsqueda en catálogos grandes

### Conceptos Clave:
1. **ExecutorService**: Gestiona un pool de hilos reutilizables
2. **Future**: Representa el resultado de una tarea asíncrona
3. **Collections.synchronizedList()**: Lista thread-safe para acceso concurrente
4. **awaitTermination()**: Espera a que todos los hilos terminen antes de continuar

---

## 🗺️ 2. HASHMAPS (Estructuras de Datos Map)

### Ubicaciones y Propósitos:

#### A. **SyncUpService.java** - Gestión de Usuarios y Administradores
**Líneas**: 12-13, 20-21
```java
private final Map<String, Usuario> usuarios;           // HashMap<String, Usuario>
private final Map<String, Administrador> administradores; // HashMap<String, Administrador>

// Inicialización
this.usuarios = new HashMap<>();
this.administradores = new HashMap<>();
```
**Propósito**: 
- Acceso O(1) a usuarios por username
- Validación rápida de existencia de usuarios
- **RF-014**: Acceso O(1) a usuarios

#### B. **CatalogoCanciones.java** - Catálogo de Canciones
**Líneas**: 11, 14
```java
private Map<Integer, Cancion> canciones; // HashMap<Integer, Cancion>
this.canciones = new HashMap<>();
```
**Propósito**:
- Acceso O(1) a canciones por ID
- Búsqueda rápida sin recorrer toda la lista
- **RF-018**: Identificación única de canciones

#### C. **GrafoDeSimilitud.java** - Grafo de Similitud
**Líneas**: 12, 15, 23, 77, 129-130
```java
// Estructura principal: Map anidado para representar grafo ponderado
private Map<Cancion, Map<Cancion, Double>> grafo; // HashMap<Cancion, HashMap<Cancion, Double>>

// En Dijkstra:
Map<Cancion, Double> distancias = new HashMap<>();      // Distancias desde origen
Map<Cancion, Cancion> padres = new HashMap<>();         // Para reconstruir rutas
```
**Propósito**:
- Representar grafo ponderado no dirigido
- Almacenar pesos de similitud entre canciones
- Algoritmo Dijkstra para encontrar canciones similares
- **RF-019, RF-020**: Grafo de similitud con Dijkstra

#### D. **GrafoSocial.java** - Grafo Social
**Líneas**: 7, 10, 104
```java
private Map<Usuario, List<Usuario>> adyacencias; // HashMap<Usuario, List<Usuario>>

// En BFS para grados de separación:
Map<Usuario, Integer> distancias = new HashMap<>(); // Distancias en el grafo
```
**Propósito**:
- Representar relaciones sociales (seguidores/seguidos)
- Almacenar adyacencias de usuarios
- BFS para sugerencias y grados de separación
- **RF-021, RF-022**: Grafo social con BFS

#### E. **NodoTrie.java** - Estructura Trie
**Líneas**: 8, 13
```java
private Map<Character, NodoTrie> hijos; // HashMap<Character, NodoTrie>
this.hijos = new HashMap<>();
```
**Propósito**:
- Almacenar hijos de cada nodo en el Trie
- Búsqueda eficiente O(m) donde m es longitud del prefijo
- **RF-023, RF-024**: Estructura Trie para autocompletado

### Resumen de HashMaps:
| Ubicación | Tipo | Clave | Valor | Complejidad | RF |
|-----------|------|-------|-------|--------------|-----|
| SyncUpService | HashMap | String (username) | Usuario | O(1) | RF-014 |
| SyncUpService | HashMap | String (username) | Administrador | O(1) | - |
| CatalogoCanciones | HashMap | Integer (id) | Cancion | O(1) | RF-018 |
| GrafoDeSimilitud | HashMap anidado | Cancion → Cancion | Double (peso) | O(1) acceso | RF-019 |
| GrafoSocial | HashMap | Usuario | List<Usuario> | O(1) acceso | RF-021 |
| NodoTrie | HashMap | Character | NodoTrie | O(1) acceso | RF-023 |

---

## 🏗️ 3. LÓGICA GENERAL DEL PROYECTO

### Arquitectura en Capas

```
┌─────────────────────────────────────────┐
│         VISTA (JavaFX)                  │
│  (FXML + View Controllers)             │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      CONTROLADORES                      │
│  (UsuarioController, CancionController) │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      SERVICIO PRINCIPAL                  │
│      (SyncUpService)                     │
│  ┌──────────────────────────────────┐   │
│  │  • Gestión de usuarios           │   │
│  │  • Gestión de canciones          │   │
│  │  • Lógica de negocio             │   │
│  └──────────────────────────────────┘   │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      MODELO (Estructuras de Datos)      │
│  ┌──────────────────────────────────┐   │
│  │  • CatalogoCanciones (HashMap)   │   │
│  │  • GrafoDeSimilitud (Grafo)      │   │
│  │  • GrafoSocial (Grafo)           │   │
│  │  • TrieAutocompletado (Trie)     │   │
│  │  • Entidades (Usuario, Cancion)  │   │
│  └──────────────────────────────────┘   │
└─────────────────────────────────────────┘
```

### Flujo de Datos Principal

#### 1. **Inicialización del Sistema** (`SyncUpApp.java`)
```
SyncUpApp.start()
    ↓
SyncUpService() (constructor)
    ↓
inicializarDatos()
    ├─→ cargarCancionesIniciales() → CatalogoCanciones
    ├─→ construirGrafoDeSimilitud() → GrafoDeSimilitud
    ├─→ construirTrie() → TrieAutocompletado
    └─→ cargarUsuariosDePrueba() → GrafoSocial
```

#### 2. **Búsqueda de Canciones**
```
Usuario escribe en búsqueda
    ↓
SearchViewController.autocompletar()
    ↓
SyncUpService.autocompletarCanciones()
    ↓
TrieAutocompletado.autocompletar(prefijo)
    ↓
Retorna List<Cancion> con O(m) complejidad
```

#### 3. **Búsqueda Avanzada (CONCURRENCIA)**
```
Usuario selecciona criterios (artista, género, año)
    ↓
CatalogoCanciones.busquedaAvanzada()
    ├─→ Divide canciones en N hilos (máx 4)
    ├─→ Cada hilo evalúa criterios en paralelo
    ├─→ Resultados se agregan a lista sincronizada
    └─→ Retorna resultados combinados
```

#### 4. **Recomendaciones (Grafo de Similitud)**
```
Usuario solicita Descubrimiento Semanal
    ↓
SyncUpService.generarDescubrimientoSemanal()
    ├─→ Obtiene favoritos del usuario
    ├─→ Para cada favorito:
    │   └─→ GrafoDeSimilitud.obtenerCancionesSimilares()
    │       └─→ Dijkstra() encuentra canciones más similares
    └─→ Retorna playlist con recomendaciones
```

#### 5. **Sugerencias Sociales (Grafo Social)**
```
Usuario solicita sugerencias de amigos
    ↓
SyncUpService.obtenerSugerenciasDeUsuarios()
    ↓
GrafoSocial.obtenerSugerenciasDeAmigos()
    ├─→ BFS: Encuentra amigos de amigos
    ├─→ Filtra usuarios ya seguidos
    └─→ Retorna lista de sugerencias
```

### Estructuras de Datos y sus Algoritmos

#### **1. Trie (Autocompletado)**
- **Ubicación**: `Model/Trie/TrieAutocompletado.java`
- **Algoritmo**: Búsqueda por prefijo
- **Complejidad**: O(m) donde m = longitud del prefijo
- **Uso**: Autocompletado en búsqueda de canciones
- **RF**: RF-023, RF-024

#### **2. Grafo de Similitud**
- **Ubicación**: `Model/Grafos/GrafoDeSimilitud.java`
- **Tipo**: Grafo ponderado no dirigido
- **Algoritmo**: Dijkstra
- **Complejidad**: O((V + E) log V)
- **Uso**: Recomendaciones basadas en similitud
- **RF**: RF-019, RF-020
- **Cálculo de Similitud** (`SyncUpService.calcularSimilitud()`):
  - Mismo género: +50 puntos
  - Mismo artista: +30 puntos
  - Años cercanos (≤5 años): +4 puntos por año de diferencia

#### **3. Grafo Social**
- **Ubicación**: `Model/Grafos/GrafoSocial.java`
- **Tipo**: Grafo dirigido (relaciones de seguimiento)
- **Algoritmo**: BFS (Breadth-First Search)
- **Complejidad**: O(V + E)
- **Uso**: Sugerencias de usuarios, grados de separación
- **RF**: RF-021, RF-022
- **Funcionalidades**:
  - `obtenerSugerenciasDeAmigos()`: Encuentra amigos de amigos
  - `estanConectados()`: Verifica si hay camino entre usuarios
  - `obtenerGradoSeparacion()`: Calcula grados de separación (6 grados)

#### **4. HashMap (Acceso Rápido)**
- **Múltiples ubicaciones** (ver sección 2)
- **Complejidad**: O(1) para acceso, inserción, eliminación
- **Uso**: Catálogo de canciones, usuarios, administradores
- **RF**: RF-014, RF-018

### Patrones de Diseño Implementados

1. **Singleton**: `SessionManager`, `NavigationManager`
2. **MVC (Model-View-Controller)**: Separación de capas
3. **Service Layer**: `SyncUpService` centraliza lógica de negocio
4. **Repository Pattern**: Controladores actúan como repositorios

### Requisitos Funcionales (RF) Clave

| RF | Descripción | Implementación |
|----|-------------|----------------|
| RF-014 | Acceso O(1) a usuarios | HashMap<String, Usuario> |
| RF-018 | Identificación única de canciones | HashMap<Integer, Cancion> |
| RF-019 | Grafo de similitud | GrafoDeSimilitud |
| RF-020 | Algoritmo Dijkstra | GrafoDeSimilitud.dijkstra() |
| RF-021 | Grafo social | GrafoSocial |
| RF-022 | BFS para sugerencias | GrafoSocial.obtenerSugerenciasDeAmigos() |
| RF-023 | Estructura Trie | TrieAutocompletado |
| RF-024 | Búsqueda por prefijo | TrieAutocompletado.autocompletar() |
| RF-027 | Búsqueda avanzada con hilos | CatalogoCanciones.busquedaAvanzada() |

---

## 📝 Puntos Clave para Sustentación

### 1. **Concurrencia**
- ✅ **Dónde**: `CatalogoCanciones.busquedaAvanzada()` (líneas 69-121)
- ✅ **Qué usa**: ExecutorService, Future, Collections.synchronizedList()
- ✅ **Por qué**: Optimizar búsquedas en catálogos grandes
- ✅ **Cómo funciona**: Divide el trabajo en 4 hilos máximo, cada uno procesa una porción del catálogo

### 2. **HashMaps**
- ✅ **6 ubicaciones principales** con diferentes propósitos
- ✅ **Complejidad O(1)** para acceso, inserción, eliminación
- ✅ **Usos**: Usuarios, canciones, grafos, Trie

### 3. **Algoritmos Avanzados**
- ✅ **Dijkstra**: Encuentra canciones similares (GrafoDeSimilitud)
- ✅ **BFS**: Sugerencias sociales y grados de separación (GrafoSocial)
- ✅ **Trie**: Autocompletado eficiente O(m)

### 4. **Arquitectura**
- ✅ **MVC**: Separación clara de responsabilidades
- ✅ **Service Layer**: Lógica centralizada en SyncUpService
- ✅ **Estructuras de datos**: Cada una optimizada para su propósito

---

## 🎓 Conceptos para Explicar en Sustentación

1. **¿Por qué HashMap y no ArrayList?**
   - Acceso O(1) vs O(n)
   - Búsqueda por clave (username, ID) es instantánea

2. **¿Por qué concurrencia en búsqueda avanzada?**
   - Catálogos grandes se benefician de procesamiento paralelo
   - Reduce tiempo de respuesta al usuario

3. **¿Cómo funciona Dijkstra en el grafo de similitud?**
   - Encuentra el camino de menor costo (mayor similitud)
   - Usa PriorityQueue para eficiencia
   - Retorna canciones más similares ordenadas

4. **¿Cómo funciona BFS en el grafo social?**
   - Explora el grafo por niveles
   - Encuentra "amigos de amigos" (nivel 2)
   - Calcula grados de separación

5. **¿Por qué Trie para autocompletado?**
   - Complejidad O(m) independiente del tamaño del catálogo
   - Eficiente para búsquedas por prefijo
   - Escalable a millones de canciones

---

**Documento generado para sustentación del proyecto SyncUp**
