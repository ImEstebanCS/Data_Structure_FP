package co.edu.uniquindio.syncup.Controller;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Radio;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.Service.SyncUpService;

public class RadioController {
    private SyncUpService service;

    public RadioController(SyncUpService service) {
        this.service = service;
    }

    public Radio crearRadio(Usuario usuario, String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return null;
        }
        return service.crearRadio(usuario, nombre);
    }

    public void iniciarRadio(Usuario usuario, Cancion cancion) {
        service.iniciarRadio(usuario, cancion);
    }

    public void agregarCancion(Usuario usuario, Cancion cancion) {
        service.agregarCancionARadio(usuario, cancion);
    }

    public Cancion siguienteCancion(Usuario usuario) {
        return service.siguienteCancionRadio(usuario);
    }

    public Radio obtenerRadio(Usuario usuario) {
        return service.obtenerRadio(usuario);
    }
}