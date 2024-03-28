package co.finanplus.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.finanplus.api.domain.usuarios.Usuario;
import co.finanplus.api.domain.usuarios.UsuarioRepository;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // endpoint para crear un usuario
    @PostMapping("/usuarios")
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setId(usuario.getId());
        nuevoUsuario.setNombre(usuario.getNombre());
        nuevoUsuario.setEmail(usuario.getEmail());
        nuevoUsuario.setPhoto_url(usuario.getPhoto_url());

        return usuarioRepository.save(nuevoUsuario);
    }

    // endpoint para obtener un usuario por su id
    @GetMapping("/usuarios/{userId}")
    public Usuario obtenerUsuario(@PathVariable String userId) {
        return usuarioRepository.findById(userId).orElse(null);
    }

    // endpoint para obtener un usuario por su email
    // endpoint para obtener un usuario por su email
    @GetMapping("/usuarios/email/{email}")
    public ResponseEntity<Usuario> obtenerUsuarioPorEmail(@PathVariable String email) {
        return usuarioRepository.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // endpoint para las tablas
    // necesita poder agregar deudas
    // necesita poder agregar saldos a favor
    // tabla con columnas deudas
    // tabla con colu mna saldos a favor
    // email con ingreso
    // tabla egresos por mes
    // tabla ingresos por mes

}