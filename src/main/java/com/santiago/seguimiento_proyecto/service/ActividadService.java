package com.santiago.seguimiento_proyecto.service;

import com.santiago.seguimiento_proyecto.dto.ActividadRequestDTO;
import com.santiago.seguimiento_proyecto.dto.ActividadResponseDTO;

import java.util.List;

public interface ActividadService {

    ActividadResponseDTO crear(Long proyectoId, ActividadRequestDTO request);

    List<ActividadResponseDTO> listarPorProyecto(Long proyectoId);

    ActividadResponseDTO obtenerPorId(Long id);

    ActividadResponseDTO actualizar(Long id, ActividadRequestDTO request);

    void eliminar(Long id);
}
