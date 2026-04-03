package com.nuvora.backend_finanzas.service.implement;

import com.nuvora.backend_finanzas.dto.TransaccionDTO;
import com.nuvora.backend_finanzas.entity.Categoria;
import com.nuvora.backend_finanzas.entity.Transaccion;
import com.nuvora.backend_finanzas.entity.Usuario;
import com.nuvora.backend_finanzas.enums.TipoTransaccion;
import com.nuvora.backend_finanzas.repository.CategoriaRepository;
import com.nuvora.backend_finanzas.repository.TransaccionRepository;
import com.nuvora.backend_finanzas.service.TransaccionService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransaccionServiceImp implements TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public TransaccionDTO createTransaccion(TransaccionDTO transaccionDTO, Usuario usuario){

        //validacion categoria
        Categoria categoria = categoriaRepository.findByCategoriaIdAndUsuario(
                transaccionDTO.getCategoriaId(), usuario
        ).orElseThrow(()-> new RuntimeException("Categoria no válida"));

        // validacion coincidencia transaccion - categoria
        if(!transaccionDTO.getTipo().equals(categoria.getTipoCategoria())) {
            throw new RuntimeException("El tipo de la transaccion no coincide con la categoria");

        }
        Transaccion transaccion = new Transaccion();
        transaccion.setMontoTransaccion(transaccionDTO.getMontoTransaccion());
        transaccion.setDescTransaccion(transaccionDTO.getDescTransaccion());
        transaccion.setTipo(transaccionDTO.getTipo());
        transaccion.setCategoria(categoria);
        transaccion.setUsuario(usuario);

        if (transaccionDTO.getFechaTransaccion() != null){
            transaccion.setFechaTransaccion(transaccionDTO.getFechaTransaccion());
        }else {
            transaccion.setFechaTransaccion(LocalDate.now());
        }

        Transaccion saved = transaccionRepository.save(transaccion);
        return TransaccionDTO.fromEntity(saved);
    }

    @Override
    public List<TransaccionDTO> listTransaccion(Usuario usuario){
        return transaccionRepository.findByUsuario(usuario)
                .stream()
                .map(TransaccionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransaccionDTO> filterByType(String tipo, Usuario usuario){
        TipoTransaccion tipoEnum = TipoTransaccion.valueOf(tipo.toUpperCase());
        return transaccionRepository.findByUsuarioAndTipo(usuario, tipoEnum)
                .stream()
                .map(TransaccionDTO::fromEntity)
                .collect(Collectors.toList());

    }

    @Override
    @SneakyThrows
    public List<TransaccionDTO> filterByCategoria(Long categoriaId, Usuario usuario){
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(()-> new RuntimeException("Categoria no enocntrada"));

        if (!categoria.getUsuario().getUsuarioId().equals(usuario.getUsuarioId())){
            throw new RuntimeException("Acceso denegado");
        }
        return transaccionRepository.findByUsuarioAndCategoria(usuario, categoria)
                .stream()
                .map(TransaccionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public TransaccionDTO updateTransaccion( Long transaccionId, TransaccionDTO nuevaDTO, Usuario usuario){
        Transaccion actual = transaccionRepository.findById(transaccionId)
                .orElseThrow(()-> new RuntimeException("Transaccion no encontrada"));

        //validacion de usuario
        if (!actual.getUsuario().getUsuarioId().equals(usuario.getUsuarioId())){
            throw new RuntimeException("No se puede editar esta transaccion");
        }

        //actualizacion
        actual.setMontoTransaccion(nuevaDTO.getMontoTransaccion());
        actual.setDescTransaccion(nuevaDTO.getDescTransaccion());

        //fecha
        if (nuevaDTO.getFechaTransaccion() != null){
            actual.setFechaTransaccion(nuevaDTO.getFechaTransaccion());
        }

        //categoria
        Categoria categoria = categoriaRepository.findByCategoriaIdAndUsuario(
                nuevaDTO.getCategoriaId(), usuario
        ).orElseThrow(()-> new RuntimeException("Categoria no encontrada"));


        if (!nuevaDTO.getTipo().equals(categoria.getTipoCategoria())){
            throw new RuntimeException("Tipo no coincide con la categoria");
        }

        actual.setCategoria(categoria);
        actual.setTipo(nuevaDTO.getTipo());

        Transaccion update = transaccionRepository.save(actual);
        return TransaccionDTO.fromEntity(update);
    }

    @Override
    public void deleteTransaccion (Long transaccionId, Usuario usuario){
        Transaccion transaccion = transaccionRepository.findById(transaccionId)
                .orElseThrow(()-> new RuntimeException("Transaccion no encontrada"));

        //validacion del usuario

        if(!transaccion.getUsuario().getUsuarioId().equals(usuario.getUsuarioId())){
            throw new RuntimeException("No se puede eliminar esta transaccion");
        }
        transaccionRepository.delete(transaccion);
    }

    @Override
    public Double calcularSaldoUsuario(Usuario usuario){
        List<Transaccion> transacciones = transaccionRepository.findByUsuario(usuario);

        double ingresos = transacciones.stream()
                .filter(t -> t.getTipo() == TipoTransaccion.INGRESO)
                .mapToDouble(Transaccion::getMontoTransaccion)
                .sum();

        double gastos = transacciones.stream()
                .filter(t -> t.getTipo() == TipoTransaccion.GASTO)
                .mapToDouble(Transaccion::getMontoTransaccion)
                .sum();

        //usa el sueldo mensual o 0 si no se encuentra definido
        double sueldo = usuario.getMontoMensual() != null ? usuario.getMontoMensual() : 0;

        return sueldo + ingresos - gastos;

    }
}

