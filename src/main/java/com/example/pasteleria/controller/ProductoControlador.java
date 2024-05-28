package com.example.pasteleria.controller;


import com.example.pasteleria.model.Producto;
import com.example.pasteleria.response.ProductoResponse;
import com.example.pasteleria.service.ProductoServicio;
import com.stripe.exception.StripeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/producto")
public class ProductoControlador {

    private final ProductoServicio productoServicio;

    public ProductoControlador(ProductoServicio productoServicio) {
        this.productoServicio = productoServicio;
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductoResponse> listarProductos(){
        return productoServicio.obtenerProductos();
    }

    @GetMapping("/categoria/{categoriaId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Producto> listarProductosPorCategoria(@PathVariable long categoriaId){
        return productoServicio.obtenerProductosPorCategoria(categoriaId);
    }

    @DeleteMapping("/car/{clienteId}")
    public ResponseEntity<?> deleteCarsByCliente(@PathVariable Long clienteId) {
        productoServicio.deleteCarsByCliente(clienteId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarProducto(@PathVariable long id){
        productoServicio.eliminarProducto(id);
    }

    @PostMapping("/{categoriaId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void guardarProducto(@PathVariable long categoriaId, @RequestParam("file") MultipartFile file, @RequestParam("nombre") String nombre, @RequestParam("descripcion") String descripcion, @RequestParam("precio") double precio, @RequestParam("stock") int stock){
        productoServicio.guardarProducto(categoriaId, file, nombre, descripcion, precio, stock);
    }

    @PostMapping("/comprar")
    @ResponseStatus(HttpStatus.CREATED)
    public String comprarProducto(@RequestParam("productoId") long productoId, @RequestParam("cantidad") int cantidad) throws StripeException {
        return productoServicio.comprarProducto(productoId, cantidad);
    }

    @GetMapping("/obtener/carrito/{clienteId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductoResponse> obtenerCarrito(@PathVariable long clienteId){
        return productoServicio.obtenerProductosCarrito(clienteId);
    }

    @PostMapping("/agregar/carrito/{clienteId}/{productoId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void agregarCarrito(@PathVariable long clienteId, @PathVariable long productoId, @RequestParam("cantidad") int cantidad, double price){
        productoServicio.añadirAlCarrito(clienteId, productoId, cantidad, price);
    }

    @PostMapping("/comprar/carrito/{clienteId}")
    @ResponseStatus(HttpStatus.CREATED)
    public String comprarCarrito(@PathVariable long clienteId) throws StripeException {
        return productoServicio.comprarCarrito(clienteId);
    }
}
