package mx.edu.utez.biblioteca.service.lector;

import jakarta.transaction.Transactional;
import mx.edu.utez.biblioteca.exception.customExceptions.CustomBadException;
import mx.edu.utez.biblioteca.model.lector.Lector;
import mx.edu.utez.biblioteca.repository.lector.LectorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LectorService {
    private final LectorRepository lectorRepository;

    public LectorService(LectorRepository lectorRepository) {
        this.lectorRepository = lectorRepository;
    }

    @Transactional
    public Lector rehabilitarLector(Long id) {
        Lector lector = buscarLector(id);
        lector.setActivo(true);
        return lectorRepository.save(lector);
    }

    private Lector buscarLector(Long id) {
        Optional<Lector> lectorExist = lectorRepository.findById(id);
        if (lectorExist.isEmpty()) {
            throw new CustomBadException("El lector no existe");
        }
        return lectorExist.get();
    }
}
