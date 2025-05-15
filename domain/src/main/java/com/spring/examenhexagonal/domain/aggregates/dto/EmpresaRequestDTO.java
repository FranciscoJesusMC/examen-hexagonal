package com.spring.examenhexagonal.domain.aggregates.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaRequestDTO {

    @NotBlank
    private String razonSocial;
    @NotBlank
    private String estado;
    @NotBlank
    private String condicion;
    @NotBlank
    private String direccion;
    @NotBlank
    private String departamento;
    @NotBlank
    private String provincia;
    @NotBlank
    private String distrito;
    @NotBlank
    private String actividadEconomica;
    @NotBlank
    private String numeroTrabajadores;

}
