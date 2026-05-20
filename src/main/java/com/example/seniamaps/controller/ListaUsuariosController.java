package com.example.seniamaps.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.seniamaps.entity.Usuario;
import com.example.seniamaps.repository.UsuarioRepository;
@Controller
public class ListaUsuariosController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/ListaUsuarios")
    public String listaUsuarios(Model model){
        List<Usuario> lista= usuarioRepository.findAll();
        model.addAttribute("usuarios", lista);
        return "ListaUsuarios";
    }
    
}