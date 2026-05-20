package com.example.seniamaps.controller;

import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/personal")
    public String mostrarAreaPersonal(Authentication authentication, Model model) {
        // 1. Spring Security nos da el 'username' del usuario que se acaba de loguear
        String usernameLogueado = authentication.getName();
        System.out.println("👉 Intentando cargar área personal para el usuario: " + usernameLogueado);
        // 2. Buscamos el objeto completo en la base de datos MariaDB
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(usernameLogueado);

        if (usuarioOpt.isPresent()) {
            // 3. Pasamos el objeto usuario a la vista de Thymeleaf
            model.addAttribute("usuario", usuarioOpt.get());
        } else {
            // Por seguridad, si no se encuentra (caso raro), mandamos al login
            return "redirect:/login";
        }

        return "personal"; // Devuelve templates/personal.html
    }
    @GetMapping("/usuarios")
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
        
        return "ListaUsuarios";
    }
}