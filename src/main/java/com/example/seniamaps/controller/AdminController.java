package com.example.seniamaps.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.repository.BusquedaRepository;
import com.example.seniamaps.repository.UsuarioRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final BusquedaRepository busquedaRepository;
    //private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            BusquedaRepository busquedaRepository

    ) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.busquedaRepository = busquedaRepository;
    }

    /*
     * =====================================================
     * UPDATE ADMIN SETTINGS
     * =====================================================
     */
    @PostMapping("/settings/update")
    public String updateAdmin(
            String username,
            String password,
            Authentication auth) {

        Usuario admin = usuarioRepository
                .findByUsername(auth.getName())
                .orElseThrow();

        admin.setUsername(username);

        if (password != null && !password.isBlank()) {
            admin.setPassword(passwordEncoder.encode(password));
        }

        usuarioRepository.save(admin);

        return "redirect:/admin/home";
    }

    /*
     * =====================================================
     * ADMIN DASHBOARD
     * =====================================================
     */
    @GetMapping("/home")
    public String adminHome() {
        return "admin/dashboard";
    }

    @GetMapping("/users/create")
    public String createUserForm() {
        return "admin/create-user";
    }

    @PostMapping("/users/create")
    public String createUser(String username, String email, String password) {

        Usuario user = new Usuario();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRol("ROLE_USER");

        usuarioRepository.save(user);

        return "redirect:/admin/users";
    }

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/users")
    public String listarUsuarios(
            @RequestParam(value = "rol", required = false) String rol, 
            Model model) {
        
        List<Usuario> listaUsuarios;

        // Imprime en la consola de Spring para ver si al pulsar las pestañas te llega el rol
        System.out.println("El rol recibido por la URL es: " + rol);

        if (rol != null && !rol.isEmpty() && !rol.equals("ALL")) {
            listaUsuarios = usuarioRepository.findByRol(rol);
        } else {
            listaUsuarios = usuarioRepository.findAll();
        }

        model.addAttribute("usuarios", listaUsuarios);
        model.addAttribute("rolSeleccionado", rol != null ? rol : "ALL");
        
        return "admin/users-history";
    }
    @GetMapping("/settings") // O la ruta que estés usando para settings
    public String verAjustes(Model model, Principal principal) {
        
        // 1. Conseguir el username del usuario que está logueado en la app
        String usernameLogueado = principal.getName();
        
        // 2. Buscarlo en tu repositorio de usuarios
        Usuario admin = usuarioRepository.findByUsername(usernameLogueado)
                            .orElseThrow(() -> new RuntimeException("Admin no encontrado"));
        
        // 3. ¡ESTA LÍNEA ES LA QUE FALTA! Añadirlo al modelo con el nombre exacto "admin"
        model.addAttribute("admin", admin);
        
        return "admin/settings";
    }
    // Cambiar el Rol de un usuario
    @PostMapping("/users/cambiar-rol")
    public String cambiarRol(@RequestParam("id") Long id, 
                            @RequestParam("nuevoRol") String nuevoRol,
                            @RequestParam(value = "rolSeleccionado", defaultValue = "ALL") String rolSeleccionado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        usuario.setRol(nuevoRol);
        usuarioRepository.save(usuario);
        
        // Redirige manteniendo el filtro por el que iba el administrador
        return "redirect:/admin/users?rol=" + rolSeleccionado + "&success=rol";
    }

    // Cambiar la Contraseña de un usuario
    @PostMapping("/users/cambiar-password")
    public String cambiarPassword(@RequestParam("id") Long id, 
                                @RequestParam("nuevaPassword") String nuevaPassword,
                                @RequestParam(value = "rolSeleccionado", defaultValue = "ALL") String rolSeleccionado) {
        if (nuevaPassword == null || nuevaPassword.trim().isEmpty()) {
            return "redirect:/admin/users?rol=" + rolSeleccionado + "&error=vacio";
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
        
        return "redirect:/admin/users?rol=" + rolSeleccionado + "&success=password";
    }
    // Eliminar un usuario definitivamente
    @PostMapping("/users/eliminar")
    public String eliminarUsuario(@RequestParam("id") Long id,
                                @RequestParam(value = "rolSeleccionado", defaultValue = "ALL") String rolSeleccionado) {
        
        // Comprobamos si existe antes de borrar para evitar excepciones
        if (!usuarioRepository.existsById(id)) {
            return "redirect:/admin/users?rol=" + rolSeleccionado + "&error=no-existe";
        }
        
        usuarioRepository.deleteById(id);
        
        // Redirige manteniendo el filtro y añadiendo el parámetro de éxito para el borrado
        return "redirect:/admin/users?rol=" + rolSeleccionado + "&success=eliminado";
    }
}