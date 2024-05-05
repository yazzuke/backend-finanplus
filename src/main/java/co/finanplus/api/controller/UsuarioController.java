    package co.finanplus.api.controller;

    import java.time.LocalDate;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RestController;

    import co.finanplus.api.domain.usuarios.Usuario;
    import co.finanplus.api.domain.usuarios.UsuarioRepository;
    import co.finanplus.api.service.ResumenMensualService;

    @RestController
    public class UsuarioController {

        @Autowired
        private UsuarioRepository usuarioRepository;

        @Autowired
        private ResumenMensualService resumenMensualService;

        // endpoint para crear un usuario
    // endpoint para crear un usuario
    @PostMapping("/usuarios")
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setId(usuario.getId());
        nuevoUsuario.setNombre(usuario.getNombre());
        nuevoUsuario.setEmail(usuario.getEmail());
        nuevoUsuario.setPhotoUrl(usuario.getPhotoUrl());  
        
        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        // Crear un resumen mensual inicial después de registrar al usuario
        LocalDate fechaInicio = LocalDate.now();
        resumenMensualService.crearResumenInicial(usuarioGuardado.getId(), fechaInicio);

        return usuarioGuardado;
    }

        // endpoint para obtener un usuario por su id
        @GetMapping("/usuarios/{userId}")
        public Usuario obtenerUsuario(@PathVariable String userId) {
            return usuarioRepository.findById(userId).orElse(null);
        }


        // endpoint para obtener un usuario por su email
        @GetMapping("/usuarios/email/{email}")
        public ResponseEntity<Usuario> obtenerUsuarioPorEmail(@PathVariable String email) {
            return usuarioRepository.findByEmail(email)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

         // endpoint para modificar el nombre de un usuario
    @PutMapping("/usuarios/{userId}/nombre")
    public ResponseEntity<Usuario> modificarNombreUsuario(@PathVariable String userId, @RequestBody String nuevoNombre) {
        return usuarioRepository.findById(userId)
            .map(usuario -> {
                usuario.setNombre(nuevoNombre);
                Usuario usuarioActualizado = usuarioRepository.save(usuario);
                return ResponseEntity.ok(usuarioActualizado);
            })
            .orElse(ResponseEntity.notFound().build());
    }



    }