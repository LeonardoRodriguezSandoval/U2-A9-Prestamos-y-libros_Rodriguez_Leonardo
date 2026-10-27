package mx.edu.utez.biblioteca.service.prestamo;

import jakarta.transaction.Transactional;
import mx.edu.utez.biblioteca.controller.prestamo.dto.RequestSolicitarPrestamoDto;
import mx.edu.utez.biblioteca.exception.customExceptions.CustomBadException;
import mx.edu.utez.biblioteca.model.lector.Lector;
import mx.edu.utez.biblioteca.model.libro.Libro;
import mx.edu.utez.biblioteca.model.prestamo.EstadoPrestamo;
import mx.edu.utez.biblioteca.model.prestamo.Prestamo;
import mx.edu.utez.biblioteca.repository.lector.LectorRepository;
import mx.edu.utez.biblioteca.repository.libro.LibroRepository;
import mx.edu.utez.biblioteca.repository.prestamo.PrestamoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PrestamoService {
    private final PrestamoRepository prestamoRepository;
    private final LectorRepository lectorRepository;
    private final LibroRepository libroRepository;

    public PrestamoService(PrestamoRepository prestamoRepository,
                           LectorRepository lectorRepository,
                           LibroRepository libroRepository) {
        this.prestamoRepository = prestamoRepository;
        this.lectorRepository = lectorRepository;
        this.libroRepository = libroRepository;
    }

    @Transactional
    public Prestamo solicitarPrestamo(RequestSolicitarPrestamoDto payload) {
        Lector lector = buscarLector(payload.getLectorId());
        if (!lector.isActivo()) {
            throw new CustomBadException("El lector no esta activo");
        }

        Libro libro = buscarLibro(payload.getLibroId());
        if (libro.getEjemplaresDisponibles() < 1) {
            throw new CustomBadException("El libro no tiene ejemplares disponibles");
        }

        long prestamosActivos = prestamoRepository.countByLectorIdAndEstadoIn(
                lector.getId(),
                List.of(EstadoPrestamo.PRESTADO, EstadoPrestamo.VENCIDO)
        );
        int limite = lector.isMembresiaPremium() ? 5 : 3;
        if (prestamosActivos >= limite) {
            throw new CustomBadException("El lector alcanzo el limite de " + limite + " prestamos activos");
        }

        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);
        libroRepository.save(libro);

        Prestamo prestamo = new Prestamo();
        prestamo.setLector(lector);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaDevolucionMaxima(LocalDate.now().plusDays(14));
        prestamo.setEstado(EstadoPrestamo.PRESTADO);

        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public Prestamo renovarPrestamo(Long id) {
        Optional<Prestamo> prestamoExist = prestamoRepository.findById(id);
        if (prestamoExist.isEmpty()) {
            throw new CustomBadException("El prestamo no existe");
        }
        Prestamo prestamo = prestamoExist.get();

        if (prestamo.getEstado() == EstadoPrestamo.VENCIDO
                || prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new CustomBadException("El prestamo esta vencido o devuelto y no se puede renovar");
        }

        prestamo.setFechaDevolucionMaxima(prestamo.getFechaDevolucionMaxima().plusDays(7));
        return prestamoRepository.save(prestamo);
    }

    public List<Prestamo> listarPrestamosActivosLector(Long lectorId) {
        buscarLector(lectorId);
        return prestamoRepository.findByLectorIdAndEstadoIn(
                lectorId,
                List.of(EstadoPrestamo.PRESTADO, EstadoPrestamo.VENCIDO)
        );
    }

    private Lector buscarLector(Long id) {
        Optional<Lector> lectorExist = lectorRepository.findById(id);
        if (lectorExist.isEmpty()) {
            throw new CustomBadException("El lector no existe");
        }
        return lectorExist.get();
    }

    private Libro buscarLibro(Long id) {
        Optional<Libro> libroExist = libroRepository.findById(id);
        if (libroExist.isEmpty()) {
            throw new CustomBadException("El libro no existe");
        }
        return libroExist.get();
    }
}
