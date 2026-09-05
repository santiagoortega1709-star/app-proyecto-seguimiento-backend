package com.santiago.seguimiento_proyecto.service.impl;

import com.santiago.seguimiento_proyecto.dto.ProyectoRequestDTO;
import com.santiago.seguimiento_proyecto.dto.ProyectoResponseDTO;
import com.santiago.seguimiento_proyecto.entity.Proyecto;
import com.santiago.seguimiento_proyecto.exception.RecursoNoEncontradoException;
import com.santiago.seguimiento_proyecto.repository.ProyectoRepository;
import com.santiago.seguimiento_proyecto.service.ProyectoService;
import com.santiago.seguimiento_proyecto.util.EvaluacionEVM;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProyectoServiceImpl implements ProyectoService {

    private final ProyectoRepository proyectoRepository;

    @Override
    public ProyectoResponseDTO crear(ProyectoRequestDTO request) {
        Proyecto proyecto = Proyecto.builder()
                .nombre(request.getNombre())
                .build();
        return EvaluacionEVM.toProyectoResponse(proyectoRepository.save(proyecto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoResponseDTO> listarTodos() {
        return proyectoRepository.findAll()
                .stream()
                .map(EvaluacionEVM::toProyectoResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProyectoResponseDTO obtenerPorId(Long id) {
        return EvaluacionEVM.toProyectoResponse(findOrThrow(id));
    }

    @Override
    public ProyectoResponseDTO actualizar(Long id, ProyectoRequestDTO request) {
        Proyecto proyecto = findOrThrow(id);
        proyecto.setNombre(request.getNombre());
        return EvaluacionEVM.toProyectoResponse(proyectoRepository.save(proyecto));
    }

    @Override
    public void eliminar(Long id) {
        findOrThrow(id);
        proyectoRepository.deleteById(id);
    }

    private Proyecto findOrThrow(Long id) {
        return proyectoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proyecto no encontrado con id: " + id));
    }
}
