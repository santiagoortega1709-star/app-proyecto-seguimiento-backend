package com.santiago.seguimiento_proyecto.service.impl;

import com.santiago.seguimiento_proyecto.dto.ActividadRequestDTO;
import com.santiago.seguimiento_proyecto.dto.ActividadResponseDTO;
import com.santiago.seguimiento_proyecto.entity.Actividad;
import com.santiago.seguimiento_proyecto.entity.Proyecto;
import com.santiago.seguimiento_proyecto.exception.RecursoNoEncontradoException;
import com.santiago.seguimiento_proyecto.repository.ActividadRepository;
import com.santiago.seguimiento_proyecto.repository.ProyectoRepository;
import com.santiago.seguimiento_proyecto.service.ActividadService;
import com.santiago.seguimiento_proyecto.util.EvaluacionEVM;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ActividadServiceImpl implements ActividadService {

    private final ActividadRepository actividadRepository;
    private final ProyectoRepository proyectoRepository;

    @Override
    public ActividadResponseDTO crear(Long proyectoId, ActividadRequestDTO request) {
        Proyecto proyecto = findProyectoOrThrow(proyectoId);
        Actividad actividad = Actividad.builder()
                .nombre(request.getNombre())
                .bac(request.getBac())
                .pvPorcentaje(request.getPvPorcentaje())
                .evPorcentaje(request.getEvPorcentaje())
                .ac(request.getAc())
                .proyecto(proyecto)
                .build();
        return EvaluacionEVM.toActividadResponse(actividadRepository.save(actividad));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActividadResponseDTO> listarPorProyecto(Long proyectoId) {
        findProyectoOrThrow(proyectoId);
        return actividadRepository.findByProyectoId(proyectoId)
                .stream()
                .map(EvaluacionEVM::toActividadResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ActividadResponseDTO obtenerPorId(Long id) {
        return EvaluacionEVM.toActividadResponse(findOrThrow(id));
    }

    @Override
    public ActividadResponseDTO actualizar(Long id, ActividadRequestDTO request) {
        Actividad actividad = findOrThrow(id);
        actividad.setNombre(request.getNombre());
        actividad.setBac(request.getBac());
        actividad.setPvPorcentaje(request.getPvPorcentaje());
        actividad.setEvPorcentaje(request.getEvPorcentaje());
        actividad.setAc(request.getAc());
        return EvaluacionEVM.toActividadResponse(actividadRepository.save(actividad));
    }

    @Override
    public void eliminar(Long id) {
        findOrThrow(id);
        actividadRepository.deleteById(id);
    }

    private Actividad findOrThrow(Long id) {
        return actividadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Actividad no encontrada con id: " + id));
    }

    private Proyecto findProyectoOrThrow(Long id) {
        return proyectoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proyecto no encontrado con id: " + id));
    }
}
