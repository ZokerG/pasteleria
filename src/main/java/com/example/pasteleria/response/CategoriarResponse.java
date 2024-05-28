package com.example.pasteleria.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriarResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private boolean estado;
}
