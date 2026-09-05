package com.santiago.seguimiento_proyecto.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ActividadResponseDTO {

    private Long id;
    private String nombre;

    /** BAC — Budget at Completion */
    private BigDecimal bac;

    /** Porcentaje de avance planificado a la fecha de corte (0-100) */
    private BigDecimal pvPorcentaje;

    /** PV — Planned Value = BAC * pvPorcentaje / 100 */
    private BigDecimal pv;

    /** Porcentaje de avance real completado (0-100) */
    private BigDecimal evPorcentaje;

    /** EV — Earned Value = BAC * evPorcentaje / 100 */
    private BigDecimal ev;

    /** AC — Actual Cost */
    private BigDecimal ac;

    /** SV — Schedule Variance = EV - PV */
    private BigDecimal sv;

    /** CV — Cost Variance = EV - AC */
    private BigDecimal cv;

    /** SPI — Schedule Performance Index = EV / PV */
    private BigDecimal spi;

    /** CPI — Cost Performance Index = EV / AC */
    private BigDecimal cpi;

    private Long proyectoId;
}
