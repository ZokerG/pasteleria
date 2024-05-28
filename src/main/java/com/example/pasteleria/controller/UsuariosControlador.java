package com.example.pasteleria.controller;

import com.example.pasteleria.model.Usuarios;
import com.example.pasteleria.request.AuthRequest;
import com.example.pasteleria.request.CrearUsuarioRequest;
import com.example.pasteleria.response.AuthUserResponse;
import com.example.pasteleria.service.UsuarioServicio;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuariosControlador {

    private final UsuarioServicio usuarioServicio;

    public UsuariosControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Usuarios> listarUsuarios(){
        return usuarioServicio.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarUsuario(@PathVariable long id){
        usuarioServicio.deleteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuarios crearUsuario(@RequestBody CrearUsuarioRequest crearUsuarioRequest){
        return usuarioServicio.crearUsuarios(crearUsuarioRequest);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthUserResponse login(@RequestBody AuthRequest request){
        return usuarioServicio.signIn(request);
    }

    @GetMapping("/buscar/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Usuarios buscarUsuario(@PathVariable long id) throws Exception {
        return usuarioServicio.findById(id);
    }
}
