package mx.edu.utez.biblioteca.controller.prestamo;

import jakarta.validation.Valid;
import mx.edu.utez.biblioteca.controller.prestamo.dto.RequestSolicitarPrestamoDto;
import mx.edu.utez.biblioteca.model.prestamo.Prestamo;
import mx.edu.utez.biblioteca.service.prestamo.PrestamoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin({"*"})
@RequestMapping("/prestamo")
public class PrestamoController {
    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    public ResponseEntity<Prestamo> solicitarPrestamo(@RequestBody @Valid RequestSolicitarPrestamoDto payload) {
        return ResponseEntity.status(201)
                .body(prestamoService.solicitarPrestamo(payload));
    }

    @PatchMapping("/{id}/renovar")
    public ResponseEntity<Prestamo> renovarPrestamo(@PathVariable Long id) {
        return ResponseEntity.status(200)
                .body(prestamoService.renovarPrestamo(id));
    }

    @GetMapping("/lector/{lectorId}/activos")
    public ResponseEntity<List<Prestamo>> listarPrestamosActivosLector(@PathVariable Long lectorId) {
        return ResponseEntity.status(200)
                .body(prestamoService.listarPrestamosActivosLector(lectorId));
    }
}
