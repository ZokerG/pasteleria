package com.example.pasteleria.service;


import com.example.pasteleria.model.CarByCliente;
import com.example.pasteleria.model.Categoria;
import com.example.pasteleria.model.Producto;
import com.example.pasteleria.repository.CarByClienteRepositorio;
import com.example.pasteleria.repository.CategoriaRepositorio;
import com.example.pasteleria.repository.ProductoRepositorio;
import com.example.pasteleria.response.ProductoResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
public class ProductoServicio {

    private final ProductoRepositorio productoRepositorio;
    private final CategoriaRepositorio categoriaRepositorio;
    private final CarByClienteRepositorio carByClienteRepositorio;
    private final StripeServicio stripeServicio;

    public ProductoServicio(ProductoRepositorio productoRepositorio, CategoriaRepositorio categoriaRepositorio, CarByClienteRepositorio carByClienteRepositorio, StripeServicio stripeServicio) {
        this.productoRepositorio = productoRepositorio;
        this.categoriaRepositorio = categoriaRepositorio;
        this.carByClienteRepositorio = carByClienteRepositorio;
        this.stripeServicio = stripeServicio;
    }


    public void eliminarProducto(long id) {
        productoRepositorio.deleteById(id);
    }

    public void guardarProducto(long categoriaId, MultipartFile file, String nombre, String descripcion, double precio, int stock) {
        try {
            Categoria categoria = categoriaRepositorio.findById(categoriaId).orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setStock(stock);
            producto.setImagen(file.getBytes());
            producto.setCategoria(categoria);
            productoRepositorio.save(producto);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el producto");
        }
    }

    public List<ProductoResponse> obtenerProductos() {
        List<Producto> productos = productoRepositorio.findAll();
        return productos.stream().map(producto -> {
            ProductoResponse productoResponse = new ProductoResponse();
            productoResponse.setId(producto.getId());
            productoResponse.setNombre(producto.getNombre());
            productoResponse.setDescripcion(producto.getDescripcion());
            productoResponse.setPrecio(producto.getPrecio());
            productoResponse.setStock(producto.getStock());
            productoResponse.setImagen(convertirImagenABase64(producto.getImagen()));
            return productoResponse;
        }).toList();
    }

    private String convertirImagenABase64(byte[] datosImagen) {
        return datosImagen != null ? Base64.getEncoder().encodeToString(datosImagen) : "";
    }

    public List<Producto> obtenerProductosPorCategoria(long categoriaId) {
        return productoRepositorio.findAllByCategoriaId(categoriaId);
    }

    public String comprarProducto(long id, int cantidad) throws StripeException {
        Producto producto = productoRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        if (producto.getStock() < cantidad) {
            throw new RuntimeException("No hay suficiente stock");
        }
        Session session = stripeServicio.crearPago(producto.getPrecio(), cantidad);
        return session.toJson();
    }

    public List<ProductoResponse> obtenerProductosCarrito(long clientId) {
        List<CarByCliente> carByClienteList = carByClienteRepositorio.findAllByClienteId(clientId);
        return carByClienteList.stream().map(carByCliente -> {
            Producto producto = productoRepositorio.findById(carByCliente.getProductoId()).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            ProductoResponse productoResponse = new ProductoResponse();
            productoResponse.setId(producto.getId());
            productoResponse.setNombre(producto.getNombre());
            productoResponse.setDescripcion(producto.getDescripcion());
            productoResponse.setPrecio(producto.getPrecio());
            productoResponse.setStock(producto.getStock());
            productoResponse.setImagen(convertirImagenABase64(producto.getImagen()));
            productoResponse.setCantidad(carByCliente.getCantidad());
            return productoResponse;
        }).toList();
    }

    public void añadirAlCarrito(long clientId, long productId, int cantidad, double price) {
        Producto producto = productoRepositorio.findById(productId).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        if (producto.getStock() < cantidad) {
            throw new RuntimeException("No hay suficiente stock");
        }
        CarByCliente carByCliente = new CarByCliente();
        carByCliente.setClienteId(clientId);
        carByCliente.setProductoId(productId);
        carByCliente.setCantidad(cantidad);
        carByCliente.setPrice(price);
        carByClienteRepositorio.save(carByCliente);
    }

    public String comprarCarrito(long clientId) throws StripeException {
        double price = 0;
        int cantidad = 0;
        List<CarByCliente> carByClienteList = carByClienteRepositorio.findAllByClienteId(clientId);
        for (CarByCliente carByCliente : carByClienteList) {
            Producto producto = productoRepositorio.findById(carByCliente.getProductoId()).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            if (producto.getStock() < carByCliente.getCantidad()) {
                throw new RuntimeException("No hay suficiente stock");
            }
            producto.setStock(producto.getStock() - carByCliente.getCantidad());
            price += producto.getPrecio();
            cantidad++;
            productoRepositorio.save(producto);
            carByClienteRepositorio.delete(carByCliente);
        }
        return stripeServicio.crearPago(price, cantidad).toJson();
    }
}
