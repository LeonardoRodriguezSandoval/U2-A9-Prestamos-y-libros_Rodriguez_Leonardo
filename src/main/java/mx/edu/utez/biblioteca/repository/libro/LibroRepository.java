package mx.edu.utez.biblioteca.repository.libro;

import mx.edu.utez.biblioteca.model.libro.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
}
