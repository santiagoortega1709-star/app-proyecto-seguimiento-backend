package com.santiago.seguimiento_proyecto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "actividades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    /**
     * BAC — Budget at Completion
     * Presupuesto total planificado para esta actividad.
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal bac;

    /**
     * Porcentaje de avance planificado a la fecha de corte (0-100).
     * Usado para calcular el EV: EV = BAC * (pvPorcentaje / 100)
     */
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal pvPorcentaje;

    /**
     * Porcentaje de avance real completado a la fecha (0-100).
     * Usado para calcular el EV real: EV = BAC * (evPorcentaje / 100)
     */
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal evPorcentaje;

    /**
     * AC — Actual Cost
     * Costo real incurrido hasta la fecha.
     */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal ac;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;
}
