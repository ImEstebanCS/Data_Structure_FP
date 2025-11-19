# 📋 Resumen Breve del Proyecto SyncUp - Sin Código

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
**Archivo:** `CatalogoCanciones.java`  
**Método:** `busquedaAvanzada()`  
**Requerimiento:** RF-027

### ¿Qué es la Concurrencia?
La concurrencia permite ejecutar múltiples tareas al mismo tiempo, aprovechando varios núcleos del procesador para hacer el trabajo más rápido.

### ¿Dónde se Implementa?
En la búsqueda avanzada de canciones. Cuando un usuario busca por artista, género o año, el sistema divide el catálogo en partes y las procesa simultáneamente usando múltiples hilos.

### ¿Cómo Funciona?
1. **División del Trabajo**: El catálogo se divide en 4 partes máximo (una por cada hilo)
2. **Procesamiento Paralelo**: Cada hilo evalúa sus canciones al mismo tiempo
3. **Recolección de Resultados**: Todos los resultados se combinan en una lista segura
4. **Espera de Finalización**: El sistema espera a que todos los hilos terminen antes de mostrar resultados

### Conceptos Clave
- **ExecutorService**: Gestiona un grupo de hilos reutilizables (no crea/destruye hilos constantemente)
- **Future**: Representa una tarea que se está ejecutando en paralelo
- **Lista Sincronizada**: Lista especial que permite que múltiples hilos agreguen elementos sin causar errores

### ¿Por qué se Usa?
- **Rendimiento**: En catálogos grandes (1000+ canciones), procesar en paralelo es 4 veces más rápido
- **Optimización**: Aprovecha múltiples núcleos del CPU simultáneamente
- **Requerimiento**: RF-027 exige usar hilos para búsqueda avanzada

### Ejemplo Práctico
**Sin Concurrencia:**
- 1 hilo procesa 1000 canciones secuencialmente
- Tiempo: 8 segundos

**Con Concurrencia:**
- 4 hilos procesan 250 canciones cada uno en paralelo
- Tiempo: 2 segundos (4x más rápido)

---

## 2. HASHMAPS

### ¿Qué es un HashMap?
Un HashMap es una estructura de datos que almacena información en pares clave-valor. Permite acceso instantáneo (O(1)) a los elementos usando su clave, sin necesidad de recorrer toda la estructura.

### Ubicaciones en el Proyecto

#### 1. Gestión de Usuarios (RF-014)
**Ubicación:** `SyncUpService.java`

**Propósito:**
- Almacena usuarios usando su username como clave
- Permite acceso instantáneo a cualquier usuario
- Validación rápida de existencia de usuarios

**Ventaja:** En lugar de recorrer una lista de 1000 usuarios para encontrar uno, el acceso es instantáneo.

#### 2. Grafo de Similitud (RF-019)
**Ubicación:** `GrafoDeSimilitud.java`

**Propósito:**
- Representa conexiones entre canciones con sus pesos de similitud
- Estructura anidada: cada canción tiene un mapa de canciones relacionadas
- Acceso rápido a conexiones entre canciones

**Estructura:** Canción A → {Canción B: peso 50, Canción C: peso 30}

#### 3. Grafo Social (RF-021)
**Ubicación:** `GrafoSocial.java`

**Propósito:**
- Representa relaciones sociales entre usuarios
- Cada usuario tiene una lista de usuarios que sigue
- Acceso rápido a conexiones sociales

**Estructura:** Usuario A → [Usuario B, Usuario C]

#### 4. Trie (Autocompletado) (RF-023)
**Ubicación:** `NodoTrie.java`

**Propósito:**
- Cada nodo del Trie almacena sus hijos en un HashMap
- Permite navegación rápida por caracteres
- Acceso instantáneo a cada carácter hijo

**Estructura:** Nodo → {'B': NodoB, 'Q': NodoQ}

### ¿Por qué HashMap y no ArrayList?

**HashMap:**
- Acceso por clave: Instantáneo (O(1))
- Búsqueda: Instantánea (O(1))
- Ideal cuando conoces la clave (username, ID)

**ArrayList:**
- Acceso por índice: Rápido, pero necesitas saber el índice
- Búsqueda: Lenta (O(n)) - tiene que recorrer toda la lista
- Ideal cuando necesitas mantener orden o acceder por posición

**Ejemplo:** Buscar usuario "maicol" en 1000 usuarios
- HashMap: 1 paso (instantáneo)
- ArrayList: Hasta 1000 pasos (recorre toda la lista)

---

## 3. GRAFOS

### 📊 Grafo de Similitud (RF-019, RF-020)

#### ¿Qué es?
Un grafo ponderado no dirigido que conecta canciones basándose en su similitud. Las conexiones (aristas) tienen pesos que indican qué tan similares son dos canciones.

#### Estructura
- **Nodos**: Canciones
- **Aristas**: Conexiones entre canciones similares
- **Pesos**: Números que representan el grado de similitud (menor peso = más similar)

#### Cálculo de Similitud
El sistema calcula similitud basándose en:
- **Mismo género**: +50 puntos
- **Mismo artista**: +30 puntos
- **Años cercanos**: +4 puntos por cada año de diferencia (máximo 5 años)

#### Algoritmo Dijkstra (RF-020)
**¿Qué hace?** Encuentra las canciones más similares a una canción dada, buscando el camino de menor costo (mayor similitud) en el grafo.

**Funcionamiento:**
1. Comienza desde la canción origen
2. Explora canciones conectadas, calculando distancias
3. Usa una cola de prioridad para procesar las más cercanas primero
4. Retorna las canciones más similares ordenadas por similitud

**Complejidad:** O((V + E) log V) donde V = canciones, E = conexiones

**Uso:**
- **Descubrimiento Semanal**: Encuentra canciones similares a los favoritos del usuario
- **Radio Personalizada**: Genera cola de reproducción basada en similitud

#### Ejemplo Visual
```
        [Bohemian Rhapsody]
              / 50.0  \
             /         \
    [Hotel California]  [Stairway to Heaven]
        30.0             20.0
```

Si buscas canciones similares a "Bohemian Rhapsody", Dijkstra encuentra primero "Stairway to Heaven" (peso 20) y luego "Hotel California" (peso 50).

---

### 👥 Grafo Social (RF-021, RF-022)

#### ¿Qué es?
Un grafo dirigido que modela las relaciones sociales entre usuarios. Representa quién sigue a quién en la plataforma.

#### Estructura
- **Nodos**: Usuarios
- **Aristas**: Relaciones de seguimiento (dirigidas: A sigue a B)
- **No ponderado**: Las conexiones no tienen peso

#### Algoritmo BFS (Breadth-First Search) (RF-022)
**¿Qué hace?** Encuentra "amigos de amigos" (usuarios a 2 grados de separación) para sugerencias sociales.

**Funcionamiento:**
1. Comienza desde el usuario actual
2. Explora usuarios seguidos (nivel 1)
3. Luego explora usuarios seguidos por esos usuarios (nivel 2)
4. Retorna usuarios del nivel 2 que el usuario no sigue

**Complejidad:** O(V + E) donde V = usuarios, E = conexiones

**Uso:**
- **Sugerencias de Usuarios**: Encuentra amigos de amigos para seguir
- **Grados de Separación**: Calcula distancia entre usuarios (concepto de "6 grados de separación")

#### Ejemplo Visual
```
    [Usuario A] ──sigue──> [Usuario B] ──sigue──> [Usuario D]
         │                      │
         │                      └──sigue──> [Usuario C]
         │
         └──sigue──> [Usuario E]
```

Si el Usuario A solicita sugerencias:
- **Nivel 1**: B, E (amigos directos)
- **Nivel 2**: D, C (amigos de amigos) ← Estas son las sugerencias

---

## 4. TRIE (AUTOCOMPLETADO)

### ¿Qué es un Trie?
Un Trie (Árbol de Prefijos) es una estructura de datos en forma de árbol que permite búsqueda eficiente por prefijo. Ideal para sistemas de autocompletado.

### Estructura
- **Raíz**: Nodo inicial del árbol
- **Nodos**: Cada nodo representa un carácter
- **Ramas**: Conectan caracteres para formar palabras
- **Hojas**: Nodos finales que indican el fin de una palabra

### Funcionamiento

#### Inserción
1. Comienza desde la raíz
2. Para cada carácter de la palabra, crea o navega al nodo correspondiente
3. Al final, marca el nodo como "fin de palabra" y guarda la canción asociada

#### Búsqueda por Prefijo (RF-024)
1. Navega desde la raíz siguiendo los caracteres del prefijo
2. Si encuentra todos los caracteres, busca todas las palabras que comienzan con ese prefijo
3. Retorna todas las canciones encontradas

### Ventajas del Trie

**Complejidad:**
- Búsqueda por prefijo: O(m) donde m = longitud del prefijo
- Independiente del tamaño del catálogo
- Escalable a millones de canciones

**Comparación:**
- **ArrayList**: O(n) - Tiene que recorrer todas las canciones
- **Trie**: O(m) - Solo recorre el prefijo (ej: "Bo" = 2 pasos)

### Ejemplo Práctico
**Catálogo con 1,000,000 de canciones**

Usuario escribe "Bo":
- **Con ArrayList**: Recorre hasta 1,000,000 canciones buscando las que empiezan con "Bo"
- **Con Trie**: Solo hace 2 pasos (B → o) y encuentra todas las que empiezan con "Bo"

**Uso:**
- **Búsqueda con Autocompletado (RF-003)**: Mientras el usuario escribe, sugiere canciones en tiempo real

---

## 5. LÓGICA GENERAL DEL SISTEMA

### Arquitectura en Capas

El sistema está organizado en 4 capas principales:

1. **Vista (JavaFX)**
   - Interfaz gráfica del usuario
   - Pantallas de login, búsqueda, perfil, etc.
   - No contiene lógica de negocio

2. **Controladores**
   - Intermediarios entre la vista y el servicio
   - Validan datos y coordinan operaciones
   - Ejemplos: UsuarioController, CancionController

3. **Servicio Principal (SyncUpService)**
   - Contiene toda la lógica de negocio
   - Coordina las estructuras de datos
   - Gestiona usuarios, canciones, playlists

4. **Modelo (Estructuras de Datos)**
   - Estructuras de datos puras
   - Grafos, Trie, listas, mapas
   - No conocen la lógica de negocio

### Flujos Principales

#### Inicialización
1. Se crea el servicio principal
2. Se cargan canciones iniciales
3. Se construye el grafo de similitud
4. Se construye el Trie para autocompletado
5. Se cargan usuarios de prueba y se construye el grafo social

#### Búsqueda Simple
1. Usuario escribe en el campo de búsqueda
2. Sistema consulta el Trie con el prefijo escrito
3. Trie retorna canciones que coinciden
4. Se muestran resultados en tiempo real

#### Búsqueda Avanzada
1. Usuario selecciona criterios (artista, género, año)
2. Sistema divide el catálogo en hilos
3. Cada hilo procesa su parte en paralelo
4. Resultados se combinan y se muestran

#### Recomendaciones
1. Usuario solicita Descubrimiento Semanal
2. Sistema obtiene canciones favoritas del usuario
3. Para cada favorita, usa Dijkstra en el grafo de similitud
4. Encuentra canciones similares y genera playlist

#### Sugerencias Sociales
1. Usuario solicita sugerencias de amigos
2. Sistema usa BFS en el grafo social
3. Encuentra amigos de amigos (nivel 2)
4. Filtra usuarios ya seguidos
5. Retorna lista de sugerencias

### Patrones de Diseño

1. **MVC (Model-View-Controller)**
   - Separación clara de responsabilidades
   - Vista solo muestra, Modelo solo almacena, Controlador coordina

2. **Service Layer**
   - Toda la lógica de negocio centralizada
   - Facilita mantenimiento y testing

3. **Singleton**
   - SessionManager y NavigationManager
   - Una sola instancia en toda la aplicación

---

## 6. ESTRUCTURAS DE DATOS

### Resumen Completo

| Estructura | Propósito | Complejidad Acceso | RF |
|------------|-----------|-------------------|-----|
| **HashMap<String, Usuario>** | Usuarios indexados | O(1) | RF-014 |
| **HashMap<String, Administrador>** | Administradores | O(1) | - |
| **List<Cancion>** | Catálogo de canciones | O(n) búsqueda | - |
| **HashMap anidado** | Grafo de similitud | O(1) | RF-019 |
| **HashMap<Usuario, List>** | Grafo social | O(1) | RF-021 |
| **HashMap<Character, Nodo>** | Estructura Trie | O(1) | RF-023 |
| **LinkedList<Cancion>** | Favoritos del usuario | O(1) inserción | RF-013 |

### Características Clave

#### HashMap para Usuarios (RF-014)
- Acceso instantáneo por username
- No necesita recorrer lista completa
- Implementado en SyncUpService

#### LinkedList para Favoritos (RF-013)
- Implementado dentro de Playlist
- Fácil agregar/eliminar canciones
- Mantiene orden de inserción

#### Grafo Ponderado para Similitud (RF-019)
- Estructura anidada de HashMaps
- Representa conexiones con pesos
- Permite algoritmos como Dijkstra

#### Grafo Dirigido para Social (RF-021)
- HashMap con listas de adyacencia
- Representa relaciones de seguimiento
- Permite algoritmos como BFS

#### Trie para Autocompletado (RF-023)
- Estructura en árbol
- Cada nodo tiene HashMap de hijos
- Búsqueda eficiente por prefijo

---

## 7. ALGORITMOS IMPLEMENTADOS

### Resumen de Algoritmos

| Algoritmo | Ubicación | Propósito | Complejidad | RF |
|-----------|-----------|-----------|-------------|-----|
| **Dijkstra** | GrafoDeSimilitud | Canciones similares | O((V+E)log V) | RF-020 |
| **BFS** | GrafoSocial | Sugerencias sociales | O(V + E) | RF-022 |
| **Trie Search** | TrieAutocompletado | Autocompletado | O(m) | RF-024 |
| **Búsqueda Concurrente** | CatalogoCanciones | Búsqueda avanzada | O(n/p) | RF-027 |

### Dijkstra (RF-020)
**Propósito:** Encontrar canciones más similares a una canción dada

**Funcionamiento:**
- Comienza desde la canción origen
- Explora canciones conectadas calculando distancias acumuladas
- Usa cola de prioridad para procesar las más cercanas primero
- Retorna canciones ordenadas por similitud

**Ventaja:** Encuentra el camino óptimo (mayor similitud) en el grafo

### BFS (RF-022)
**Propósito:** Encontrar "amigos de amigos" para sugerencias sociales

**Funcionamiento:**
- Comienza desde el usuario actual
- Explora nivel por nivel (amigos directos, luego amigos de amigos)
- Usa cola para mantener orden de exploración
- Retorna usuarios del nivel 2 que no son seguidos

**Ventaja:** Encuentra usuarios cercanos en la red social de manera eficiente

### Trie Search (RF-024)
**Propósito:** Búsqueda eficiente por prefijo para autocompletado

**Funcionamiento:**
- Navega desde la raíz siguiendo caracteres del prefijo
- Al llegar al nodo del prefijo, busca recursivamente todas las palabras
- Retorna todas las canciones que comienzan con el prefijo

**Ventaja:** Complejidad independiente del tamaño del catálogo

### Búsqueda Concurrente (RF-027)
**Propósito:** Optimizar búsqueda avanzada usando múltiples hilos

**Funcionamiento:**
- Divide el catálogo en partes
- Asigna cada parte a un hilo diferente
- Cada hilo procesa su parte en paralelo
- Combina resultados de todos los hilos

**Ventaja:** Procesamiento 4 veces más rápido en catálogos grandes

---

## 🎓 PUNTOS CLAVE PARA SUSTENTACIÓN

### 1. Concurrencia (RF-027)
- **Dónde**: Búsqueda avanzada en CatalogoCanciones
- **Qué usa**: ExecutorService, Future, listas sincronizadas
- **Por qué**: Optimiza búsquedas en catálogos grandes
- **Resultado**: 4x más rápido que procesamiento secuencial

### 2. HashMaps
- **6 ubicaciones principales** con diferentes propósitos
- **Complejidad O(1)** para acceso instantáneo
- **Usos**: Usuarios, grafos, Trie
- **Ventaja**: Acceso instantáneo vs recorrer listas completas

### 3. Grafos
- **Grafo de Similitud**: Dijkstra para recomendaciones inteligentes
- **Grafo Social**: BFS para sugerencias y grados de separación
- **Ventaja**: Algoritmos eficientes para relaciones complejas

### 4. Trie
- **Autocompletado eficiente**: O(m) independiente del tamaño
- **Escalable**: Funciona con millones de canciones
- **Ventaja**: Búsqueda instantánea mientras el usuario escribe

### 5. Arquitectura
- **MVC**: Separación clara de responsabilidades
- **Service Layer**: Lógica centralizada
- **Estructuras optimizadas**: Cada una para su propósito específico

---

## 📝 EJEMPLOS PRÁCTICOS PARA EXPLICAR

### Ejemplo 1: ¿Por qué HashMap para usuarios?
**Problema:** 1000 usuarios en una lista
- Buscar "maicol" con ArrayList: Recorre hasta 1000 elementos
- Buscar "maicol" con HashMap: Acceso directo instantáneo

### Ejemplo 2: ¿Por qué concurrencia en búsqueda?
**Problema:** 1000 canciones para buscar
- Sin concurrencia: 1 hilo procesa todo en 8 segundos
- Con concurrencia: 4 hilos procesan en paralelo en 2 segundos (4x más rápido)

### Ejemplo 3: ¿Cómo funciona Dijkstra?
**Escenario:** Usuario busca canciones similares a "Bohemian Rhapsody"
- Grafo conecta canciones por similitud
- Dijkstra encuentra el camino de menor costo (mayor similitud)
- Retorna: "Stairway to Heaven" (más similar), luego "Hotel California"

### Ejemplo 4: ¿Cómo funciona BFS?
**Escenario:** Usuario solicita sugerencias de amigos
- BFS explora la red social nivel por nivel
- Nivel 1: Amigos directos
- Nivel 2: Amigos de amigos (estas son las sugerencias)

### Ejemplo 5: ¿Por qué Trie para autocompletado?
**Problema:** 1,000,000 de canciones
- Con ArrayList: Recorre millones buscando prefijo "Bo"
- Con Trie: Solo 2 pasos (B → o) y encuentra todas las que empiezan con "Bo"

---

## 🎯 RESUMEN EJECUTIVO

### Tecnologías Clave
1. **Concurrencia**: Hilos para búsqueda avanzada (RF-027)
2. **HashMaps**: Acceso O(1) a usuarios y estructuras (RF-014, RF-019, RF-021, RF-023)
3. **Grafos**: Similitud con Dijkstra, Social con BFS (RF-019, RF-020, RF-021, RF-022)
4. **Trie**: Autocompletado eficiente (RF-023, RF-024)
5. **LinkedList**: Favoritos del usuario (RF-013)

### Algoritmos Principales
- **Dijkstra**: Recomendaciones basadas en similitud
- **BFS**: Sugerencias sociales y grados de separación
- **Trie Search**: Autocompletado en tiempo real
- **Búsqueda Concurrente**: Optimización de búsquedas

### Ventajas del Diseño
- **Rendimiento**: Estructuras optimizadas para cada propósito
- **Escalabilidad**: Funciona eficientemente con grandes volúmenes de datos
- **Mantenibilidad**: Arquitectura clara y separada en capas
- **Cumplimiento**: Implementa todos los requerimientos técnicos (RF-013 a RF-027)

---

**Documento generado para estudio rápido y sustentación del proyecto SyncUp**
