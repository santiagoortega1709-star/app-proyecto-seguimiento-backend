package com.santiago.seguimiento_proyecto.util;

import com.santiago.seguimiento_proyecto.dto.ActividadResponseDTO;
import com.santiago.seguimiento_proyecto.dto.ProyectoResponseDTO;
import com.santiago.seguimiento_proyecto.entity.Actividad;
import com.santiago.seguimiento_proyecto.entity.Proyecto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Utilidad para calcular métricas de Earned Value Management (EVM).
 *
 * Fórmulas aplicadas:
 *   PV  = BAC * (pvPorcentaje / 100)
 *   EV  = BAC * (evPorcentaje / 100)
 *   CV  = EV - AC
 *   SV  = EV - PV
 *   CPI = EV / AC
 *   SPI = EV / PV
 *   EAC = BAC / CPI
 *   VAC = BAC - EAC
 */
public final class EvaluacionEVM {

    private static final int ESCALA = 4;
    private static final RoundingMode REDONDEO = RoundingMode.HALF_UP;
    private static final BigDecimal CIEN = BigDecimal.valueOf(100);
    private static final BigDecimal UNO  = BigDecimal.ONE;

    private EvaluacionEVM() {}

    // -------------------------------------------------------------------------
    // Conversión de entidad Actividad → DTO con métricas EVM completas
    // -------------------------------------------------------------------------

    public static ActividadResponseDTO toActividadResponse(Actividad a) {
        BigDecimal bac = a.getBac();
        BigDecimal pv  = calcularPV(bac, a.getPvPorcentaje());
        BigDecimal ev  = calcularEV(bac, a.getEvPorcentaje());
        BigDecimal ac  = a.getAc();

        BigDecimal cv  = ev.subtract(ac).setScale(ESCALA, REDONDEO);
        BigDecimal sv  = ev.subtract(pv).setScale(ESCALA, REDONDEO);
        BigDecimal cpi = dividir(ev, ac);
        BigDecimal spi = dividir(ev, pv);
        BigDecimal eac = calcularEAC(bac, cpi);
        BigDecimal vac = calcularVAC(bac, eac);

        return ActividadResponseDTO.builder()
                .id(a.getId())
                .nombre(a.getNombre())
                .bac(bac)
                .pvPorcentaje(a.getPvPorcentaje())
                .pv(pv)
                .evPorcentaje(a.getEvPorcentaje())
                .ev(ev)
                .ac(ac)
                .cv(cv)
                .sv(sv)
                .cpi(cpi)
                .spi(spi)
                .eac(eac)
                .vac(vac)
                .interpretacionCpi(interpretarCPI(cpi))
                .interpretacionSpi(interpretarSPI(spi))
                .proyectoId(a.getProyecto().getId())
                .build();
    }

    // -------------------------------------------------------------------------
    // Conversión de entidad Proyecto → DTO con métricas EVM consolidadas
    // -------------------------------------------------------------------------

    public static ProyectoResponseDTO toProyectoResponse(Proyecto p) {
        List<ActividadResponseDTO> actividadesDTO = p.getActividades()
                .stream()
                .map(EvaluacionEVM::toActividadResponse)
                .toList();

        BigDecimal bacTotal = sumar(actividadesDTO.stream().map(ActividadResponseDTO::getBac).toList());
        BigDecimal pvTotal  = sumar(actividadesDTO.stream().map(ActividadResponseDTO::getPv).toList());
        BigDecimal evTotal  = sumar(actividadesDTO.stream().map(ActividadResponseDTO::getEv).toList());
        BigDecimal acTotal  = sumar(actividadesDTO.stream().map(ActividadResponseDTO::getAc).toList());

        BigDecimal cv  = evTotal.subtract(acTotal).setScale(ESCALA, REDONDEO);
        BigDecimal sv  = evTotal.subtract(pvTotal).setScale(ESCALA, REDONDEO);
        BigDecimal cpi = dividir(evTotal, acTotal);
        BigDecimal spi = dividir(evTotal, pvTotal);
        BigDecimal eac = calcularEAC(bacTotal, cpi);
        BigDecimal vac = calcularVAC(bacTotal, eac);

        return ProyectoResponseDTO.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .bacTotal(bacTotal)
                .pvTotal(pvTotal)
                .evTotal(evTotal)
                .acTotal(acTotal)
                .cv(cv)
                .sv(sv)
                .cpi(cpi)
                .spi(spi)
                .eac(eac)
                .vac(vac)
                .interpretacionCpi(interpretarCPI(cpi))
                .interpretacionSpi(interpretarSPI(spi))
                .actividades(actividadesDTO)
                .build();
    }

    // -------------------------------------------------------------------------
    // Fórmulas EVM
    // -------------------------------------------------------------------------

    /** PV = BAC * (pvPorcentaje / 100) */
    private static BigDecimal calcularPV(BigDecimal bac, BigDecimal pvPct) {
        return bac.multiply(pvPct).divide(CIEN, ESCALA, REDONDEO);
    }

    /** EV = BAC * (evPorcentaje / 100) */
    private static BigDecimal calcularEV(BigDecimal bac, BigDecimal evPct) {
        return bac.multiply(evPct).divide(CIEN, ESCALA, REDONDEO);
    }

    /** EAC = BAC / CPI (null si CPI es null o cero) */
    private static BigDecimal calcularEAC(BigDecimal bac, BigDecimal cpi) {
        return dividir(bac, cpi);
    }

    /** VAC = BAC - EAC (null si EAC es null) */
    private static BigDecimal calcularVAC(BigDecimal bac, BigDecimal eac) {
        if (eac == null) return null;
        return bac.subtract(eac).setScale(ESCALA, REDONDEO);
    }

    /** División segura; retorna null si el divisor es null o cero. */
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

    // -------------------------------------------------------------------------
    // Interpretaciones
    // -------------------------------------------------------------------------

    /**
     * CPI > 1 → Bajo presupuesto (se avanza más de lo que cuesta)
     * CPI = 1 → En presupuesto
     * CPI < 1 → Sobre presupuesto (se gasta más de lo que se avanza)
     */
    private static String interpretarCPI(BigDecimal cpi) {
        if (cpi == null) return "Sin datos suficientes";
        int comparacion = cpi.compareTo(UNO);
        if (comparacion > 0) return "Bajo presupuesto — eficiente en costos";
        if (comparacion < 0) return "Sobre presupuesto — gastando más de lo planificado";
        return "En presupuesto";
    }

    /**
     * SPI > 1 → Adelantado respecto al cronograma
     * SPI = 1 → En cronograma
     * SPI < 1 → Atrasado respecto al cronograma
     */
    private static String interpretarSPI(BigDecimal spi) {
        if (spi == null) return "Sin datos suficientes";
        int comparacion = spi.compareTo(UNO);
        if (comparacion > 0) return "Adelantado — avanzando más de lo planificado";
        if (comparacion < 0) return "Atrasado — avanzando menos de lo planificado";
        return "En cronograma";
    }
}
