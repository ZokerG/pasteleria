package com.example.pasteleria.service;

import com.example.pasteleria.model.Categoria;
import com.example.pasteleria.repository.CategoriaRepositorio;
import com.example.pasteleria.response.CategoriarResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServicio {

    private final CategoriaRepositorio categoriaRepositorio;

    public CategoriaServicio(CategoriaRepositorio categoriaRepositorio) {
        this.categoriaRepositorio = categoriaRepositorio;
    }

    public void eliminarCategoria(long id) {
        categoriaRepositorio.deleteById(id);
    }

    public void guardarCategoria(String nombre, String descripcion) {
        try {
            Categoria categoria = new Categoria();
            categoria.setNombre(nombre);
            categoria.setDescripcion(descripcion);
            categoria.setEstado(true);
            categoriaRepositorio.save(categoria);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la categoria");
        }
    }

    public List<CategoriarResponse> findAll() {
        return categoriaRepositorio.findAll()
                .stream().map(this::converCategoria).toList();
    }

    private CategoriarResponse converCategoria(Categoria categoria){
        return new CategoriarResponse(categoria.getId(), categoria.getNombre(), categoria.getDescripcion(), categoria.isEstado());
    }
}
