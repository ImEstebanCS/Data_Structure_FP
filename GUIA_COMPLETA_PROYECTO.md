# 📚 Guía Completa del Proyecto SyncUp - Documentación para Sustentación

## 🎯 Índice
1. [Concurrencia y Hilos](#1-concurrencia-y-hilos)
2. [HashMaps](#2-hashmaps)
3. [Grafos](#3-grafos)
4. [Trie (Autocompletado)](#4-trie-autocompletado)
5. [Lógica General del Sistema](#5-lógica-general-del-sistema)
6. [Estructuras de Datos](#6-estructuras-de-datos)
7. [Algoritmos Implementados](#7-algoritmos-implementados)

---

## 1. CONCURRENCIA Y HILOS

### 📍 Ubicación
**Archivo:** `SyncUp/src/main/java/co/edu/uniquindio/syncup/Model/Entidades/CatalogoCanciones.java`  
**Método:** `busquedaAvanzada()` (líneas 88-162)  
**Requerimiento:** RF-027

### 🔧 Implementación Detallada

#### ¿Qué es la Concurrencia?
La concurrencia permite ejecutar múltiples tareas simultáneamente, aprovechando múltiples núcleos del procesador para mejorar el rendimiento.

#### ¿Dónde se Implementa?
En el método `busquedaAvanzada()` que busca canciones por múltiples criterios (artista, género, año) usando operadores lógicos AND/OR.

#### Código Completo:
```java
public List<Cancion> busquedaAvanzada(String artista, String genero, int año, boolean usarOR) {
    if (canciones.isEmpty()) {
        return new ArrayList<>();
    }

    // ✅ PASO 1: Crear lista sincronizada (thread-safe)
    // Collections.synchronizedList() garantiza que múltiples hilos puedan
    // agregar elementos sin causar errores de concurrencia
    List<Cancion> resultados = Collections.synchronizedList(new ArrayList<>());

    // ✅ PASO 2: Calcular número de hilos (máximo 4)
    // Divide el trabajo en partes para procesar en paralelo
    int numHilos = Math.min(4, canciones.size());
    int cancionesPorHilo = (int) Math.ceil((double) canciones.size() / numHilos);

    // ✅ PASO 3: Crear ExecutorService (pool de hilos)
    // ExecutorService gestiona un grupo de hilos reutilizables
    ExecutorService executor = Executors.newFixedThreadPool(numHilos);
    List<Future<?>> futures = new ArrayList<>();

    // ✅ PASO 4: Dividir el catálogo en sublistas y asignar a cada hilo
    for (int i = 0; i < numHilos; i++) {
        int inicio = i * cancionesPorHilo;
        int fin = Math.min((i + 1) * cancionesPorHilo, canciones.size());

        if (inicio >= canciones.size()) {
            break;
        }

        // Crear copia real de la sublista (evita problemas de concurrencia)
        final List<Cancion> sublista = new ArrayList<>(canciones.subList(inicio, fin));

        // ✅ PASO 5: Enviar tarea al hilo
        // Future representa el resultado de una tarea asíncrona
        Future<?> future = executor.submit(() -> {
            try {
                // Cada hilo procesa su sublista de canciones
                for (Cancion cancion : sublista) {
                    if (evaluarCriterios(cancion, artista, genero, año, usarOR)) {
                        resultados.add(cancion); // Thread-safe gracias a synchronizedList
                    }
                }
            } catch (Exception e) {
                System.err.println("Error en hilo de búsqueda: " + e.getMessage());
            }
        });

        futures.add(future);
    }

    // ✅ PASO 6: Esperar a que todos los hilos terminen
    // future.get() bloquea hasta que el hilo complete su trabajo
    for (Future<?> future : futures) {
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error en búsqueda concurrente: " + e.getMessage());
        }
    }

    // ✅ PASO 7: Cerrar el ExecutorService correctamente
    executor.shutdown();
    try {
        if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
            executor.shutdownNow(); // Forzar cierre si no termina en 5 segundos
        }
    } catch (InterruptedException e) {
        executor.shutdownNow();
        Thread.currentThread().interrupt();
    }

    return new ArrayList<>(resultados);
}
```

### 🎓 Conceptos Clave

1. **ExecutorService**: Gestiona un pool de hilos reutilizables. Evita crear/destruir hilos constantemente.
2. **Future**: Representa el resultado de una tarea asíncrona. Permite esperar y obtener resultados.
3. **Collections.synchronizedList()**: Lista thread-safe que permite acceso concurrente seguro.
4. **awaitTermination()**: Espera a que todos los hilos terminen antes de continuar.

### 💡 ¿Por qué se Usa?
- **Rendimiento**: En catálogos grandes (1000+ canciones), procesar en paralelo es mucho más rápido.
- **Optimización**: Divide el trabajo en 4 partes máximo, aprovechando múltiples núcleos del CPU.
- **Requerimiento**: RF-027 exige usar hilos para búsqueda avanzada.

### 📊 Ejemplo de Funcionamiento

**Sin Concurrencia (Secuencial):**
```
Hilo 1: [Canciones 1-250] → 2 segundos
Hilo 1: [Canciones 251-500] → 2 segundos
Hilo 1: [Canciones 501-750] → 2 segundos
Hilo 1: [Canciones 751-1000] → 2 segundos
Total: 8 segundos
```

**Con Concurrencia (Paralelo):**
```
Hilo 1: [Canciones 1-250] → 2 segundos
Hilo 2: [Canciones 251-500] → 2 segundos  } Todos en paralelo
Hilo 3: [Canciones 501-750] → 2 segundos  }
Hilo 4: [Canciones 751-1000] → 2 segundos }
Total: 2 segundos (4x más rápido)
```

---

## 2. HASHMAPS

### 📍 Ubicaciones y Propósitos

Los HashMaps proporcionan acceso O(1) (tiempo constante) a los elementos, a diferencia de ArrayList que es O(n).

### 🗺️ Ubicación 1: Gestión de Usuarios (RF-014)

**Archivo:** `SyncUp/src/main/java/co/edu/uniquindio/syncup/Service/SyncUpService.java`  
**Líneas:** 12-13, 20-21

```java
private final Map<String, Usuario> usuarios;           // HashMap<String, Usuario>
private final Map<String, Administrador> administradores; // HashMap<String, Administrador>

// Inicialización
this.usuarios = new HashMap<>();
this.administradores = new HashMap<>();
```

**Propósito:**
- Acceso O(1) a usuarios por username
- Validación rápida de existencia
- **RF-014**: Requiere acceso O(1) a usuarios

**Ejemplo de Uso:**
```java
// O(1) - Instantáneo
Usuario usuario = usuarios.get("maicol");

// vs ArrayList que sería O(n) - Tiene que recorrer toda la lista
for (Usuario u : listaUsuarios) {
    if (u.getUsername().equals("maicol")) {
        return u; // Lento si hay muchos usuarios
    }
}
```

### 🗺️ Ubicación 2: Grafo de Similitud (RF-019)

**Archivo:** `SyncUp/src/main/java/co/edu/uniquindio/syncup/Model/Grafos/GrafoDeSimilitud.java`  
**Líneas:** 12, 15, 23, 77, 129-130

```java
// Estructura principal: HashMap anidado para representar grafo ponderado
private Map<Cancion, Map<Cancion, Double>> grafo;
// Estructura: grafo[cancion1][cancion2] = peso_de_similitud

// En Dijkstra:
Map<Cancion, Double> distancias = new HashMap<>();      // Distancias desde origen
Map<Cancion, Cancion> padres = new HashMap<>();         // Para reconstruir rutas
```

**Propósito:**
- Representar grafo ponderado no dirigido
- Almacenar pesos de similitud entre canciones
- Acceso rápido O(1) a conexiones entre canciones

**Estructura del Grafo:**
```
Cancion1 → {Cancion2: 50.0, Cancion3: 30.0}
Cancion2 → {Cancion1: 50.0, Cancion4: 20.0}
Cancion3 → {Cancion1: 30.0}
```

### 🗺️ Ubicación 3: Grafo Social (RF-021)

**Archivo:** `SyncUp/src/main/java/co/edu/uniquindio/syncup/Model/Grafos/GrafoSocial.java`  
**Líneas:** 7, 10, 104

```java
private Map<Usuario, List<Usuario>> adyacencias; // HashMap<Usuario, List<Usuario>>

// En BFS para grados de separación:
Map<Usuario, Integer> distancias = new HashMap<>(); // Distancias en el grafo
```

**Propósito:**
- Representar relaciones sociales (seguidores/seguidos)
- Almacenar adyacencias de usuarios
- Acceso rápido O(1) a conexiones sociales

**Estructura del Grafo Social:**
```
Usuario1 → [Usuario2, Usuario3]  // Usuario1 sigue a Usuario2 y Usuario3
Usuario2 → [Usuario4]
Usuario3 → [Usuario5, Usuario6]
```

### 🗺️ Ubicación 4: Trie (Autocompletado) (RF-023)

**Archivo:** `SyncUp/src/main/java/co/edu/uniquindio/syncup/Model/Trie/NodoTrie.java`  
**Líneas:** 8, 13

```java
private Map<Character, NodoTrie> hijos; // HashMap<Character, NodoTrie>
this.hijos = new HashMap<>();
```

**Propósito:**
- Almacenar hijos de cada nodo en el Trie
- Acceso O(1) a cada carácter hijo
- Búsqueda eficiente O(m) donde m es longitud del prefijo

**Estructura del Trie:**
```
Raíz
 ├─ 'B' → Nodo
 │   ├─ 'o' → Nodo
 │   │   └─ 'h' → Nodo (fin: "Boh")
 ├─ 'Q' → Nodo
 │   └─ 'u' → Nodo
 │       └─ 'e' → Nodo (fin: "Que")
```

### 📊 Resumen de HashMaps

| Ubicación | Tipo | Clave | Valor | Complejidad | RF | Propósito |
|-----------|------|-------|-------|-------------|-----|-----------|
| **SyncUpService** | HashMap | String (username) | Usuario | O(1) | RF-014 | Acceso rápido a usuarios |
| **SyncUpService** | HashMap | String (username) | Administrador | O(1) | - | Acceso rápido a admins |
| **GrafoDeSimilitud** | HashMap anidado | Cancion → Cancion | Double (peso) | O(1) | RF-019 | Grafo ponderado |
| **GrafoSocial** | HashMap | Usuario | List<Usuario> | O(1) | RF-021 | Relaciones sociales |
| **NodoTrie** | HashMap | Character | NodoTrie | O(1) | RF-023 | Estructura Trie |

### 💡 ¿Por qué HashMap y no ArrayList?

**HashMap:**
- Acceso por clave: O(1) - Instantáneo
- Búsqueda: O(1) - Instantáneo
- Inserción: O(1) - Instantáneo

**ArrayList:**
- Acceso por índice: O(1) - Pero necesitas saber el índice
- Búsqueda: O(n) - Tiene que recorrer toda la lista
- Inserción: O(1) al final, O(n) en medio

**Ejemplo Práctico:**
```java
// Con HashMap: O(1)
Usuario usuario = usuarios.get("maicol"); // Instantáneo

// Con ArrayList: O(n)
Usuario usuario = null;
for (Usuario u : listaUsuarios) { // Recorre toda la lista
    if (u.getUsername().equals("maicol")) {
        usuario = u;
        break;
    }
}
```

---

## 3. GRAFOS

### 📊 Grafo de Similitud (RF-019, RF-020)

#### 📍 Ubicación
**Archivo:** `SyncUp/src/main/java/co/edu/uniquindio/syncup/Model/Grafos/GrafoDeSimilitud.java`

#### 🎯 ¿Qué es?
Un **grafo ponderado no dirigido** que conecta canciones basándose en su similitud. Las aristas tienen pesos que representan qué tan similares son dos canciones.

#### 🔧 Estructura
```java
private Map<Cancion, Map<Cancion, Double>> grafo;
// Estructura: grafo[cancion1][cancion2] = peso_de_similitud
```

**Ejemplo Visual:**
```
        [Cancion A: "Bohemian Rhapsody"]
              / 50.0  \
             /         \
    [Cancion B] 30.0  [Cancion C]
    "Hotel CA"         "Stairway"
```

#### 🧮 Cálculo de Similitud
**Ubicación:** `SyncUpService.calcularSimilitud()`

```java
private int calcularSimilitud(Cancion c1, Cancion c2) {
    int similitud = 0;
    
    // Mismo género: +50 puntos
    if (c1.getGenero().equals(c2.getGenero())) {
        similitud += 50;
    }
    
    // Mismo artista: +30 puntos
    if (c1.getArtista().equals(c2.getArtista())) {
        similitud += 30;
    }
    
    // Años cercanos (≤5 años): +4 puntos por año de diferencia
    int diferenciaAños = Math.abs(c1.getAño() - c2.getAño());
    if (diferenciaAños <= 5) {
        similitud += (5 - diferenciaAños) * 4;
    }
    
    return similitud; // Menor valor = más similar
}
```

#### 🚀 Algoritmo Dijkstra (RF-020)

**¿Qué hace?** Encuentra las canciones más similares a una canción dada, encontrando el camino de menor costo (mayor similitud) en el grafo.

**Implementación:**
```java
private Map<Cancion, Double> dijkstra(Cancion inicio) {
    Map<Cancion, Double> distancias = new HashMap<>();
    PriorityQueue<Map.Entry<Cancion, Double>> cola = new PriorityQueue<>(
        Comparator.comparingDouble(Map.Entry::getValue)
    );
    
    // Inicializar todas las distancias como infinito
    for (Cancion cancion : grafo.keySet()) {
        distancias.put(cancion, Double.MAX_VALUE);
    }
    
    // La distancia al nodo inicial es 0
    distancias.put(inicio, 0.0);
    cola.offer(new AbstractMap.SimpleEntry<>(inicio, 0.0));
    
    while (!cola.isEmpty()) {
        Map.Entry<Cancion, Double> actual = cola.poll();
        Cancion cancionActual = actual.getKey();
        double distActual = actual.getValue();
        
        // Si encontramos una distancia mayor, la ignoramos
        if (distActual > distancias.get(cancionActual)) {
            continue;
        }
        
        // Explorar vecinos (canciones conectadas)
        Map<Cancion, Double> vecinos = grafo.get(cancionActual);
        if (vecinos != null) {
            for (Map.Entry<Cancion, Double> vecino : vecinos.entrySet()) {
                Cancion cancionVecina = vecino.getKey();
                double peso = vecino.getValue();
                double nuevaDistancia = distActual + peso;
                
                // Si encontramos un camino más corto, actualizamos
                if (nuevaDistancia < distancias.get(cancionVecina)) {
                    distancias.put(cancionVecina, nuevaDistancia);
                    cola.offer(new AbstractMap.SimpleEntry<>(cancionVecina, nuevaDistancia));
                }
            }
        }
    }
    
    return distancias;
}
```

**Complejidad:** O((V + E) log V) donde V = canciones, E = conexiones

**Uso:**
- **Descubrimiento Semanal (RF-005)**: Encuentra canciones similares a los favoritos del usuario
- **Radio Personalizada (RF-006)**: Genera cola de reproducción basada en similitud

---

### 👥 Grafo Social (RF-021, RF-022)

#### 📍 Ubicación
**Archivo:** `SyncUp/src/main/java/co/edu/uniquindio/syncup/Model/Grafos/GrafoSocial.java`

#### 🎯 ¿Qué es?
Un **grafo dirigido** que modela las relaciones sociales entre usuarios (quién sigue a quién).

#### 🔧 Estructura
```java
private Map<Usuario, List<Usuario>> adyacencias;
// Estructura: adyacencias[usuario1] = [usuario2, usuario3, ...]
```

**Ejemplo Visual:**
```
    [Usuario A] ──sigue──> [Usuario B] ──sigue──> [Usuario D]
         │                      │
         │                      └──sigue──> [Usuario C]
         │
         └──sigue──> [Usuario E]
```

#### 🚀 Algoritmo BFS (Breadth-First Search) (RF-022)

**¿Qué hace?** Encuentra "amigos de amigos" (usuarios a 2 grados de separación) para sugerencias.

**Implementación para Sugerencias:**
```java
public List<Usuario> obtenerSugerenciasDeAmigos(Usuario usuario, int limite) {
    Set<Usuario> sugerencias = new HashSet<>();
    List<Usuario> siguiendo = obtenerSiguiendo(usuario); // Nivel 1
    
    // Para cada amigo directo (nivel 1)
    for (Usuario amigo : siguiendo) {
        List<Usuario> amigosDeAmigos = obtenerSiguiendo(amigo); // Nivel 2
        
        // Agregar amigos de amigos que el usuario no sigue
        for (Usuario sugerido : amigosDeAmigos) {
            if (!sugerido.equals(usuario) && !siguiendo.contains(sugerido)) {
                sugerencias.add(sugerido);
            }
        }
    }
    
    return resultado.subList(0, Math.min(limite, resultado.size()));
}
```

**Implementación para Grados de Separación:**
```java
public int obtenerGradoSeparacion(Usuario usuario1, Usuario usuario2) {
    if (usuario1.equals(usuario2)) {
        return 0; // Mismo usuario
    }
    
    Map<Usuario, Integer> distancias = new HashMap<>();
    Queue<Usuario> cola = new LinkedList<>();
    
    cola.add(usuario1);
    distancias.put(usuario1, 0);
    
    while (!cola.isEmpty()) {
        Usuario actual = cola.poll();
        int distanciaActual = distancias.get(actual);
        
        if (actual.equals(usuario2)) {
            return distanciaActual; // Encontramos el destino
        }
        
        // Explorar vecinos (usuarios seguidos)
        List<Usuario> vecinos = adyacencias.getOrDefault(actual, new ArrayList<>());
        for (Usuario vecino : vecinos) {
            if (!distancias.containsKey(vecino)) {
                distancias.put(vecino, distanciaActual + 1);
                cola.add(vecino);
            }
        }
    }
    
    return -1; // No hay conexión
}
```

**Complejidad:** O(V + E) donde V = usuarios, E = conexiones

**Uso:**
- **Sugerencias de Usuarios (RF-008)**: Encuentra amigos de amigos
- **Grados de Separación**: Calcula distancia entre usuarios (concepto de "6 grados de separación")

---

## 4. TRIE (AUTOCOMPLETADO)

### 📍 Ubicación
**Archivo:** `SyncUp/src/main/java/co/edu/uniquindio/syncup/Model/Trie/TrieAutocompletado.java`  
**Requerimientos:** RF-023, RF-024

### 🎯 ¿Qué es?
Un **Árbol de Prefijos (Trie)** es una estructura de datos que permite búsqueda eficiente por prefijo. Ideal para autocompletado.

### 🔧 Estructura

**NodoTrie:**
```java
public class NodoTrie {
    private Map<Character, NodoTrie> hijos; // HashMap<Character, NodoTrie>
    private boolean esFinDePalabra;
    private Cancion cancion;
}
```

**Ejemplo Visual del Trie:**
```
        Raíz
       /    \
      B      Q
     /        \
    o          u
   /            \
  h              e
 /                \
e (fin)           e (fin)
"Bohe"            "Quee"
```

### 🚀 Funcionamiento

#### 1. Inserción
```java
public void insertar(String palabra, Cancion cancion) {
    NodoTrie nodo = raiz;
    palabra = palabra.toLowerCase();
    
    // Recorrer cada carácter
    for (char c : palabra.toCharArray()) {
        // Si no existe el hijo, crearlo
        nodo.getHijos().putIfAbsent(c, new NodoTrie());
        nodo = nodo.getHijos().get(c);
    }
    
    // Marcar como fin de palabra y guardar canción
    nodo.setEsFinDePalabra(true);
    nodo.setCancion(cancion);
}
```

#### 2. Búsqueda por Prefijo (RF-024)
```java
public List<Cancion> autocompletar(String prefijo) {
    List<Cancion> resultados = new ArrayList<>();
    NodoTrie nodo = raiz;
    prefijo = prefijo.toLowerCase();
    
    // Navegar hasta el prefijo
    for (char c : prefijo.toCharArray()) {
        if (!nodo.getHijos().containsKey(c)) {
            return resultados; // No hay palabras con ese prefijo
        }
        nodo = nodo.getHijos().get(c);
    }
    
    // Buscar todas las palabras que comienzan con el prefijo
    buscarTodasLasCanciones(nodo, resultados);
    return resultados;
}

private void buscarTodasLasCanciones(NodoTrie nodo, List<Cancion> resultados) {
    // Si es fin de palabra, agregar la canción
    if (nodo.isEsFinDePalabra() && nodo.getCancion() != null) {
        resultados.add(nodo.getCancion());
    }
    
    // Recursivamente buscar en todos los hijos
    for (NodoTrie hijo : nodo.getHijos().values()) {
        buscarTodasLasCanciones(hijo, resultados);
    }
}
```

### 💡 Ventajas del Trie

**Complejidad:**
- Búsqueda por prefijo: **O(m)** donde m = longitud del prefijo
- Independiente del tamaño del catálogo
- Escalable a millones de canciones

**Comparación:**
```
ArrayList: O(n) - Tiene que recorrer todas las canciones
Trie: O(m) - Solo recorre el prefijo (ej: "Bo" = 2 pasos)
```

**Uso:**
- **Búsqueda con Autocompletado (RF-003)**: Mientras el usuario escribe, sugiere canciones

---

## 5. LÓGICA GENERAL DEL SISTEMA

### 🏗️ Arquitectura en Capas

```
┌─────────────────────────────────────────┐
│         VISTA (JavaFX)                  │
│  (FXML + View Controllers)             │
│  - LoginViewController                 │
│  - MainViewController                  │
│  - SearchViewController                │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      CONTROLADORES                      │
│  (Lógica de Negocio)                    │
│  - UsuarioController                    │
│  - CancionController                    │
│  - PlaylistController                   │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      SERVICIO PRINCIPAL                  │
│      (SyncUpService)                     │
│  ┌──────────────────────────────────┐   │
│  │  • Gestión de usuarios           │   │
│  │  • Gestión de canciones          │   │
│  │  • Lógica de negocio             │   │
│  │  • Coordinación de estructuras   │   │
│  └──────────────────────────────────┘   │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      MODELO (Estructuras de Datos)      │
│  ┌──────────────────────────────────┐   │
│  │  • CatalogoCanciones (List)      │   │
│  │  • GrafoDeSimilitud (Grafo)      │   │
│  │  • GrafoSocial (Grafo)           │   │
│  │  • TrieAutocompletado (Trie)     │   │
│  │  • Entidades (Usuario, Cancion)  │   │
│  └──────────────────────────────────┘   │
└─────────────────────────────────────────┘
```

### 🔄 Flujos Principales

#### 1. Inicialización del Sistema
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

#### 2. Búsqueda de Canciones
```
Usuario escribe "Bo" en búsqueda
    ↓
SearchViewController.autocompletar("Bo")
    ↓
CancionController.autocompletar("Bo")
    ↓
SyncUpService.autocompletarCanciones("Bo")
    ↓
TrieAutocompletado.autocompletar("Bo")
    ↓
Retorna: ["Bohemian Rhapsody", "Boogie Wonderland", ...]
Complejidad: O(2) - Solo 2 pasos (B → o)
```

#### 3. Búsqueda Avanzada (CONCURRENCIA)
```
Usuario selecciona: Artista="Queen", Género="Rock", Año=1975
    ↓
CatalogoCanciones.busquedaAvanzada()
    ├─→ Divide canciones en 4 hilos
    ├─→ Hilo 1: Procesa canciones 1-250
    ├─→ Hilo 2: Procesa canciones 251-500
    ├─→ Hilo 3: Procesa canciones 501-750
    └─→ Hilo 4: Procesa canciones 751-1000
    ↓
Todos los hilos evalúan criterios en paralelo
    ↓
Resultados combinados en lista sincronizada
    ↓
Retorna: Lista filtrada de canciones
```

#### 4. Recomendaciones (Grafo de Similitud)
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

#### 5. Sugerencias Sociales (Grafo Social)
```
Usuario solicita sugerencias de amigos
    ↓
SyncUpService.obtenerSugerenciasDeUsuarios()
    ↓
GrafoSocial.obtenerSugerenciasDeAmigos()
    ├─→ BFS: Encuentra amigos de amigos (nivel 2)
    ├─→ Filtra usuarios ya seguidos
    └─→ Retorna lista de sugerencias
```

---

## 6. ESTRUCTURAS DE DATOS

### 📚 Resumen Completo

| Estructura | Ubicación | Propósito | Complejidad | RF |
|------------|-----------|-----------|-------------|-----|
| **HashMap<String, Usuario>** | SyncUpService | Usuarios indexados | O(1) acceso | RF-014 |
| **HashMap<String, Administrador>** | SyncUpService | Administradores | O(1) acceso | - |
| **List<Cancion>** | CatalogoCanciones | Catálogo de canciones | O(n) búsqueda | - |
| **HashMap<Cancion, Map<Cancion, Double>>** | GrafoDeSimilitud | Grafo ponderado | O(1) acceso | RF-019 |
| **HashMap<Usuario, List<Usuario>>** | GrafoSocial | Grafo social | O(1) acceso | RF-021 |
| **HashMap<Character, NodoTrie>** | NodoTrie | Estructura Trie | O(1) acceso | RF-023 |
| **LinkedList<Cancion>** | Playlist | Favoritos del usuario | O(1) inserción | RF-013 |

### 🔑 Características Clave

1. **HashMap para Usuarios (RF-014)**
   - Acceso O(1) por username
   - Validación rápida de existencia
   - Implementado en `SyncUpService`

2. **LinkedList para Favoritos (RF-013)**
   - Implementado dentro de `Playlist`
   - `Usuario.getListaFavoritos()` retorna `LinkedList<Cancion>`
   - O(1) para agregar/eliminar al final

3. **Grafo Ponderado para Similitud (RF-019)**
   - HashMap anidado: `Map<Cancion, Map<Cancion, Double>>`
   - Representa conexiones entre canciones con pesos

4. **Grafo Dirigido para Social (RF-021)**
   - HashMap: `Map<Usuario, List<Usuario>>`
   - Representa relaciones de seguimiento

5. **Trie para Autocompletado (RF-023)**
   - HashMap en cada nodo: `Map<Character, NodoTrie>`
   - Búsqueda O(m) independiente del tamaño

---

## 7. ALGORITMOS IMPLEMENTADOS

### 🎯 Resumen de Algoritmos

| Algoritmo | Ubicación | Propósito | Complejidad | RF |
|-----------|-----------|-----------|-------------|-----|
| **Dijkstra** | GrafoDeSimilitud | Canciones similares | O((V+E)log V) | RF-020 |
| **BFS** | GrafoSocial | Sugerencias sociales | O(V + E) | RF-022 |
| **Trie Search** | TrieAutocompletado | Autocompletado | O(m) | RF-024 |
| **Búsqueda Concurrente** | CatalogoCanciones | Búsqueda avanzada | O(n/p) | RF-027 |

### 📊 Comparación de Complejidades

**Búsqueda de Usuario:**
- Con HashMap: **O(1)** - Instantáneo
- Con ArrayList: **O(n)** - Recorre toda la lista

**Búsqueda por Prefijo:**
- Con Trie: **O(m)** - Solo recorre el prefijo
- Con ArrayList: **O(n)** - Recorre todas las canciones

**Recomendaciones:**
- Con Dijkstra: **O((V+E)log V)** - Eficiente para grafos
- Sin grafo: **O(n²)** - Comparar todas con todas

**Sugerencias Sociales:**
- Con BFS: **O(V + E)** - Explora por niveles
- Sin grafo: **O(n²)** - Comparar todos los usuarios

---

## 🎓 PUNTOS CLAVE PARA SUSTENTACIÓN

### 1. Concurrencia (RF-027)
- ✅ **Dónde**: `CatalogoCanciones.busquedaAvanzada()`
- ✅ **Qué usa**: ExecutorService, Future, Collections.synchronizedList()
- ✅ **Por qué**: Optimiza búsquedas en catálogos grandes
- ✅ **Cómo**: Divide trabajo en 4 hilos máximo, procesa en paralelo

### 2. HashMaps
- ✅ **6 ubicaciones principales** con diferentes propósitos
- ✅ **Complejidad O(1)** para acceso, inserción, eliminación
- ✅ **Usos**: Usuarios, canciones, grafos, Trie

### 3. Grafos
- ✅ **Grafo de Similitud**: Dijkstra para recomendaciones
- ✅ **Grafo Social**: BFS para sugerencias y grados de separación

### 4. Trie
- ✅ **Autocompletado eficiente**: O(m) independiente del tamaño
- ✅ **Escalable**: Funciona con millones de canciones

### 5. Arquitectura
- ✅ **MVC**: Separación clara de responsabilidades
- ✅ **Service Layer**: Lógica centralizada
- ✅ **Estructuras optimizadas**: Cada una para su propósito

---

## 📝 EJEMPLOS PRÁCTICOS PARA EXPLICAR

### Ejemplo 1: ¿Por qué HashMap para usuarios?
```
Sin HashMap (ArrayList):
- 1000 usuarios
- Buscar "maicol": Recorre hasta 1000 elementos = O(1000)

Con HashMap:
- 1000 usuarios
- Buscar "maicol": Acceso directo = O(1)
```

### Ejemplo 2: ¿Por qué concurrencia en búsqueda?
```
Sin concurrencia:
- 1000 canciones
- 1 hilo procesa todo: 10 segundos

Con concurrencia (4 hilos):
- 1000 canciones divididas en 4 partes
- 4 hilos procesan en paralelo: 2.5 segundos
- 4x más rápido
```

### Ejemplo 3: ¿Cómo funciona Dijkstra?
```
Canción origen: "Bohemian Rhapsody"
Grafo:
  Bohemian → Hotel CA (peso: 50)
  Bohemian → Stairway (peso: 30)
  Hotel CA → Stairway (peso: 20)

Dijkstra encuentra:
  1. Stairway (distancia: 30) - Más similar
  2. Hotel CA (distancia: 50) - Menos similar
```

### Ejemplo 4: ¿Cómo funciona BFS?
```
Usuario A sigue a B y C
Usuario B sigue a D
Usuario C sigue a E

BFS encuentra:
  Nivel 1: B, C (amigos directos)
  Nivel 2: D, E (amigos de amigos) ← Sugerencias
```

---

**Documento generado para sustentación del proyecto SyncUp**  
**Última actualización:** Basado en requerimientos RF-001 a RF-029
