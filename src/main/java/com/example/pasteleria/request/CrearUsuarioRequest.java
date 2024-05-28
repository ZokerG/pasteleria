package com.example.pasteleria.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrearUsuarioRequest {
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String confirmarPassword;
    private String rol;
}
