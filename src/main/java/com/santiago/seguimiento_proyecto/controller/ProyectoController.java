package com.santiago.seguimiento_proyecto.controller;

import com.santiago.seguimiento_proyecto.dto.ProyectoRequestDTO;
import com.santiago.seguimiento_proyecto.dto.ProyectoResponseDTO;
import com.santiago.seguimiento_proyecto.service.ProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
@RequiredArgsConstructor
public class ProyectoController {

    private final ProyectoService proyectoService;

    /**
     * POST /api/proyectos
     * Crea un nuevo proyecto.
     */
    @PostMapping
    public ResponseEntity<ProyectoResponseDTO> crear(@RequestBody ProyectoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proyectoService.crear(request));
    }

    /**
     * GET /api/proyectos
     * Lista todos los proyectos con sus métricas EVM agregadas.
     */
    @GetMapping
    public ResponseEntity<List<ProyectoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(proyectoService.listarTodos());
    }

    /**
     * GET /api/proyectos/{id}
     * Obtiene un proyecto por ID con sus actividades y métricas EVM.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProyectoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(proyectoService.obtenerPorId(id));
    }

    /**
     * PUT /api/proyectos/{id}
     * Actualiza el nombre del proyecto.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProyectoResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody ProyectoRequestDTO request) {
        return ResponseEntity.ok(proyectoService.actualizar(id, request));
    }

    /**
     * DELETE /api/proyectos/{id}
     * Elimina un proyecto y todas sus actividades (cascade).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        proyectoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
