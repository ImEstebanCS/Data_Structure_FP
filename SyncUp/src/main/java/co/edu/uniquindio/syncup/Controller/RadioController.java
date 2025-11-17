package co.edu.uniquindio.syncup.Controller;

import co.edu.uniquindio.syncup.Model.Entidades.Cancion;
import co.edu.uniquindio.syncup.Model.Entidades.Radio;
import co.edu.uniquindio.syncup.Model.Entidades.Usuario;
import co.edu.uniquindio.syncup.Service.SyncUpService;

public class RadioController {
    private final SyncUpService service;

    public RadioController(SyncUpService service) {
        this.service = service;
    }

    public Radio iniciarRadio(Usuario usuario, Cancion cancionSemilla) {
        return service.iniciarRadio(usuario, cancionSemilla);
    }

    public Radio obtenerRadio(Usuario usuario) {
        return service.obtenerRadio(usuario);
    }
}