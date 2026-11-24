package leoastray.examencinerest.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import leoastray.examencinerest.model.dto.PeliculaDetalleDTO;
import leoastray.examencinerest.model.dto.PeliculaListadoDTO;
import leoastray.examencinerest.model.dto.PeliculaRequestDTO;
import leoastray.examencinerest.service.IPeliculaService;

@Service 
public class PeliculaServiceImpl implements IPeliculaService {

    @Override
    @Transactional
    public PeliculaDetalleDTO crear(PeliculaRequestDTO dto) {
        // TODO Auto-generated method stub
        throw new IllegalArgumentException();
    }

    @Override
    @Transactional(readOnly = true)
    public PeliculaListadoDTO leerLista(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'leerLista'");
    }

    @Override
    @Transactional(readOnly = true)
    public PeliculaDetalleDTO leerDetalle(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'leerDetalle'");
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminar'");
    }

    @Override
    @Transactional
    public PeliculaListadoDTO actualizar(PeliculaRequestDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizar'");
    }

}
