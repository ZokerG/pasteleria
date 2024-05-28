package com.example.pasteleria.controller;

import com.example.pasteleria.model.Categoria;
import com.example.pasteleria.response.CategoriarResponse;
import com.example.pasteleria.service.CategoriaServicio;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaContralador {

    private final CategoriaServicio categoriaServicio;

    public CategoriaContralador(CategoriaServicio categoriaServicio) {
        this.categoriaServicio = categoriaServicio;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarCategoria(@PathVariable long id){
        categoriaServicio.eliminarCategoria(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoriarResponse> listarCategorias(){
        return categoriaServicio.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void guardarCategoria(@RequestParam("nombre") String nombre, @RequestParam("descripcion") String descripcion){
        categoriaServicio.guardarCategoria(nombre, descripcion);
    }
}
