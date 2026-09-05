package com.santiago.seguimiento_proyecto.util;

import com.santiago.seguimiento_proyecto.dto.ActividadResponseDTO;
import com.santiago.seguimiento_proyecto.dto.ProyectoResponseDTO;
import com.santiago.seguimiento_proyecto.entity.Actividad;
import com.santiago.seguimiento_proyecto.entity.Proyecto;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

/**
 * Utilidad para calcular métricas de Earned Value Management (EVM).
 */
public final class EvaluacionEVM {

    private static final int ESCALA = 4;
    private static final RoundingMode REDONDEO = RoundingMode.HALF_UP;
    private static final MathContext MC = new MathContext(10, REDONDEO);

    private EvaluacionEVM() {}

    /**
     * Convierte una entidad Actividad a su DTO con todas las métricas EVM calculadas.
     */
    public static ActividadResponseDTO toActividadResponse(Actividad a) {
        BigDecimal pv = calcularPV(a.getBac(), a.getPvPorcentaje());
        BigDecimal ev = calcularEV(a.getBac(), a.getEvPorcentaje());
        BigDecimal ac = a.getAc();

        return ActividadResponseDTO.builder()
                .id(a.getId())
                .nombre(a.getNombre())
                .bac(a.getBac())
                .pvPorcentaje(a.getPvPorcentaje())
                .pv(pv)
                .evPorcentaje(a.getEvPorcentaje())
                .ev(ev)
                .ac(ac)
                .sv(ev.subtract(pv).setScale(ESCALA, REDONDEO))
                .cv(ev.subtract(ac).setScale(ESCALA, REDONDEO))
                .spi(dividir(ev, pv))
                .cpi(dividir(ev, ac))
                .proyectoId(a.getProyecto().getId())
                .build();
    }

    /**
     * Convierte una entidad Proyecto a su DTO con métricas EVM agregadas.
     */
    public static ProyectoResponseDTO toProyectoResponse(Proyecto p) {
        List<ActividadResponseDTO> actividadesDTO = p.getActividades()
                .stream()
                .map(EvaluacionEVM::toActividadResponse)
                .toList();

        BigDecimal bacTotal = sumar(actividadesDTO.stream().map(ActividadResponseDTO::getBac).toList());
        BigDecimal pvTotal  = sumar(actividadesDTO.stream().map(ActividadResponseDTO::getPv).toList());
        BigDecimal evTotal  = sumar(actividadesDTO.stream().map(ActividadResponseDTO::getEv).toList());
        BigDecimal acTotal  = sumar(actividadesDTO.stream().map(ActividadResponseDTO::getAc).toList());

        return ProyectoResponseDTO.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .bacTotal(bacTotal)
                .pvTotal(pvTotal)
                .evTotal(evTotal)
                .acTotal(acTotal)
                .spi(dividir(evTotal, pvTotal))
                .cpi(dividir(evTotal, acTotal))
                .actividades(actividadesDTO)
                .build();
    }

    // --- helpers ---

    /** PV = BAC * (pvPorcentaje / 100) */
    private static BigDecimal calcularPV(BigDecimal bac, BigDecimal pvPct) {
        return bac.multiply(pvPct).divide(BigDecimal.valueOf(100), ESCALA, REDONDEO);
    }

    /** EV = BAC * (evPorcentaje / 100) */
    private static BigDecimal calcularEV(BigDecimal bac, BigDecimal evPct) {
        return bac.multiply(evPct).divide(BigDecimal.valueOf(100), ESCALA, REDONDEO);
    }

    /** División segura; retorna null si el divisor es cero. */
    private static BigDecimal dividir(BigDecimal numerador, BigDecimal denominador) {
        if (denominador == null || denominador.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return numerador.divide(denominador, ESCALA, REDONDEO);
    }

    private static BigDecimal sumar(List<BigDecimal> valores) {
        return valores.stream()
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(ESCALA, REDONDEO);
    }
}
