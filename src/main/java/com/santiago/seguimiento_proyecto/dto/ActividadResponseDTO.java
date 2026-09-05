package com.santiago.seguimiento_proyecto.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ActividadResponseDTO {

    private Long id;
    private String nombre;

    /** BAC — Budget at Completion: presupuesto total planificado */
    private BigDecimal bac;

    /** Porcentaje de avance planificado a la fecha de corte (0-100) */
    private BigDecimal pvPorcentaje;

    /** PV — Planned Value = BAC * pvPorcentaje / 100 */
    private BigDecimal pv;

    /** Porcentaje de avance real completado (0-100) */
    private BigDecimal evPorcentaje;

    /** EV — Earned Value = BAC * evPorcentaje / 100 */
    private BigDecimal ev;

    /** AC — Actual Cost: costo real incurrido hasta la fecha */
    private BigDecimal ac;

    /** CV — Cost Variance = EV - AC */
    private BigDecimal cv;

    /** SV — Schedule Variance = EV - PV */
    private BigDecimal sv;

    /** CPI — Cost Performance Index = EV / AC */
    private BigDecimal cpi;

    /** SPI — Schedule Performance Index = EV / PV */
    private BigDecimal spi;

    /** EAC — Estimate at Completion = BAC / CPI */
    private BigDecimal eac;

    /** VAC — Variance at Completion = BAC - EAC */
    private BigDecimal vac;

    /** Interpretación del CPI: "Bajo presupuesto", "Sobre presupuesto" o "En presupuesto" */
    private String interpretacionCpi;

    /** Interpretación del SPI: "Adelantado", "Atrasado" o "En cronograma" */
    private String interpretacionSpi;

    private Long proyectoId;
}
