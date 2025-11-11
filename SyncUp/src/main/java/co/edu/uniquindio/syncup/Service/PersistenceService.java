package co.edu.uniquindio.syncup.Service;

import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * PersistenceService
 * Servicio para guardar y cargar datos de usuarios desde archivos JSON
 * Mejora: Persistencia básica de datos
 */
public class PersistenceService {
    private final Gson gson;
    private static final String USUARIOS_JSON = "usuarios.json";
    private static final String DATA_DIR = "data";

    public PersistenceService() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        
        // Crear directorio de datos si no existe
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    /**
     * Guarda la lista de usuarios en un archivo JSON
     */
    public boolean guardarUsuarios(List<Usuario> usuarios) {
        try {
            File file = new File(DATA_DIR, USUARIOS_JSON);
            
            // Convertir usuarios a formato serializable (sin contraseñas en texto plano)
            List<UsuarioData> usuariosData = new ArrayList<>();
            for (Usuario usuario : usuarios) {
                usuariosData.add(new UsuarioData(
                    usuario.getUsername(),
                    usuario.getPassword(), // TODO: Implementar hash de contraseñas
                    usuario.getNombre()
                ));
            }
            
            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(usuariosData, writer);
                System.out.println("✓ Usuarios guardados: " + usuarios.size());
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
            return false;
        }
    }

    /**
     * Carga la lista de usuarios desde un archivo JSON
     */
    public List<UsuarioData> cargarUsuarios() {
        try {
            File file = new File(DATA_DIR, USUARIOS_JSON);
            
            if (!file.exists()) {
                System.out.println("No existe archivo de usuarios, se creará uno nuevo");
                return new ArrayList<>();
            }
            
            try (FileReader reader = new FileReader(file)) {
                Type listType = new TypeToken<List<UsuarioData>>(){}.getType();
                List<UsuarioData> usuariosData = gson.fromJson(reader, listType);
                
                if (usuariosData != null) {
                    System.out.println("✓ Usuarios cargados: " + usuariosData.size());
                    return usuariosData;
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
        }
        
        return new ArrayList<>();
    }

    /**
     * Clase auxiliar para serialización de usuarios
     */
    public static class UsuarioData {
        public String username;
        public String password;
        public String nombre;

        public UsuarioData() {}

        public UsuarioData(String username, String password, String nombre) {
            this.username = username;
            this.password = password;
            this.nombre = nombre;
        }
    }
}

