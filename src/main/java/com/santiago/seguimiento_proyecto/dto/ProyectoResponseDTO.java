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

    /** BAC total: suma de BAC de todas las actividades */
    private BigDecimal bacTotal;

    /** PV total: suma de (BAC * pvPorcentaje / 100) de todas las actividades */
    private BigDecimal pvTotal;

    /** EV total: suma de (BAC * evPorcentaje / 100) de todas las actividades */
    private BigDecimal evTotal;

    /** AC total: suma de AC de todas las actividades */
    private BigDecimal acTotal;

    /** CV consolidado = EV - AC */
    private BigDecimal cv;

    /** SV consolidado = EV - PV */
    private BigDecimal sv;

    /** CPI consolidado = EV / AC */
    private BigDecimal cpi;

    /** SPI consolidado = EV / PV */
    private BigDecimal spi;

    /** EAC consolidado = BAC / CPI */
    private BigDecimal eac;

    /** VAC consolidado = BAC - EAC */
    private BigDecimal vac;

    /** Interpretación del CPI: "Bajo presupuesto", "Sobre presupuesto" o "En presupuesto" */
    private String interpretacionCpi;

    /** Interpretación del SPI: "Adelantado", "Atrasado" o "En cronograma" */
    private String interpretacionSpi;

    private List<ActividadResponseDTO> actividades;
}
