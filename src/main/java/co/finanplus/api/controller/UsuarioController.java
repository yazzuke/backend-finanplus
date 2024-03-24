package co.finanplus.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.finanplus.api.domain.usuarios.Usuario;
import co.finanplus.api.domain.usuarios.UsuarioRepository;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository; // Asumiendo que tienes un repositorio para usuarios

    @PostMapping("/usuarios")
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setId(usuario.getId());
        nuevoUsuario.setNombre(usuario.getNombre());
        nuevoUsuario.setEmail(usuario.getEmail());

        return usuarioRepository.save(nuevoUsuario);
    }

}