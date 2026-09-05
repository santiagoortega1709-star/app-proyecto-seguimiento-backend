package com.santiago.seguimiento_proyecto.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ProyectoResponseDTO {

    private Long id;
    private String nombre;

    /** Suma de BAC de todas las actividades */
    private BigDecimal bacTotal;

    /** Valor Planificado total (PV = suma de BAC * pvPorcentaje/100) */
    private BigDecimal pvTotal;

    /** Valor Ganado total (EV = suma de BAC * evPorcentaje/100) */
    private BigDecimal evTotal;

    /** Costo Real total (AC = suma de AC de todas las actividades) */
    private BigDecimal acTotal;

    /** SPI = EV / PV */
    private BigDecimal spi;

    /** CPI = EV / AC */
    private BigDecimal cpi;

    private List<ActividadResponseDTO> actividades;
}
