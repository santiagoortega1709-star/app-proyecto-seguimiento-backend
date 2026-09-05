package com.santiago.seguimiento_proyecto.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ActividadRequestDTO {

    private String nombre;

    /** BAC — Budget at Completion: presupuesto total planificado */
    private BigDecimal bac;

    /** Porcentaje de avance planificado a la fecha de corte (0-100) */
    private BigDecimal pvPorcentaje;

    /** Porcentaje de avance real completado (0-100) */
    private BigDecimal evPorcentaje;

    /** AC — Actual Cost: costo real incurrido hasta la fecha */
    private BigDecimal ac;
}
