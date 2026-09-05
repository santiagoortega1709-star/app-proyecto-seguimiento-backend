package com.santiago.seguimiento_proyecto.controller;

import com.santiago.seguimiento_proyecto.dto.ActividadRequestDTO;
import com.santiago.seguimiento_proyecto.dto.ActividadResponseDTO;
import com.santiago.seguimiento_proyecto.service.ActividadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos/{proyectoId}/actividades")
@RequiredArgsConstructor
public class ActividadController {

    private final ActividadService actividadService;

    /**
     * POST /api/proyectos/{proyectoId}/actividades
     * Crea una nueva actividad dentro de un proyecto.
     *
     * Body: { nombre, bac, pvPorcentaje, evPorcentaje, ac }
     */
    @PostMapping
    public ResponseEntity<ActividadResponseDTO> crear(
            @PathVariable Long proyectoId,
            @RequestBody ActividadRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(actividadService.crear(proyectoId, request));
    }

    /**
     * GET /api/proyectos/{proyectoId}/actividades
     * Lista todas las actividades de un proyecto con métricas EVM individuales.
     */
    @GetMapping
    public ResponseEntity<List<ActividadResponseDTO>> listarPorProyecto(@PathVariable Long proyectoId) {
        return ResponseEntity.ok(actividadService.listarPorProyecto(proyectoId));
    }

    /**
     * GET /api/proyectos/{proyectoId}/actividades/{id}
     * Obtiene una actividad específica con sus métricas EVM.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ActividadResponseDTO> obtenerPorId(
            @PathVariable Long proyectoId,
            @PathVariable Long id) {
        return ResponseEntity.ok(actividadService.obtenerPorId(id));
    }

    /**
     * PUT /api/proyectos/{proyectoId}/actividades/{id}
     * Actualiza los datos de una actividad.
     *
     * Body: { nombre, bac, pvPorcentaje, evPorcentaje, ac }
     */
    @PutMapping("/{id}")
    public ResponseEntity<ActividadResponseDTO> actualizar(
            @PathVariable Long proyectoId,
            @PathVariable Long id,
            @RequestBody ActividadRequestDTO request) {
        return ResponseEntity.ok(actividadService.actualizar(id, request));
    }

    /**
     * DELETE /api/proyectos/{proyectoId}/actividades/{id}
     * Elimina una actividad del proyecto.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long proyectoId,
            @PathVariable Long id) {
        actividadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
