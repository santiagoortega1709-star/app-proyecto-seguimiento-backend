package com.santiago.seguimiento_proyecto.service;

import com.santiago.seguimiento_proyecto.dto.ProyectoRequestDTO;
import com.santiago.seguimiento_proyecto.dto.ProyectoResponseDTO;

import java.util.List;

public interface ProyectoService {

    ProyectoResponseDTO crear(ProyectoRequestDTO request);

    List<ProyectoResponseDTO> listarTodos();

    ProyectoResponseDTO obtenerPorId(Long id);

    ProyectoResponseDTO actualizar(Long id, ProyectoRequestDTO request);

    void eliminar(Long id);
}
