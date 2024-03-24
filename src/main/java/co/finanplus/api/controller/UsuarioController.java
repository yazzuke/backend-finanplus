package co.finanplus.api.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.finanplus.api.domain.usuarios.Usuario;
import co.finanplus.api.service.UsuarioService;
import jakarta.annotation.security.PermitAll;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    @PermitAll
    public String saveUsuario(@RequestBody Usuario usuario) throws InterruptedException, ExecutionException {
        return usuarioService.saveUsuario(usuario);
    }
}
