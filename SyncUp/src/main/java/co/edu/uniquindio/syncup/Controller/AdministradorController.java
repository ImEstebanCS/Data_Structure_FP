package co.edu.uniquindio.syncup.Controller;


import co.edu.uniquindio.syncup.Model.Entidades.Administrador;
import co.edu.uniquindio.syncup.Service.SyncUpService;

public class AdministradorController {
    private SyncUpService service;

    public AdministradorController(SyncUpService service) {
        this.service = service;
    }

    public boolean registrar(String username, String password, String nombre) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }
        return service.registrarAdministrador(username, password, nombre);
    }

    public Administrador autenticar(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        return service.autenticarAdministrador(username, password);
    }
}